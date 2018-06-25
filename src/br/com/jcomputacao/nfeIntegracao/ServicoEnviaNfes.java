/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.jcomputacao.nfeIntegracao;

import br.com.jcomputacao.nfe.NFeUF;
import br.com.jcomputacao.nfe.NFeUtil;
import br.com.jcomputacao.nfe4.ws.recepcao.NFeAutorizacao4Stub;
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
public class ServicoEnviaNfes {

    public String enviar(String xml) throws AxisFault, XMLStreamException, RemoteException {
        Boolean virtual = Boolean.parseBoolean(System.getProperty("nfe.sefaz.virutal", "true"));
        NFeWSEndpointResolver nwe = new NFeWSEndpointResolver();
        String endpoint = nwe.getEndpoing(NFeUF.SAO_PAULO, ServicoTipoNfe.NfeAutorizacao);
        System.out.println(endpoint);
        NFeAutorizacao4Stub stub;
        if (virtual && NFeUtil.getAmbiente() == 1) {
            stub = new NFeAutorizacao4Stub("https://www.sefazvirtual.fazenda.gov.br/NfeAutorizacao/NfeAutorizacao.asmx");
        } else {
            stub = new NFeAutorizacao4Stub();
        }
        NFeAutorizacao4Stub.NfeDadosMsg nfeDadosMsg = new NFeAutorizacao4Stub.NfeDadosMsg();
        OMElement element = AXIOMUtil.stringToOM(xml);
        nfeDadosMsg.setExtraElement(element);        
        Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.INFO, "Enviando Lote de NFes");
        NFeAutorizacao4Stub.NfeResultMsg recepcao = stub.nfeAutorizacaoLote(nfeDadosMsg);
        Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.INFO, "Resultado do enviando de NFes");

        element = recepcao.getExtraElement();
        xml = element.toString();

        Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.ALL, xml);
        return xml;
    }
}
