package br.com.jcomputacao.nfeEmissor;

import br.com.jcomputacao.model.NfeItemModel;
import br.com.jcomputacao.util.NumberUtil;
import br.inf.portalfiscal.nfe.xml.pl009v4.nfes.TIpi;
import br.inf.portalfiscal.nfe.xml.pl009v4.nfes.TIpi.IPINT;
import br.inf.portalfiscal.nfe.xml.pl009v4.nfes.TNFe.InfNFe.Det.Imposto.COFINS;
import br.inf.portalfiscal.nfe.xml.pl009v4.nfes.TNFe.InfNFe.Det.Imposto.COFINS.COFINSAliq;
import br.inf.portalfiscal.nfe.xml.pl009v4.nfes.TNFe.InfNFe.Det.Imposto.COFINS.COFINSNT;
import br.inf.portalfiscal.nfe.xml.pl009v4.nfes.TNFe.InfNFe.Det.Imposto.PIS;
import br.inf.portalfiscal.nfe.xml.pl009v4.nfes.TNFe.InfNFe.Det.Imposto.PIS.PISAliq;
import br.inf.portalfiscal.nfe.xml.pl009v4.nfes.TNFe.InfNFe.Det.Imposto.PIS.PISNT;
import br.inf.portalfiscal.nfe.xml.pl009v4.nfes.TNFe.InfNFe.Det.Imposto.ICMS;
import br.inf.portalfiscal.nfe.xml.pl009v4.nfes.TNFe.InfNFe.Det.Imposto.ICMS.ICMS00;
import br.inf.portalfiscal.nfe.xml.pl009v4.nfes.TNFe.InfNFe.Det.Imposto.ICMS.ICMS10;
import br.inf.portalfiscal.nfe.xml.pl009v4.nfes.TNFe.InfNFe.Det.Imposto.ICMS.ICMS20;
import br.inf.portalfiscal.nfe.xml.pl009v4.nfes.TNFe.InfNFe.Det.Imposto.ICMS.ICMS40;
import br.inf.portalfiscal.nfe.xml.pl009v4.nfes.TNFe.InfNFe.Det.Imposto.ICMS.ICMS51;
import br.inf.portalfiscal.nfe.xml.pl009v4.nfes.TNFe.InfNFe.Det.Imposto.ICMS.ICMS60;
import br.inf.portalfiscal.nfe.xml.pl009v4.nfes.TNFe.InfNFe.Det.Imposto.ICMS.ICMS90;
import br.inf.portalfiscal.nfe.xml.pl009v4.nfes.TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN101;
import br.inf.portalfiscal.nfe.xml.pl009v4.nfes.TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN102;
import br.inf.portalfiscal.nfe.xml.pl009v4.nfes.TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN201;
import br.inf.portalfiscal.nfe.xml.pl009v4.nfes.TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN202;
import br.inf.portalfiscal.nfe.xml.pl009v4.nfes.TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN500;
import br.inf.portalfiscal.nfe.xml.pl009v4.nfes.TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN900;
import br.inf.portalfiscal.nfe.xml.pl009v4.nfes.TNFe.InfNFe.Det.Imposto.ICMSUFDest;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
/**
 *
 * @author robson.costa
 */
public class IntegracaoNfeEmissorFiscal {

    Set<Integer> cstsPisCofinsOutr = new HashSet<Integer>();
    Set<Integer> cstsPisCofinsNt = new HashSet<Integer>();
    
//    private static final boolean nfeTributaDifal = Boolean.parseBoolean(System.getProperty("nfe.tributa.difal", "false"));
//    private String informacaoAdicionalProduto = "";
//
//    public String getInformacaoAdicionalProduto() {
//        return informacaoAdicionalProduto;
//    }
//
//    public void setInformacaoAdicionalProduto(String informacaoAdicionalProduto) {
//        this.informacaoAdicionalProduto = informacaoAdicionalProduto;
//    }
//    
    public IntegracaoNfeEmissorFiscal() {
        List<Integer> listCstsPisCofinsOutr = Arrays.asList(49 ,50 ,51 ,52 ,53 ,54 ,55 ,56 ,60 ,61 ,62 ,63 ,64 ,65 ,66 ,67 ,70 ,71 ,72 ,73 ,74 ,75 ,98 ,99);
        cstsPisCofinsOutr.addAll(listCstsPisCofinsOutr);
        
        List<Integer> listCstsPisCofinsNt = Arrays.asList(4, 5 , 6 , 7 , 8 , 9);
        cstsPisCofinsNt.addAll(listCstsPisCofinsNt);
    }
    
    // ================================= PIS ===================================

    public PIS atribuiPis(NfeItemModel item) {
        PIS pis = new PIS();

        if (isPisAliq(item)) {
            pis = setPisAliq(item);
        } else if (cstsPisCofinsNt.contains(item.getPisSt())) {
            pis = setPisNt(item);
        } else if (this.cstsPisCofinsOutr.contains(item.getPisSt())) {
            pis = setPisOutr(item);
        }
        return pis;
    }
    
    private PIS setPisNt(NfeItemModel item) {
        PISNT pisnt = new PISNT();
        PIS pis = new PIS();
        
        pisnt.setCST("0"+Integer.toString(item.getPisSt()));
        pis.setPISNT(pisnt);
        return pis;
    }
    
    private PIS setPisOutr(NfeItemModel item) {
        PIS pis = new PIS();
        PIS.PISOutr pisOutr = new PIS.PISOutr();
        double aliquotaPis = item.getPisAliquota() * 100;

        pisOutr.setCST(Integer.toString(item.getPisSt()));
//        pisOutr.setVBC(NumberUtil.decimalBanco(item.getPisBase()));
//        pisOutr.setPPIS(NumberUtil.decimalBanco(aliquotaPis));
//        pisOutr.setVPIS(NumberUtil.decimalBanco(item.getPisValor()));
        pisOutr.setQBCProd("0.0000");
        pisOutr.setVAliqProd("0.0000");
        pisOutr.setVPIS("0.00");
        pis.setPISOutr(pisOutr);
        return pis;
    }
    
    private boolean isPisAliq(NfeItemModel item) {
        return item.getPisSt() == 01 || item.getPisSt() == 02;
    }
    
    private PIS setPisAliq(NfeItemModel item) {
        PISAliq pisAliquota = new PISAliq();
        PIS pis = new PIS();
        double aliquotaPis = item.getPisAliquota() * 100;
        double valorPis;
        
//        pisAliquota.setCST(Integer.toString(item.getPisSt()));
        if (item.isDestacaDescontoNoCorpoDoDocumentoFiscal()) {
        // PS pegar do que foi retornado do EMISSOR FISCAL ? como funciona quando tem que destacar o desconto 
        // pisAliquota.setVBC(NumberUtil.decimalBanco(item.getValorTotal() - item.getDescontoValor()));
            pisAliquota.setVBC(NumberUtil.decimalBanco(item.getPisBase() - item.getDescontoValor()));
            valorPis = (item.getPisBase() - item.getDescontoValor()) * item.getPisAliquota();
        } else {
            pisAliquota.setVBC(NumberUtil.decimalBanco(item.getPisBase()));
            valorPis = item.getPisValor();
        }
        pisAliquota.setCST("0"+Integer.toString(item.getPisSt()));
        pisAliquota.setPPIS(NumberUtil.decimalBanco(aliquotaPis));
        pisAliquota.setVPIS(NumberUtil.decimalBanco(valorPis));
        pis.setPISAliq(pisAliquota);
        
        return pis;
    }
    
    // ================================ COFINS =================================

    public COFINS atribuiCofins(NfeItemModel item) {
        COFINS cofins = new COFINS();

        if (isCofinsAliq(item)) {
            cofins = setCofinsAliq(item);
        } else if (cstsPisCofinsNt.contains(item.getCofinsSt())) {
            cofins = setCofinsNt(item);
        } else if (this.cstsPisCofinsOutr.contains(item.getCofinsSt())) {
            cofins = setCofinsOutr(item);
        }
        return cofins;
    }
    
    private COFINS setCofinsNt(NfeItemModel item) {
        COFINSNT cofinsnt = new COFINSNT();
        COFINS cofins = new COFINS();
        
        cofinsnt.setCST("0"+Integer.toString(item.getCofinsSt()));
        cofins.setCOFINSNT(cofinsnt);
        return cofins;
    }
    
    private COFINS setCofinsOutr(NfeItemModel item) {
        COFINS cofins = new COFINS();
        COFINS.COFINSOutr cofinsOutr = new COFINS.COFINSOutr();
        double aliquotaCofins = item.getCofinsAliquota() * 100;

        cofinsOutr.setCST(Integer.toString(item.getCofinsSt()));
//        cofinsOutr.setVBC(NumberUtil.decimal(item.getPisBase()));
//        cofinsOutr.setVCOFINS(NumberUtil.decimalBanco(item.getCofinsValor()));
//        cofinsOutr.setPCOFINS(NumberUtil.decimalBanco(aliquotaCofins));
        cofinsOutr.setQBCProd("0.0000");
        cofinsOutr.setVAliqProd("0.0000");
        cofinsOutr.setVCOFINS("0.00");
        cofins.setCOFINSOutr(cofinsOutr);
        return cofins;
    }
    
    private boolean isCofinsAliq(NfeItemModel item) {
       return item.getCofinsSt() == 01 || item.getCofinsSt() == 02;
    }
    
    private COFINS setCofinsAliq(NfeItemModel item) {
        COFINSAliq cofinsAliquota = new COFINSAliq();
        COFINS cofins = new COFINS();
        double aliquotaCofins = item.getCofinsAliquota() * 100;
        double valorCofins;
        
        if (item.isDestacaDescontoNoCorpoDoDocumentoFiscal()) {
            cofinsAliquota.setVBC(NumberUtil.decimalBanco(item.getCofinsBase() - item.getDescontoValor()));
            valorCofins = (item.getCofinsBase() - item.getDescontoValor()) * item.getCofinsAliquota();
        } else {
            cofinsAliquota.setVBC(NumberUtil.decimalBanco(item.getCofinsBase()));
            valorCofins = item.getCofinsValor();
        }
        cofinsAliquota.setCST("0"+Integer.toString(item.getCofinsSt()));
        cofinsAliquota.setPCOFINS(NumberUtil.decimalBanco(aliquotaCofins));
        cofinsAliquota.setVCOFINS(NumberUtil.decimalBanco(valorCofins));
        cofins.setCOFINSAliq(cofinsAliquota);
        return cofins;
    }
    
    
    
    
    
    
    // ================================= IPI ===================================
    public boolean isIpiNt(NfeItemModel item) {
        return item.getIpiSt() == 0 && item.getIpiValor() == 0 || item.getIpiSt() == 51 ;
    }
    
    public TIpi setIpiNt(NfeItemModel item,String cnpj) {
        TIpi ipi = new TIpi();
        IPINT ipiNt = new IPINT();
        ipi.setCNPJProd(cnpj);
        ipi.setIPINT(ipiNt);
        ipiNt.setCST(Integer.toString(item.getIpiSt()));
        return ipi;
    }
    
    public TIpi setIpi(NfeItemModel item, String cnpj) {
        TIpi.IPITrib tributacao = new TIpi.IPITrib();
        TIpi ipi = new TIpi();
        double aliquotaIpi = item.getIpiAliquota() * 100;

        if (item.isDestacaDescontoNoCorpoDoDocumentoFiscal()) {
            tributacao.setVBC(NumberUtil.decimalBanco(item.getIpiBase() - item.getDescontoValor()));
        } else {
            tributacao.setVBC(NumberUtil.decimalBanco(item.getIpiBase()));
        }
        tributacao.setPIPI(NumberUtil.decimalBanco(aliquotaIpi));
        tributacao.setVIPI(NumberUtil.decimalBanco(item.getIpiValor()));
        tributacao.setCST(Integer.toString(item.getIpiSt()));
        ipi.setIPITrib(tributacao);
        ipi.setCNPJProd(cnpj);
        
        if (item.getIpiSt() == 55) {
            ipi.setCEnq("105");
            return ipi;
        }
        ipi.setCEnq("999");
        return ipi;
    }
    
    // ================================= ICMS ===================================
//    
//    private String montaMsgInformacaoAdicionalProduto(ICMS icms, NfeItemModel item) {
//        String msgAliquota = " (" + NumberUtil.decimalBanco(item.getIcmsIndicePobrezaAliquota()) + "%)";
//        switch (item.getSituacaoTributaria()) {
////            case 0: acho que nem tem
//            case 10:
//                if (item.getValorIcmsIndicePobreza() > 0) {
//                    ICMS10 tribuICMS10 = icms.getICMS10();
//                    this.informacaoAdicionalProduto += getMsgBaseFcpAndValorFcp(tribuICMS10.getVBC(), item.getValorIcmsIndicePobrezaString(), msgAliquota);
//                }
//                if (item.getValorIcmsSTIndicePobrezaOperacaoInternaInterestadualST() > 0) {
//                    this.informacaoAdicionalProduto += getMsgValorFcpSt(item.getValorIcmsSTIndicePobrezaOperacaoInternaInterestadualST(), msgAliquota);
//                }
//                return this.informacaoAdicionalProduto;
//            case 20:
////                if (item.getValorIcmsIndicePobreza() > 0) {
//                ICMS20 tribuICMS20 = icms.getICMS20();
//                return getMsgBaseFcpAndValorFcp(tribuICMS20.getVBC(), item.getValorIcmsIndicePobrezaString(), msgAliquota);
////                }
//            case 90:
//                ICMS90 tribuICMS90 = icms.getICMS90();
//                if (item.getValorIcmsIndicePobreza() > 0) {
//                    this.informacaoAdicionalProduto += getMsgBaseFcpAndValorFcp(tribuICMS90.getVBC(), item.getValorIcmsIndicePobrezaString(), msgAliquota);
//                }
//                if (item.getValorIcmsSTIndicePobrezaOperacaoInternaInterestadualST() > 0) {
//                    this.informacaoAdicionalProduto += getMsgValorFcpSt(item.getValorIcmsSTIndicePobrezaOperacaoInternaInterestadualST(), msgAliquota);
//                }
//                return this.informacaoAdicionalProduto;
//            default:
//                return "";
//        }
//    }
//    
//    private String getMsgBaseFcpAndValorFcp(String vBaseCalcFcp, String valorFcp, String aliquota) {
//        return "Valor base calculo FCP R$ " +vBaseCalcFcp + "; "
//                + " Valor FCP R$ " + valorFcp + aliquota + "; ";
//    }
//    
//    private String getMsgValorFcpSt(double valorFcpSt, String aliquota) {
//        return " Valor FCP ST R$ " + valorFcpSt + aliquota + "; ";
//    }
//            
//    public ICMS atribuiIcms(ICMS icms, NfeItemModel item, String origem) {
//        switch(item.getSituacaoTributaria()) {
//            case 0:
//                return atribuiIcms00(icms, item, origem);
//            case 10:
//                return atribuiIcms10(icms, item, origem);
//            case 20:
//                return atribuiIcms20(icms, item, origem);
//            case 40:
//                return atribuiIcms40(icms, item, origem);
//            case 60:
////                return atribuiIcms60(icms, item, origem);
//        }
//        
//        return null;
//    }
//    
//    private ICMS atribuiIcms00(ICMS icms, NfeItemModel item, String origem) {
//        ICMS00 tribuIcms00 = new ICMS00();
//        tribuIcms00.setCST(Integer.toString(item.getSituacaoTributaria()));
//        tribuIcms00.setOrig(origem);
//        tribuIcms00.setModBC("3");
//        tribuIcms00.setVBC(NumberUtil.decimalBanco(item.getBaseIcmsValor()));
//        tribuIcms00.setPICMS(NumberUtil.decimalBanco(item.getIcmsAliquota() * 100));
//        tribuIcms00.setVICMS(NumberUtil.decimalBanco(item.getIcmsValor()));
//        icms.setICMS00(tribuIcms00);
//        return  icms;
//    }
//    
//    private ICMS atribuiIcms10(ICMS icms, NfeItemModel item, String origem) {
//        ICMS10 tribuICMS10 = new ICMS10();
//        tribuICMS10.setCST(Integer.toString(item.getSituacaoTributaria()));
//        tribuICMS10.setOrig(origem);
//        tribuICMS10.setModBC("3");
//        tribuICMS10.setVBC(NumberUtil.decimalBanco(item.getBaseIcmsValor()));
//        tribuICMS10.setPICMS(NumberUtil.decimalBanco(item.getIcmsAliquota() * 100));
//        tribuICMS10.setVICMS(NumberUtil.decimalBanco(item.getIcmsValor()));
//        tribuICMS10.setModBCST("4");
//        // AGORA TENHO QUE VER COMO FUNCIONA A ST
//        tribuICMS10.setVBCST(NumberUtil.decimalBanco(item.getBaseIcmsStValor()));
//        tribuICMS10.setPMVAST(NumberUtil.decimalBanco(item.getIva()));
//        
//        tribuICMS10.setPICMSST(NumberUtil.decimalBanco(item.getIcmsAliquota() * 100));        
//        tribuICMS10.setVICMSST(NumberUtil.decimalBanco(item.getValorIcmsSt()));
//        
//        if (item.getValorIcmsStPorcentagemReducao() > 0) {
//            tribuICMS10.setPRedBCST(NumberUtil.decimalBanco(item.getValorIcmsStPorcentagemReducao()));
//        }
//        
////  //       Essa parte (FCP e DIFAL) está sendo CALCULADA AQUI no ERP e não no EMISSOR-FISCAL 
//// SEGUNDO a GABRIELA: FCP e DIFAL são emitidos com CST 00
//
////        if (nfeTributaDifal && item.getValorIcmsSt() > 0 && item.getIcmsIndicePobrezaAliquota() > 0
////                && item.getValorIcmsUFDestino() == 0) { //ICMS UF Destino = 0, garante que a nota nao tenha DIFAL (nota para contribuite de ICMS)
////            tribuICMS10.setVBCFCP(tribuICMS10.getVBC());
////            tribuICMS10.setPFCP(NumberUtil.decimalBanco(item.getIcmsIndicePobrezaAliquota() * 100));
////            tribuICMS10.setVFCP(NumberUtil.decimalBanco(item.getValorIcmsIndicePobreza()));
////            tribuICMS10.setVBCFCPST(tribuICMS10.getVBCST());
////            tribuICMS10.setPFCPST(NumberUtil.decimalBanco(item.getIcmsIndicePobrezaAliquota()));
////            tribuICMS10.setVFCPST(NumberUtil.decimalBanco(item.getValorIcmsSTIndicePobrezaOperacaoInternaInterestadualST()));
////            if (item.getValorIcmsIndicePobreza() > 0) {
////                this.informacaoAdicionalProduto += montaMsgInformacaoAdicionalProduto(icms, item);
////            }
////            if (item.getValorIcmsSTIndicePobrezaOperacaoInternaInterestadualST() > 0) {
////                this.informacaoAdicionalProduto += montaMsgInformacaoAdicionalProduto(icms, item);
////            }
////        }
//        icms.setICMS10(tribuICMS10);
//        return icms;
//    }
//    
//    
//    private ICMS atribuiIcms20(ICMS icms, NfeItemModel item, String origem) {
//        ICMS20 tributaICMS20 = new ICMS20();
//        tributaICMS20.setCST(Integer.toString(item.getSituacaoTributaria()));
//        tributaICMS20.setOrig(origem);
//        tributaICMS20.setModBC("3");
//        tributaICMS20.setVBC(NumberUtil.decimal(item.getBaseIcmsValor()));
//        tributaICMS20.setPICMS(NumberUtil.decimal(item.getIcmsAliquota() * 100));
//        tributaICMS20.setVICMS(NumberUtil.decimal(item.getIcmsValor()));
//        if (item.getValorIcmsStPorcentagemReducaoPorcentagem() > 0) {
//            tributaICMS20.setPRedBC(NumberUtil.decimalBanco(item.getValorIcmsStPorcentagemReducao() * 100));
//        } else {
//            tributaICMS20.setPRedBC("0.00");
//        }
//        // TILL HERE is OK, without prolems (I think and i hope)
//        // Seguindo a classe IntegracaoNfe, aqui teria o preenchimento referente ao FCP
//        // Porém a Gabriela (contabilidade) disse que: DIFAL e FCP, é na CST 00
//        
//        icms.setICMS20(tributaICMS20); 
//        return icms;
//    }
//    private ICMS atribuiIcms40(ICMS icms, NfeItemModel item, String origem) {
//        ICMS40 tributaICMS40 = new ICMS40();
//        tributaICMS40.setCST(Integer.toString(item.getSituacaoTributaria()));
//        tributaICMS40.setOrig(origem);
//        icms.setICMS40(tributaICMS40);
//        return icms;
//    }
    
    
    
    
    
    
//    private ICMS atribuiIcms60(ICMS icms, NfeItemModel item, String origem) {
//        ICMS60 tribuataICMS60 = new ICMS60();
////        tribuataICMS60
//    }

//    private ICMS atribuiIcms40(ICMS icms, NfeItemModel item, String origem) {
//        
//    }
    
    // TENHO que montar o metodo de difaL; ICMSUfDest difal(item)

    

}
