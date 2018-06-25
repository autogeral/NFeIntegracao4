package br.com.jcomputacao.nfe4.ws;

import br.com.jcomputacao.nfe.ws.WsConnectionConfig;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.axis2.wsdl.WSDL2Java;

/**
 *
 * @author murilo.lima
 */
public class AutoGeracao {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String nfeRecepcao = "https://nfe.fazenda.sp.gov.br/ws/nfeautorizacao4.asmx?WSDL";
        String nfeRetRecepcao = "https://nfe.fazenda.sp.gov.br/ws/nferetautorizacao4.asmx?WSDL";        
        String nfeInutilizacao = "https://nfe.fazenda.sp.gov.br/ws/nfeinutilizacao4.asmx?WSDL";
        String nfeConsultaProtocolo = "https://nfe.fazenda.sp.gov.br/ws/nfeconsultaprotocolo4.asmx?WSDL";
        String nfeStatusServico = "https://nfe.fazenda.sp.gov.br/ws/nfestatusservico4.asmx?WSDL";
        String nfeConsultaCadastro = "https://nfe.fazenda.sp.gov.br/ws/cadconsultacadastro4.asmx?WSDL";
        String recepcaoEvento = "https://nfe.fazenda.sp.gov.br/ws/nferecepcaoevento4.asmx?WSDL";        
//
        System.setProperty("sun.security.ssl.allowUnsafeRenegotiation", "true");
        System.setProperty("nfe.certificado.senha", "552289800533");
        System.setProperty("nfe.certificado.caminho", "C:/DBF/dist/A1_20190306AG.pfx");
        WsConnectionConfig.setProperties("05437537000137");
        AutoGeracao gerador = new AutoGeracao();
        gerador.geraWSDL(nfeConsultaCadastro, "consultaCadastro");
        gerador.geraWSDL(nfeStatusServico, "statusServico");
        gerador.geraWSDL(nfeConsultaProtocolo, "consultaProtocolo");
        gerador.geraWSDL(nfeRecepcao, "recepcao");
        gerador.geraWSDL(nfeRetRecepcao, "retornoRecepcao");        
        gerador.geraWSDL(nfeInutilizacao, "inutilizacao");
        gerador.geraWSDL(recepcaoEvento, "recepcaoEvento");
    }
    
    public void geraWSDL(String wsdl, String pack) {
        try {
            WSDL2Java.main(new String[]{"-uri", wsdl, "-p", "br.com.jcomputacao.nfe4.ws." + pack});
            System.out.println("*** Geracao concluida ***");
        } catch (Exception ex) {
            Logger.getLogger(AutoGeracao.class.getName()).log(Level.SEVERE, "*** Erro com a geracao ***", ex);
        }
    }
    
}
