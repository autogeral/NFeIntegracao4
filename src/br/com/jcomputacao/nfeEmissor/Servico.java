/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.jcomputacao.nfeEmissor;

import br.com.jcomputacao.exception.DbfDatabaseException;
import br.com.jcomputacao.exception.DbfException;
import br.com.jcomputacao.model.CadastroModel;
import br.com.jcomputacao.model.LojaModel;
import br.com.jcomputacao.model.MovimentoOperacaoModel;
import br.com.jcomputacao.model.NfeModel;
import br.com.jcomputacao.model.beans.LojaBean;
import br.com.jcomputacao.model.beans.MovimentoOperacaoBean;
import br.com.jcomputacao.nfe.NFeUF;
import br.com.jcomputacao.nfe.NFeUtil;
import br.com.jcomputacao.nfe.validacao.Validador;
import br.com.jcomputacao.nfe.validacao.ValidadorListener;
import br.com.jcomputacao.util.StringUtil;
import br.inf.portalfiscal.nfe.xml.pl009v4.nfes.TNFe.InfNFe.Ide;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 *
 * @author murilo.lima
 */
public class Servico {

    protected static JAXBContext context = null;
    protected static final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    static {
        try {
            context = JAXBContext.newInstance("br.inf.portalfiscal.nfe.xml.pl009v4.nfes");
        } catch (JAXBException ex) {
            logger.log(Level.SEVERE, "Erro ao criar o contexto do JAXB", ex);
        }
    }

    /**
     * o nome do arquivo efetivamente escrito pode ser alterado pois se houver
     * um arquivo com o mesmo nome entao sera criado um arquvio com um numero
     * sequencial subsequente ao ultimo sequencial encontrado ex: escrever a.xml
     * mas existe a.xml entao sera escrito o arquivo a[1].xml escrever a.xml mas
     * existem a.xml e a[1].xml entao serao escrito a[2].xml
     *
     * @param filename - o nome do arquivo que se pretende escrever.
     * @param xml - conteudo do arquivo
     * @return filename - o nome do arquivo efetivamente escrito.
     * @throws IOException
     */
    protected String escreve(String fname, String xml) throws IOException {
        String originalName = fname;
        String baseDir = System.getProperty("nfe.xmlFiles", "C:/DBF/dist/nfexml/");
        if (baseDir != null) {
            File f = new File(baseDir);
            if (f.exists() || f.mkdirs()) {
                fname = baseDir + fname;
            }
        }
        File file = new File(fname);
        int i = 1;
        while (file.exists()) {
            String simpleName = originalName.substring(0, originalName.lastIndexOf("."));
            String extension = originalName.substring(originalName.lastIndexOf(".") + 1);
            fname = baseDir + simpleName + "[" + (i++) + "]." + extension;
            file = new File(fname);
        }
        FileWriter fw = new FileWriter(file);
        fw.write(xml);
        fw.flush();
        fw.close();
        return fname;
    }

    protected String obtemCnpjEmitente(LojaModel loja) throws DbfDatabaseException {
        String ocnpj = StringUtil.somenteNumeros(loja.getCnpj());
        ocnpj = StringUtil.ajusta(ocnpj, 14, StringUtil.ALINHAMENTO_DIREITA, '0');
        return ocnpj;
    }

    protected String obtemCnpjEmitente(NfeModel nfeModel) throws DbfDatabaseException {
        LojaModel lojaModel = LojaBean.getLojaPorCodigo(nfeModel.getLoja());
        return obtemCnpjEmitente(lojaModel);
    }

    protected String trataString(String str) {
        String r = StringUtil.htmlIso8859encode(str);
        r = StringUtil.soEspacoSimples(r).trim();
        return r;
    }

    protected Ide criaIdentificacao(NfeModel nfeModel) throws DbfDatabaseException, DbfException {
        MovimentoOperacaoModel operacao = MovimentoOperacaoBean.getMovimentoPorCodigo(nfeModel.getOperacaoCodigo());
        Ide ide = new Ide();
        ide.setNatOp(nfeModel.getOperacaoDescricao());
        ide.setNNF(nfeModel.getNumero());
        try {
            ide.setDhEmi(converteData(nfeModel.getCadastro()));
            ide.setDhSaiEnt(converteData(nfeModel.getAlterado()));
            String idLocalDestOperacao;
            if (operacao.getInterestadual()) {
                idLocalDestOperacao = "2";
            } else {
                idLocalDestOperacao = "1";
            }
            ide.setIdDest(idLocalDestOperacao);
        } catch (ParseException ex) {
            throw new DbfException("Erro ao manipular a data", ex);
        }
        
        LojaModel lojaModel = LojaBean.getLojaPorCodigo(nfeModel.getLoja());
        ide.setCMunFG(Integer.toString(lojaModel.getCidade().getIbgeCodigo()));

        NFeUF uf = NFeUF.porSigla(lojaModel.getCidade().getEstado().getSigla());
        ide.setCUF(uf.getCodigo());

        /**
         * 1 eh Normal eh emissao normal;<br/>
         * 2 eh Contingencia FS eh emissao em contingencia com impressao do DANFE em Formulario de Seguranca; <br/>
         * 3 eh Contingencia SCAN eh emissao em contingencia no Sistema de Contingencia do Ambiente Nacional eh SCAN;<br/>
         * 4 eh Contingencia DPEC - emissao em contingencia com envio da Declaracao Previa de Emissao em Contingencia eh DPEC;<br/>
         * 5 eh Contingencia FS-DA - emissao em contingencia com impressao do DANFE em Formulario de Seguranca para Impressao de Documento Auxiliar de Documento Fiscal Eletronico (FS-DA)<br/>
         * 6 Contingencia SVC-AN (SEFAZ Virtual de Contingencia do AN);<br/>
         * 7 Contin- gencia SVC-RS (SEFAZ Virtual de Contingencia do RS);<br/>
         * 9 Contingencia off-line da NFC-e (as demais opces de contingencia sao validas tambem para NFC-e);<br/>
         * OBS: Para a NFC-e somente estao disponoeis e sao validas as opces de contingencia 5 e 9.<<br/>
         */
        ide.setTpEmis("1");
        // 1 - PRODUCAO
        // 2 - HOMOLOGACAO
        ide.setTpAmb(Integer.toString(NFeUtil.getAmbiente()));
        ide.setMod(NFeUtil.getModelo());
        ide.setSerie("1");

        //Indica operacao com consumidor final
        // 0 Normal
        // 1 Consumidor Final
        //BEMATECH utiliza da seguinte forma
        //Operacao com consumidor final
        //No sistema bematech ERP a identificacao que uma operacao foi realizada
        //para consumidor final ocorre quando no cadastro da pessoa o campo 
        //Inscricao Estadual nao esta preenchido. Desta forma, a tag IndFinal 
        //sera preenchido como "1". Caso a pessoa da operacao possua inscricao 
        //estadual preenchida, a tag IndFinal sera preenchida como "0".
        //Considerando que o cliente eh comercio entao ele revende
        //caso nao seja comercio entao eh consumidor final
        CadastroModel c = CadastroModel.getCadastroPorCodigoLoja(nfeModel.getCadastroLoja(), nfeModel.getCadastroCodigo());
        ide.setIndFinal((c.getTributacaoCodigo() == 0 ? "0" : "1"));

        //Indicador de presenca do comprador no estabelecimento no momento da 
        //operacao
        // 0 Nao se aplica (por exemplo, Nota Fiscal complementar ou ajuste)
        // 1 Operacao presencial
        // 2 Operacao nao presencial, pela internet
        // 3 Operacao nao presencial, Teleatendimento
        // 4 NFC-e em operacao com entrega a domicilio
        // 5 Operação presencial, fora do estabelecimento;
        // 9 Operacao nao presencial, outros
        ide.setIndPres(nfeModel.getVendedorNome().toUpperCase().contains("ECOMMERCE") ? "2" : operacao.isComplementoImposto() || operacao.isComplementoValor() ? "0" : "1");
        ide.setTpNF(nfeModel.getSaida() ? "1" : "0");

        /**
         * Identificador do processo de emissao da NF-e: 0 - emissao de NF-e com
         * aplicativo do contribuinte; 1 - emissao de NF-e avulsa pelo Fisco; 2
         * - emissao de NF-e avulsa, pelo contribuinte com seu certificado
         * digital, atraves do site do Fisco; 3- emissao NF-e pelo contribuinte
         * com aplicativo fornecido pelo Fisco.
         */
        ide.setProcEmi("0");
        ide.setVerProc("4.00");
        // 1 Normal
        // 2 Complementar
        // 3 De ajuste
        // 4 Devolucao de mercadoria
        ide.setFinNFe(operacao.isDevolucao() ? "4" : operacao.isComplementoImposto() || operacao.isComplementoValor() ? "2" : operacao.isTransferenciaIcms() ? "3" : "1");

        //Tipo de impressao da danfe
        //1 - retrato
        //2 - paisagem
        ide.setTpImp("1");

        //TO-DO
        //Referencias a outras NFes e Cupons devem constar aqui
        return ide;
    }

    private String converteData(Date data) throws ParseException {
        String dataString = "";
        SimpleDateFormat dateFormatGmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sszzz");
        //Time in GMT
        //quando o horario de verão adianta 1 hora, o time zone precisa ser 
        //02:00 e quando atrasa é 03:00
        String horaTimeZone = System.getProperty("timeZone.dataHora.horarioVerao", "03:00");
        dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT-" + horaTimeZone));
        dataString = dateFormatGmt.format(data).toString().replaceAll("GMT", "");
        if (dataString.length() > 24) {
            int i;
            String pedacoString1;
            String pedacoString2;
            if (dataString.contains(".")) {
                i = dataString.indexOf(".");
                pedacoString1 = dataString.substring(0, i);
                i = dataString.indexOf("-" + horaTimeZone);
                pedacoString2 = dataString.substring(i, dataString.length());
                dataString = pedacoString1 + pedacoString2;
            }
        }
        return dataString;
    }

    protected void validar(String xml, String urlXsd) throws DbfException {
        Validador v = new Validador();
        ValidadorListener vl = new ValidadorListener();
        try {
            URL url = new URL(urlXsd);
            v.validar(xml, vl, url);
        } catch (ParserConfigurationException ex) {
            throw new DbfException("Erro de configuracao ao tentar validar o XML", ex);
        } catch (SAXException ex) {
            throw new DbfException("Erro de XML ao tentar validar o XML\n" + ex.getLocalizedMessage(), ex);
        } catch (IOException ex) {
            throw new DbfException("Erro de arquivo ao tentar validar o XML", ex);
        }
        if (!vl.semErros()) {
            StringBuilder msg = new StringBuilder("Erro de validacao do XML ");
            List<SAXParseException> erros = vl.getErros();
            for (SAXParseException erro : erros) {
                msg.append("\n").append(erro.getLocalizedMessage());
                logger.log(Level.SEVERE, "Erro de validacao", erro);
            }
            erros = vl.getFatals();
            for (SAXParseException erro : erros) {
                msg.append("\n").append(erro.getLocalizedMessage());
                logger.log(Level.SEVERE, "Erro fatal de validacao", erro);
            }
            throw new DbfException(msg.toString());
        }
    }
}
