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

/**
 *
 * @author robson.costa
 */
public class IntegracaoNfeEmissorFiscal {
    
    // ================================= PIS ===================================
    public boolean isPisNt(NfeItemModel item) {
        return item.getPisSt() == 4 || item.getPisSt() == 6 || 
            item.getPisSt() == 7 || item.getPisSt() == 8 ||
            item.getPisSt() == 9;  
    }
    
    public PIS setPisNt(NfeItemModel item) {
        PISNT pisnt = new PISNT();
        PIS pis = new PIS();
        
        pisnt.setCST(Integer.toString(item.getPisSt()));
        pis.setPISNT(pisnt);
        return pis;
    }
    
    public PIS setPis(NfeItemModel item) {
        PISAliq pisAliquota = new PISAliq();
        PISNT pisnt = new PISNT();
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
        pisAliquota.setCST(Integer.toString(item.getPisSt()));
        pisAliquota.setPPIS(NumberUtil.decimalBanco(aliquotaPis));
        pisAliquota.setVPIS(NumberUtil.decimalBanco(valorPis));
        pis.setPISAliq(pisAliquota);
        if(item.getPisSt() == 99) {
            pisnt.setCST(Integer.toString(item.getPisSt()));
            pis.setPISNT(pisnt);
        }
        return pis;
    }
    
    // ================================ COFINS =================================

    public boolean isCofinsNt(NfeItemModel item) {
        return item.getCofinsSt() == 4 || item.getCofinsSt() == 6 || 
               item.getCofinsSt() == 7 || item.getCofinsSt() == 8 ||
               item.getCofinsSt() == 9;  
    }
    
    public COFINS setCofinsNt(NfeItemModel item) {
        COFINSNT cofinsnt = new COFINSNT();
        COFINS cofins = new COFINS();
        
        cofinsnt.setCST(Integer.toString(item.getCofinsSt()));
        cofins.setCOFINSNT(cofinsnt);
        return cofins;
    }
    
    public COFINS setCofins(NfeItemModel item) {
        COFINSAliq cofinsAliquota = new COFINSAliq();
        COFINSNT cofinsnt = new COFINSNT();
        COFINS cofins = new COFINS();
        double aliquotaCofins = item.getCofinsAliquota() * 100;
        double valorCofins;
        
        if (item.isDestacaDescontoNoCorpoDoDocumentoFiscal()) {
            cofinsAliquota.setVBC(NumberUtil.decimalBanco(item.getCofinsBase() - item.getDescontoValor()));
            valorCofins = (item.getCofinsBase() - item.getDescontoValor()) * item.getCofinsAliquota();
        } else {
            cofinsAliquota.setVCOFINS(NumberUtil.decimalBanco(item.getCofinsBase()));
            valorCofins = item.getCofinsValor();
        }
        cofinsAliquota.setCST(NumberUtil.decimalBanco(item.getCofinsSt()));
        cofinsAliquota.setPCOFINS(NumberUtil.decimalBanco(aliquotaCofins));
        cofinsAliquota.setVCOFINS(NumberUtil.decimalBanco(valorCofins));
        cofins.setCOFINSAliq(cofinsAliquota);
        if (item.getCofinsSt() == 99) {
            cofinsnt.setCST(Integer.toString(item.getCofinsSt()));
            cofins.setCOFINSNT(cofinsnt);
        }
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
    
}
