package br.com.jcomputacao.nfce1.ws;

import br.com.jcomputacao.nfe.ws.WsConnectionConfig;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.axis2.wsdl.WSDL2Java;

/**
 * 09/06/2015 23:04:00
 * @author murilo
 */
public class AutoGeracao {

    public static void main(String[] args) {

        String NfeAutorizacao = "https://nfce.fazenda.sp.gov.br/ws/nfeautorizacao.asmx?WSDL";
        String NfeRetAutorizacao = "https://nfce.fazenda.sp.gov.br/ws/nferetautorizacao.asmx?WSDL";
        String NfeInutilizacao2 = "https://nfce.fazenda.sp.gov.br/ws/nfeinutilizacao2.asmx?WSDL";
        String NfeConsulta2 = "https://nfce.fazenda.sp.gov.br/ws/nfeconsulta2.asmx?WSDL";
        String RecepcaoEvento = "https://nfce.fazenda.sp.gov.br/ws/recepcaoevento.asmx?WSDL";
        String NfeStatusServico2 = "https://nfce.fazenda.sp.gov.br/ws/nfestatusservico2.asmx?WSDL";
        //1.2 WebService do ambiente de produção da contingência EPEC:

        String RecepcaoEPEC = "https://nfce.epec.fazenda.sp.gov.br/EPECws/RecepcaoEPEC.asmx?WSDL";
        String NfeStatusServico2EPEC = "https://nfce.epec.fazenda.sp.gov.br/EPECws/EPECStatusServico.asmx?WSDL";

/*
        String NfeAutorizacao = "https://homologacao.nfce.fazenda.sp.gov.br/ws/nfeautorizacao.asmx?WSDL";
        String NfeRetAutorizacao = "https://homologacao.nfce.fazenda.sp.gov.br/ws/nferetautorizacao.asmx?WSDL";
        String NfeInutilizacao2 = "https://homologacao.nfce.fazenda.sp.gov.br/ws/nfeinutilizacao2.asmx?WSDL";
        String NfeConsulta2 = "https://homologacao.nfce.fazenda.sp.gov.br/ws/nfeconsulta2.asmx?WSDL";
        String RecepcaoEvento = "https://homologacao.nfce.fazenda.sp.gov.br/ws/recepcaoevento.asmx?WSDL";
        String NfeStatusServico2 = "https://homologacao.nfce.fazenda.sp.gov.br/ws/nfestatusservico2.asmx?WSDL";

        String RecepcaoEPEC = "https://homologacao.nfce.epec.fazenda.sp.gov.br/EPECws/RecepcaoEPEC.asmx?WSDL";
        String NfeStatusServico2EPEC = "https://homologacao.nfce.epec.fazenda.sp.gov.br/EPECws/EPECStatusServico.asmx?WSDL";
*/
        
//
        System.setProperty("sun.security.ssl.allowUnsafeRenegotiation", "true");
        System.setProperty("nfe.certificado.senha", "55223602");
        //
        System.setProperty("nfe.certificado.caminho", "/Users/murilo/Documents/Auto Geral/A1_20160307.pfx");
        WsConnectionConfig.setProperties("05437537000137");
        AutoGeracao gerador = new AutoGeracao();
        gerador.geraWSDL(NfeAutorizacao, "nfeAutorizacao");
        gerador.geraWSDL(NfeRetAutorizacao, "nfeRetAutorizacao");
        gerador.geraWSDL(NfeInutilizacao2, "nfeInutilizacao2");
        gerador.geraWSDL(NfeConsulta2, "nfeConsulta2");
        gerador.geraWSDL(RecepcaoEvento, "recepcaoEvento");
        gerador.geraWSDL(NfeStatusServico2, "nfeStatusServico2");
        gerador.geraWSDL(RecepcaoEPEC, "recepcaoEPEC");
        gerador.geraWSDL(NfeStatusServico2EPEC, "nfeStatusServico2EPEC");
    }
    
    public void geraWSDL(String wsdl, String pack) {
        try {
            WSDL2Java.main(new String[]{"-uri", wsdl, "-p", "br.com.jcomputacao.nfce1.ws." + pack});
            System.out.println("*** Geracao concluida ***");
        } catch (Exception ex) {
            Logger.getLogger(br.com.jcomputacao.nfe3.ws.AutoGeracao.class.getName()).log(Level.SEVERE, "*** Erro com a geracao ***", ex);
        }
    }

}
