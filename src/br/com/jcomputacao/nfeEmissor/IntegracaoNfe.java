package br.com.jcomputacao.nfeEmissor;

import br.com.autogeral.emissorfiscal.cliente.EmissorFiscalClienteDocumentoFiscal;
import br.com.autogeral.emissorfiscal.cliente.EmissorFiscalClienteFactory;
import br.com.autogeral.emissorfiscal.dto.DocumentoFiscalDTO;
import br.com.jcomputacao.exception.DbfDatabaseException;
import br.com.jcomputacao.exception.DbfException;
import br.com.jcomputacao.model.AnpModel;
import br.com.jcomputacao.model.BoletoModel;
import br.com.jcomputacao.model.CadastroEnderecoModel;
import br.com.jcomputacao.model.CadastroEnderecoTipo;
import br.com.jcomputacao.model.CadastroModel;
import br.com.jcomputacao.model.CadastroTelefoneTipo;
import br.com.jcomputacao.model.CestNcmModel;
import br.com.jcomputacao.model.CompraItemDBFModel;
import br.com.jcomputacao.model.EmpresaTributacao;
import br.com.jcomputacao.model.Entidade;
import br.com.jcomputacao.model.EntidadeEndereco;
import br.com.jcomputacao.model.EntidadeTelefone;
import br.com.jcomputacao.model.FinalidadeTipo;
import br.com.jcomputacao.model.IndicadorPagamento;
import br.com.jcomputacao.model.LojaModel;
import br.com.jcomputacao.model.ModoPagamentoModel;
import br.com.jcomputacao.model.MovimentoOperacaoModel;
import br.com.jcomputacao.model.NFeStatus;
import br.com.jcomputacao.model.NfceModel;
import br.com.jcomputacao.model.NfeImpostoAdicionalModel;
import br.com.jcomputacao.model.NfeItemModel;
import br.com.jcomputacao.model.NfeLote;
import br.com.jcomputacao.model.NfeLoteRetorno;
import br.com.jcomputacao.model.NfeModel;
import br.com.jcomputacao.model.NfePagamentoModel;
import br.com.jcomputacao.model.NfePagamentoParcelaModel;
import br.com.jcomputacao.model.NfeReferenciaModel;
import br.com.jcomputacao.model.ProdutoDBFModel;
import br.com.jcomputacao.model.ProdutoOrigem;
import br.com.jcomputacao.model.TipoMaquinaCartao;
import br.com.jcomputacao.model.VendaTipo;
import br.com.jcomputacao.model.beans.LojaBean;
import br.com.jcomputacao.model.beans.ModoPagamentoBean;
import br.com.jcomputacao.model.beans.MovimentoOperacaoBean;
import br.com.jcomputacao.model.beans.ProdutoTributacaoBean;
import br.com.jcomputacao.nfe.ChaveAcesso;
import br.com.jcomputacao.nfe.NFeUtil;
import br.com.jcomputacao.nfe.XmlUtil;
import br.com.jcomputacao.nfe.assinatura.Assinador;
import br.com.jcomputacao.nfe.assinatura.AssinadorTipo;
import br.com.jcomputacao.nfe.ws.WsConnectionConfig;
import static br.com.jcomputacao.nfeEmissor.Servico.context;
import static br.com.jcomputacao.nfeEmissor.Servico.logger;
import br.com.jcomputacao.nfeIntegracao.BuscaRetornoNfe;
import br.com.jcomputacao.nfeIntegracao.ServicoEnviaNfes;
import br.com.jcomputacao.tributacao.DeOlhoNoImpostoLogic;
import br.com.jcomputacao.util.Ambiente;
import br.com.jcomputacao.util.NumberUtil;
import br.com.jcomputacao.util.StringUtil;
import br.inf.portalfiscal.nfe.xml.pl009v4_2021.nfes.ObjectFactory;
import br.inf.portalfiscal.nfe.xml.pl009v4_2021.nfes.TEnderEmi;
import br.inf.portalfiscal.nfe.xml.pl009v4_2021.nfes.TEndereco;
import br.inf.portalfiscal.nfe.xml.pl009v4_2021.nfes.TIpi;
import br.inf.portalfiscal.nfe.xml.pl009v4_2021.nfes.TIpi.IPINT;
import br.inf.portalfiscal.nfe.xml.pl009v4_2021.nfes.TIpi.IPITrib;
import br.inf.portalfiscal.nfe.xml.pl009v4_2021.nfes.TLocal;
import br.inf.portalfiscal.nfe.xml.pl009v4_2021.nfes.TNFe;
import br.inf.portalfiscal.nfe.xml.pl009v4_2021.nfes.TNFe.InfNFe;
import br.inf.portalfiscal.nfe.xml.pl009v4_2021.nfes.TNFe.InfNFe.Cobr.Dup;
import br.inf.portalfiscal.nfe.xml.pl009v4_2021.nfes.TNFe.InfNFe.Dest;
import br.inf.portalfiscal.nfe.xml.pl009v4_2021.nfes.TNFe.InfNFe.Det;
import br.inf.portalfiscal.nfe.xml.pl009v4_2021.nfes.TNFe.InfNFe.Det.Imposto;
import br.inf.portalfiscal.nfe.xml.pl009v4_2021.nfes.TNFe.InfNFe.Det.Imposto.COFINS;
import br.inf.portalfiscal.nfe.xml.pl009v4_2021.nfes.TNFe.InfNFe.Det.Imposto.COFINS.COFINSAliq;
import br.inf.portalfiscal.nfe.xml.pl009v4_2021.nfes.TNFe.InfNFe.Det.Imposto.COFINS.COFINSNT;
import br.inf.portalfiscal.nfe.xml.pl009v4_2021.nfes.TNFe.InfNFe.Det.Imposto.COFINSST;
import br.inf.portalfiscal.nfe.xml.pl009v4_2021.nfes.TNFe.InfNFe.Det.Imposto.ICMS;
import br.inf.portalfiscal.nfe.xml.pl009v4_2021.nfes.TNFe.InfNFe.Det.Imposto.ICMS.ICMS00;
import br.inf.portalfiscal.nfe.xml.pl009v4_2021.nfes.TNFe.InfNFe.Det.Imposto.ICMS.ICMS10;
import br.inf.portalfiscal.nfe.xml.pl009v4_2021.nfes.TNFe.InfNFe.Det.Imposto.ICMS.ICMS40;
import br.inf.portalfiscal.nfe.xml.pl009v4_2021.nfes.TNFe.InfNFe.Det.Imposto.ICMS.ICMS51;
import br.inf.portalfiscal.nfe.xml.pl009v4_2021.nfes.TNFe.InfNFe.Det.Imposto.ICMS.ICMS60;
import br.inf.portalfiscal.nfe.xml.pl009v4_2021.nfes.TNFe.InfNFe.Det.Imposto.ICMS.ICMS90;
import br.inf.portalfiscal.nfe.xml.pl009v4_2021.nfes.TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN101;
import br.inf.portalfiscal.nfe.xml.pl009v4_2021.nfes.TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN102;
import br.inf.portalfiscal.nfe.xml.pl009v4_2021.nfes.TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN201;
import br.inf.portalfiscal.nfe.xml.pl009v4_2021.nfes.TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN202;
import br.inf.portalfiscal.nfe.xml.pl009v4_2021.nfes.TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN500;
import br.inf.portalfiscal.nfe.xml.pl009v4_2021.nfes.TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN900;
import br.inf.portalfiscal.nfe.xml.pl009v4_2021.nfes.TNFe.InfNFe.Det.Imposto.ICMSUFDest;
import br.inf.portalfiscal.nfe.xml.pl009v4_2021.nfes.TNFe.InfNFe.Det.Imposto.PIS;
import br.inf.portalfiscal.nfe.xml.pl009v4_2021.nfes.TNFe.InfNFe.Det.Imposto.PIS.PISAliq;
import br.inf.portalfiscal.nfe.xml.pl009v4_2021.nfes.TNFe.InfNFe.Det.Imposto.PIS.PISNT;
import br.inf.portalfiscal.nfe.xml.pl009v4_2021.nfes.TNFe.InfNFe.Det.Imposto.PISST;
import br.inf.portalfiscal.nfe.xml.pl009v4_2021.nfes.TNFe.InfNFe.Det.ImpostoDevol;
import br.inf.portalfiscal.nfe.xml.pl009v4_2021.nfes.TNFe.InfNFe.Det.ImpostoDevol.IPI;
import br.inf.portalfiscal.nfe.xml.pl009v4_2021.nfes.TNFe.InfNFe.Det.Prod;
import br.inf.portalfiscal.nfe.xml.pl009v4_2021.nfes.TNFe.InfNFe.Det.Prod.Comb;
import br.inf.portalfiscal.nfe.xml.pl009v4_2021.nfes.TNFe.InfNFe.Emit;
import br.inf.portalfiscal.nfe.xml.pl009v4_2021.nfes.TNFe.InfNFe.Ide;
import br.inf.portalfiscal.nfe.xml.pl009v4_2021.nfes.TNFe.InfNFe.Ide.NFref;
import br.inf.portalfiscal.nfe.xml.pl009v4_2021.nfes.TNFe.InfNFe.Ide.NFref.RefECF;
import br.inf.portalfiscal.nfe.xml.pl009v4_2021.nfes.TNFe.InfNFe.Pag;
import br.inf.portalfiscal.nfe.xml.pl009v4_2021.nfes.TNFe.InfNFe.Pag.DetPag;
import br.inf.portalfiscal.nfe.xml.pl009v4_2021.nfes.TNFe.InfNFe.Pag.DetPag.Card;
import br.inf.portalfiscal.nfe.xml.pl009v4_2021.nfes.TNFe.InfNFe.InfIntermed;
import br.inf.portalfiscal.nfe.xml.pl009v4_2021.nfes.TNFe.InfNFe.Total;
import br.inf.portalfiscal.nfe.xml.pl009v4_2021.nfes.TNFe.InfNFe.Total.ICMSTot;
import br.inf.portalfiscal.nfe.xml.pl009v4_2021.nfes.TNFe.InfNFe.Total.RetTrib;
import br.inf.portalfiscal.nfe.xml.pl009v4_2021.nfes.TNFe.InfNFe.Transp;
import br.inf.portalfiscal.nfe.xml.pl009v4_2021.nfes.TNFe.InfNFe.Transp.Transporta;
import br.inf.portalfiscal.nfe.xml.pl009v4_2021.nfes.TNfeProc;
import br.inf.portalfiscal.nfe.xml.pl009v4_2021.nfes.TProcEvento;
import br.inf.portalfiscal.nfe.xml.pl009v4_2021.nfes.TProtNFe;
import br.inf.portalfiscal.nfe.xml.pl009v4_2021.nfes.TProtNFe.InfProt;
import br.inf.portalfiscal.nfe.xml.pl009v4_2021.nfes.TRetConsReciNFe;
import br.inf.portalfiscal.nfe.xml.pl009v4_2021.nfes.TRetEnviNFe;
import br.inf.portalfiscal.nfe.xml.pl009v4_2021.nfes.TRetEvento;
import br.inf.portalfiscal.nfe.xml.pl009v4_2021.nfes.TUf;
import br.inf.portalfiscal.nfe.xml.pl009v4_2021.nfes.TUfEmi;
import br.inf.portalfiscal.nfe.xml.pl009v4_2021.nfes.TVeiculo;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.rmi.RemoteException;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import javax.swing.JOptionPane;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.stream.StreamSource;
import static br.com.jcomputacao.util.NfeUtil.isAdicionaFreteNoTotal;

/**
 * 07/03/2016 12:27:11
 *
 * @author murilo.lima
 */
public class IntegracaoNfe extends Servico {

    private static final String prolog = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>";
    private String chave;
    private String cnpj;
    private boolean simples = false;
    private final boolean tributaIpi = Boolean.parseBoolean(System.getProperty("nfe.tributaIpi", "false"));;
    public static final String ALIQUOTA_ZERO_PIS_COFINS = "ALIQUOTA REDUZIDA A 0 (ZERO) % PARA PIS E COFINS CONFORME ARTIGO 3o DA LEI 10485 DE 07/2002.";
    public static final String SUBSTITUICAO_TRIBUTARIA = "SUBSTITUICAO TRIBUTARIA CONFORME ARTIGO 313/O DO DECRETO 52804/08.";
    private final int valorQuantidadePrecisao = Integer.parseInt(System.getProperty("decimal.precisao", "2"));    
    private Date notBefore;
    private Date notAfter;
    private boolean valorFCPSTRetido;
    private String informacaoAdicionalProduto = "";
    private final boolean nfeTributaDifal = Boolean.parseBoolean(System.getProperty("nfe.tributa.difal", "false"));
    private boolean isAdicionaFreteNoTotal = false; 
    private final EmissorFiscalClienteDocumentoFiscal efClienteDocFiscal = (EmissorFiscalClienteDocumentoFiscal) EmissorFiscalClienteFactory.getCliente(DocumentoFiscalDTO.class);
    // Dever� ser usada da forma que est� abaixo
    private transient boolean isUsingEmissorFiscal = Boolean.parseBoolean(System.getProperty("emissor-fiscal.ativo","false"));
    private transient List<String> operacoesDevolucaoParaFornecedorPeloEmissor;
    private transient boolean isDevolucaoParaFornecedorPeloEmissorFiscal = false;
    private DocumentoFiscalDTO docFiscalDto;
    private final IntegracaoNfeEmissorFiscal nfeEmissorFiscal = new IntegracaoNfeEmissorFiscal();
    /*
        Contabilidade disse que temos que informar na OBS, que foi uma venda balc�o
        quando o cliente � de fora do estado mas fazemos venda com CFOP interno.
    */
    private boolean obsEntregaBalcaoClienteForaEstado = false;

    public IntegracaoNfe() {
        String operacoes = System.getProperty("operacoes.devolucao.pelo.emissor-fiscal", "6,7,23,28,39,40");
        this.operacoesDevolucaoParaFornecedorPeloEmissor = Arrays.asList(operacoes.split(","));
    }
    
    public Date getNotBefore() {
        return notBefore;
    }

    public Date getNotAfter() {
        return notAfter;
    }

    private int trataRetorno(String recibo, NfeModel nfe, String idLote) throws DbfDatabaseException, DbfException {
        NfeLoteRetorno nfeLoteRetorno = new NfeLoteRetorno();
        nfeLoteRetorno.setLote(idLote);
        nfeLoteRetorno.setXml(recibo);
        int status = 0;
        boolean isNfeUpdated = false;
        String chaveAcessoAntes = nfe.getNfeChaveAcesso();

        try {
            Unmarshaller unmarshaller = context.createUnmarshaller();
            ByteArrayInputStream bais = new ByteArrayInputStream(recibo.getBytes("UTF-8"));
            TRetConsReciNFe rcr = unmarshaller.unmarshal(new StreamSource(bais), TRetConsReciNFe.class).getValue();
            bais.close();

            logger.log(Level.FINE, "Cod.Status : {0}", rcr.getCStat());
            nfeLoteRetorno.setStatusRecibo(rcr.getCStat());
            status = nfeLoteRetorno.getStatusRecibo();
            nfe.setProtocoloStatus(rcr.getCStat());
            if (status == 105) {
                nfe.setStatus(NFeStatus.PROCESSAMENTO);
            }
            logger.log(Level.FINE, "Motivo     : {0}", rcr.getXMotivo());
            nfeLoteRetorno.setMotivo(rcr.getXMotivo());
            logger.log(Level.FINE, "Cod.Msg    : {0}", rcr.getCMsg());
            nfeLoteRetorno.setCodigoMensagem(rcr.getCMsg());
            logger.log(Level.FINE, "Cod.Msg    : {0}", rcr.getXMsg());
            nfeLoteRetorno.setMensagem(rcr.getXMsg());
            logger.log(Level.FINE, "Motivo     : {0}", rcr.getNRec());
            nfeLoteRetorno.setRecibo(rcr.getNRec());
            logger.log(Level.FINE, "Versao     : {0}", rcr.getVersao());
            logger.log(Level.FINE, "Ver.Aplic. : {0}", rcr.getVerAplic());
            logger.log(Level.FINE, "Ambiente   : {0}", rcr.getTpAmb());
            nfeLoteRetorno.setTipoAmbiente(rcr.getTpAmb());

            List<TProtNFe> protNFes = rcr.getProtNFe();
            if (protNFes != null) {
                for (TProtNFe prot : protNFes) {
                    InfProt infProt = prot.getInfProt();
                    if (infProt != null) {
                        logger.log(Level.FINE, "Motivo    : {0}", infProt.getXMotivo());
                        String cStat = infProt.getCStat();
                        if (cStat != null) {
                            status = Integer.parseInt(cStat);
                        }
                        nfe.setProtocoloStatus(cStat);
                        if (100 == status) {
                            trataSucesso(nfe, prot);
                        } else if (105 == status) {
                            nfe.setNfeChaveAcesso(StringUtil.somenteNumeros(infProt.getChNFe()));
                            nfe.setStatus(NFeStatus.PROCESSAMENTO);
                        } else if (301 == status || 302 == status || 110 == status) {
                            nfe.setNfeChaveAcesso(StringUtil.somenteNumeros(infProt.getChNFe()));
                            nfe.setNumeroProtocolo(infProt.getNProt());
                            nfe.setProtocoloDataHora(infProt.getDhRecbto().toString());
                            if (infProt.getDigVal() != null) {
                                List<String> digest = XmlUtil.getTagConteudo(nfeLoteRetorno.getXml(), "digVal", false);
                                if (digest != null && digest.size() > 0) {
                                    nfe.setDigestValue(digest.get(0));
                                }
                            }
                            nfe.setStatus(NFeStatus.DENEGADA);
                            trataDenegacao(nfe, prot);
                        } else if (539 == status) {
                            String motivo = infProt.getXMotivo();
                            Ambiente.debug("539 Motivo : " + motivo + " Chave protocolo " + infProt.getXMotivo());
                            if (motivo.contains("[") && motivo.contains("]")) {
                                String dchave = motivo.substring((motivo.indexOf("[") + 1), motivo.indexOf("]"));
                                nfe.setNfeChaveAcesso(StringUtil.somenteNumeros(dchave));
                            } else {
                                String dchave = StringUtil.somenteNumeros(motivo);
                                if (dchave != null && dchave.length() == 44) {
                                    nfe.setNfeChaveAcesso(dchave);
                                } else {
                                    nfe.setNfeChaveAcesso(StringUtil.somenteNumeros(infProt.getChNFe()));
                                }
                            }
                        } else if (204 != status) { // no caso de erro 204 a chave dever ser mantida para buscar a situacao posteriormente
                            if (!chaveAcessoAntes.equals(infProt.getChNFe())) {
                                nfe.setNfeChaveAcesso(StringUtil.somenteNumeros(infProt.getChNFe()));
                            }
                            nfe.setNfeXml(null);
                        }
                        isNfeUpdated = nfe.update();
                        logger.log(Level.FINE, "NFe salva {0}", isNfeUpdated);
                        if (100 != status && 105 != status && 302 != status && 110 != status && 301 != status) {
                            String msg = infProt.getCStat() + " " + infProt.getXMotivo();
                            throw new DbfException(msg);
                        }
                    } else {
                        logger.log(Level.FINE, "nao ha informacoes para este protocolo");
                    }
                }
            } else {
                logger.log(Level.FINE, "nao ha protocolos para o lote");
            }
        } catch (JAXBException ex) {
            logger.log(Level.SEVERE, "Erro ao converter o XML em objeto", ex);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Erro com o arquivo XML", ex);
        } finally {
            nfeLoteRetorno.store();
            if (nfe.isUpdateble()) {
                isNfeUpdated = nfe.update();
                logger.log(Level.FINE, "NFe {0}.{1} salva {2}", new Object[]{nfe.getNumero(), nfe.getLoja(), isNfeUpdated });
            }
        }
        
        return status;
    }
    
    public int enviar(NfceModel nfce) throws DbfException {
        return -1;
    }

    public int enviar(NfeModel nfe) throws DbfException, IOException {
        NfeLote lote;
        cnpj = obtemCnpjEmitente(nfe);
        WsConnectionConfig.setProperties(cnpj);

        if (nfe.getStatus().ordinal() < NFeStatus.PROCESSAMENTO.ordinal()) {
            lote = enviarLote(nfe);
        } else if (nfe.getStatus() == NFeStatus.PROCESSAMENTO) {
            lote = new NfeLote();
            if (!lote.retrieve(Integer.toString(nfe.getLoja()), nfe.getNfeLote())) {
                nfe.setStatus(NFeStatus.DIGITACAO);
                nfe.setProtocoloStatus("0");
                if (nfe.update()) {
                    lote = enviarLote(nfe);
                } else {
                    throw new DbfException("Nfe ja transmitida mas lote nao encontrado " + nfe.getNfeLote(), false);
                }
            }
        } else {
            throw new DbfException("Nfe ja transmitida e processada", false);
        }

        int status = 0;
        try {
            BuscaRetornoNfe retorno = new BuscaRetornoNfe();
            String recibo = retorno.consultar(Long.toString(lote.getRecibo()));
            String fname = "lote" + lote.getLote() + "-ret.xml";
            try {
                escreve(fname, recibo);
            } catch (IOException ex) {
                throw new DbfException("Erro ao tentar escrever o arquivo " + lote, ex, false);
            }
//            logger.log(Level.FINE, "Recibo : {0}", recibo);
            System.out.println("Rebibo " + recibo);
            status = trataRetorno(recibo, nfe, Long.toString(lote.getLote()));
        } catch (RemoteException ex) {
            throw new DbfException("Erro ao tentar obter o retorno do recibo " + Long.toString(lote.getRecibo()), ex, false);
        } catch (XMLStreamException ex) {
            throw new DbfException("Erro ao tentar obter o retorno do recibo " + Long.toString(lote.getRecibo()), ex, false);
        }
        return status;
    }

    private NfeLote enviarLote(NfeModel nfe) throws DbfException, IOException {
        String xml = converterEAssinar(nfe);

        String fname = (chave != null ? chave : "") + "-nfe.xml";
        try {
            escreve(fname, xml);
        } catch (IOException ex) {
            throw new DbfException("Erro ao tentar escrever o arquivo " + fname, ex);
        }
        validar(xml);
        nfe.setNfeXml(xml);
        nfe.setStatus(NFeStatus.ASSINADA);

        long timeInMillis = Calendar.getInstance().getTimeInMillis();
        String idLote = Long.toString(timeInMillis);

        logger.log(Level.FINE, "ID Lote : {0}", idLote);
        /*
         a tag <indSinc>0</indSinc> foi adicionada ao lote de envio da NFe
         O valor 0 indica que o tipo de envio � ass�ncrono e neste caso, ao enviar
         a nfe, um recibo � gerado com um n�mero onde � poss�vel consultar a situacao
         desta nota.
         O valor 1 indica que o tipo de envio � s�ncrono e nenhum recibo � gerado para 
         consulta futura, o pr�prio retorno do envio j� mostra se houve rejei��o ou j�
         traz o xml autorizado. Esse tipo de envio s� ocorrer� se o emitente solicitar
         e constar apenas uma NFe no lote, e a SEFAZ autorizadora implementar o proces-
         samento s�ncrono para a resposta do lote de NFe
         
         */
        String lote = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><enviNFe xmlns=\"http://www.portalfiscal.inf.br/nfe\" versao=\"4.00\">"
                + "<idLote>" + idLote + "</idLote>"
                + "<indSinc>0</indSinc>"
                + xml.replace(prolog, "")
                + "</enviNFe>";
        logger.log(Level.FINE, lote);
        nfe.setNfeLote(idLote);

        try {
            fname = "lote" + idLote + ".xml";
            escreve(fname, lote);
        } catch (IOException ex) {
            throw new DbfException("Erro ao tentar escrever o arquivo " + lote, ex);
        }
        ServicoEnviaNfes envio = new ServicoEnviaNfes();
        String resultado = null;
        try {
            resultado = envio.enviar(lote);
        } catch (XMLStreamException ex) {
            throw new DbfException("Erro ao tentar se comunicar com o servico da SEFAZ", ex, false);
        } catch (RemoteException ex) {
            throw new DbfException("Erro ao tentar se comunicar com o servico da SEFAZ", ex, false);
        }
        lote = "lote" + idLote + "-ret.xml";
        try {
            escreve(lote, resultado);
        } catch (IOException ex) {
            throw new DbfException("Erro ao tentar escrever o arquivo " + lote, ex);
        }
        Ambiente.debug("Colocando o numero do lote na nfe " + nfe.update());
        NfeLote loteRetorno = trataLoteRetorno(resultado, idLote);
        try {
            Thread.sleep(loteRetorno.getTempoMedio() * 1000);
        } catch (InterruptedException ex) {
            throw new DbfException("Erro ao tentar aguardar " + loteRetorno.getTempoMedio() + " segundos ", ex);
        }
        return loteRetorno;
    }

    private NfeLote trataLoteRetorno(String resultado, String idLote) throws DbfDatabaseException, DbfException {
        NfeLote nfeLote = new NfeLote();
        nfeLote.setLote(idLote);
        nfeLote.setXml(resultado);

        logger.log(Level.INFO, "Lote salvo : {0}", nfeLote.store());
        TRetEnviNFe rLote = null;
        try {
            rLote = quantosSegundosAEsperar(resultado);
        } catch (JAXBException ex) {
            throw new DbfException("Erro ao tentar tratar o retorno do envio da NFe", ex);
        } catch (UnsupportedEncodingException ex) {
            throw new DbfException("Erro ao tentar tratar o retorno do envio da NFe", ex);
        }
//        logger.log(Level.FINE, "Recebido em   : {0}", rLote.getDhRecbto().toString());
//        nfeLote.setRecebido(rLote.getDhRecbto().toString());
        logger.log(Level.FINE, "Status        : {0}", rLote.getCStat());
        nfeLote.setStatus(rLote.getCStat());
        logger.log(Level.FINE, "UF            : {0}", rLote.getCUF());
        logger.log(Level.FINE, "Tipo Ambiente : {0}", rLote.getTpAmb());
        logger.log(Level.FINE, "Vers\u00c3\u00a3o aplic. : {0}", rLote.getVerAplic());
        logger.log(Level.FINE, "Vers\u00c3\u00a3o        : {0}", rLote.getVersao());
        logger.log(Level.FINE, "Motivo        : {0}", rLote.getXMotivo());

        if (rLote.getInfRec() != null) {
            String recibo = rLote.getInfRec().getNRec();
            String tMed = rLote.getInfRec().getTMed();
            logger.log(Level.FINE, "Numero recibo : {0}", recibo);
            logger.log(Level.FINE, "Tempo m\u00c3\u00a9dio   : {0}", tMed);
            nfeLote.setRecibo(recibo);
            nfeLote.setTempoMedio(tMed);
            logger.log(Level.FINE, "Lote atualizado : {0}", nfeLote.update());
        } else {
            logger.log(Level.FINE, "Lote atualizado : {0}", nfeLote.update());
        }
        return nfeLote;
    }

    public void trataSucesso(NfeModel nfe, TProtNFe prot) throws IOException, JAXBException, DbfException {
        InfProt infProt = prot.getInfProt();
        nfe.setNfeChaveAcesso(StringUtil.somenteNumeros(infProt.getChNFe()));
        nfe.setStatus(NFeStatus.AUTORIZADA);
        logger.log(Level.FINE, "Status    : {0}", infProt.getCStat());
        nfe.setProtocoloStatus(infProt.getCStat());
        logger.log(Level.FINE, "Chave NFe : {0}", infProt.getChNFe());
        logger.log(Level.FINE, "Data/Hora : {0}", infProt.getDhRecbto().toString());
        nfe.setProtocoloDataHora(infProt.getDhRecbto().toString());
        logger.log(Level.FINE, "N# Prot.  : {0}", infProt.getNProt());
        String xml = colocaProtocoloNoXmlDaNfe(nfe, prot);
        nfe.setNumeroProtocolo(infProt.getNProt());
        if (infProt.getDigVal() != null) {
            List<String> digest = XmlUtil.getTagConteudo(xml, "DigestValue", false);
            if (digest != null && digest.size() > 0) {
                nfe.setDigestValue(digest.get(0));
            }
        }
    }

    public void trataSucessoCancelamento(NfeModel nfe, TProcEvento proc) throws IOException, JAXBException, DbfException {
        TRetEvento.InfEvento infEvent = proc.getRetEvento().getInfEvento();
        nfe.setNfeChaveAcesso(StringUtil.somenteNumeros(infEvent.getChNFe()));
        nfe.setStatus(NFeStatus.CANCELADA);
        logger.log(Level.FINE, "Status    : {0}", infEvent.getCStat());
        nfe.setProtocoloStatus(infEvent.getCStat());
        logger.log(Level.FINE, "Chave NFe : {0}", infEvent.getChNFe());
        logger.log(Level.FINE, "Data/Hora : {0}", infEvent.getDhRegEvento().toString());
        nfe.setProtocoloDataHora(infEvent.getDhRegEvento().toString());
        logger.log(Level.FINE, "N# Prot.  : {0}", infEvent.getNProt());
        colocaProtocoloNoXmlDaNfeCancelada(nfe, proc);
        nfe.setNumeroProtocolo(infEvent.getNProt());
    }
    
    public void trataDenegacao(NfeModel nfe, TProtNFe prot) throws JAXBException, UnsupportedEncodingException, IOException {
        Unmarshaller unmarshaller = context.createUnmarshaller();
        TNfeProc proc = new TNfeProc();
        ByteArrayInputStream bais = new ByteArrayInputStream((nfe.getNfeXml()).getBytes("UTF-8"));
        TNFe tnfe = unmarshaller.unmarshal(new StreamSource(bais), TNFe.class).getValue();
        bais.close();
        proc.setNFe(tnfe);
        proc.setVersao("4.00");
        proc.setProtNFe(prot);

        Marshaller marshaller = context.createMarshaller();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        marshaller.marshal(proc, baos);
        String xml = baos.toString();
        xml = xml.replaceAll("xmlns:ns2=\".+#\"\\s", "").replaceAll("ns2:", "").replaceAll("tNfeProc", "nfeProc").replace("<Signature>", "<Signature xmlns=\"http://www.w3.org/2000/09/xmldsig#\">");

        InfProt infProt = prot.getInfProt();
        String fileName = infProt.getChNFe() + "-procNFe.xml";
        if (xml.contains("<NFe>")) {
            xml = xml.replace("<NFe>", "<NFe xmlns=\"http://www.portalfiscal.inf.br/nfe\">");
        }
        escreve(fileName, xml);
        nfe.setNfeXml(xml);        
    }

    private String colocaProtocoloNoXmlDaNfeCancelada(NfeModel nfe, TProcEvento proc) throws IOException, JAXBException {
        Marshaller marshaller = context.createMarshaller();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        marshaller.marshal(proc, baos);
        String xml = baos.toString();
        xml = xml.replaceAll("xmlns:ns2=\".+#\"\\s", "").replaceAll("ns2:", "");

        String fileName = proc.getRetEvento().getInfEvento().getChNFe() + "-procEventoCancNFe.xml";
        escreve(fileName, xml);
        if (!"135".equals(nfe.getProtocoloStatus())) {
            nfe.setProtocoloStatus("135");
        }
        nfe.setNfeXmlCancelamento(xml);
        return xml;
    }

    private TRetEnviNFe quantosSegundosAEsperar(String lote) throws JAXBException, UnsupportedEncodingException {
        Unmarshaller unmarshaller = context.createUnmarshaller();
        InputStream is = new ByteArrayInputStream((lote).getBytes("UTF-8"));
        TRetEnviNFe rcr = unmarshaller.unmarshal(new StreamSource(is), TRetEnviNFe.class).getValue();
        return rcr;
    }

    public String converterEAssinar(NfeModel nfe) throws DbfException, IOException {
        return assinar(converter(nfe));
    }
        
    public String converter(NfeModel nfe) throws DbfException, IOException {
        String xml;
        this.isDevolucaoParaFornecedorPeloEmissorFiscal = operacoesDevolucaoParaFornecedorPeloEmissor.contains(nfe.getOperacaoCodigoString());
        if (isUsingEmissorFiscal && (!this.isDevolucaoParaFornecedorPeloEmissorFiscal) && !nfe.getOperacao().isTransferenciaIcms()) {
            System.out.println("USANDO O EMISSOR-FISCAL! ");
            // Criar um DTO para converter o NFEModel para JSON (e ai sim enviar para o emissor-fiscal)
            this.docFiscalDto = new DocumentoFiscalDTO(nfe);
            Optional<DocumentoFiscalDTO> opDocFiscalDto = efClienteDocFiscal.save(docFiscalDto);
            if (opDocFiscalDto.isPresent()) {
                this.docFiscalDto = opDocFiscalDto.get();
                nfe = docFiscalDto.converteParaNfeOrSatModel(nfe, opDocFiscalDto.get());
            } else {
                isUsingEmissorFiscal = false;
            }
        } else {
            // Como a DEVOLU��O PARA FORNECEDOR agora (m�s 10/21) come�ar� a ser pelo emissor-fiscal
            // Ser� setado para TRUE (para montar os imposto no xml com as informa��es que vier do emissorfiscal)
            final boolean isUsingEmissorFiscalDevolucao = Boolean.parseBoolean(System.getProperty("emissor-fiscal.devolucao.ativo", "false"));
            isUsingEmissorFiscal = (isUsingEmissorFiscalDevolucao && isDevolucaoParaFornecedorPeloEmissorFiscal);
        }
        
        try {
            xml = exportarString(nfe);
            xml = xml.replaceAll("xmlns:ns2=\".+#\"\\s", "").replaceAll("ns2:", "");
        } catch (JAXBException ex) {
            throw new DbfException("Erro de XML oo tentar tornar a NFe um XML", ex);
        } catch (DbfDatabaseException ex) {
            throw new DbfException("Erro de banco de dados ao tentar tornar a NFe um XML", ex);
        }
        List<String> pedacosXml = XmlUtil.getTagConteudo(xml, "NFe", true);
        StringBuilder sb = new StringBuilder();
        for (String pedaco : pedacosXml) {
            sb.append(StringUtil.soEspacoSimples(pedaco).replace("\n", ""));
        }

        xml = sb.toString().replace("> <", "><");
        if (xml.contains("<NFe>")) {
            xml = xml.replace("<NFe>", "<NFe xmlns=\"http://www.portalfiscal.inf.br/nfe\">");
        }
        return xml;
    }

    public String assinar(String xml) throws DbfException {
        try {
            Ambiente.debug(xml);
            Assinador a = new Assinador();
            xml = a.assinar(xml, NFeUtil.getCertificadoCaminho(cnpj), NFeUtil.getCertificadoSenha(cnpj), AssinadorTipo.INFORMACAO, cnpj);
            notBefore = a.getNotBefore();
            notAfter = a.getNotAfter();
        } catch (Exception ex) {
            throw new DbfException("Erro ao tentar assinar o XML da NFe", ex);
        }
        return xml;
    }

    public String exportarString(NfeModel nfeModel) throws JAXBException, DbfDatabaseException, DbfException {
        cnpj = obtemCnpjEmitente(nfeModel);
        isAdicionaFreteNoTotal = isAdicionaFreteNoTotal(nfeModel.getFreteConta());
        return exportarString(exportarXml(nfeModel));
    }

    private TNFe exportarXml(NfeModel nfeModel) throws DbfDatabaseException, DbfException, JAXBException {
        TNFe nfe = new TNFe();
        TNFe.InfNFe inf = new TNFe.InfNFe();
        inf.setVersao("4.00");
        Ide ide = criaIdentificacao(nfeModel);
        inf.setIde(ide);        
        nfe.setInfNFe(inf);
        Emit emitente = criaEmitente(nfeModel);
        inf.setEmit(emitente);
        Dest destinatario = criaDestinatario(nfeModel);
        inf.setDest(destinatario);
        
        if(!"SP".equals(destinatario.getEnderDest().getUF().toString())
                && !nfeModel.getOperacao().getInterestadual()) {            
            this.obsEntregaBalcaoClienteForaEstado = true;
        }
        Pag pgto = criaPagamento(nfeModel);
        inf.setPag(pgto);
        // TODO ( ESSE 'infIntermed' CAMPO SOMENTE DEVER� SER PREENCHIDO, CASO O CAMPO "indIntermed" , seja == 1) (campo dentro do OBJ ide)
        // 
        //Indicador de presenca do comprador no estabelecimento no momento da operacao
        // 0 Nao se aplica (por exemplo, Nota Fiscal complementar ou ajuste)
        // 1 Operacao presencial
        // 2 Operacao nao presencial, pela internet
        // 3 Operacao nao presencial, Teleatendimento
        // 4 NFC-e em operacao com entrega a domicilio
        // 5 Opera��o presencial, fora do estabelecimento;
        // 9 Operacao nao presencial, outros
        // Quando N�O presencial && exigir intermediador ENTRARA na valida��o abaixo
//        if (!ide.getIndPres().equals("0") && !ide.getIndPres().equals("1")  
//                && !ide.getIndPres().equals("5") && !ide.getIndPres().equals("9")) {
//            InfIntermed infIntermed = criarInfoDoIntermediador();
//            inf.setInfIntermed(infIntermed);
//        }
        if (nfeModel.getEntrega()) {
            TLocal localEntrega = criarLocalEntrega(nfeModel);
            inf.setEntrega(localEntrega);
        }

        if (nfeModel.getReferencias() != null && !nfeModel.getReferencias().isEmpty()) {
            for (NfeReferenciaModel ref : nfeModel.getReferencias()) {
                NFref nFref = new NFref();
                if (ref.isNfe()) {
                    nFref.setRefNFe(ref.getChave());
                } else if (ref.isCupom()) {
                    RefECF refEcf = new RefECF();
                    refEcf.setMod(ref.getModeloEcf());
                    refEcf.setNECF(Integer.toString(ref.getEcf()));
                    refEcf.setNCOO(Integer.toString(ref.getCoo()));
                    nFref.setRefECF(refEcf);
                }
                ide.getNFref().add(nFref);
            }
        }

        if (nfeModel.isCobrancaGerada()) {
            TNFe.InfNFe.Cobr cob = new TNFe.InfNFe.Cobr();
            TNFe.InfNFe.Cobr.Fat fat = new TNFe.InfNFe.Cobr.Fat();
            List<BoletoModel> boletos = BoletoModel.getItens(" WHERE B.NF_NUMERO=" + nfeModel.getNota()
                    + " AND B.LOJA=" + nfeModel.getLoja()
                    + " AND B.NOTA_TIPO='NFE'");
            double valorTotalLiquido = 0;
            int i = 0;
            String nFaturas = "";
            for (BoletoModel bol : boletos) {
                Dup dup = new Dup();
                NumberFormat nf = NumberFormat.getInstance();
                nf.setMaximumIntegerDigits(3);
                nf.setMinimumIntegerDigits(3);
                dup.setNDup(nf.format(++i));
                dup.setDVenc(bol.getVencimentoDbString());
                dup.setVDup(NumberUtil.decimalBanco(bol.getValor()));
                valorTotalLiquido += bol.getValor();
                if(!nFaturas.isEmpty()) {
                    nFaturas += ", ";
                }
                nFaturas += bol.getCodigo() + "." + bol.getLoja();
                cob.getDup().add(dup);
            }
            fat.setNFat(StringUtil.limitaTamanho(nFaturas, 59));
            fat.setVLiq(NumberUtil.decimalBanco(valorTotalLiquido));            
            fat.setVOrig(NumberUtil.decimalBanco(valorTotalLiquido));
            fat.setVDesc("0.00");

            cob.setFat(fat);
            inf.setCobr(cob);
        }

        boolean insereCobrancaXmlAtravezNfesPagamentosParcelas = Boolean.parseBoolean(System.getProperty("insere.cobrancaXml.atravez.nfesPagamentosParcelas", "false"));
        if (insereCobrancaXmlAtravezNfesPagamentosParcelas) {
            if (inf.getCobr() == null) {
                NfePagamentoModel pagamento = new NfePagamentoModel();
                TNFe.InfNFe.Cobr cob = new TNFe.InfNFe.Cobr();
                for (NfePagamentoModel p : nfeModel.getPagamentos()) {
                    String where = " WHERE R.LOJA='" + nfeModel.getLoja() + "' AND R.NFE='" + nfeModel.getNfe() + "' "
                            + "AND R.PAGAMENTO='" + p.getPagamentoCodigo() + "'";
                    pagamento.setParcelas(NfePagamentoParcelaModel.getParcelas(where));
                }

                for (NfePagamentoParcelaModel nppm : pagamento.getParcelas()) {
                    Dup dup = new Dup();
                    dup.setNDup(nppm.getParcela() + "/" + pagamento.getParcelas().size());
                    dup.setDVenc(nppm.getVencimento().toString());
                    dup.setVDup(NumberUtil.decimalBanco(nppm.getValor()));
                    cob.getDup().add(dup);
                }
                inf.setCobr(cob);
            }
        }

        List<Det> detalhes = inf.getDet();
        System.setProperty("nfe.descricao.simples", nfeModel.getOperacao().isComplementoImposto() ? "false" : "true");
        preencheDetalhes(nfeModel, detalhes, destinatario);
        System.setProperty("nfe.descricao.simples", "true");

        if (StringUtil.isNotNull(nfeModel.getNfeChaveAcesso())) {
            chave = nfeModel.getNfeChaveAcesso();
            ChaveAcesso ca = new ChaveAcesso(chave);
            ide.setCNF(ca.getCodigoNumerico());
            ide.setCDV(ca.getDigito());
        } else {
            Marshaller marshaller = context.createMarshaller();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            marshaller.marshal(nfe, baos);
            String xml = baos.toString();
            xml = xml.replaceAll("xmlns:ns2=\".+#\"\\s", "").replaceAll("ns2:", "");
            String cnf = ChaveAcesso.gerarCodigoNumerico(xml);
            try {                
                //Evitando salvar a chave de acesso quando o codigo numerico da mesma
                //nao � gerado.
                if (Long.parseLong(cnf) == 0L) {
                    ide = null;
                }
                ide.setCNF(cnf);
            } catch (NullPointerException ex) {
                throw new DbfException("Erro ao tentar gerar a chave de acesso ", ex, false);
            }

            String anoEmissao = ide.getDhEmi().substring(0, 4);
            String mesEmissao = ide.getDhEmi().substring(5, 7);
            String tipoEmissao = ide.getTpEmis();
            if (cnpj == null) {
                cnpj = obtemCnpjEmitente(nfeModel);
            }
            String achave = ChaveAcesso.gerarChaveAcesso(ide.getCUF(), anoEmissao, mesEmissao, cnpj, ide.getMod(), ide.getSerie(), ide.getNNF(), tipoEmissao, cnf);
            String dv = ChaveAcesso.gerarDigitoChaveAcesso(achave);
            chave = achave + dv;
            nfeModel.setNfeChaveAcesso(chave);
            ide.setCDV(dv);
        }

        inf.setId("NFe" + chave);

        inf.setTotal(criaTotal(nfeModel));
        inf.setTransp(criaTransporte(nfeModel));
        atribuiObservacoes(inf, nfeModel);

        return nfe;
    }

    private String exportarString(TNFe nfe) throws JAXBException {
        Marshaller marshaller = context.createMarshaller();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        TNfeProc proc = new TNfeProc();
        proc.setVersao("4.00");
        TProtNFe protocolo = new TProtNFe();
        InfProt infProt = new InfProt();
        infProt.setNProt("ABRACADABRA");
        protocolo.setInfProt(infProt);
        protocolo.setVersao("4.00");
        proc.setProtNFe(protocolo);
        proc.setNFe(nfe);
        marshaller.marshal(proc, baos);
        return baos.toString();
    }

    private Total criaTotal(NfeModel nfeModel) throws DbfDatabaseException, DbfException {
        Total total = new Total();
        total.setICMSTot(criaTotalIcms(nfeModel));
        total.setRetTrib(criaTotalRet(nfeModel));
        return total;
    }

    private Transp criaTransporte(NfeModel nota) throws DbfException {
        Transp transporte = new Transp();

        /**
         * 0=Contrata��o do Frete por conta do Remetente (CIF);
         * 1=Contrata��o do Frete por conta do Destinat�rio (FOB);
         * 2=Contrata��o do Frete por conta de Terceiros;
         * 3=Transporte Pr�prio por conta do Remetente;
         * 4=Transporte Pr�prio por conta do Destinat�rio;
         * 9=Sem Ocorr�ncia de Transporte
         * 
         */
        transporte.setModFrete(Integer.toString(nota.getFreteConta().getNfeCodigo()));
        
        if(StringUtil.isNotNull(nota.getPlaca()) && !nota.getPlaca().isEmpty()) {
            TVeiculo veiculo = new TVeiculo();
            if(nota.getPlaca().contains("/")) {
                System.out.println(nota.getPlaca());
                veiculo.setPlaca(nota.getPlaca().substring(0, nota.getPlaca().indexOf("/")));
                veiculo.setUF(TUf.valueOf(nota.getPlaca().substring(nota.getPlaca().indexOf("/") + 1, nota.getPlaca().length())));
            } else {
                veiculo.setPlaca(nota.getPlaca());
                veiculo.setUF(TUf.SP);
            }
            transporte.setVeicTransp(veiculo);
        }

        if (nota.getTransportadoraCodigo() > 0) {
            CadastroModel tmodel = new CadastroModel();
            if (tmodel.retrieve(NumberUtil.getNullSafeForUI(nota.getTransportadoraLoja()), 
                    NumberUtil.getNullSafeForUI(nota.getTransportadoraCodigo()))) {
                Transporta transp = new Transporta();
                transp.setXNome(StringUtil.ajusta(StringUtil.htmlIso8859encode(tmodel.getNome()), 60, StringUtil.ALINHAMENTO_ESQUERDA).trim());
                transp.setIE(StringUtil.somenteNumerosELetras(tmodel.getIe()));
                transp.setCNPJ(StringUtil.ajusta(StringUtil.somenteNumeros(tmodel.getCnpj()), 14, StringUtil.ALINHAMENTO_DIREITA, '0'));                
                transp.setXEnder(StringUtil.htmlIso8859encode(tmodel.getEndereco()).trim() + ", " + (tmodel.getEnderecoNumero() > 0 ? tmodel.getEnderecoNumero() : "S/N"));

                if (tmodel.getCidade() == null) {
                    throw new DbfException("Transportadora sem UF");
                }
                transp.setUF(TUf.valueOf(tmodel.getCidade().getEstado().getSigla()));
                transp.setXMun(StringUtil.noDeadKeysToUpperCase(tmodel.getCidade().getDescricao()));
                transporte.setTransporta(transp);
            }

        }

        //nota.getPesoBruto()
        if (nota.getNumeroVolumes() > 0 || StringUtil.isNotNull(nota.getEspecieVolumes())
                || StringUtil.isNotNull(nota.getMarcaVolume()) || (nota.getPesoBruto() > 0)
                || (nota.getPesoLiquido() > 0)) {
            TNFe.InfNFe.Transp.Vol volume = new TNFe.InfNFe.Transp.Vol();

            if (nota.getNumeroVolumes() > 0) {
                volume.setNVol(NumberUtil.decimalBanco(nota.getNumeroVolumes()));
            }
            if (StringUtil.isNotNull(nota.getQuantidadeVolumes())) {
                volume.setQVol(nota.getQuantidadeVolumes());
            }
            if (StringUtil.isNotNull(nota.getEspecieVolumes())) {
                volume.setEsp(nota.getEspecieVolumes());
            }
            if (StringUtil.isNotNull(nota.getMarcaVolume())) {
                volume.setMarca(nota.getMarcaVolume());
            }
            if (nota.getPesoBruto() > 0) {
                volume.setPesoB(NFeUtil.tresCasas(nota.getPesoBruto()));
            }
            if (nota.getPesoLiquido() > 0) {
                volume.setPesoL(NFeUtil.tresCasas(nota.getPesoLiquido()));
            }
            transporte.getVol().add(volume);
        }
        return transporte;
    }

    private RetTrib criaTotalRet(NfeModel nfeModel) {
        RetTrib ret = null;
//        RetTrib ret = new RetTrib();
//
//        Double aliquotaPis = Double.parseDouble(System.getProperty("nfe.pis.aliquota", "1.65"));
//        double porcentagemPis = aliquotaPis/100;
//        double valorPis = nfeModel.getTotalNaoIsento()*porcentagemPis;
//        ret.setVRetPIS(NumberUtil.decimalBanco(valorPis));
//
//
//        Double aliquotaCofins = Double.parseDouble(System.getProperty("nfe.cofins.aliquota", "6.0"));
//        double porcentagemCofins = aliquotaCofins/100;
//        double valorCofins = nfeModel.getTotalNaoIsento()*porcentagemCofins;
//        ret.setVRetCOFINS(NumberUtil.decimalBanco(valorCofins));

        return ret;
    }

    private ICMSTot criaTotalIcms(NfeModel nota) throws DbfDatabaseException, DbfException {
        // DESCOMENTAR DEPOIS que CADASTRAR as TRIBUTACOES DE ICMS
        if (isUsingEmissorFiscal) {
            ICMSTot icms = nfeEmissorFiscal.atribuiTotalIcms(nota, this.isDevolucaoParaFornecedorPeloEmissorFiscal);
            return icms;
        }
        
        ICMSTot icms = new ICMSTot();
        boolean destacaImpostoCorpoNotaParaSimplesNacional = Boolean.parseBoolean(
                    System.getProperty("destaca.impostos.corpoNota", "false"));
        MovimentoOperacaoModel operacao = MovimentoOperacaoBean.getMovimentoPorCodigo(nota.getOperacaoCodigo());
        if (operacao != null && operacao.isTransferenciaIcms()) {
            icms.setVBC(NumberUtil.decimalBanco(0));
            icms.setVICMS(NumberUtil.decimalBanco(0));
        } else {
            icms.setVBC(NumberUtil.decimalBanco((!simples || (simples && destacaImpostoCorpoNotaParaSimplesNacional) ? nota.getIcmsBase() : 0)));
            icms.setVICMS(NumberUtil.decimalBanco((!simples || (simples && destacaImpostoCorpoNotaParaSimplesNacional) ? nota.getIcmsValor() : 0)));
        }
        
        if (nota.getIcmsValor() > 0
                || (nota.getOperacao().isComplementoImposto() && nota.getIcmsStBase() > 0)) {
            icms.setVBCST(NumberUtil.decimalBanco(nota.getIcmsStBase()));
            //at� que realmente precise, Mirella do Office disse para deixar sempre 
            // com valor = 0.00 o ICMS Desonera��o
            icms.setVICMSDeson(NumberUtil.decimalBanco(0));
        } else {
            icms.setVBCST(NumberUtil.decimalBanco(0));
            //at� que realmente precise, Mirella do Office disse para deixar sempre 
            // com valor = 0.00 o ICMS Desonera��o
            icms.setVICMSDeson(NumberUtil.decimalBanco(0));
        }        
        //uso, alem da variavel que indica se as notas tributam ou nao o difal,
        //o valor do icms de destino para validar se o sistema preenche ou 
        //n�o essa parte do XML, pois o Difal sempre ter� o valor do icms de destino
        //maior que zero quando o mesmo for preenchido na NFe
        if(this.nfeTributaDifal && nota.getValorIcmsUFDestino() > 0) {
            icms.setVFCPUFDest(NumberUtil.decimal(nota.getValorIcmsIndicePobreza()).replace(",", "."));
            icms.setVICMSUFDest(NumberUtil.decimal(nota.getValorIcmsUFDestino()).replace(",", "."));
            icms.setVICMSUFRemet(NumberUtil.decimal(nota.getValorIcmsUFRemetente()).replace(",", "."));
        }
        icms.setVFCP(!this.valorFCPSTRetido && nota.getValorIcmsUFDestino() == 0 && !this.simples ? NumberUtil.decimal(nota.getValorIcmsIndicePobreza()).replace(",", ".") : "0.00");
        icms.setVFCPST(!this.valorFCPSTRetido ? NumberUtil.decimal(nota.getValorIcmsSTIndicePobreza()).replace(",", ".") : "0.00");
        icms.setVFCPSTRet(this.valorFCPSTRetido ? NumberUtil.decimal(nota.getValorIcmsSTIndicePobreza()) : "0.00");

        icms.setVST(NumberUtil.decimalBanco(nota.getIcmsStValor()));
        icms.setVProd(NumberUtil.decimalBanco(nota.getValorProdutos()));
        String vFrete = isAdicionaFreteNoTotal ? NumberUtil.decimalBanco(nota.getValorFrete()) : "0.00";
        icms.setVFrete(vFrete);
        icms.setVSeg(NumberUtil.decimalBanco(nota.getValorSeguro()));
        if(nota.isDestacaDescontoNoCorpoDoDocumentoFiscal()
                && nota.getDescontoValor() > 0) {
            icms.setVDesc(NumberUtil.decimalBanco(nota.getDescontoValor()));
        } else {
            icms.setVDesc("0.00");
        }
        icms.setVII("0.00");
        icms.setVIPI(this.tributaIpi ? NumberUtil.decimalBanco(nota.getValorIpi()) : "0.00");
        icms.setVIPIDevol(!this.tributaIpi ? NumberUtil.decimalBanco(nota.getValorIpi()) : "0.00");
        icms.setVOutro(NumberUtil.decimalBanco(nota.getOutrasDespesas()));
        icms.setVNF(NumberUtil.decimalBanco(nota.getValorTotal()));

        if (simples) {            
            boolean possuiItemCst202 = false;
            //segundo o que foi passado pela contabilidade o CST 202 qdo 
            //tributa pis cofins, precisa destacar na nota para o simples
            for(NfeItemModel i : nota.getNotaItens()) {
                if(i.getSituacaoTributaria() == 202 && !i.getPisCofins()) {
                    possuiItemCst202 = true;
                    break;
                }
            }
            if (destacaImpostoCorpoNotaParaSimplesNacional || possuiItemCst202) {
                Double aliquotaPis = Double.parseDouble(System.getProperty("nfe.pis.aliquota", "1.65"));
                double porcentagemPis = aliquotaPis / 100;
                if (operacao == null) {
                    throw new DbfException("Nao foi encontrada operacao para a nfe " + nota.getNumero() + "." + nota.getLoja());
                }
                if (operacao.isTransferencia()) {
                    icms.setVPIS(NumberUtil.decimalBanco(0d));
                    icms.setVCOFINS(NumberUtil.decimalBanco(0d));
                } else {
                    double valorPis = nota.getTotalNaoIsento() * porcentagemPis;
                    icms.setVPIS(NumberUtil.decimalBanco(valorPis));

                    Double aliquotaCofins = Double.parseDouble(System.getProperty("nfe.cofins.aliquota", "6.0"));
                    double porcentagemCofins = aliquotaCofins / 100;
                    double valorCofins = nota.getTotalNaoIsento() * porcentagemCofins;
                    icms.setVCOFINS(NumberUtil.decimalBanco(valorCofins));
                }
            } else {
                icms.setVPIS(NumberUtil.decimalBanco(0));
                icms.setVCOFINS(NumberUtil.decimalBanco(0));
            }
        } else {
            Double aliquotaPis = Double.parseDouble(System.getProperty("nfe.pis.aliquota", "1.65"));
            double porcentagemPis = aliquotaPis / 100;
            if (operacao == null) {
                throw new DbfException("Nao foi encontrada operacao para a nfe " + nota.getNumero() + "." + nota.getLoja());
            }
            List<Integer> operacoesPisCofinsZerado = Arrays.asList(24, 25, 79);
            if (operacao.isTransferencia() || operacoesPisCofinsZerado.contains(nota.getOperacao().getCodigo())) {
                icms.setVPIS(NumberUtil.decimalBanco(0d));
                icms.setVCOFINS(NumberUtil.decimalBanco(0d));
            } else {
                if (isUsingEmissorFiscal) {
                    icms.setVPIS(NumberUtil.decimalBanco(nota.getPisValor()));
                    icms.setVCOFINS(NumberUtil.decimalBanco(nota.getCofinsValor()));
                    return icms;
                }
                double valorPis = nota.getTotalNaoIsento() * porcentagemPis;
                icms.setVPIS(NumberUtil.decimalBanco(valorPis));

                Double aliquotaCofins = Double.parseDouble(System.getProperty("nfe.cofins.aliquota", "6.0"));
                double porcentagemCofins = aliquotaCofins / 100;
                double valorCofins = nota.getTotalNaoIsento() * porcentagemCofins;
                icms.setVCOFINS(NumberUtil.decimalBanco(valorCofins));
            }
        }
        return icms;
    }

    private Emit criaEmitente(NfeModel nfeModel) throws DbfDatabaseException {
        LojaModel estab = LojaBean.getLojaPorCodigo(nfeModel.getLoja());
        Emit emit = new Emit();
        emit.setXNome(StringUtil.ajusta(StringUtil.htmlIso8859encode(estab.getRazaoSocial()), 60, StringUtil.ALINHAMENTO_ESQUERDA).trim());
        emit.setXFant(StringUtil.ajusta(StringUtil.htmlIso8859encode(estab.getNomeFantasia()), 60, StringUtil.ALINHAMENTO_ESQUERDA).trim());

        emit.setCNPJ(obtemCnpjEmitente(nfeModel));
        emit.setIE(StringUtil.somenteNumerosELetras(estab.getIe()));
        if (StringUtil.isNotNull(estab.getIm())) {
            emit.setIM(estab.getIm());
            emit.setCNAE(estab.getCnae());
        }        
        simples = !EmpresaTributacao.NORMAL.equals(estab.getTributacao());
        emit.setCRT(Integer.toString(estab.getTributacao().getCrt()));
        TEnderEmi endereco = new TEnderEmi();
        endereco.setCPais("1058");
        endereco.setXPais("BRASIL");
        endereco.setUF(TUfEmi.valueOf(estab.getCidade().getEstado().getSigla()));
        endereco.setXMun(estab.getCidade().getDescricao());
        endereco.setCMun(Integer.toString(estab.getCidade().getIbgeCodigo()));
        endereco.setCEP(StringUtil.somenteNumeros(estab.getCep()));
        endereco.setXBairro(trataString(estab.getBairro()));
        endereco.setXLgr(trataString(estab.getLogradouroEndereco()));
        endereco.setNro(NumberUtil.getNullSafeForUI(estab.getNumeroEndereco()));
        if (StringUtil.isNotNull(estab.getComplementoEndereco().trim()) && !estab.getComplementoEndereco().isEmpty()) {
            endereco.setXCpl(StringUtil.htmlIso8859encode(estab.getComplementoEndereco().trim()));
        }
        endereco.setFone(StringUtil.somenteNumeros(estab.getTelefone()));
        emit.setEnderEmit(endereco);
        return emit;
    }

    private Dest criaDestinatario(NfeModel nfeModel) throws DbfException {
        Entidade ent = null;
        EntidadeEndereco entEnd = null;
        EntidadeTelefone entTel = null;
        CadastroModel cm = new CadastroModel();
        if (cm.retrieve(nfeModel.getCadastroLojaString(), nfeModel.getCadastroCodigoString())) {
            if (nfeModel.getCadastroCodigo() != 0) {
                cm.valida();
            } else {
                throw new DbfException("Nao encontrou o cadastro da Nfe");
            }
            ent = cm;
            entEnd = cm.getEndereco(CadastroEnderecoTipo.PRINCIPAL, CadastroEnderecoTipo.FATURAMENTO, CadastroEnderecoTipo.FINANCEIRO, CadastroEnderecoTipo.ENTREGA);
            entTel = cm.getTelefone(CadastroTelefoneTipo.PRINCIPAL, CadastroTelefoneTipo.RESIDENCIAL, CadastroTelefoneTipo.COMERCIAL, CadastroTelefoneTipo.CELULAR, CadastroTelefoneTipo.CELULAR_COMERCIAL, CadastroTelefoneTipo.CELULAR_PESSOAL, CadastroTelefoneTipo.RECADOS, CadastroTelefoneTipo.OUTROS);

        } else {
            throw new DbfException("nao encontrou o destinatario da NFe " + nfeModel.getNumero() + "." + nfeModel.getLoja());
        }
        Dest dest = new Dest();
        dest.setXNome(StringUtil.ajusta(trataString(ent.getNome()), 60, StringUtil.ALINHAMENTO_ESQUERDA).trim());
        if (ent.isPessoaFisica()) {
            String doc = StringUtil.somenteNumeros(ent.getCpfCnpj());
            doc = StringUtil.ajusta(doc, 11, StringUtil.ALINHAMENTO_DIREITA, '0');
            dest.setCPF(doc);
            dest.setIndIEDest("9");
        } else {
            String doc = StringUtil.somenteNumeros(ent.getCpfCnpj());
            doc = StringUtil.ajusta(doc, 14, StringUtil.ALINHAMENTO_DIREITA, '0');
            dest.setCNPJ(doc);
            //dest.setIndIEDest((StringUtil.isNull(ent.getRgIe())) ? "2" : "1");
            //http://tdn.totvs.com/pages/releaseview.action;jsessionid=05EE4EEDC1A9644B0FF7E1BF9DE41EDE?pageId=219121739
            //Caso for informado a IE do Cliente e esteja marcado como ?Contribuinte ICMS?, o resultado ser� ?1 = Contribuinte ICMS?.
            //Caso N�O for informado a IE do Cliente e esteja marcado como ?Contribuinte ICMS?, o resultado ser� ?2 = Contribuinte Isento?.
            //Caso N�O esteja marcado como ?Contribuinte ICMS?, o resultado ser� ?9 = N�o Contribuinte?.
            String indicadorIEDest = (StringUtil.isNull(ent.getRgIe())) && cm.getTributacaoCodigo() == 0 ? "2" : (StringUtil.isNotNull(ent.getRgIe())) && cm.getTributacaoCodigo() == 0 ? "1" : "9";
            dest.setIndIEDest(indicadorIEDest);
            if (StringUtil.isNotNull(ent.getRgIe())) {
                dest.setIE(ent.getRgIe());
            }
        }
        if (StringUtil.isNotNull(ent.getEmail())) {
            dest.setEmail(ent.getEmail());
        }

        if ("2".equals(System.getProperty("nfe.ambiente", "2"))) {
//            if (dest.getCPF() != null && !dest.getCPF().isEmpty()) {
//                dest.setCPF(null);
//            }
            
//            dest.setCNPJ("99999999000191");
            dest.setXNome("NF-E EMITIDA EM AMBIENTE DE HOMOLOGACAO - SEM VALOR FISCAL");
//            dest.setIndIEDest("9");
//            dest.setIE(null);
        }

        TEndereco endereco = new TEndereco();
        endereco.setCPais("1058");
        endereco.setXPais("BRASIL");
        endereco.setUF(TUf.valueOf(entEnd.getCidade().getEstado().getSigla()));
        endereco.setXMun(entEnd.getCidade().getDescricao());
        endereco.setCMun(Integer.toString(entEnd.getCidade().getIbgeCodigo()));
        endereco.setCEP(StringUtil.somenteNumeros(entEnd.getCep()));
        if (StringUtil.isNull(entEnd.getBairro())) {
            throw new DbfException("Bairro do cliente n�o encontrado para utilizacao em documento fiscal");
        }
        endereco.setXBairro(trataString(entEnd.getBairro()));
        endereco.setXLgr(trataString(entEnd.getLogradouroEndereco()));
        if (0 == entEnd.getNumeroEndereco()) {
            endereco.setNro("S/N");
        } else {
            endereco.setNro(Integer.toString(entEnd.getNumeroEndereco()));
        }
        if (StringUtil.isNotNull(entEnd.getComplementoEndereco())) {
            endereco.setXCpl(trataString(entEnd.getComplementoEndereco()));
        }
        
        boolean validarTelefone = Boolean.parseBoolean(System.getProperty("cliente.validarTelefones", "true"));
        if (validarTelefone) {
            if (StringUtil.isNull(entTel.getTelefone())) {
                throw new DbfException("Telefone nao encontrador para utilizacao em documento fiscal");
            }
            endereco.setFone(StringUtil.somenteNumeros(entTel.getTelefone()));
        }
        dest.setEnderDest(endereco);
        return dest;
    }
    
    private Pag criaPagamento(NfeModel nfeModel) throws DbfDatabaseException {
//        vai precisar de troco?
        MovimentoOperacaoModel operacao = MovimentoOperacaoBean.getMovimentoPorCodigo(nfeModel.getOperacaoCodigo());
        Pag pag = new Pag();
        if (operacao.isBaixaConsumo()) {
            DetPag detPag = new DetPag();
            detPag.setTPag("90");
            detPag.setVPag("0.00");
            pag.getDetPag().add(detPag);
//            detPag.setXPag("Baixa de Consumo");
        }
        for (NfePagamentoModel pagamento : nfeModel.getPagamentos()) {
            DetPag detPag = new DetPag();        
            IndicadorPagamento indicadorPagamento;
            if(!operacao.isVenda()) {
                //Para as notas com finalidade de Ajuste ou Devolu��o o
                //campo Meio de Pagamento deve ser preenchido com 90=Sem Pagamento
                detPag.setTPag("90");
                detPag.setVPag("0.00");
                pag.getDetPag().add(detPag);
                indicadorPagamento = IndicadorPagamento.OUTROS;
                break;
            } else {
                //01=Dinheiro
                //02=Cheque
                //03=Cart�o de Cr�dito
                //04=Cart�o de D�bito
                //05=Cr�dito Loja
                //10=Vale Alimenta��o
                //11=Vale Refei��o
                //12=Vale Presente
                //13=Vale Combust�vel
                //15=Boleto Banc�rio
                //90= Sem pagamento
                //99=Outros
                ModoPagamentoModel modoPagamento = ModoPagamentoBean.getModoPagamentoPorCodigo(pagamento.getModoPagamentoCodigo());
                int codigoNfePagamento = modoPagamento.getNfeCodigo().getMeioPagamentoCodigo();
                detPag.setTPag(Ambiente.ajusta(Integer.toString(codigoNfePagamento), 2, Ambiente.ALINHAMENTO_DIREITA, '0'));  
                detPag.setVPag(NumberUtil.decimalBanco(pagamento.getValorPagamento()));
                //0= Pagamento � Vista 1= Pagamento � Prazo
                indicadorPagamento = codigoNfePagamento == 1 || codigoNfePagamento == 4 ? IndicadorPagamento.A_VISTA : IndicadorPagamento.A_PRAZO;
                
                detPag.setIndPag(indicadorPagamento.getIndicadorPagamento());
                if (codigoNfePagamento == 99) {
                    detPag.setXPag(modoPagamento.getDescricaoRelatorio());
                } 
                if(codigoNfePagamento == 3 || codigoNfePagamento == 4) {
                    Card card = new Card();
                    //1 = Pagamento integrado com o sistema de automa��o da
                    //  empresa (Ex.: equipamento TEF, Com�rcio Eletr�nico);
                    //2 = Pagamento n�o integrado com o sistema de automa��
                    //   da empresa (Ex.: equipamento POS);
                    boolean tef = pagamento.getIntencaoVenda() != null && pagamento.getIntencaoVenda() > 1;
                    card.setTpIntegra(tef ? "1" : "2" );
                    //Quando for TEF e mudar o campo acima ai os campos abaixo
                    //ser�o obrigat�rios;
                    //Informar o CNPJ da Credenciadora de cart�o de cr�dito/d�bito
                    if (tef) {
                        //https://atendimento.tecnospeed.com.br/hc/pt-br/articles/360012471354-Principais-Empresas-Credenciadoras-Adquirentes-de-Cart%C3%A3o-de-Cr%C3%A9dito-e-seus-CNPJs-Campo-CNPJ-YA05-
                        card.setCNPJ(TipoMaquinaCartao.REDE.equals(pagamento.getMaquinaCartao()) ? "01425787000104" 
                                : TipoMaquinaCartao.STONE.equals(pagamento.getMaquinaCartao()) ? "34590184000109" : "");
                        //01=Visa
                        //02=Mastercard
                        //03=American Express
                        //04=Sorocred
                        //05=Diners Club
                        //06=Elo
                        //07=Hipercard
                        //08=Aura
                        //09=Cabal
                        //99=Outros
//                        card.setTBand(chave);
                        card.setCAut(pagamento.getNumeroDocumentoFinanceiro());   
                    }
                    detPag.setCard(card);
                }
            }
            pag.getDetPag().add(detPag);
        }        
        return pag;
    }

    /**
     * TODO
     * 
        -> � VENDA realizada pela AUTOGERAL (se o campo VEND_CANA estiver preenchida da seguinte forma)
                - Site
                - "----" ("em branco")
                - APP
                - Ou se tiver o idReferenciaPgto() (no obj nfePagamento)
            OU SEJA, n�o dever� ser preenchido o GRUPO ABAIXO
        -> CASO CONTRARIO, (preencher� o grupo abaixo)
               - Tem que ter o CNPJ do marktplace e do operador da transacao
     * 
     * "Grupo de Informa��es do Intermediador da transa��o". 
     * 
     * ESSE CAMPO SOMENTE DEVER� SER PREENCHIDO, CASO O CAMPO "indIntermed", seja == 1
     * 
     * PS: Preencher com as informa��es dos intermediadores
     * 
     * @return
     * @throws DbfDatabaseException 
     */
    private InfIntermed criarInfoDoIntermediador() throws DbfDatabaseException {
        InfIntermed infIntermed = new InfIntermed();
        infIntermed.setCNPJ(StringUtil.somenteNumeros(LojaBean.getLojaAtual().getCpfCnpj()));
        infIntermed.setIdCadIntTran(LojaBean.getLojaAtual().getNomeFantasia());

        return infIntermed;
    }
    
    private TLocal criarLocalEntrega(NfeModel nfeModel) throws DbfException {
        Entidade ent = null;
        CadastroModel cm = new CadastroModel();
        if (cm.retrieve(nfeModel.getCadastroLojaString(), nfeModel.getCadastroCodigoString())) {
            if (nfeModel.getCadastroCodigo() != 0) {
                cm.valida();
            } else {
                throw new DbfException("Nao encontrou o cadastro da Nfe");
            }
            ent = cm;            
        } else {
            throw new DbfException("nao encontrou o destinatario da NFe " + nfeModel.getNumero() + "." + nfeModel.getLoja());
        }
        CadastroEnderecoModel enderecoEntrega = nfeModel.getEnderecoEntregaVenda();
        if (enderecoEntrega == null) {
            throw new DbfException("Endereco de entrega nao encontrado!");
        }
        TLocal localEntrega = new TLocal();

        if (ent.isPessoaFisica()) {
            String doc = StringUtil.somenteNumeros(ent.getCpfCnpj());
            doc = StringUtil.ajusta(doc, 11, StringUtil.ALINHAMENTO_DIREITA, '0');
            localEntrega.setCPF(doc);
        } else {
            String doc = StringUtil.somenteNumeros(ent.getCpfCnpj());
            doc = StringUtil.ajusta(doc, 14, StringUtil.ALINHAMENTO_DIREITA, '0');
            localEntrega.setCNPJ(doc);
        }                
        localEntrega.setUF(TUf.valueOf(enderecoEntrega.getCidade().getEstado().getSigla()));
        localEntrega.setXMun(enderecoEntrega.getCidade().getDescricao());
        localEntrega.setCMun(Integer.toString(enderecoEntrega.getCidade().getIbgeCodigo()));        
        if (StringUtil.isNull(enderecoEntrega.getBairro())) {
            throw new DbfException("Bairro do cliente n�o encontrado para utilizacao em documento fiscal");
        }
        localEntrega.setXBairro(trataString(enderecoEntrega.getBairro()));
        localEntrega.setXLgr(trataString(enderecoEntrega.getLogradouroEndereco()));
        if (0 == enderecoEntrega.getNumeroEndereco()) {
            localEntrega.setNro("S/N");
        } else {
            localEntrega.setNro(Integer.toString(enderecoEntrega.getNumeroEndereco()));
        }
        if (StringUtil.isNotNull(enderecoEntrega.getComplementoEndereco())) {
            localEntrega.setXCpl(trataString(enderecoEntrega.getComplementoEndereco()));
        }
        return localEntrega;
    }

    private void preencheDetalhes(NfeModel nfeModel, List<Det> detalhes, Dest destinatario) throws DbfDatabaseException, DbfException {
        for (NfeItemModel item : nfeModel.getVendaItens()) {
            detalhes.add(criaDetalhe(item, nfeModel, destinatario));
        }
    }

    private Det criaDetalhe(NfeItemModel item, NfeModel nfe, Dest destinatario) throws DbfDatabaseException, DbfException {
        Det det = new Det();
        this.informacaoAdicionalProduto = "";
        det.setNItem(Integer.toString(item.getItem()));
        det.setProd(produto(item, destinatario));        
        det.setImposto(imposto(item));
        boolean operacaoSimplesDevolucao = nfe.getOperacaoCodigo() == 10 || nfe.getOperacaoCodigo() == 11;
        if (isUsingEmissorFiscal && item.getIpiValor() > 0 && !operacaoSimplesDevolucao) {
            ImpostoDevol ipiDevolvido = nfeEmissorFiscal.criaIpiDevolvido(item);
            det.setImpostoDevol(ipiDevolvido);
        } else if(!this.tributaIpi && item.getIpiValor() > 0 && !operacaoSimplesDevolucao) {//IPI DEVOLVIDO
            det.setImpostoDevol(criaImpostoDevolvido(item));
        }
        if(this.informacaoAdicionalProduto != null && !this.informacaoAdicionalProduto.isEmpty()) {
            det.setInfAdProd(this.informacaoAdicionalProduto);
        }
        return det;
    }

    private Prod produto(NfeItemModel item, Dest destinatario) throws DbfException {
        Prod prod = new Prod();
        if (item.getCfop() > 5600 && item.getCfop() < 5610) {
            prod.setCProd("CFOP" + item.getCfop());
        } else {
            String produtoCodigo = StringUtil.ajusta(item.getCodigoXString(), ProdutoDBFModel.getDigitosCodigoX(), StringUtil.ALINHAMENTO_DIREITA, '0')
                    + item.getCodigoSequencia();
            prod.setCProd(produtoCodigo);
        }
        String ignorados[] = new String[]{",", ".", "$"};
        prod.setCEAN(item.getCodigoBarras() != null && !item.getCodigoBarras().isEmpty() ? item.getCodigoBarras() : "SEM GTIN");


        // DESCOMENTAR DEPOIS que CADASTRAR as TRIBUTACOES DE ICMS
        if (isUsingEmissorFiscal) {
            prod.setCFOP(Integer.toString(item.getCfop()));
            String cestFormatado = CestNcmModel.formataCest(item.getCEST() == null ? 199900 : item.getCEST());
            prod.setCEST(StringUtil.somenteNumeros(cestFormatado));
        } else {
            prod.setCFOP(Integer.toString(item.getCfop()));
            if (item.getCfop() != 5605) {
                CestNcmModel ncm = buscarExistenciaCodigoCestParaItem(item);
                if (item.getCEST() != null) {
                    String cest = "0199900";
                    try {
                        cest = StringUtil.somenteNumeros(ncm.buscaCest());
                    } catch (NullPointerException ex) {
                        System.err.println("Erro ao tentar atribuir cest ::: " + ex.getStackTrace());
                    }
                    prod.setCEST(cest);
                } else {
                    //Cest para os produtos onde o ncm n�o est� relacionado a nenhum 
                    //item da lista de cests.
                    //Apenas para alguns segmentos existe esta opcao de outros
                    String cest = System.getProperty("codigo.cest.outros");
                    if (cest != null) {
                        prod.setCEST(cest);
                    }
                }             // DESCOMENTAR DEPOIS que CADASTRAR as TRIBUTACOES DE ICMS
                
            }
        }
      
        boolean descricaoSimples = Boolean.parseBoolean(System.getProperty("nfe.descricao.simples", "true"));
        String descricao = null;
        if (item.getFinalidadeCodigo() == FinalidadeTipo.COMERCIALIZACAO.ordinal() + 1 && descricaoSimples) {
            descricao = item.getDescricaoAbreviada();
        } else {
            descricao = item.getDescricao().trim();
        }

        descricao = StringUtil.htmlIso8859encode(descricao).trim();
        int limiteDescricao = NumberUtil.getIntegerNullSafe(System.getProperty("nfe.item.descricao.tamanho.limite", "115"));
        if (limiteDescricao > 0 && descricao.length() > limiteDescricao) {
            descricao = descricao.substring(0, limiteDescricao).trim();
        }
        prod.setXProd(descricao);
//        if(StringUtil.isNotNull(item.getCodigoBarras())) {
        prod.setCEAN(item.getCodigoBarras() != null && !item.getCodigoBarras().isEmpty() ? StringUtil.somenteNumeros(item.getCodigoBarras()) : "SEM GTIN");
        prod.setCEANTrib(item.getCodigoBarras() != null && !item.getCodigoBarras().isEmpty() ? StringUtil.somenteNumeros(item.getCodigoBarras()) : "SEM GTIN");        
//        }
        prod.setIndTot(null);
        prod.setEXTIPI(null);
        prod.setNCM(("".equals(item.getClasseFiscal()) ? null : StringUtil.somenteNumeros(item.getClasseFiscal())));

//        item
        prod.setQCom(NFeUtil.quatroCasas(item.getQuantidade()));
        prod.setUCom(StringUtil.noDeadKeysToUpperCase(item.getUnidade()));
        prod.setVUnCom(NumberUtil.decimalBanco(item.getValorUnidade(), valorQuantidadePrecisao));
        prod.setVProd(NumberUtil.decimalBanco(item.getValorTotal()));

        prod.setQTrib(prod.getQCom());
        prod.setUTrib(prod.getUCom());
        prod.setVUnTrib(prod.getVUnCom());
        
        if(item.isDestacaDescontoNoCorpoDoDocumentoFiscal() || (this.isUsingEmissorFiscal && this.isDevolucaoParaFornecedorPeloEmissorFiscal)
                && item.getDescontoValor() > 0) {
            prod.setVDesc(NumberUtil.decimalBanco(item.getDescontoValor()));
        }

        /**
         * Este campo devera ser preenchido com: 0 eh o valor do item (vProd)
         * nao compoe o valor total da NF-e (vProd) 1 eh o valor do item (vProd)
         * compoe o valor total da NF-e (vProd) (v2.0)
         */
        prod.setIndTot("1");

        if (item.getValorFrete() > 0 && isAdicionaFreteNoTotal) {
            prod.setVFrete(NumberUtil.decimalBanco(item.getValorFrete()));
        }
        if (item.getValorOutrasDespesas() > 0) {
            prod.setVOutro(NumberUtil.decimalBanco(item.getValorOutrasDespesas()));
        }

        if (StringUtil.isNotNull(item.getPedidoClienteNumero())) {
            prod.setXPed(item.getPedidoClienteNumero());
        }

        if (item.getPedidoClienteItem() > 0) {
            prod.setNItemPed(item.getPedidoClienteItemString());
        }

        /*
         * PODE ACONTECER DE N�O TER TRIBUTACAO MAS NOS CASOS EM QUE � UMA TRANSFERENCIA DE SALDO DEVEDOR
         * EM QUE NO ITEM DA NOTA N�O � UM PRODUTO E SIM UMA DESCRICAO DO SALTO DEVEDOR, POR ISSO A VERIFICA��O
         * SE EXITE TRIBUTACAO
         */
        if (item.getTributacaoCodigo() > 0) {
            if (ProdutoTributacaoBean.getTributacao(item.getTributacaoCodigo()).isCombustivelOuOleo()) {
                Comb comb = new Comb();
                comb.setUFCons(destinatario.getEnderDest().getUF());                
                if (item.getCodigoAnp() == null) {
                    throw new DbfException("O item " + prod.getCProd() + " " + prod.getXProd() + " eh oleo/lubrificante e esta sem o c�digo ANP");
                } else {
                    comb.setCProdANP(item.getCodigoAnp().toString());
                    comb.setDescANP(AnpModel.buscaDescricaoAnpPorCodigo(item.getCodigoAnp()));
                    prod.setComb(comb);
                }
            }
        }
        
        //Devemos replicar a informa��o do FCI das origens 3-5-8  das nossas 
        //compras para as nossas vendas
        if (item.getOrigem().equals(ProdutoOrigem.NACIONAL_CONTEUDO_IMPORTADO_MAIOR_40)
                || item.getOrigem().equals(ProdutoOrigem.NACIONAL_CONTEUDO_IMPORTADO_MAIOR_70)
                || item.getOrigem().equals(ProdutoOrigem.NACIONAL_CONTEUDO_IMPORTADO_MENOR_40)) {
            ProdutoDBFModel produto = new ProdutoDBFModel();
            //TODO o ideal vai ser procurar em todas as lojas, pois nem tudo mais ta vindo direto 
            //para matriz
            List<CompraItemDBFModel> ultimasCompras = produto.getUltimasComprasDoItem(item.getProdutoCodigo(), 1);
            CompraItemDBFModel itemFiltrado = null;
            if (ultimasCompras != null && !ultimasCompras.isEmpty()) {
                for (CompraItemDBFModel i : ultimasCompras) {
                    if (i.getXmlUltimaCompraItem() != null
                            && !i.getXmlUltimaCompraItem().isEmpty()) {
                        itemFiltrado = i;
                        break;
                    }
                }
                if (itemFiltrado != null) {
                    String xml = itemFiltrado.getXmlUltimaCompraItem();
                    String xmlProduto = xml.substring(xml.indexOf("<cProd>" + itemFiltrado.getCodigoProdutoFornecedor() + "</cProd>"));
                    xmlProduto = xmlProduto.substring(0, xmlProduto.indexOf("</prod>"));
                    List<String> tagConteudo = XmlUtil.getTagConteudo(xmlProduto, "nFCI", false);
                    if (tagConteudo != null && !tagConteudo.isEmpty()) {
                        prod.setNFCI(tagConteudo.get(0));
                    }
                }
            }
            
        }
        
        return prod;
    }

    private String colocaProtocoloNoXmlDaNfe(NfeModel nfe, TProtNFe prot) throws IOException, JAXBException {
        Unmarshaller unmarshaller = context.createUnmarshaller();
        TNfeProc proc = new TNfeProc();
        ByteArrayInputStream bais = new ByteArrayInputStream((nfe.getNfeXml()).getBytes("UTF-8"));
        TNFe tnfe = unmarshaller.unmarshal(new StreamSource(bais), TNFe.class).getValue();
        bais.close();
        proc.setNFe(tnfe);
        proc.setVersao("4.00");
        proc.setProtNFe(prot);

        Marshaller marshaller = context.createMarshaller();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        marshaller.marshal(proc, baos);
        String xml = baos.toString();
        xml = xml.replaceAll("xmlns:ns2=\".+#\"\\s", "").replaceAll("ns2:", "").replaceAll("tNfeProc", "nfeProc").replace("<Signature>", "<Signature xmlns=\"http://www.w3.org/2000/09/xmldsig#\">");

        InfProt infProt = prot.getInfProt();
        String fileName = infProt.getChNFe() + "-procNFe.xml";
        if (xml.contains("<NFe>")) {
            xml = xml.replace("<NFe>", "<NFe xmlns=\"http://www.portalfiscal.inf.br/nfe\">");
        }
        escreve(fileName, xml);
        nfe.setNfeXml(xml);
        return xml;
    }
    
    private ImpostoDevol criaImpostoDevolvido(NfeItemModel item) {
        ImpostoDevol impDev = new ImpostoDevol();
        IPI ipi = new IPI();
        double valorIpiDevolvido = (item.getIpiAliquota() > 1 ? item.getIpiBase() * (item.getIpiAliquota() / 100) : item.getIpiBase() * item.getIpiAliquota());
        ipi.setVIPIDevol(NumberUtil.decimalBanco(valorIpiDevolvido));
        impDev.setIPI(ipi);
        impDev.setPDevol(NumberUtil.decimalBanco((item.getIpiAliquota() < 1 ? item.getIpiAliquota() * 100 : item.getIpiAliquota())));
        return impDev;
    }
    
    private Imposto imposto(NfeItemModel item) throws DbfDatabaseException {
        Imposto imp = new Imposto();

        imp.getContent().add(new ObjectFactory().createTNFeInfNFeDetImpostoICMS(icms(item)));        
        if (this.tributaIpi && !simples) {
            imp.getContent().add(new ObjectFactory().createTNFeInfNFeDetImpostoIPI(ipi(item)));
        }
        //new ObjectFactory().createTNFeInfNFeDetImpostoDevolIPI()
        
        imp.getContent().add(new ObjectFactory().createTNFeInfNFeDetImpostoPIS(pis(item)));
//        imp.getContent().add(new ObjectFactory().createTNFeInfNFeDetImpostoPISST(pisSt(item)));
        imp.getContent().add(new ObjectFactory().createTNFeInfNFeDetImpostoCOFINS(cofins(item)));
//        imp.getContent().add(new ObjectFactory().createTNFeInfNFeDetImpostoCOFINSST(cofinsSt(item)));        
        if(this.nfeTributaDifal && item.getValorIcmsUFDestino() > 0) {
            imp.getContent().add(new ObjectFactory().createTNFeInfNFeDetImpostoICMSUFDest(difal(item)));
        }

        return imp;
    }

    private ICMS icms(NfeItemModel item) throws DbfDatabaseException {
        if (simples) {
            return icmsSimples(item);
        } else {
            return icmsNormal(item);
        }
    }

    private ICMS icmsNormal(NfeItemModel item) throws DbfDatabaseException {
        ICMS icms = new ICMS();
        String origem = Integer.toString(item.getOrigem().ordinal());
        String st = Integer.toString(item.getSituacaoTributaria() % 100);
        st = StringUtil.ajusta(st, 2, StringUtil.ALINHAMENTO_DIREITA, '0');

        // DESCOMENTAR DEPOIS que CADASTRAR as TRIBUTACOES DE ICMS
        if (isUsingEmissorFiscal) {
            return nfeEmissorFiscal.atribuiIcms(icms, item, origem);
        }
        
        switch (item.getCfop()) {            
            case 5201: 
            case 5927: 
            {
                if ("90".equals(st)) {
                    atribuiIcms90(icms, item, origem, st);
                } else {
                    atribuiIcms00(icms, item, origem, st);
                }
            }
            break;
            case 5102:
            case 5202:
            case 1202: //devolucao do 5102
            case 1102: //compra de sucata
            case 6101:
            case 6102:
            case 6202:
            case 6201:
            case 2102:
            case 2202:
            case 6107:
            case 6108:
            case 5152:
            case 5653:
            case 6653:
            case 5111:
            case 5101:
            case 6556:
            case 5919:
            case 5918://RETORNO DE CONSIGNACAO
            case 5410:
            case 5552: {
                if ("41".equals(st)) {
                    atribuiIcms40(icms, origem, st);
                }  else if("51".equals(st)) {
                    ICMS51 tributacaoIcms51 = new ICMS51();
                    tributacaoIcms51.setCST(st);
                    tributacaoIcms51.setOrig(origem);
                    icms.setICMS51(tributacaoIcms51);
                } else if("10".equals(st)) {
                    atribuiIcms10(icms, item, origem, st);
                } else {
                    atribuiIcms00(icms, item, origem, st);
                }
            }
            break;
            case 1201:
            case 5652:
            case 6652:
            case 5401:
            case 6401:
            case 2410:
            case 1410:
                atribuiIcms10(icms, item, origem, st);
                break;
            case 5908:
            case 6915:            
            case 1101:            
            case 6916://RETORNO DE MERCADORIA OU BEM RECEBIDO PARA CONSERTO OU REPARO
            case 6911://REMESSA DE AMOSTRA GRATIS FORA DO ESTADO
            case 5911://REMESSA DE AMOSTRA GRATIS DENTRO DO ESTADO
            case 6501://REMESSA DE PRODUCAO DO ESTABELECIMENTO COM FIM ESPECIFICO DE EXPORTACAO
            case 6109://VENDA INTERESTADUAL PARA ZONA FRANCA
                ICMS40 tributacaoIcms40 = new ICMS40();
                tributacaoIcms40.setCST(st);
                tributacaoIcms40.setOrig(origem);
                icms.setICMS40(tributacaoIcms40);
                break;
            case 5901:
            case 5902:
            case 6551:
            case 1916://RETORNO DE REMESSA PARA CONCERTO            
            case 5501:
                boolean icms40 = Boolean.parseBoolean(System.getProperty("nfe.integracao.cfop5901.icms40", "true"));
                if (icms40) {
                    ICMS40 tributacaoIcms5_4 = new ICMS40();
                    tributacaoIcms5_4.setCST(st);
                    tributacaoIcms5_4.setOrig(origem);
                    icms.setICMS40(tributacaoIcms5_4);
                } else {
                    ICMS51 tributacaoIcms51 = new ICMS51();
                    tributacaoIcms51.setCST(st);
                    tributacaoIcms51.setOrig(origem);
                    icms.setICMS51(tributacaoIcms51);
                }
                break;
            case 5405:            
            case 1661:
            case 1662:
            case 1411: //1411 dados tributarios sao iguais a 5405            
            case 5409:
            case 5656:            
            case 5659:
            case 6659:
            case 2556://devolucao interestadual com icms
            case 5661: {
                switch (item.getSituacaoTributaria()) {
                    case 10:
                        atribuiIcms10(icms, item, origem, st);
                        break;
                    case 60:
                        atribuiIcms60(icms, item, origem, st);
                        break;
                    default:
                        atribuiIcms00(icms, item, origem, st);
                        break;
                }
            }
            break;
            case 2411: //operacao interestadual
            {
                boolean existeCalculoSt = Boolean.parseBoolean(
                        System.getProperty("nfe.icms.substituicao", "false"));
                if (existeCalculoSt || item.getSituacaoTributaria() == 10) {
                    atribuiIcms10(icms, item, origem, st);
                } else {
                    atribuiIcms60(icms, item, origem, st);
                }
            }
            break;
            case 6404:
            case 6403:
            case 5411:
            case 5413:
            case 5910:
            case 6411:
            case 1949:
            case 2949:
            case 5949:            
            case 5925://RETORNO DE MERCADORIA PARA INDUSTRIALIZACAO, PARA O ADQUIRENTE POR NAO TER TRANSITADO A MESMA, NO ESTABELECIMENTO DO ADQUIRENTE
            case 5117://REMESSA DE VENDA PARA ENTREGA FUTURA         
            case 5551: // VENDA DE IMOBILIZADO
            case 6656://Venda de combust�vel ou lubrificante adquirido ou recebido de terceiros destinado a consumidor ou usu�rio final
            case 5915:
            case 1551://COMPRA DE ATIVO
            case 1407://COMPRA DE MATERIAL PARA CONSUMO QUANDO FORNECEDOR � MEI
            case 1403:
            case 6949: {
                if ("60".equals(st)) {
                    atribuiIcms60(icms, item, origem, st);
                } else if ("10".equals(st)) {
                    atribuiIcms10(icms, item, origem, st);
                } else if ("20".equals(st)) {
                    atribuiIcms20(icms, item, origem, st);
                } else if ("40".equals(st)) {
                    atribuiIcms40(icms, origem, st);
                } else if ("41".equals(st)) {
                    atribuiIcms40(icms, origem, st);
                } else if ("90".equals(st)) {
                    atribuiIcms90(icms, item, origem, st);
                } else {
                    atribuiIcms00(icms, item, origem, st);
                }
            }
            break;
            case 5556://devolucao de material de consumo
                if("20".equals(st)) {
                    atribuiIcms20(icms, item, origem, st);
                } else {
                    atribuiIcms00(icms, item, origem, st);
                }
                break;
            case 5557: { //transferencia material uso/consumo
                if ("60".equals(st)) {
                    atribuiIcms60(icms, item, origem, st);
                } else if ("40".equals(st) || "41".equals(st)) {
                    atribuiIcms40(icms, origem, st);
                } else if("20".equals(st)) {
                    atribuiIcms20(icms, item, origem, st);
                }
            }
            break;
            case 5913://RETORNO DE REMESSA PARA DEMONSTRACAO
                if ("50".equals(st) || "41".equals(st)) {
                    ICMS40 tributacaoIcms5_4 = new ICMS40();
                    tributacaoIcms5_4.setCST(st);
                    tributacaoIcms5_4.setOrig(origem);
                    icms.setICMS40(tributacaoIcms5_4);
                }
                break;
            case 5922:
                 if ("40".equals(st)) {
                    ICMS40 tributacaoIcms5_4 = new ICMS40();
                    tributacaoIcms5_4.setCST(st);
                    tributacaoIcms5_4.setOrig(origem);
                    icms.setICMS40(tributacaoIcms5_4);
                } else if ("90".equals(st)) {
                    ICMS90 tributacaoIcms90 = new ICMS90();
                    tributacaoIcms90.setCST(st);
                    tributacaoIcms90.setOrig(origem);
                    icms.setICMS90(tributacaoIcms90);
                }
                break;
            case 5601:
            case 5602:
            case 5603:
            case 5604:
            case 5605:
            case 5553: // DEVOLUCAO COMPRA DE BEM PARA ATIVO IMOBILIZADO
            case 5606: {
                ICMS90 tributacaoIcms90 = new ICMS90();
                tributacaoIcms90.setCST("90");
                tributacaoIcms90.setOrig("0");
                if (item.getIcmsAliquota() > 0) {
                    tributacaoIcms90.setModBC("3");
                    tributacaoIcms90.setVBC("0.00");
                    tributacaoIcms90.setPICMS("0.00");
                    tributacaoIcms90.setVICMS("0.00");
                } else if(item.getIcmsValor() > 0) {                    
                    tributacaoIcms90.setModBC("3");
                    tributacaoIcms90.setVBC("0.00");
                    tributacaoIcms90.setPICMS("0.00");
                    tributacaoIcms90.setVICMS(item.getCfop() == 5605 ? "0.00" : NumberUtil.decimalBanco(item.getIcmsValor()));
                }
                icms.setICMS90(tributacaoIcms90);
            }
            break;
            case 5929:
            case 6929: {
                if ("60".equals(st)) {
                    atribuiIcms60(icms, item, origem, st);
                } else if ("90".equals(st)) {
                    atribuiIcms90(icms, item, origem, st);
                } else {
                    atribuiIcms00(icms, item, origem, st);
                }
            }
            break;

        }
        return icms;
    }

    private ICMS icmsSimples(NfeItemModel item) {
        ICMS icms = new ICMS();
        String st = Integer.toString(item.getSituacaoTributaria());
        String origem = Integer.toString(item.getOrigem().ordinal());
        boolean destacaImpostoCorpoNotaParaSimplesNacional = Boolean.parseBoolean(
                System.getProperty("destaca.impostos.corpoNota", "false"));
        switch (item.getSituacaoTributaria()) {
            case 101: {
                ICMSSN101 icmssn101 = new ICMSSN101();
                icmssn101.setOrig(origem);
                icmssn101.setCSOSN(st);
                double simplesMeEppIcmsAliquota = Double.parseDouble(System.getProperty("nfe.me.epp.credito.icms.aliquota"));
                double valorIcmsCredito = item.getValorTotal() * simplesMeEppIcmsAliquota;
                System.setProperty("nfe.me.epp.credito.icms", "true");
                simplesMeEppIcmsAliquota *= 100;
                icmssn101.setPCredSN(NumberUtil.decimalBanco(simplesMeEppIcmsAliquota));
                icmssn101.setVCredICMSSN(NumberUtil.decimalBanco(valorIcmsCredito));

                icms.setICMSSN101(icmssn101);
            }
            break;
            case 102:
            case 203:
            case 300:
            case 400: {
                ICMSSN102 icmssn102 = new ICMSSN102();
                icmssn102.setOrig(origem);
                icmssn102.setCSOSN(st);
                icms.setICMSSN102(icmssn102);
            }
            break;
            case 201: {
                ICMSSN201 icmssn201 = new ICMSSN201();
                icmssn201.setOrig(origem);
                icmssn201.setCSOSN(st);
                icmssn201.setModBCST("4");
                icmssn201.setPMVAST(NumberUtil.decimalBanco((item.getBaseIcmsStValor() > 0 ? (item.getIva() - 1) * 100 : 0)));
                icmssn201.setVBCST(NumberUtil.decimalBanco(item.getBaseIcmsStValor()));
                icmssn201.setPICMSST(NumberUtil.decimalBanco(item.getIcmsStAliquota() * 100));
                icmssn201.setVICMSST(NumberUtil.decimalBanco(item.getValorIcmsSt()));
                double simplesMeEppIcmsAliquota = Double.parseDouble(System.getProperty("nfe.me.epp.credito.icms.aliquota").replace(",", "."));
                double valorIcmsCredito = item.getValorTotal() * simplesMeEppIcmsAliquota;
                simplesMeEppIcmsAliquota *= 100;
                icmssn201.setPCredSN(NumberUtil.decimalBanco(simplesMeEppIcmsAliquota));
                icmssn201.setVCredICMSSN(NumberUtil.decimalBanco(valorIcmsCredito));          
                
                if(this.nfeTributaDifal && item.getIcmsIndicePobrezaAliquota() > 0 
                        && item.getValorIcmsSt() > 0) {
                    icmssn201.setVBCFCPST(NumberUtil.decimalBanco(item.getBaseIcmsStValor()));
                    icmssn201.setPFCPST(NumberUtil.decimalBanco(item.getIcmsIndicePobrezaAliquota()));
                    icmssn201.setVFCPST(NumberUtil.decimalBanco(item.getValorIcmsSTIndicePobrezaOperacaoInternaInterestadualST()));
                    String msgAliquota = " (" + NumberUtil.decimalBanco(item.getIcmsIndicePobrezaAliquota()) + "%)";                    
                    if (item.getValorIcmsSTIndicePobrezaOperacaoInternaInterestadualST() > 0) {
                        this.informacaoAdicionalProduto += "Valor FCP ST R$ " + item.getValorIcmsSTIndicePobrezaOperacaoInternaInterestadualST() + msgAliquota + ";";
                    }
                }
                icms.setICMSSN201(icmssn201);
            }
            break;
            case 202: {
                ICMSSN202 icmssn202 = new ICMSSN202();
                icmssn202.setOrig(origem);
                icmssn202.setCSOSN(st);
                icmssn202.setModBCST("4");
                icmssn202.setPMVAST(NumberUtil.decimalBanco((item.getBaseIcmsStValor() > 0 ? (item.getIva() - 1) * 100 : 0)));
                icmssn202.setVBCST(NumberUtil.decimalBanco(item.getBaseIcmsStValor()));
                icmssn202.setPICMSST(NumberUtil.decimalBanco(item.getIcmsStAliquota() * 100));
                icmssn202.setVICMSST(NumberUtil.decimalBanco(item.getValorIcmsSt()));         
                if(this.nfeTributaDifal && item.getIcmsIndicePobrezaAliquota() > 0 
                        && item.getValorIcmsSt() > 0) {
                    icmssn202.setVBCFCPST(NumberUtil.decimalBanco(item.getBaseIcmsStValor()));
                    icmssn202.setPFCPST(NumberUtil.decimalBanco(item.getIcmsIndicePobrezaAliquota()));
                    icmssn202.setVFCPST(NumberUtil.decimalBanco(item.getValorIcmsSTIndicePobrezaOperacaoInternaInterestadualST()));
                    String msgAliquota = " (" + NumberUtil.decimalBanco(item.getIcmsIndicePobrezaAliquota()) + "%)";                    
                    if (item.getValorIcmsSTIndicePobrezaOperacaoInternaInterestadualST() > 0) {
                        this.informacaoAdicionalProduto += "Valor FCP ST R$ " + item.getValorIcmsSTIndicePobrezaOperacaoInternaInterestadualST() + msgAliquota + ";";
                    }
                }
                icms.setICMSSN202(icmssn202);
            }
            break;
            case 500: {
                ICMSSN500 icmssn500 = new ICMSSN500();
                icmssn500.setOrig(origem);
                icmssn500.setCSOSN(st);                
                if (item.getIcmsIndicePobrezaAliquota() > 0 && item.getIcmsStAliquota() > 0) {
                    double aliquotaPST = item.getIcmsStAliquota() * 100;
                    aliquotaPST += (item.getIcmsIndicePobrezaAliquota() > 1 ? item.getIcmsIndicePobrezaAliquota() : item.getIcmsIndicePobrezaAliquota() * 100);
                    icmssn500.setPST(NumberUtil.decimalBanco(aliquotaPST));
                    icmssn500.setVBCFCPSTRet(NumberUtil.decimalBanco(item.getBaseIcmsStValor()));
                    icmssn500.setPFCPSTRet(NumberUtil.decimalBanco(item.getIcmsIndicePobrezaAliquota() > 1 ? item.getIcmsIndicePobrezaAliquota() : item.getIcmsIndicePobrezaAliquota() * 100));
                    this.valorFCPSTRetido = true;
                    icmssn500.setVFCPSTRet(NumberUtil.decimalBanco(item.getValorIcmsSTIndicePobrezaOperacaoInternaInterestadualST()));
                    String msgAliquota = " (" + NumberUtil.decimalBanco(item.getIcmsIndicePobrezaAliquota()) + "%)";                    
                    if (item.getValorIcmsSTIndicePobrezaOperacaoInternaInterestadualST() > 0) {
                        this.informacaoAdicionalProduto += " Valor FCP retido por ST R$ " + item.getValorIcmsSTIndicePobrezaOperacaoInternaInterestadualST() + msgAliquota + ";";
                    }
                } else {
                    icmssn500.setPST(NumberUtil.decimalBanco(item.getIcmsStAliquota() * 100));
                    icmssn500.setVBCSTRet("0.00");
                    icmssn500.setVICMSSTRet("0.00");
                }
                icms.setICMSSN500(icmssn500);
            }
            break;
            case 900: {
                ICMSSN900 icmssn900 = new ICMSSN900();
                if (destacaImpostoCorpoNotaParaSimplesNacional) {
                    icms.setICMSSN900(aplicaImpostosParaSimplesIcmsSn900(icmssn900, st, origem, item));
                } else {
                    icmssn900.setOrig(origem);
                    icmssn900.setCSOSN(st);
                    icms.setICMSSN900(icmssn900);
                }
            }
            break;
            case 20: {
                ICMS.ICMS20 icms20 = new ICMS.ICMS20();
                icms20.setOrig(origem);
                icms20.setCST(st);
                icms20.setModBC("0");
                icms20.setPRedBC("0.00");
                icms20.setVBC("0.00");
                icms20.setPICMS("0.00");
                icms20.setVICMS("0.00");
                icms.setICMS20(icms20);
            }
            break;
            case 40: 
            case 41: {
                ICMS.ICMS40 icms40 = new ICMS.ICMS40();
                icms40.setOrig(origem);
                icms40.setCST(st);
                icms.setICMS40(icms40);
            }
            break;
        }
        return icms;
    }

    private PIS pis(NfeItemModel item) {
        PISAliq pisAliquota = new PISAliq();
        PISNT pisnt = new PISNT();
        PIS.PISOutr pisOutr = new PIS.PISOutr();
        PIS pis = new PIS();

        if (simples) {
            boolean destacaImpostoCorpoNotaParaSimplesNacional = Boolean.parseBoolean(
                    System.getProperty("destaca.impostos.corpoNota", "false"));
            if (item.getSituacaoTributaria() == 41
                    || item.getSituacaoTributaria() == 20 || item.getSituacaoTributaria() == 201
                    || item.getSituacaoTributaria() == 102 || item.getSituacaoTributaria() == 101) {                
                pisOutr.setCST("99");
                pisOutr.setVBC("0.00");
                pisOutr.setPPIS("0.00");
                pisOutr.setVPIS("0.00");
                pis.setPISOutr(pisOutr);
            } else if (item.getSituacaoTributaria() == 102) {
                pisOutr.setCST("07");
                pisOutr.setVBC("0.00");
                pisOutr.setPPIS("0.00");
                pisOutr.setVPIS("0.00");
                pis.setPISOutr(pisOutr);
            }
             else if (item.getSituacaoTributaria() == 900
                    && destacaImpostoCorpoNotaParaSimplesNacional && !item.getPisCofins()) {
                pis.setPISAliq(aplicaPisParaSimplesIcmsSn900(pisAliquota, item));
            } else if (item.getSituacaoTributaria() == 900
                    && (!destacaImpostoCorpoNotaParaSimplesNacional || item.getPisCofins())) {                
                pisOutr.setCST("99");
                pisOutr.setVBC("0.00");
                pisOutr.setPPIS("0.00");
                pisOutr.setVPIS("0.00");
                pis.setPISOutr(pisOutr);
            } else if(item.getSituacaoTributaria() == 400 && item.getPisCofins()) {                
                pisOutr.setCST("99");
                pisOutr.setVBC("0.00");
                pisOutr.setPPIS("0.00");
                pisOutr.setVPIS("0.00");
                pis.setPISOutr(pisOutr);
            } else if(item.getSituacaoTributaria() == 202 && !item.getPisCofins()) {
                pis.setPISAliq(aplicaPisParaSimplesIcmsSn202(pisAliquota, item));
            } else {
                pisnt.setCST("04");
                pis.setPISNT(pisnt);
            }
            return pis;
        }

        double aliquotaPis;
        double porcentagemPis;
        double valorPis;

        // O valor e verdadeiro quando o produto for isento
        if (item.getPisCofins()) {
            if (isUsingEmissorFiscal) {
                return nfeEmissorFiscal.atribuiPis(item);
            }

            switch (item.getCfop()) {
                case 5929:
                case 6929:
                case 5101:
                case 5102:
                case 5201:
                case 5202:
                case 6101:
                case 6102:
                case 6202:
                case 6201:
                case 1201:
                case 1202: //devolucao do 5102                
                case 5401:
                case 6401:
                case 2410://devolucao do 6401
                case 1410://devolucao do 5401
                case 5405:
                case 5553:
                case 5652:
                case 5653:
                case 6652:
                case 6653:
                case 5656:
                case 1661: // devolucacao de 5661
                case 1662: // devolucacao de 5656
                case 6656:
                case 6403:
                case 6404:
                case 6108:
                case 6107:
                case 1411:
                case 2411:
                case 5111:
                case 6556:
                case 5661:                
                case 5919:// RETORNO DE CONSIGNADO PARA FORNECEDOR
                case 5922://VENDA DE REMESSA FUTURA
                    boolean isCfopDevolucao = cfopsEntradaDevolucao().contains(item.getCfop());
                    if (isCfopDevolucao) {
                        pisOutr.setCST("70");
                        pisOutr.setVBC("0.00");
                        pisOutr.setPPIS("0.00");
                        pisOutr.setVPIS("0.00");
                        pis.setPISOutr(pisOutr);
                        break;
                    }
                    pisnt.setCST("04");
                    pis.setPISNT(pisnt);
                    break;
                case 5602:
                case 5410:
                case 5411:
                case 5413:
                case 6411:
                case 1949:
                case 2949:                                
                case 6949:
                case 5901:
                case 5902:
                case 5910:                
                case 6915:
                case 6916:
                case 5915:
                case 1916://RETORNO DE REMESSA PARA CONCERTO
                case 2102:
                case 5501:
                case 5949:
                case 6109://VENDA INTERESTADUAL ZONA FRANCA
                    pisnt.setCST("07");
                    pis.setPISNT(pisnt);
                    break;
                case 5605:
                case 5152:
                case 5409:
                case 5659:
                case 5927:
                case 6659: 
                case 5556://DEVOLUCAO DE MERCADORIA DE CONSUMO
                case 5557://TRANSFERENCIA MATERIAL DE USO/CONSUMO    
                case 6501://REMESSA DE PRODUCAO DO ESTABELECIMENTO COM FIM ESPECIFICO DE EXPORTACAO
                case 5552://TRANSFERENCIA DE BEM OU ATIVO IMOBILIZADO                
                    pisnt.setCST("08");
                    pis.setPISNT(pisnt);
                    break;                
                case 2202:                   
                case 1551://COMPRA DE ATIVO
                    pisOutr.setCST("70");
                    pisOutr.setVBC("0.00");                    
                    pisOutr.setPPIS("0.00");
                    pisOutr.setVPIS("0.00");
                    pis.setPISOutr(pisOutr);
                    break;                
                case 6551:
                case 5908:
                case 5551: //VENDA DE IMOBILIZADO
                case 1101:                
                case 5913://RETORNO DE REMESSA PARA DEMONSTRACAO                
                case 5925://RETORNO DE MERCADORIA PARA INDUSTRIALIZACAO, PARA O ADQUIRENTE POR NAO TER TRANSITADO A MESMA, NO ESTABELECIMENTO DO ADQUIRENTE
                case 5117://REMESSA DE VENDA PARA ENTREGA FUTURA         
                case 6911://REMESSA DE AMOSTRA GRATIS FORA DO ESTADO
                case 5911://REMESSA DE AMOSTRA GRATIS DENTRO DO ESTADO
                case 5918://RETORNO DE CONSIGNACAO                                        
                    pisOutr.setCST("99");
                    pisOutr.setQBCProd("0.0000");
                    pisOutr.setVAliqProd("0.0000");
                    pisOutr.setVPIS("0.00");
                    pis.setPISOutr(pisOutr);
                    break;
                case 5601:
                case 5603:
                case 5604:
                case 5606:
                    pisnt.setCST("99");
                    pisAliquota.setPPIS("0.00");
                    pisAliquota.setVBC("0.00");
                    pisAliquota.setVPIS("0.00");
                    pis.setPISAliq(pisAliquota);
                    pis.setPISNT(pisnt);
                    break;
                case 1403:
                case 1407:
                    pisOutr.setCST("98");
                    pisOutr.setQBCProd("0.0000");
                    pisOutr.setVAliqProd("0.0000");
                    pisOutr.setVPIS("0.00");
                    pis.setPISOutr(pisOutr);
                    break;                    
            }
        } else {
//            ProdutoTributacaoModel tributacao = ProdutoTributacaoBean.getTributacao(item.getTributacaoCodigo());
//            tributacao.
            if (isUsingEmissorFiscal) {
                return nfeEmissorFiscal.atribuiPis(item);
            }
            switch (item.getCfop()) {
                case 5101:
                case 5102:
                case 5201:
                case 5202:
                case 6201:
                case 6102:
                case 6202:
                case 1102://Compra de sucata
                case 1201:
                case 1202:
                case 2202:
                case 5405:
                case 5652:
                case 5653:
                case 6652:
                case 6653:
                case 5411:
                case 5413:
                case 5656:
                case 1661: // devolucacao de 5661
                case 1662: // devolucacao de 5656
                case 6656:
                case 6403:
                case 6404:
                case 6108:
                case 6107:
                case 1411:
                case 6411:
                case 2949:
                case 2411:
                case 5111:
                case 6556:
                case 2102:                
                case 5661:
                case 5922://VENDA DE REMESSA FUTURA
                    pisAliquota.setCST("01"); /// ALTERADO DE PISNT PARA PISALIQUOTA pois o código 01 refe-se ao CST do Pis Aliquota.
                    double pisBase = 0;
                    if (item.isDestacaDescontoNoCorpoDoDocumentoFiscal()) {
                        pisBase = item.getValorTotal() - item.getDescontoValor();
                        pisAliquota.setVBC(NumberUtil.decimalBanco(pisBase));
                    } else {
                        pisBase = item.getValorTotal();
                        pisAliquota.setVBC(NumberUtil.decimalBanco(pisBase));
                    }
                    aliquotaPis = Double.parseDouble(System.getProperty("nfe.pis.aliquota", "1.65"));
                    porcentagemPis = aliquotaPis / 100;
                    if (item.isDestacaDescontoNoCorpoDoDocumentoFiscal()) {
                        valorPis = (item.getValorTotal() - item.getDescontoValor()) * porcentagemPis;
                    } else {
                        valorPis = item.getValorTotal() * porcentagemPis;
                    }                    
                    pisAliquota.setPPIS(NumberUtil.decimalBanco(aliquotaPis));
                    pisAliquota.setVPIS(NumberUtil.decimalBanco(valorPis));
                    pis.setPISAliq(pisAliquota);
                    
                    // CASO SEJA UMA CFOP de DEVOLUCAO, emite com CST de ENTRADA
                    boolean isCfopDevolucao = cfopsEntradaDevolucao().contains(item.getCfop());
                    if (isCfopDevolucao) {
                        pisOutr.setCST("50");
                        pisOutr.setVBC(NumberUtil.decimalBanco(pisBase));
                        pisOutr.setPPIS(NumberUtil.decimalBanco(aliquotaPis));
                        pisOutr.setVPIS(NumberUtil.decimalBanco(valorPis));
                        pis.setPISOutr(pisOutr);
                        // Setando para null a tag de pis emitida na saida
                        pis.setPISAliq(null);
                        break;
                    }
                    break;
                case 5929: // CFOPs referente a venda efetuada anteriormente por SAT e virou nota
                case 6929:    
                   pisAliquota.setCST("01");
                   pisAliquota.setVBC("0.00");
                   pisAliquota.setPPIS("0.00");
                   pisAliquota.setVPIS("0.00");
                   pis.setPISAliq(pisAliquota);
                   break;    
                case 6401:
                case 6101:
                case 5401:
                case 2410:
                case 1410:
                    pisAliquota.setCST("02"); /// ALTERADO DE PISNT PARA PISALIQUOTA pois o código 01 refe-se ao CST do Pis Aliquota.
                    if (item.isDestacaDescontoNoCorpoDoDocumentoFiscal()) {
                        pisAliquota.setVBC(NumberUtil.decimalBanco(item.getValorTotal() - item.getDescontoValor()));
                    } else {
                        pisAliquota.setVBC(NumberUtil.decimalBanco(item.getValorTotal()));
                    }
                    aliquotaPis = Double.parseDouble(System.getProperty("nfe.pis.aliquota", "1.65"));
                    porcentagemPis = aliquotaPis / 100;
                    if (item.isDestacaDescontoNoCorpoDoDocumentoFiscal()) {
                        valorPis = (item.getValorTotal() - item.getDescontoValor()) * porcentagemPis;
                    } else {
                        valorPis = item.getValorTotal() * porcentagemPis;
                    }
                    pisAliquota.setPPIS(NumberUtil.decimalBanco(aliquotaPis));
                    pisAliquota.setVPIS(NumberUtil.decimalBanco(valorPis));
                    pis.setPISAliq(pisAliquota);
                    break;                
                case 5602:
                case 1949:
                case 5949:
                case 6949:                
                case 6916:
                case 5901:
                case 5902:
                case 5919:
                    pisnt.setCST("07");
                    pis.setPISNT(pisnt);
                    break;
                case 5605:
                case 5152:
                case 6915:
                case 5409:
                case 5927:
                case 5659:
                case 6659:                
                case 5557:
                case 5552:
                case 5915:
                case 5556://DEVOLUCAO DE MERCADORIA DE CONSUMO 
                    pisnt.setCST("08");
                    pis.setPISNT(pisnt);
                    break;
                case 5601:
                case 5603:
                case 5604:
                case 5606:
                case 6551:
                    pisnt.setCST("99");
                    pisAliquota.setCST("99");
//                    pisAliquota.setPorcentagemPis("0.0000");
                    pisAliquota.setPPIS("0.00");
                    pisAliquota.setVBC("0.00");
                    pisAliquota.setVPIS("0.00");
                    pis.setPISAliq(pisAliquota);
                    pis.setPISNT(pisnt);
                    break;
                case 5908:                                            
                case 5117://REMESSA DE VENDA PARA ENTREGA FUTURA     
                case 6911://REMESSA DE AMOSTRA GRATIS FORA DO ESTADO
                case 5911://REMESSA DE AMOSTRA GRATIS DENTRO DO ESTADO
                case 5918://RETORNO DE CONSIGNACAO                                        
                    pisOutr.setCST("99");
                    pisOutr.setQBCProd("0.0000");
                    pisOutr.setVAliqProd("0.0000");
                    pisOutr.setVPIS("0.00");
                    pis.setPISOutr(pisOutr);
                    break;
            }
        }
        return pis;
    }

    /**
     * Caso seja o CFOP de DEVOLU��O entrada. (Ou seja, n�s que emitimos a NFE pelo cliente GERALMENTE PF)
     * Segundo a Gabi, o correto � sair com as CST de PIS/COFINS de entrada, j� que � uma NFE de entrada;
     * CST PIS/COFINS saida                     entrada
     * 01 - Tributado (1,65% e 7,60)        50 - Tributado (1,65% e 7,60)        
     * 04 - Monof�sico                      70 - Monofasico
     * @return 
     */
    private List<Integer> cfopsEntradaDevolucao() {
        List<Integer> cfopEntrDevo = Arrays.asList(1202, 1410, 1411, 1661, 1662, 2410);
        return cfopEntrDevo;
    }
    
    private PISST pisSt(NfeItemModel item) {
        return null;
    }

    private COFINS cofins(NfeItemModel item) {
        COFINS cofins = new COFINS();
        COFINSAliq aliquota = new COFINSAliq();
        COFINSNT cofinsnt = new COFINSNT();
        COFINS.COFINSOutr cofinsOutr = new COFINS.COFINSOutr();

        if (simples) {
            boolean destacaImpostoCorpoNotaParaSimplesNacional = Boolean.parseBoolean(
                    System.getProperty("destaca.impostos.corpoNota", "false"));
            if (item.getSituacaoTributaria() == 41
                    || item.getSituacaoTributaria() == 20 || item.getSituacaoTributaria() == 201
                    || item.getSituacaoTributaria() == 102 || item.getSituacaoTributaria() == 101) {                
                cofinsOutr.setCST("99");
                cofinsOutr.setVBC("0.00");
                cofinsOutr.setVCOFINS("0.00");
                cofinsOutr.setPCOFINS("0.00");
                cofins.setCOFINSOutr(cofinsOutr);
            } else if (item.getSituacaoTributaria() == 102) {
                cofinsOutr.setCST("07");
                cofinsOutr.setVBC("0.00");
                cofinsOutr.setVCOFINS("0.00");
                cofinsOutr.setPCOFINS("0.00");
                cofins.setCOFINSOutr(cofinsOutr);
            } else if (item.getSituacaoTributaria() == 900
                    && destacaImpostoCorpoNotaParaSimplesNacional && !item.getPisCofins()) {
                cofins.setCOFINSAliq(aplicaCofinsParaSimplesIcmsSn900(aliquota, item));
            } else if (item.getSituacaoTributaria() == 900
                    && (!destacaImpostoCorpoNotaParaSimplesNacional || !item.getPisCofins())) {                
                cofinsOutr.setCST("99");
                cofinsOutr.setVBC("0.00");
                cofinsOutr.setVCOFINS("0.00");
                cofinsOutr.setPCOFINS("0.00");
                cofins.setCOFINSOutr(cofinsOutr);
            } else if(item.getSituacaoTributaria() == 400 && item.getPisCofins()) {                 
                cofinsOutr.setCST("99");
                cofinsOutr.setVBC("0.00");
                cofinsOutr.setVCOFINS("0.00");
                cofinsOutr.setPCOFINS("0.00");
                cofins.setCOFINSOutr(cofinsOutr);
            } else if(item.getSituacaoTributaria() == 202 && !item.getPisCofins()) {
                cofins.setCOFINSAliq(aplicaCofinsParaSimplesIcmsSn202(aliquota, item));
            } else {
                cofinsnt.setCST("04");
                cofins.setCOFINSNT(cofinsnt);
            }
            return cofins;
        }

        double aliquotaCofins;
        double porcentagemCofins;
        double valorCofins;

        // O valor e verdadeiro quando o produto for isento
        if (item.getPisCofins()) {
            if (isUsingEmissorFiscal) {
                return nfeEmissorFiscal.atribuiCofins(item);
            }
            
            switch (item.getCfop()) {
                case 5101:
                case 5102:
                case 5201:
                case 5202:
                case 6101:
                case 6102:
                case 6202:
                case 6201:
                case 1201:
                case 1202:
                case 5401:
                case 6401:
                case 2410:
                case 1410:
                case 5405:
                case 5553:
                case 5652:
                case 5653:
                case 6652:
                case 6653:
                case 5656:
                case 1661: // devolucacao de 5661
                case 1662: // devolucacao de 5656                    
                case 6656:
                case 5929:
                case 6929:
                case 6403:
                case 6404:
                case 6108:
                case 6107:
                case 1411:
                case 2411:
                case 5111:
                case 6556:
                case 5661:
                case 5919:
                case 5922://VENDA DE REMESSA FUTURA
                    boolean isCfopDevolucao = cfopsEntradaDevolucao().contains(item.getCfop());
                    if (isCfopDevolucao) {
                        cofinsOutr.setCST("70");
                        cofinsOutr.setVBC("0.00");
                        cofinsOutr.setPCOFINS("0.00");
                        cofinsOutr.setVCOFINS("0.00");
                        cofins.setCOFINSOutr(cofinsOutr);
                        break;
                    }
                    cofinsnt.setCST("04");
                    cofins.setCOFINSNT(cofinsnt);
                    break;
                case 5602:
                case 1949:
                case 2949:
                case 5410:
                case 5411:
                case 5413:                                
                case 6411:
                case 6949:                
                case 6916:
                case 5915:
                case 5901:
                case 5902:
                case 5910:
                case 1916: //RETORNO DE REMESSA PARA CONCERTO
                case 2102:
                case 5501:
                case 5949:
                case 6109://VENDA INTERESTADUAL ZONA FRANCA
                    cofinsnt.setCST("07");
                    cofins.setCOFINSNT(cofinsnt);
                    break;
                case 5605:
                case 5152:
                case 6915:
                case 5409:
                case 5927:
                case 5659:
                case 6659: 
                case 5556://DEVOLUCAO DE MERCADORIA PARA CONSUMO  
                case 5557://TRANSFERENCIA MATERIAL DE USO/CONSUMO    
                case 6501://REMESSA DE PRODUCAO DO ESTABELECIMENTO COM FIM ESPECIFICO DE EXPORTACAO                    
                case 5552://TRANSFERENCIA DE BEM DO ATIVO IMOBILIZADO                
                    cofinsnt.setCST("08");
                    cofins.setCOFINSNT(cofinsnt);
                    break;              
                case 1551://COMPRA DE ATIVO
                case 2202://DEVOLUCAO DE VENDA INTERESTADUAL PARA MONOFASICO
                    cofinsOutr.setCST("70");
                    cofinsOutr.setVBC("0.00");                    
                    cofinsOutr.setPCOFINS("0.00");
                    cofinsOutr.setVCOFINS("0.00");
                    cofins.setCOFINSOutr(cofinsOutr);
                    break;
                case 5601:
                case 5603:
                case 5604:
                case 5606:                             
                    cofinsnt.setCST("99");
                    aliquota.setCST("99");
                    aliquota.setVBC(NumberUtil.decimalBanco(item.getValorTotal()));
                    aliquota.setPCOFINS("0.00");
                    aliquota.setVCOFINS("0.00");
                    cofins.setCOFINSAliq(aliquota);
                    break;
                case 5908:
                case 6551:
                case 5551:// VENDA DE IMOBILIZADO    
                case 1101:                
                case 5913://RETORNO DE REMESSA PARA DEMONSTRACAO                
                case 5925://RETORNO DE MERCADORIA PARA INDUSTRIALIZACAO, PARA O ADQUIRENTE POR NAO TER TRANSITADO A MESMA, NO ESTABELECIMENTO DO ADQUIRENTE     
                case 5117://REMESSA DE VENDA PARA ENTREGA FUTURA           
                case 6911://REMESSA DE AMOSTRA GRATIS FORA DO ESTADO
                case 5911://REMESSA DE AMOSTRA GRATIS DENTRO DO ESTADO
                case 5918://RETORNO DE CONSIGNACAO                    
                    cofinsOutr.setCST("99");
                    cofinsOutr.setQBCProd("0.0000");
                    cofinsOutr.setVAliqProd("0.0000");
                    cofinsOutr.setVCOFINS("0.00");
                    cofins.setCOFINSOutr(cofinsOutr);
                    break;
                case 1403:
                case 1407://RETORNO DE CONSIGNACAO                    
                    cofinsOutr.setCST("98");
                    cofinsOutr.setQBCProd("0.0000");
                    cofinsOutr.setVAliqProd("0.0000");
                    cofinsOutr.setVCOFINS("0.00");
                    cofins.setCOFINSOutr(cofinsOutr);
                    break;
            }
        } else {
            if (isUsingEmissorFiscal) {
                return nfeEmissorFiscal.atribuiCofins(item);
            }
            switch (item.getCfop()) {
                case 5101:
                case 5102:
                case 5201:
                case 5202:
                case 6201:
                case 6102:
                case 6202:
                case 1102://compra de sucata
                case 1201:
                case 1202:
                case 2202:
                case 5405:
                case 5652:
                case 5653:
                case 6652:
                case 6653:
                case 5411:
                case 5413:
                case 5656:
                case 1661: // devolucacao de 5661
                case 1662: // devolucacao de 5656                    
                case 6656:
                case 6403:
                case 6404:
                case 6411:
                case 6108:
                case 6107:
                case 1411:
                case 2411:
                case 2949:
                case 5111:
                case 6556:
                case 2102:
                case 5661:
                case 5922://VENDA DE REMESSA FUTURA
                    aliquota.setCST("01");
                    double cofinsBase = 0;
                    if (item.isDestacaDescontoNoCorpoDoDocumentoFiscal()) {
                        cofinsBase = item.getValorTotal() - item.getDescontoValor();
                        aliquota.setVBC(NumberUtil.decimalBanco(cofinsBase));
                    } else {
                        cofinsBase = item.getValorTotal();
                        aliquota.setVBC(NumberUtil.decimalBanco(cofinsBase));
                    }
                    aliquotaCofins = Double.parseDouble(System.getProperty("nfe.cofins.aliquota", "6.0"));
                    porcentagemCofins = aliquotaCofins / 100;
                    if (item.isDestacaDescontoNoCorpoDoDocumentoFiscal()) {
                        valorCofins = (item.getValorTotal() - item.getDescontoValor()) * porcentagemCofins;
                    } else {
                        valorCofins = item.getValorTotal() * porcentagemCofins;
                    }
                    aliquota.setPCOFINS(NumberUtil.decimalBanco(aliquotaCofins));
                    aliquota.setVCOFINS(NumberUtil.decimalBanco(valorCofins));
                    cofins.setCOFINSAliq(aliquota);
                     // CASO SEJA UMA CFOP de DEVOLUCAO, ser� emitida com uma CST de ENTRADA
                    boolean isCfopDevolucao = cfopsEntradaDevolucao().contains(item.getCfop());
                    if (isCfopDevolucao) {
                        cofinsOutr.setCST("50");
                        cofinsOutr.setVBC(NumberUtil.decimalBanco(cofinsBase));
                        cofinsOutr.setPCOFINS(NumberUtil.decimalBanco(aliquotaCofins));
                        cofinsOutr.setVCOFINS(NumberUtil.decimalBanco(valorCofins));
                        cofins.setCOFINSOutr(cofinsOutr);
                        // Setando para null a tag de cofins emitida na saida
                        cofins.setCOFINSAliq(null);
                        break;
                    }
                    break;
                case 5929: // CFOPs referente a venda efetuada anteriormente por SAT e virou nota
                case 6929:    
                   aliquota.setCST("01");
                   aliquota.setVBC("0.00");
                   aliquota.setPCOFINS("0.00");
                   aliquota.setVCOFINS("0.00");
                   cofins.setCOFINSAliq(aliquota);
                   break;
                case 6401:
                case 5401:
                case 6101:
                case 2410:
                case 1410:
                    aliquota.setCST("02");
                    if (item.isDestacaDescontoNoCorpoDoDocumentoFiscal()) {
                        aliquota.setVBC(NumberUtil.decimalBanco(item.getValorTotal() - item.getDescontoValor()));
                    }else {
                        aliquota.setVBC(NumberUtil.decimalBanco(item.getValorTotal()));
                    }
                    aliquota.setVBC(NumberUtil.decimalBanco(item.getValorTotal()));
                    aliquotaCofins = Double.parseDouble(System.getProperty("nfe.cofins.aliquota", "6.0"));
                    porcentagemCofins = aliquotaCofins / 100;
                    if (item.isDestacaDescontoNoCorpoDoDocumentoFiscal()) {
                        valorCofins = (item.getValorTotal() - item.getDescontoValor()) * porcentagemCofins;
                    } else {
                        valorCofins = item.getValorTotal() * porcentagemCofins;
                    }                    
                    aliquota.setPCOFINS(NumberUtil.decimalBanco(aliquotaCofins));
                    aliquota.setVCOFINS(NumberUtil.decimalBanco(valorCofins));
                    cofins.setCOFINSAliq(aliquota);
                    break;
                case 5602:
                case 1949:
                case 5949:
                case 6949:
                case 5901:
                case 5902:                
                case 6916:                
                case 5919:
                    cofinsnt.setCST("07");
                    cofins.setCOFINSNT(cofinsnt);
                    break;
                case 5605:
                    cofinsnt.setCST("08");
                    cofins.setCOFINSNT(cofinsnt);
                    break;
                case 5152:
                case 6915:
                case 5409:
                case 5927:
                case 5659:
                case 6659:
                case 5557:
                case 5915:
                case 5552:
                case 5556://DEVOLUCAO DE MATERIAL DE CONSUMO 
                    cofinsnt.setCST("08");
                    cofins.setCOFINSNT(cofinsnt);
                    break;
                case 5601:
                case 5603:
                case 5604:
                case 5606:                                  
                    cofinsnt.setCST("99");
                    aliquota.setCST("99");
                    if (item.isDestacaDescontoNoCorpoDoDocumentoFiscal()) {
                        aliquota.setVBC(NumberUtil.decimalBanco(item.getValorTotal() - item.getDescontoValor()));
                    } else {
                        aliquota.setVBC(NumberUtil.decimalBanco(item.getValorTotal()));
                    }
                    aliquota.setPCOFINS("0.00");
                    aliquota.setVCOFINS("0.00");
                    cofins.setCOFINSAliq(aliquota);
                    break;
                case 5908:
                case 6551:   
                case 5117://REMESSA DE VENDA PARA ENTREGA FUTURA          
                case 6911://REMESSA DE AMOSTRA GRATIS FORA DO ESTADO
                case 5911://REMESSA DE AMOSTRA GRATIS DENTRO DO ESTADO
                case 5918://RETORNO DE CONSIGNACAO                                        
                    cofinsOutr.setCST("99");
                    cofinsOutr.setQBCProd("0.0000");
                    cofinsOutr.setVAliqProd("0.0000");
                    cofinsOutr.setVCOFINS("0.00");
                    cofins.setCOFINSOutr(cofinsOutr);
                    break;
            }
        }
        return cofins;

    }

    private COFINSST cofinsSt(NfeItemModel item) {
        return null;
    }

    private TIpi ipi(NfeItemModel item) throws DbfDatabaseException {             
        TIpi ipi = new TIpi();
        if (this.tributaIpi) {
            if (isUsingEmissorFiscal) {
                if(nfeEmissorFiscal.isIpiNt(item)) {
                    return nfeEmissorFiscal.setIpiNt(item, cnpj);
                }
                return nfeEmissorFiscal.setIpi(item, cnpj);
            }
            ipi.setCNPJProd(cnpj);
            if (item.getIpiSt() == 0) {
                if (item.getIpiValor() == 0) {
                    if (item.getCfop() == 5901 || item.getCfop() == 5902) {
                        IPINT ipint = new IPINT();                        
                        ipint.setCST("51");
                        ipi.setIPINT(ipint);
                    } else {
                        IPINT ipint = new IPINT();
                        ipint.setCST("51");
                        ipi.setIPINT(ipint);
                    }
                } else {
                    IPITrib tributacao = new IPITrib();
                    if (item.isDestacaDescontoNoCorpoDoDocumentoFiscal()) {
                        tributacao.setVBC(NumberUtil.decimalBanco(item.getValorTotal() - item.getDescontoValor()));
                    } else {
                        tributacao.setVBC(NumberUtil.decimalBanco(item.getValorTotal()));
                    }
                    tributacao.setPIPI(NumberUtil.decimalBanco(item.getIpiAliquota() * 100));
                    tributacao.setVIPI(NumberUtil.decimalBanco(item.getIpiValor()));
                    if (item.getCfop() == 5908) {
                        tributacao.setCST("99");
                    } else {
                        tributacao.setCST("50");
                    }

                    ipi.setIPITrib(tributacao);
                }
            } else {
                if(item.getIpiValor() > 0) {
                    IPITrib tributacao = new IPITrib();                    
                    if (item.isDestacaDescontoNoCorpoDoDocumentoFiscal()) {
                        tributacao.setVBC(NumberUtil.decimalBanco(item.getValorTotal() - item.getDescontoValor()));
                    } else {
                        tributacao.setVBC(NumberUtil.decimalBanco(item.getValorTotal()));
                    }
                    tributacao.setPIPI(NumberUtil.decimalBanco(item.getIpiAliquota() * 100));
                    tributacao.setVIPI(NumberUtil.decimalBanco(item.getIpiValor()));
                    tributacao.setCST(Integer.toString(item.getIpiSt()));
                    ipi.setIPITrib(tributacao);
                } else {
                    IPINT ipint = new IPINT();
                    ipint.setCST(Integer.toString(item.getIpiSt()));
                    ipi.setIPINT(ipint);
                }
            }

        } else {
            if (simples) {
                IPINT ipint = new IPINT();
                ipi.setIPINT(ipint);
                ipint.setCST("52");
            } else {
                IPINT ipint = new IPINT();
                ipi.setIPINT(ipint);
                ipint.setCST("53");
            }
        }
        if(this.tributaIpi && item.getIpiSt() == 55) {
            ipi.setCEnq("105");
        } else {
            ipi.setCEnq("999");
        }
        return ipi;
    }

    private ICMSUFDest difal(NfeItemModel item) {
        // DESCOMENTAR DEPOIS que CADASTRAR as TRIBUTACOES DE ICMS
        if (isUsingEmissorFiscal) {
            return nfeEmissorFiscal.atribuiDifal(item);
        }
        
        ICMSUFDest difal = new ICMSUFDest();
        if (item.isDestacaDescontoNoCorpoDoDocumentoFiscal()) {
            String vB = NumberUtil.decimalBanco(item.getValorTotal() - item.getDescontoValor());
            difal.setVBCUFDest(vB);
            difal.setVBCFCPUFDest(item.getIcmsIndicePobrezaAliquota() > 0 ? vB : "0.00");
        } else {
            String vB = NumberUtil.decimalBanco(item.getValorTotal());
            difal.setVBCUFDest(vB);
            difal.setVBCFCPUFDest(item.getIcmsIndicePobrezaAliquota() > 0 ? vB : "0.00");
        }
        difal.setPFCPUFDest(NumberUtil.decimalBanco(item.getIcmsIndicePobrezaAliquota()));
        difal.setPICMSUFDest(NumberUtil.decimalBanco(item.getIcmsInternaUFDestinoAliquota()));
        difal.setPICMSInter(NumberUtil.decimalBanco(item.getIcmsAliquota() * 100));      
        difal.setPICMSInterPart(NumberUtil.decimalBanco(item.getIcmsProvisorioPartilhaAliquota()));
        difal.setVFCPUFDest(NumberUtil.decimalBanco(item.getValorIcmsIndicePobreza()));
        difal.setVICMSUFDest(NumberUtil.decimalBanco(item.getValorIcmsUFDestino()));
        difal.setVICMSUFRemet(NumberUtil.decimalBanco(item.getValorIcmsUFRemetente()));
        return difal;
    }
    
    
    private void atribuiObservacoes(InfNFe inf, NfeModel nfeModel) throws DbfDatabaseException, DbfException {
        TNFe.InfNFe.InfAdic infAdicionais = new TNFe.InfNFe.InfAdic();
        StringBuilder info = new StringBuilder();
        if (!nfeModel.getOperacao().isTransferenciaIcms()) {
            DeOlhoNoImpostoLogic deOlho = new DeOlhoNoImpostoLogic();
            List<String> msgsDeOlho = deOlho.mensagemParaVenda(nfeModel, false);
            if (msgsDeOlho != null && !msgsDeOlho.isEmpty()) {
                for (String msgs : msgsDeOlho) {
                    info.append(msgs).append(".");
                }
            }
            if (!deOlho.getAvisos().isEmpty()) {
                StringBuilder avisos = new StringBuilder();
                for (String aviso : deOlho.getAvisos()) {
                    if (avisos.length() > 0) {
                        avisos.append("\n");
                    }
                    avisos.append(aviso);
                }
                JOptionPane.showInternalMessageDialog(Ambiente.getDesktop(), avisos.toString(), "Lei 12.741/2012", JOptionPane.WARNING_MESSAGE);
            }
        }

        String operacaoObs = nfeModel.getOperacao().getObs().trim();
        if (StringUtil.isNotNull(operacaoObs)) {
            info.append(operacaoObs);
            if (!operacaoObs.endsWith(".")) {
                info.append(".");
            }
        }        
        if (StringUtil.isNotNull(nfeModel.getObs())) {
            info.append(nfeModel.getObs());
        }
        boolean nfeInformaCaixa = Boolean.parseBoolean(System.getProperty("nfe.informaCaixa", "true"));
        if (nfeInformaCaixa && !nfeModel.getOperacao().isTransferenciaIcms()) {
            if (nfeModel.getCaixaCodigo() > 0) {
                info.append(" CAIXA: ").append(nfeModel.getCaixaCodigo()).append(".").append(nfeModel.getCaixaLoja());
            } else {
                info.append(" Caixa nao aberto");
            }
        }
        boolean nfeInformaVendedor = Boolean.parseBoolean(System.getProperty("nfe.informaVendedor", "true"));
        if (nfeInformaVendedor && !nfeModel.getOperacao().isTransferenciaIcms()) {
            if (StringUtil.isNotNull(nfeModel.getVendedorNome())) {
                info.append(" VENDEDOR: ").append(nfeModel.getVendedorNome());
            }
        }
        boolean nfeInformaComprador = Boolean.parseBoolean(System.getProperty("nfe.informaComprador", "true"));
        if (nfeInformaComprador && !nfeModel.getOperacao().isTransferenciaIcms()) {
            if (StringUtil.isNotNull(nfeModel.getCompradorNome())) {
                info.append(" COMPRADOR: ").append(nfeModel.getCompradorNome());
            }
        }
        boolean nfeInformaEnderecoEntrega = Boolean.parseBoolean(System.getProperty("nfe.informaEnderecoEntrega", "true"));
        if(nfeInformaEnderecoEntrega && !nfeModel.getOperacao().isTransferenciaIcms()) {
            if(nfeModel.getEntrega()) {
                info.append(" ENTREGA: ").append(nfeModel.getEnderecoEntregaVenda());
            }
        }
        if(this.obsEntregaBalcaoClienteForaEstado && !nfeModel.getOperacao().isTransferenciaIcms()) {
            info.append(" VENDA BALCAO");     
        }
        if(nfeModel.getOrigemTipo() != null && nfeModel.getOrigemTipo().equals(VendaTipo.ROMANEIO) && !nfeModel.getOperacao().isTransferenciaIcms()) {
            info.append("Cod. Painel ").append(formataNumeroPainel(String.valueOf(nfeModel.getOrigemCodigo())));
        }
        infAdicionais.setInfCpl(StringUtil.noSpecialKeys(info.toString(), new String[]{".", ",", "$", "%"}));
        inf.setInfAdic(infAdicionais);

        boolean isentoPisCofins = false;
        boolean substituicaoTributaria = false;
        boolean cstComCreditoDeIcms = false;
        for (NfeItemModel item : nfeModel.getVendaItens()) {
            if (item.getPisCofins()) {
                isentoPisCofins = true;
            }
            if (cfopComMensagemST(item.getCfop())) {
                substituicaoTributaria = true;
            }
            if(cstComMensagemCreditoDeIcms(item.getSituacaoTributaria())) {
                cstComCreditoDeIcms = true;
            }
        }
        boolean simplesMeEppIpi = Boolean.parseBoolean(System.getProperty("nfe.me.epp.sem.credito.ipi", "false"));
        boolean simplesMeEppIcms = Boolean.parseBoolean(System.getProperty("nfe.me.epp.credito.icms", "false"));

        if (isentoPisCofins || substituicaoTributaria
                || nfeModel.getPossuiImpostoAdicional() || simplesMeEppIpi || simplesMeEppIcms) {
            String informacoes = "";

            if (simplesMeEppIpi) {
                informacoes += "I - DOCUMENTO EMITIDO POR ME OU EPP OPTANTE PELO SIMPLES NACIONAL. NAO GERA DIREITO A CREDITO FISCAL DE ISS E DE IPI.";
            }

            if (simplesMeEppIcms && cstComCreditoDeIcms) {
                double simplesMeEppIcmsAliquota = Double.parseDouble(System.getProperty("nfe.me.epp.credito.icms.aliquota"));
                double valorIcmsCredito = nfeModel.getValorTotal() * simplesMeEppIcmsAliquota;
                simplesMeEppIcmsAliquota *= 100;
                informacoes += "II-PERMITE O APROVEITAMENTO DO CREDITO DE ICMS NO VALOR DE R$ "
                        + NumberUtil.decimal(valorIcmsCredito) + ",CORRESPONDENTE A ALIQUOTA DE "
                        + NumberUtil.decimal(simplesMeEppIcmsAliquota) + "% NOS TERMOS DO ART 23 DA LC 123.";
            }

            if (isentoPisCofins && !simplesMeEppIpi) {
                informacoes += ALIQUOTA_ZERO_PIS_COFINS;
            }
            if (substituicaoTributaria) {
                if (isentoPisCofins) {
                    informacoes += " ";
                }
                if (!simples) {
                    informacoes += SUBSTITUICAO_TRIBUTARIA;
                } else {
                    //a mensagem de substituiaco tributaria muda muito para simples nacional, no que diz respeito ao protocolo
                    //como foi visto com o F�bio da Gisa contabilidade, para nosso cliente FHEL.
                    //UM MESMO PRODUTO PODE TER PROTOCOLO DIFERENTE PARA CADA ESTADO
                    //E PRODUTOS DIFERENTES PODEM TER PROTOCOLOS DIFERENTES PARA O MESMO ESTADO
                    //para isso foi criado um parametro onde o valor tera a UF, classe fiscal do produto que possui ST
                    //e o protocolo que atende esse produto.
                    //EX de como montar o parametro --> (MG-39259090=32/2009;70139900=34/2009)(RJ-39259090=32/2014;70139900=131/2013)
                    //Estados separados por (), NCMs separados por ; e para cada NCM tem um protocolo que � informado 
                    //depois do sinal de =.
                    String parametroMsgProtocolo = System.getProperty("nfe.me.epp.st.msgIII");
                    String msgST = "III- ICMS RETIDO POR SUBSTITUICAO TRIBUTARIA";
                    String protocolo = "";
                    if (StringUtil.isNotNull(parametroMsgProtocolo) && !parametroMsgProtocolo.isEmpty()) {
                        if (parametroMsgProtocolo.contains(nfeModel.getClienteEstado())) {
                            parametroMsgProtocolo = parametroMsgProtocolo.substring(parametroMsgProtocolo.indexOf("(" + nfeModel.getClienteEstado()), parametroMsgProtocolo.indexOf(")", parametroMsgProtocolo.indexOf("(" + nfeModel.getClienteEstado())) + 1);
                            System.out.println(parametroMsgProtocolo);
                            for (NfeItemModel item : nfeModel.getVendaItens()) {
                                if (cfopComMensagemST(item.getCfop())) {
                                    if (parametroMsgProtocolo.contains(item.getClasseFiscal())) {
                                        String p = parametroMsgProtocolo.substring(parametroMsgProtocolo.indexOf(item.getClasseFiscal()), parametroMsgProtocolo.indexOf(")", parametroMsgProtocolo.indexOf(item.getClasseFiscal())));
                                        if (p.contains(";")) {
                                            p = parametroMsgProtocolo.substring(parametroMsgProtocolo.indexOf(item.getClasseFiscal()), parametroMsgProtocolo.indexOf(";", parametroMsgProtocolo.indexOf(item.getClasseFiscal())));;
                                        }
                                        protocolo = (!protocolo.isEmpty() ? 
                                                    (!protocolo.contains(p.replace(item.getClasseFiscal(), "").replace("=", "")) ? protocolo + ", " + p.replace(item.getClasseFiscal(), "").replace("=", "") : protocolo)
                                                    : protocolo + p.replace(item.getClasseFiscal(), "").replace("=", ""));
                                    }
                                }
                            }
                        }
                    }

                    informacoes += msgST + (!protocolo.isEmpty() ? " NOS TERMOS DO PROTOCOLO ICMS " + protocolo : protocolo);
                    System.out.println(informacoes);
                }
            }

            if (nfeModel.getPossuiImpostoAdicional()) {
                for (NfeImpostoAdicionalModel imposto : nfeModel.getImpostosAdicionais()) {
                    String mensagem = imposto.getImposto().getMensagem();
                    if (StringUtil.isNotNull(mensagem)) {
                        mensagem = MessageFormat.format(mensagem, imposto.getValorBase(), imposto.getPorcentagem(), imposto.getValorTotal());
                        informacoes += mensagem;
                    }
                }
                //MessageFormat.format("At {1,time} on {1,date}, there was {2} on planet {0,number,integer}.", planet, new Date(), event)
            }
            if(this.nfeTributaDifal && nfeModel.getValorIcmsUFDestino() == 0 &&
                    (nfeModel.getValorIcmsIndicePobreza() > 0 || nfeModel.getValorIcmsSTIndicePobreza() > 0)) {
                String mensagem = "Valor Total do FCP";
                if(nfeModel.getValorIcmsIndicePobreza() > 0) {
                    informacoes += mensagem + " R$ " + nfeModel.getValorIcmsIndicePobrezaString() + ";";
                }
                if(nfeModel.getValorIcmsSTIndicePobreza() > 0) {
                    informacoes += mensagem + 
                            (!this.valorFCPSTRetido ? " por ST" : " retido anteriormente por ST") + " R$ " + nfeModel.getValorIcmsSTIndicePobrezaString() + ";";
                }                
            }
            infAdicionais.setInfAdFisco(informacoes);
        }

        if (nfeModel.getVendaItens() != null && nfeModel.getVendaItens().size() == 1) {
            NfeItemModel item = nfeModel.getVendaItens().get(0);
            if (nfeModel.getVendaItens().get(0).getCfop() > 5600
                    && nfeModel.getVendaItens().get(0).getCfop() < 5607) {
                infAdicionais.setInfAdFisco(item.getDescricao());
            }
        }

        nfeModel.setInfoAdicionalFisco(infAdicionais.getInfAdFisco() == null ? "" : infAdicionais.getInfAdFisco());
        nfeModel.setInfoComplementar(infAdicionais.getInfCpl() == null ? "" : infAdicionais.getInfCpl());
    }
    
    private boolean cstComMensagemCreditoDeIcms(int cst) {
        boolean retorno = false;
        switch (cst) {
            case 201:
            case 101:
                retorno = true;
                break;
        }
        return retorno;
    }

    private boolean cfopComMensagemST(int cfop) {
        boolean retorno = false;
        switch (cfop) {
            case 5405:
            case 6404:
            case 6403:
            case 5409:
            case 6409:
            case 5411:
            case 5413:
            case 6411:
            case 1411:
            case 2411:
                retorno = true;
                break;
        }
        return retorno;
    }

    private void atribuiIcms60(ICMS icms, NfeItemModel item, String origem, String st) throws DbfDatabaseException {
        ProdutoDBFModel produto = new ProdutoDBFModel();
        List<CompraItemDBFModel> ultimasCompras = produto.getUltimasComprasDoItem(item.getProdutoCodigo(), 0);
        CompraItemDBFModel itemFiltrado=null;
        
        for(CompraItemDBFModel i: ultimasCompras){
            if(i.getIcmsStValor() > 0){
                itemFiltrado = i;
                break;
            }
        }
              
        ICMS60 tributacaoIcms60 = new ICMS60();
        tributacaoIcms60.setCST(st);
        tributacaoIcms60.setOrig(origem);
        tributacaoIcms60.setVBCSTRet(NumberUtil.decimalBanco(itemFiltrado != null
                ? itemFiltrado.getIcmsStBase() / itemFiltrado.getQuantidadeEstoque() * item.getQuantidade()
                : item.getValorBase()));
        if(item.getIcmsIndicePobrezaAliquota() > 0 && item.getIcmsStAliquota() > 0) {
            double aliquotaPST = item.getIcmsStAliquota() * 100;
            aliquotaPST += (item.getIcmsIndicePobrezaAliquota() > 1 ? item.getIcmsIndicePobrezaAliquota() : item.getIcmsIndicePobrezaAliquota() * 100);
            tributacaoIcms60.setPST(NumberUtil.decimalBanco(aliquotaPST));
            tributacaoIcms60.setVBCFCPSTRet(NumberUtil.decimalBanco(item.getBaseIcmsStValor()));
            tributacaoIcms60.setPFCPSTRet(NumberUtil.decimalBanco(item.getIcmsIndicePobrezaAliquota() > 1 ? item.getIcmsIndicePobrezaAliquota() : item.getIcmsIndicePobrezaAliquota() * 100));
            this.valorFCPSTRetido = true;
            tributacaoIcms60.setVFCPSTRet(NumberUtil.decimalBanco(item.getValorIcmsSTIndicePobrezaOperacaoInternaInterestadualST()));
        } else {
            tributacaoIcms60.setPST(NumberUtil.decimalBanco(itemFiltrado!=null?
                    itemFiltrado.getIcmsStAliquota():
                    0.18
                    ));
        }        
        tributacaoIcms60.setVICMSSTRet(NumberUtil.decimalBanco(itemFiltrado!=null? 
                itemFiltrado.getIcmsStValor()/itemFiltrado.getQuantidadeEstoque()*item.getQuantidade(): 
                item.getValorBase()*0.18));
        tributacaoIcms60.setVICMSSubstituto(NumberUtil.decimalBanco(item.getValorTotal()*0.18));
        icms.setICMS60(tributacaoIcms60);
    }

    private void atribuiIcms40(ICMS icms, String origem, String st) {
        ICMS40 tributacaoIcms40 = new ICMS40();
        tributacaoIcms40.setCST(st);
        tributacaoIcms40.setOrig(origem);
        icms.setICMS40(tributacaoIcms40);
    }

    private void atribuiIcms90(ICMS icms, NfeItemModel item, String origem, String st) {
        ICMS90 tributacaoIcms90 = new ICMS90();
        tributacaoIcms90.setCST(st);
        tributacaoIcms90.setOrig(origem);

        if (item.getBaseIcmsValor() > 0) {
            tributacaoIcms90.setModBC("3");
            tributacaoIcms90.setPRedBC("0.00");
            tributacaoIcms90.setVBC(NumberUtil.decimalBanco(item.getBaseIcmsValor()));
            tributacaoIcms90.setPICMS(NumberUtil.decimalBanco(item.getIcmsAliquota() * 100));
            tributacaoIcms90.setVICMS(NumberUtil.decimalBanco(item.getIcmsValor()));
            if (this.nfeTributaDifal && item.getIcmsValor() > 0 && item.getIcmsIndicePobrezaAliquota() > 0
                    && item.getValorIcmsUFDestino() == 0) {//ICMS UF Destino = 0, garante que a nota nao tenha DIFAL (nota para contribuite de ICMS)
                tributacaoIcms90.setVBCFCP(tributacaoIcms90.getVBC());
                tributacaoIcms90.setPFCP(NumberUtil.decimalBanco((item.getIcmsIndicePobrezaAliquota() < 1 ? item.getIcmsIndicePobrezaAliquota() * 100 : item.getIcmsIndicePobrezaAliquota())));
                tributacaoIcms90.setVFCP(NumberUtil.decimalBanco(item.getValorIcmsIndicePobreza()));
                String msgAliquota = " (" + NumberUtil.decimalBanco(item.getIcmsIndicePobrezaAliquota()) + "%)";
                if (item.getValorIcmsIndicePobreza() > 0) {
                    this.informacaoAdicionalProduto += "Valor base calculo FCP R$ " + tributacaoIcms90.getVBC() + "; "
                            + " Valor FCP R$ " + item.getValorIcmsIndicePobrezaString() + msgAliquota + ";";
                }             
            }
        }

        if (item.getBaseIcmsStValor() > 0) {
            /**
             * TIPOS DE MODALIDADES PARA ModBC/ModBCST 0 ? Pre�o tabelado ou
             * m�ximo sugerido; 1 ? Lista Negativa (valor); 2 ? Lista Positiva
             * (valor); 3 ? Lista Neutra (valor); 4 ? Margem Valor Agregado (%),
             * 5 ? Pauta (valor).
             */
            tributacaoIcms90.setModBCST("3");
            tributacaoIcms90.setPRedBCST("0.00");
            tributacaoIcms90.setPICMSST(NumberUtil.decimalBanco(item.getIcmsStAliquota() * 100));
            tributacaoIcms90.setPMVAST(NumberUtil.decimalBanco((item.getIva() != 0 ? ((item.getIva() - 1) * 100) : item.getIva() * 100)));
            tributacaoIcms90.setVBCST(NumberUtil.decimalBanco(item.getBaseIcmsStValor()));
            tributacaoIcms90.setVICMSST(NumberUtil.decimalBanco(item.getValorIcmsSt()));
            
            if (this.nfeTributaDifal && item.getValorIcmsSt() > 0 && item.getIcmsIndicePobrezaAliquota() > 0
                    && item.getValorIcmsUFDestino() == 0) {//ICMS UF Destino = 0, garante que a nota nao tenha DIFAL (nota para contribuite de ICMS)
                tributacaoIcms90.setVBCFCPST(tributacaoIcms90.getVBCST());
                tributacaoIcms90.setPFCPST(NumberUtil.decimalBanco(item.getIcmsIndicePobrezaAliquota()));
                tributacaoIcms90.setVFCPST(NumberUtil.decimalBanco(item.getValorIcmsSTIndicePobrezaOperacaoInternaInterestadualST()));
                String msgAliquota = " (" + NumberUtil.decimalBanco(item.getIcmsIndicePobrezaAliquota()) + "%)";             
                if (item.getValorIcmsSTIndicePobrezaOperacaoInternaInterestadualST() > 0) {
                    this.informacaoAdicionalProduto += " Valor FCP ST R$ " + item.getValorIcmsSTIndicePobrezaOperacaoInternaInterestadualST() + msgAliquota + ";";
                }
            }
        }
        icms.setICMS90(tributacaoIcms90);
    }

    private void atribuiIcms00(ICMS icms, NfeItemModel item, String origem, String st) {
        ICMS00 tributacaoIcms00 = new ICMS00();
        tributacaoIcms00.setCST(st);
        tributacaoIcms00.setOrig(origem);
        tributacaoIcms00.setModBC("3");  // 3 Valor da Operacao
        tributacaoIcms00.setVBC(NumberUtil.decimalBanco(item.getBaseIcmsValor()));
        tributacaoIcms00.setPICMS(NumberUtil.decimalBanco(item.getIcmsAliquota() * 100));
        tributacaoIcms00.setVICMS(NumberUtil.decimalBanco(item.getIcmsValor()));
        icms.setICMS00(tributacaoIcms00);
    }

    private void atribuiIcms10(ICMS icms, NfeItemModel item, String origem, String st) {
        ICMS10 tributacaoICMS10 = new ICMS10();
        tributacaoICMS10.setCST(st);
        tributacaoICMS10.setOrig(origem);
        tributacaoICMS10.setModBC("3");
        tributacaoICMS10.setVBC(NumberUtil.decimalBanco(item.getBaseIcmsValor()));
        tributacaoICMS10.setPICMS(NumberUtil.decimalBanco(item.getIcmsAliquota() * 100));
        tributacaoICMS10.setVICMS(NumberUtil.decimalBanco(item.getIcmsValor()));
        tributacaoICMS10.setModBCST("4");
        tributacaoICMS10.setVBCST(NumberUtil.decimalBanco(item.getBaseIcmsStValor()));
        tributacaoICMS10.setPMVAST(NumberUtil.decimalBanco((item.getBaseIcmsStValor() > 0 ? (item.getIva() - 1) * 100 : 0)));
        tributacaoICMS10.setPICMSST(NumberUtil.decimalBanco(item.getIcmsStAliquota() * 100));        
        tributacaoICMS10.setVICMSST(NumberUtil.decimalBanco(item.getValorIcmsSt()));
        if(item.getValorIcmsStPorcentagemReducao() > 0) {
            tributacaoICMS10.setPRedBCST(NumberUtil.decimalBanco(item.getValorIcmsStPorcentagemReducao()));
        }
        if (this.nfeTributaDifal && item.getValorIcmsSt() > 0 && item.getIcmsIndicePobrezaAliquota() > 0
                && item.getValorIcmsUFDestino() == 0) {//ICMS UF Destino = 0, garante que a nota nao tenha DIFAL (nota para contribuite de ICMS)
            tributacaoICMS10.setVBCFCP(tributacaoICMS10.getVBC());
            tributacaoICMS10.setPFCP(NumberUtil.decimalBanco((item.getIcmsIndicePobrezaAliquota() < 1 ? item.getIcmsIndicePobrezaAliquota() * 100 : item.getIcmsIndicePobrezaAliquota())));            
            tributacaoICMS10.setVFCP(NumberUtil.decimalBanco(item.getValorIcmsIndicePobreza()));
            tributacaoICMS10.setVBCFCPST(tributacaoICMS10.getVBCST());
            tributacaoICMS10.setPFCPST(NumberUtil.decimalBanco(item.getIcmsIndicePobrezaAliquota()));            
            tributacaoICMS10.setVFCPST(NumberUtil.decimalBanco(item.getValorIcmsSTIndicePobrezaOperacaoInternaInterestadualST()));                    
            String msgAliquota = " (" + NumberUtil.decimalBanco(item.getIcmsIndicePobrezaAliquota()) + "%)";
            if (item.getValorIcmsIndicePobreza() > 0) {
                this.informacaoAdicionalProduto += "Valor base calculo FCP R$ " + tributacaoICMS10.getVBC() + "; " 
                        + " Valor FCP R$ " + item.getValorIcmsIndicePobrezaString() + msgAliquota + ";";
            }
            if (item.getValorIcmsSTIndicePobrezaOperacaoInternaInterestadualST() > 0) {
                this.informacaoAdicionalProduto += " Valor FCP ST R$ " + item.getValorIcmsSTIndicePobrezaOperacaoInternaInterestadualST() + msgAliquota + ";";
            }
        }
        icms.setICMS10(tributacaoICMS10);
    }
    
    private void atribuiIcms20(ICMS icms, NfeItemModel item, String origem, String st) {
        ICMS.ICMS20 tributaICMS20 = new ICMS.ICMS20();
        tributaICMS20.setCST(st);
        tributaICMS20.setOrig(origem);
        tributaICMS20.setModBC("3");
        tributaICMS20.setVBC(NumberUtil.decimalBanco(item.getBaseIcmsValor()));
        tributaICMS20.setPICMS(NumberUtil.decimalBanco(item.getIcmsAliquota() * 100));
        tributaICMS20.setVICMS(NumberUtil.decimalBanco(item.getIcmsValor()));
        if(item.getValorIcmsStPorcentagemReducaoPorcentagem() > 0) {
            tributaICMS20.setPRedBC(NumberUtil.decimalBanco(item.getValorIcmsStPorcentagemReducao()));
        } else if(item.getBaseIcms() != 1 && (item.getBaseIcms() * 100) != 1) {
            double baseIcms = item.getBaseIcms();
            if(baseIcms < 1) {
                baseIcms *= 100;
            }
            double reducao = 100 - baseIcms;
            tributaICMS20.setPRedBC(NumberUtil.decimalBanco(reducao));
        } else {
            tributaICMS20.setPRedBC("0.00");
        }
        if (this.nfeTributaDifal && item.getIcmsValor() > 0 && item.getIcmsIndicePobrezaAliquota() > 0
                && item.getValorIcmsUFDestino() == 0) {//ICMS UF Destino = 0, garante que a nota nao tenha DIFAL (nota para contribuite de ICMS)
            tributaICMS20.setVBCFCP(tributaICMS20.getVBC());
            tributaICMS20.setPFCP(NumberUtil.decimalBanco((item.getIcmsIndicePobrezaAliquota() < 1 ? item.getIcmsIndicePobrezaAliquota() * 100 : item.getIcmsIndicePobrezaAliquota())));
            tributaICMS20.setVFCP(NumberUtil.decimalBanco(item.getValorIcmsIndicePobreza()));            
            String msgAliquota = " (" + NumberUtil.decimalBanco(item.getIcmsIndicePobrezaAliquota()) + "%)";
            if (item.getValorIcmsIndicePobreza() > 0) {
                this.informacaoAdicionalProduto += "Valor base calculo FCP R$ " + tributaICMS20.getVBC() + "; " 
                        + " Valor FCP R$ " + item.getValorIcmsIndicePobrezaString() + msgAliquota + ";";
            }            
        }
        
        icms.setICMS20(tributaICMS20);
    }

    private PISAliq aplicaPisParaSimplesIcmsSn900(PISAliq pisAliquota, NfeItemModel item) {
        pisAliquota.setCST("01"); /// ALTERADO DE PISNT PARA PISALIQUOTA pois o código 01 refe-se ao CST do Pis Aliquota.
        if(item.isDestacaDescontoNoCorpoDoDocumentoFiscal()) {
            pisAliquota.setVBC(NumberUtil.decimalBanco(item.getValorTotal() - item.getDescontoValor()));
        } else {
            pisAliquota.setVBC(NumberUtil.decimalBanco(item.getValorTotal()));
        }
        double aliquotaPis = Double.parseDouble(System.getProperty("nfe.pis.aliquota", "1.65"));
        double porcentagemPis = aliquotaPis / 100;
        double valorPis = 0;
        if(item.isDestacaDescontoNoCorpoDoDocumentoFiscal()) {
            valorPis = (item.getValorTotal() - item.getDescontoValor()) * porcentagemPis;
        } else {
            valorPis = item.getValorTotal() * porcentagemPis;
        }
        pisAliquota.setPPIS(NumberUtil.decimalBanco(aliquotaPis));
        pisAliquota.setVPIS(NumberUtil.decimalBanco(valorPis));
        return pisAliquota;
    }

    private COFINSAliq aplicaCofinsParaSimplesIcmsSn900(COFINSAliq aliquota, NfeItemModel item) {
        aliquota.setCST("01");
        if (item.isDestacaDescontoNoCorpoDoDocumentoFiscal()) {
            aliquota.setVBC(NumberUtil.decimalBanco(item.getValorTotal() - item.getDescontoValor()));
        } else {
            aliquota.setVBC(NumberUtil.decimalBanco(item.getValorTotal()));
        }        
        double aliquotaCofins = Double.parseDouble(System.getProperty("nfe.cofins.aliquota", "6.0"));
        double porcentagemCofins = aliquotaCofins / 100;
        double valorCofins = 0;
        if (item.isDestacaDescontoNoCorpoDoDocumentoFiscal()) {
            valorCofins = (item.getValorTotal() - item.getDescontoValor()) * porcentagemCofins;
        } else {
            valorCofins = item.getValorTotal() * porcentagemCofins;
        }        
        aliquota.setPCOFINS(NumberUtil.decimalBanco(aliquotaCofins));
        aliquota.setVCOFINS(NumberUtil.decimalBanco(valorCofins));
        return aliquota;
    }
    
    private PISAliq aplicaPisParaSimplesIcmsSn202(PISAliq pisAliquota, NfeItemModel item) {
        pisAliquota.setCST("02"); /// ALTERADO DE PISNT PARA PISALIQUOTA pois o código 01 refe-se ao CST do Pis Aliquota.
        if(item.isDestacaDescontoNoCorpoDoDocumentoFiscal()) {
            pisAliquota.setVBC(NumberUtil.decimalBanco(item.getValorTotal() - item.getDescontoValor()));
        } else {
            pisAliquota.setVBC(NumberUtil.decimalBanco(item.getValorTotal()));
        }
        double aliquotaPis = Double.parseDouble(System.getProperty("nfe.pis.aliquota", "1.65"));
        double porcentagemPis = aliquotaPis / 100;
        double valorPis = 0;
        if(item.isDestacaDescontoNoCorpoDoDocumentoFiscal()) {
            valorPis = (item.getValorTotal() - item.getDescontoValor()) * porcentagemPis;
        } else {
            valorPis = item.getValorTotal() * porcentagemPis;
        }
        pisAliquota.setPPIS(NumberUtil.decimalBanco(aliquotaPis));
        pisAliquota.setVPIS(NumberUtil.decimalBanco(valorPis));
        return pisAliquota;
    }

    private COFINSAliq aplicaCofinsParaSimplesIcmsSn202(COFINSAliq aliquota, NfeItemModel item) {
        aliquota.setCST("02");
        if (item.isDestacaDescontoNoCorpoDoDocumentoFiscal()) {
            aliquota.setVBC(NumberUtil.decimalBanco(item.getValorTotal() - item.getDescontoValor()));
        } else {
            aliquota.setVBC(NumberUtil.decimalBanco(item.getValorTotal()));
        }        
        double aliquotaCofins = Double.parseDouble(System.getProperty("nfe.cofins.aliquota", "6.0"));
        double porcentagemCofins = aliquotaCofins / 100;
        double valorCofins = 0;
        if (item.isDestacaDescontoNoCorpoDoDocumentoFiscal()) {
            valorCofins = (item.getValorTotal() - item.getDescontoValor()) * porcentagemCofins;
        } else {
            valorCofins = item.getValorTotal() * porcentagemCofins;
        }        
        aliquota.setPCOFINS(NumberUtil.decimalBanco(aliquotaCofins));
        aliquota.setVCOFINS(NumberUtil.decimalBanco(valorCofins));
        return aliquota;
    }

    private ICMSSN900 aplicaImpostosParaSimplesIcmsSn900(ICMSSN900 icmssn900, String st, String origem, NfeItemModel item) {
        icmssn900.setCSOSN(st);
        icmssn900.setOrig(origem);
        icmssn900.setModBC("0"); //0 para margem do valor agregados
        icmssn900.setVBC(NumberUtil.decimalBanco(item.getBaseIcmsValor()));
        icmssn900.setPICMS(NumberUtil.decimalBanco(item.getIcmsAliquota() * 100));
        icmssn900.setVICMS(NumberUtil.decimalBanco(item.getIcmsValor()));
        if (item.getIcmsStAliquota() > 0) {
            icmssn900.setModBCST("4"); //4 para margem do valor agregados para ST
            icmssn900.setVBCST(NumberUtil.decimalBanco(item.getBaseIcmsStValor()));
            icmssn900.setPMVAST(NumberUtil.decimalBanco((item.getIva() - 1) * 100));
            icmssn900.setPICMSST(NumberUtil.decimalBanco(item.getIcmsStAliquota() * 100));
            icmssn900.setVICMSST(NumberUtil.decimalBanco(item.getValorIcmsSt()));
            if (this.nfeTributaDifal && item.getValorIcmsSt() > 0 && item.getIcmsIndicePobrezaAliquota() > 0 
                    && item.getValorIcmsUFDestino() == 0) {//ICMS UF Destino = 0, garante que a nota nao tenha DIFAL (nota para contribuite de ICMS)                
                icmssn900.setVBCFCPST(icmssn900.getVBCST());
                icmssn900.setPFCPST(NumberUtil.decimalBanco(item.getIcmsIndicePobrezaAliquota()));
                icmssn900.setVFCPST(NumberUtil.decimalBanco(item.getValorIcmsSTIndicePobrezaOperacaoInternaInterestadualST()));
                String msgAliquota = " (" + NumberUtil.decimalBanco(item.getIcmsIndicePobrezaAliquota()) + "%)";                
                if (item.getValorIcmsSTIndicePobrezaOperacaoInternaInterestadualST() > 0) {
                    this.informacaoAdicionalProduto += "Valor FCP ST R$ " + item.getValorIcmsSTIndicePobrezaOperacaoInternaInterestadualST() + msgAliquota + ";";
                }
            }
        }
        return icmssn900;
    }

    private void validar(String xml) throws DbfException {
        String urlXsd = System.getProperty("nfe.validacao.localizacao.nfe");
        validar(xml, urlXsd);
    }

    private CestNcmModel buscarExistenciaCodigoCestParaItem(NfeItemModel item) throws DbfDatabaseException {
        CestNcmModel ncm = null;
        String classeCompleta = item.getClasseFiscal();
        String classeCom7Digitos = item.getClasseFiscal().substring(0, 7);
        String classeCom6Digitos = item.getClasseFiscal().substring(0, 6);
        String classeCom5Digitos = item.getClasseFiscal().substring(0, 5);
        String classeCom4Digitos = item.getClasseFiscal().substring(0, 4);
        String classeCom3Digitos = item.getClasseFiscal().substring(0, 3);
        String where = "\n WHERE CN.NCM_SH=" + classeCompleta + " OR CN.NCM_SH=" + classeCom7Digitos + " OR CN.NCM_SH=" + classeCom6Digitos + " OR "
                + "CN.NCM_SH=" + classeCom5Digitos + " OR CN.NCM_SH=" + classeCom4Digitos + " OR CN.NCM_SH=" + classeCom3Digitos;
        List<CestNcmModel> listaNcmsSemLike = CestNcmModel.getItens(where);
        if (listaNcmsSemLike != null && !listaNcmsSemLike.isEmpty()) {
            if (listaNcmsSemLike.size() == 1) {
                ncm = listaNcmsSemLike.get(0);
            } else {
                int sizeNcmAnterior = 0;
                for (CestNcmModel n : listaNcmsSemLike) {
                    String aux = String.valueOf(n.getNcm());
                    if (aux.length() > sizeNcmAnterior) {
                        sizeNcmAnterior = aux.length();
                        ncm = n;
                    }
                }
            }
        }
        if (ncm != null && ncm.getCestCodigo() > 0) {
            item.setCEST(ncm.getCestCodigo());
        } 
        return ncm;
    }    
    
    private String formataNumeroPainel(String numero) {
        if (numero.length() > 4) {
            return numero.substring(numero.length() - 5, numero.length());
        }

        return numero;
    }
}
