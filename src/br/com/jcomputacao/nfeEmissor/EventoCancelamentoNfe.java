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
import br.com.jcomputacao.model.beans.LojaBean;
import br.com.jcomputacao.nfe.NFeUtil;
import br.com.jcomputacao.nfe.assinatura.Assinador;
import br.com.jcomputacao.nfe.assinatura.AssinadorTipo;
import br.com.jcomputacao.nfe.ws.WsConnectionConfig;
import static br.com.jcomputacao.nfeEmissor.Servico.logger;
import br.com.jcomputacao.nfeIntegracao.ServicoEventoCancelaNfe;
import br.com.jcomputacao.util.Ambiente;
import br.com.jcomputacao.util.StringUtil;
import br.inf.portalfiscal.eventoCanc.xml.pl006q.nfes.TEvento;
import br.inf.portalfiscal.eventoCanc.xml.pl006q.nfes.TProcEvento;
import br.inf.portalfiscal.eventoCanc.xml.pl006q.nfes.TRetEnvEvento;
import br.inf.portalfiscal.eventoCanc.xml.pl006q.nfes.TRetEvento;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.logging.Level;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.stream.StreamSource;
import org.apache.axis2.AxisFault;

/**
 *
 * @author murilo.lima
 */
public class EventoCancelamentoNfe extends Servico {

    private static final String prolog = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
    private JAXBContext contextCancelamento = null;

    public void cancelar(NfeModel nfe) throws DbfDatabaseException, DbfException {
        try {
            this.contextCancelamento = JAXBContext.newInstance("br.inf.portalfiscal.eventoCanc.xml.pl006q.nfes");
        } catch (JAXBException ex) {
            logger.log(Level.SEVERE, "Erro ao criar o contexto do JAXB", ex);
        }
        String cnpj = obtemCnpjEmitente(nfe);
        WsConnectionConfig.setProperties(cnpj);

        String xml = null;
        String chave = null;
        String tipoEvento = "110111";
        String numeroSequenciaEvento = "1";
        Marshaller marshaller = null;
        TEvento eveCanc = new TEvento();

        try {
            eveCanc.setVersao("1.00");
            TEvento.InfEvento eveInfCanc = new TEvento.InfEvento();
            TEvento.InfEvento.DetEvento detEvento = new TEvento.InfEvento.DetEvento();
            chave = nfe.getNfeChaveAcesso();
            String id = "ID" + tipoEvento + chave + "0" + numeroSequenciaEvento;
            eveInfCanc.setId(id);
            eveInfCanc.setChNFe(chave);
            eveInfCanc.setCOrgao("35");
            eveInfCanc.setCNPJ(StringUtil.somenteNumeros(LojaBean.getLojaAtual().getCpfCnpj()));
            eveInfCanc.setNSeqEvento(numeroSequenciaEvento);
            eveInfCanc.setTpAmb(Integer.toString(NFeUtil.getAmbiente()));
            eveInfCanc.setVerEvento("1.00");
            eveInfCanc.setTpEvento(tipoEvento);
            boolean dataHoraDaNfe = Boolean.parseBoolean(System.getProperty("nfe.evento.cancelamento.dataHora.nfe", "false"));
            if (dataHoraDaNfe) {
                eveInfCanc.setDhEvento(converteData(nfe.getAlterado()));
            } else {
                eveInfCanc.setDhEvento(buscaDataAtual());
            }
            detEvento.setDescEvento("Cancelamento");
            detEvento.setNProt(nfe.getNumeroProtocoloString());
            detEvento.setVersao("1.00");
            detEvento.setXJust(trataString(nfe.getDevolucaoMotivoDescricao()));
            eveInfCanc.setDetEvento(detEvento);
            eveCanc.setInfEvento(eveInfCanc);
            marshaller = contextCancelamento.createMarshaller();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            marshaller.marshal(eveCanc, baos);
            xml = baos.toString();
            xml = xml.replaceAll("xmlns:ns2=\".+#\"\\s", "").replaceAll("ns2:", "");
        } catch (JAXBException ex) {
            throw new DbfException("Erro ao montar os dados para a assinatura", ex);
        } catch (ParseException ex) {
            throw new DbfException("Erro ao manipular a data", ex);
        }
        System.out.print(xml);

        try {
            xml = Assinador.assinar(xml, NFeUtil.getCertificadoCaminho(cnpj), NFeUtil.getCertificadoSenha(cnpj), AssinadorTipo.CANCELAMENTO, cnpj);
        } catch (Exception ex) {
            throw new DbfException("Erro ao tentar assinar o cancelamento", ex);
        }

        validar(xml);

        String fileName = chave + "-ped-can.xml";
        try {
            escreve(fileName, xml);
        } catch (IOException ex) {
            throw new DbfException("Erro ao tentar gravar o arquivo " + fileName, ex);
        }

        long timeInMillis = Calendar.getInstance().getTimeInMillis();
        String idLote = Long.toString(timeInMillis);

        logger.log(Level.FINE, "ID Lote : {0}", idLote);
        String lote = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><envEvento xmlns=\"http://www.portalfiscal.inf.br/nfe\" versao=\"1.00\">"
                + "<idLote>" + idLote + "</idLote>"
                + xml.replace(prolog, "")
                + "</envEvento>";
        logger.log(Level.FINE, lote);
        nfe.setNfeLoteCancelamento(idLote);

        try {
            fileName = "loteCancelamento" + idLote + ".xml";
            escreve(fileName, lote);
        } catch (IOException ex) {
            throw new DbfException("Erro ao tentar escrever o arquivo " + lote, ex);
        }

        ServicoEventoCancelaNfe servico = new ServicoEventoCancelaNfe();
        fileName = chave + "-can.xml";
        String resultado = null;
        try {
            resultado = servico.executar(lote);
            logger.log(Level.FINER, "Arquivo escrito {0}", fileName);
        } catch (AxisFault ex) {
            throw new DbfException("Erro ao tentar acessar o servico da SEFAZ", ex);
        } catch (RemoteException ex) {
            throw new DbfException("Erro ao tentar acessar acessar o servico da SEFAZ", ex);
        } catch (XMLStreamException ex) {
            throw new DbfException("Erro de XML ao tentar acessar acessar o servico da SEFAZ", ex);
        }
        try {
            escreve(fileName, resultado);
        } catch (IOException ex) {
            throw new DbfException("Erro ao criar o arquivo " + fileName, ex);
        }

        Unmarshaller unmarshaller = null;
        TRetEnvEvento retEnvEvento = null;
        try {
            unmarshaller = contextCancelamento.createUnmarshaller();
            ByteArrayInputStream bais = new ByteArrayInputStream(resultado.getBytes("UTF-8"));
            retEnvEvento = unmarshaller.unmarshal(new StreamSource(bais), TRetEnvEvento.class).getValue();
            bais.close();
        } catch (JAXBException ex) {
            throw new DbfException("Erro ao de XML tentar converter o XML do retorno em objeto", ex);
        } catch (IOException ex) {
            throw new DbfException("Erro ao tentar converter o XML do retorno em objeto", ex);
        }

        TRetEvento retEvento = null;
        if (retEnvEvento.getRetEvento() != null && !retEnvEvento.getRetEvento().isEmpty()) {
            for (TRetEvento retEv : retEnvEvento.getRetEvento()) {
                retEvento = retEv;
            }
        }

        TProcEvento procEventoCanc = new TProcEvento();
        procEventoCanc.setVersao("1.00");
        procEventoCanc.setEvento(eveCanc);
        procEventoCanc.setRetEvento(retEvento);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            marshaller.marshal(procEventoCanc, baos);
        } catch (JAXBException ex) {
            throw new DbfException("Erro ao tentar montar o resultado do processamento para armazenar", ex);
        }

        xml = baos.toString();
        xml = xml.replaceAll("xmlns:ns2=\".+#\"\\s", "").replaceAll("ns2:", "");

        try {
            fileName = chave + "-procEventoCanc.xml";
            escreve(fileName, xml);
        } catch (IOException ex) {
            throw new DbfException("Erro ao criar o arquivo " + fileName, ex);
        }
        if (retEvento.getInfEvento() != null && retEvento.getInfEvento().getCStat() != null) {
            if (Integer.parseInt(retEvento.getInfEvento().getCStat()) == 135
                    || Integer.parseInt(retEvento.getInfEvento().getCStat()) == 155) {
                nfe.setNfeXmlCancelamento(xml);
                nfe.setStatus(NFeStatus.CANCELADA);
                nfe.setProtocoloStatus(retEvento.getInfEvento().getCStat());
                nfe.update();
            } else {
                nfe.setProtocoloStatus(retEvento.getInfEvento().getCStat());
                nfe.update();
                String msg = retEvento.getInfEvento().getCStat() + " "
                        + retEvento.getInfEvento().getXMotivo();
                throw new DbfException(msg);
            }
        } else {
            throw new DbfException("Resultado desconhecido vide arquivo " + fileName);
        }
    }

    private String buscaDataAtual() throws DbfDatabaseException, ParseException {
        String dataString = "";
        String query = "SELECT NOW()";
        try {
            java.sql.Statement stmt = Ambiente.getConnectionForSelect().createStatement();
            ResultSet rs = stmt.executeQuery(query);

            if (rs.next()) {
                java.sql.Timestamp data = rs.getTimestamp(1);
                //PEGANDO A DATA EM MILIGEGUNDOS
                dataString = converteData(data);
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            throw new DbfDatabaseException(e, query);
        }
        return dataString;
    }

    private String converteData(Date data) throws ParseException {
        String dataString = "";
        SimpleDateFormat dateFormatGmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sszzz");
        //Time in GMT
        dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT-03:00"));
        dataString = dateFormatGmt.format(data).toString().replaceAll("GMT", "");
        if (dataString.length() > 24) {
            int i;
            String pedacoString1;
            String pedacoString2;
            if (dataString.contains(".")) {
                i = dataString.indexOf(".");
                pedacoString1 = dataString.substring(0, i);
                i = dataString.indexOf("-03:00");
                pedacoString2 = dataString.substring(i, dataString.length());
                dataString = pedacoString1 + pedacoString2;
            }
        }
        return dataString;
    }

    private void validar(String xml) throws DbfException {
        String urlXsd = System.getProperty("nfe.validacao.localizacao.cancelamento");
        validar(xml, urlXsd);
    }
}
