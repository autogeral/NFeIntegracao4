/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.jcomputacao.nfeIntegracao;

import br.com.jcomputacao.nfe.NFeUF;
import br.com.jcomputacao.nfe.NFeUtil;
import br.com.jcomputacao.nfe4.ws.retornoRecepcao.NFeRetAutorizacao4Stub;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.stream.XMLStreamException;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.impl.llom.util.AXIOMUtil;
import org.apache.axis2.AxisFault;

/**
 *
 * @author murilo.lima
 */
public class BuscaRetornoNfe {

    public String consultar(String recibo) throws AxisFault, XMLStreamException, RemoteException {
        Boolean virtual = Boolean.parseBoolean(System.getProperty("nfe.sefaz.virutal", "true"));
        NFeWSEndpointResolver nwe = new NFeWSEndpointResolver();
        String endpoint = nwe.getEndpoing(NFeUF.SAO_PAULO, ServicoTipoNfe.NfeRetAutorizacao);
        System.out.println(endpoint);
        NFeRetAutorizacao4Stub stub;
        if (virtual && NFeUtil.getAmbiente() == 1) {
            stub = new NFeRetAutorizacao4Stub("https://www.sefazvirtual.fazenda.gov.br/NfeRetAutorizacao/NfeRetAutorizacao.asmx");
        } else {
            stub = new NFeRetAutorizacao4Stub();
        }        

        NFeRetAutorizacao4Stub.NfeDadosMsg dados = new NFeRetAutorizacao4Stub.NfeDadosMsg();
        StringBuilder sb = new StringBuilder("<consReciNFe xmlns=\"http://www.portalfiscal.inf.br/nfe\" versao=\"4.00\">");
        sb.append("<tpAmb>").append(NFeUtil.getAmbiente()).append("</tpAmb>");
        sb.append("<nRec>").append(recibo).append("</nRec>");
        sb.append("</consReciNFe>");

        String xml = sb.toString();
        Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.ALL, xml);

        OMElement element = AXIOMUtil.stringToOM(xml);
        dados.setExtraElement(element);

        NFeRetAutorizacao4Stub.NfeResultMsg resultado = stub.nfeRetAutorizacaoLote(dados);
        element = resultado.getExtraElement();
        xml = element.toString();
        Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.ALL, xml);
        return xml;
    }
}
