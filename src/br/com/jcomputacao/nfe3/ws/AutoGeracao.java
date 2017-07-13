package br.com.jcomputacao.nfe3.ws;

import br.com.jcomputacao.nfe.ws.WsConnectionConfig;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.axis2.wsdl.WSDL2Java;

/**
 * 27/03/2014 13:40:08
 * @author Murilo
 */
public class AutoGeracao {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String nfeRecepcao = "https://nfe.fazenda.sp.gov.br/ws/nfeautorizacao.asmx?WSDL";
        String nfeRetRecepcao = "https://nfe.fazenda.sp.gov.br/ws/nferetautorizacao.asmx?WSDL";
        String nfeCancelamento = "https://nfe.fazenda.sp.gov.br/nfeweb/services/nfecancelamento2.asmx?WSDL";
        String nfeInutilizacao = "https://nfe.fazenda.sp.gov.br/ws/nfeinutilizacao2.asmx?WSDL";
        String nfeConsultaProtocolo = "https://nfe.fazenda.sp.gov.br/ws/nfeconsulta2.asmx?WSDL";
        String nfeStatusServico = "https://nfe.fazenda.sp.gov.br/ws/nfestatusservico2.asmx?WSDL";
        String nfeConsultaCadastro = "https://nfe.fazenda.sp.gov.br/ws/cadconsultacadastro2.asmx?WSDL";
        String recepcaoEvento = "https://nfe.fazenda.sp.gov.br/ws/recepcaoevento.asmx?WSDL";
        //String nfedownload = "https://www.sefazvirtual.fazenda.gov.br/NfeDownloadNF/NfeDownloadNF.asmx?WSDL";
        String nfedownload = "https://www.nfe.fazenda.gov.br/NfeDownloadNF/NfeDownloadNF.asmx?WSDL";
        String nfeConsultaDest = "https://www.nfe.fazenda.gov.br/NFeConsultaDest/NFeConsultaDest.asmx?WSDL";
//
        System.setProperty("sun.security.ssl.allowUnsafeRenegotiation", "true");
        System.setProperty("nfe.certificado.senha", "552289800533");
        System.setProperty("nfe.certificado.caminho", "C:/DBF/dist/A1_20180308AG.pfx");
        WsConnectionConfig.setProperties("05437537000137");
        AutoGeracao gerador = new AutoGeracao();
//        gerador.geraWSDL(nfeConsultaCadastro, "consultaCadastro");
//        gerador.geraWSDL(nfeStatusServico, "statusServico");
//        gerador.geraWSDL(nfeConsultaProtocolo, "consultaProtocolo");
//        gerador.geraWSDL(nfeRecepcao, "recepcao");
//        gerador.geraWSDL(nfeRetRecepcao, "retornoRecepcao");
//        gerador.geraWSDL(nfeCancelamento, "cancelamento");
//        gerador.geraWSDL(nfeInutilizacao, "inutilizacao");
//        gerador.geraWSDL(recepcaoEvento, "recepcaoEvento");
        gerador.geraWSDL(nfedownload, "nfedownload");
        gerador.geraWSDL(nfeConsultaDest, "nfeConsultaDest");
    }
    
    public void geraWSDL(String wsdl, String pack) {
        try {
            WSDL2Java.main(new String[]{"-uri", wsdl, "-p", "br.com.jcomputacao.nfe.manifesto.ws." + pack});
            System.out.println("*** Geracao concluida ***");
        } catch (Exception ex) {
            Logger.getLogger(AutoGeracao.class.getName()).log(Level.SEVERE, "*** Erro com a geracao ***", ex);
        }
    }

}
