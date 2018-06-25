/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.jcomputacao.nfeEmissor;

import br.com.jcomputacao.exception.DbfDatabaseException;
import br.com.jcomputacao.exception.DbfException;
import br.com.jcomputacao.model.NFeStatus;
import br.com.jcomputacao.model.NfeModel;
import br.com.jcomputacao.nfe.NFeUtil;
import br.com.jcomputacao.nfe.assinatura.Assinador;
import br.com.jcomputacao.nfe.assinatura.AssinadorTipo;
import br.com.jcomputacao.nfe.ws.WsConnectionConfig;
import static br.com.jcomputacao.nfeEmissor.Servico.context;
import br.com.jcomputacao.nfeIntegracao.ServicoInutilizaNfe;
import br.com.jcomputacao.util.StringUtil;
import br.inf.portalfiscal.nfe.xml.pl009v4.nfes.TInutNFe;
import br.inf.portalfiscal.nfe.xml.pl009v4.nfes.TInutNFe.InfInut;
import br.inf.portalfiscal.nfe.xml.pl009v4.nfes.TNFe.InfNFe.Ide;
import br.inf.portalfiscal.nfe.xml.pl009v4.nfes.TProcInutNFe;
import br.inf.portalfiscal.nfe.xml.pl009v4.nfes.TRetInutNFe;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

/**
 *
 * @author murilo.lima
 */
public class InutilizaNfe extends Servico {

    public int inutilizar(NfeModel nfe) throws DbfDatabaseException, DbfException {
        String cnpj = obtemCnpjEmitente(nfe);
        WsConnectionConfig.setProperties(cnpj);

        InfInut inf = new InfInut();
        inf.setId(nfe.getNfeChaveAcesso());
        Ide ide = criaIdentificacao(nfe);

        String anoEmissao = ide.getDhEmi().substring(0, 4);
        /**
         * Identificador da TAG a ser assinada formada com C\u00f3digo da UF +
         * Ano (2 posi\u00e7ões) + CNPJ + modelo + série + nro inicial e nro
         * final precedida do literal ID
         */
        //39646 ID35110543753700013755001000039646000039646
        //40604 ID35110543753700013755001000040604000040604
        //42187 ID35110543753700013755001000042187000042187
        String numero = StringUtil.ajusta(nfe.getNumero(), 9, StringUtil.ALINHAMENTO_DIREITA, '0');
        String id = "ID" + ide.getCUF() + anoEmissao.substring(2) + cnpj
                + StringUtil.ajusta(ide.getMod(), 2, StringUtil.ALINHAMENTO_DIREITA, '0')
                + StringUtil.ajusta(ide.getSerie(), 3, StringUtil.ALINHAMENTO_DIREITA, '0')
                + numero + numero;
        inf.setId(id);
        inf.setAno(anoEmissao.substring(2));
        inf.setMod(ide.getMod());
        inf.setSerie(ide.getSerie());
        inf.setCUF(ide.getCUF());
        inf.setCNPJ(cnpj);
        inf.setNNFIni(nfe.getNumero());
        inf.setNNFFin(nfe.getNumero());
        inf.setTpAmb(Integer.toString(NFeUtil.getAmbiente()));

        if (StringUtil.isNull(nfe.getDevolucaoMotivoDescricao()) || nfe.getDevolucaoMotivoDescricao().length() < 15) {
            throw new DbfException("Justificativa de inutilizacao nula ou menor que 15 caracteres");
        }
        inf.setXJust(StringUtil.noSpecialKeys(nfe.getDevolucaoMotivoDescricao().trim()));
        inf.setXServ("INUTILIZAR");

        TInutNFe inutNFe = new TInutNFe();
        inutNFe.setVersao("4.00");
        inutNFe.setInfInut(inf);

        Marshaller marshaller = null;
        String xml = "";
        try {
            marshaller = context.createMarshaller();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            marshaller.marshal(inutNFe, baos);
            xml = baos.toString();
            xml = xml.replaceAll("xmlns:ns2=\".+#\"\\s", "").replaceAll("ns2:", "");
        } catch (JAXBException ex) {
            throw new DbfException("Erro ao montar a mensagem de cancelamento", ex);
        }

        try {
            Assinador a = new Assinador();
            xml = a.assinar(xml, NFeUtil.getCertificadoCaminho(cnpj), NFeUtil.getCertificadoSenha(cnpj), AssinadorTipo.INUTILIZACAO, cnpj);
        } catch (Exception ex) {
            throw new DbfException("Erro ao tentar assinar o XML de inutilizacao da NFe", ex);
        }

        validar(xml);

        /*
         * Pedido de Inutilizacao de Numeracao: O nome do arquivo sera composto por: UF
         * + Ano de inutilizacao + CNPJ do emitente + Modelo + Serie + Numero Inicial +
         * Numero Final com extensao -ped-inu.xml;
         */
        String fileNamePrefix = inf.getCUF() + inf.getAno() + inf.getCNPJ() + inf.getMod()
                + inf.getSerie() + inf.getNNFIni() + inf.getNNFFin();
        String fileName = fileNamePrefix + "-ped-inu.xml";
        try {
            escreve(fileName, xml);
        } catch (IOException ex) {
            throw new DbfException("Erro ao tentar escrever o arquivo " + fileName, ex);
        }

        ServicoInutilizaNfe is = new ServicoInutilizaNfe();
        String retorno = null;
        try {
            retorno = is.inutilizar(xml);
        } catch (Exception ex) {
            throw new DbfException("Erro ao tentar enviar a informa\u00e7\u00e3o para a SEFAZ", ex);
        }

        fileName = fileNamePrefix + "-inu.xml";
        try {
            escreve(fileName, retorno);
        } catch (Exception ex) {
            throw new DbfException("Erro ao tentar escrever o arquivo " + fileName, ex);
        }

        int status = 0;
        try {
            Unmarshaller unmarshaller = context.createUnmarshaller();
            ByteArrayInputStream bais = new ByteArrayInputStream(retorno.getBytes("UTF-8"));
            TRetInutNFe rin = unmarshaller.unmarshal(new StreamSource(bais), TRetInutNFe.class).getValue();
            bais.close();

            TRetInutNFe.InfInut rinf = rin.getInfInut();
            if (rinf != null && rinf.getCStat() != null) {
                status = Integer.parseInt(rin.getInfInut().getCStat());
                if (status == 102) {
                    TProcInutNFe proc = new TProcInutNFe();
                    proc.setInutNFe(inutNFe);
                    proc.setRetInutNFe(rin);

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    marshaller.marshal(proc, baos);
                    xml = baos.toString();
                    xml = xml.replaceAll("xmlns:ns2=\".+#\"\\s", "").replaceAll("ns2:", "");

                    nfe.setStatus(NFeStatus.INUTILIZADA);
                    nfe.setProtocoloStatus(rin.getInfInut().getCStat());
                    xml = xml.replaceAll("xmlns:ns2=\".+#\"\\s", "").replaceAll("ns2:", "");
                    nfe.setNfeXmlCancelamento(xml);
                    nfe.setNfeXml(null);
                } else {
                    throw new DbfException("Erro ao tentar inutilizar a NFe " + status + " " + rinf.getXMotivo());
                }
            }
        } catch (JAXBException ex) {
            logger.log(Level.SEVERE, "Erro ao converter o XML em objeto", ex);
            throw new DbfException("Erro ao tratar o retorno da inutilizacao, arquivo " + fileName, ex);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Erro com o arquivo XML", ex);
            throw new DbfException("Erro ao tratar o retorno da inutilizacao, arquivo " + fileName, ex);
        }
        return status;

    }

    private void validar(String xml) throws DbfException {
        String urlXsd = System.getProperty("nfe.validacao.localizacao.inutilizacao");
        validar(xml, urlXsd);
    }
}
