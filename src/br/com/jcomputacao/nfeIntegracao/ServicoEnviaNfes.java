/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.jcomputacao.nfeIntegracao;

import br.com.jcomputacao.nfe.NFeUF;
import br.com.jcomputacao.nfe.NFeUtil;
import br.com.jcomputacao.nfe3.ws.recepcao.NfeAutorizacaoStub;
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
        NfeAutorizacaoStub stub;
        if (virtual && NFeUtil.getAmbiente() == 1) {
            stub = new NfeAutorizacaoStub("https://www.sefazvirtual.fazenda.gov.br/NfeAutorizacao/NfeAutorizacao.asmx");
        } else {
            stub = new NfeAutorizacaoStub();
        }
        NfeAutorizacaoStub.NfeDadosMsg nfeDadosMsg = new NfeAutorizacaoStub.NfeDadosMsg();
        OMElement element = AXIOMUtil.stringToOM(xml);
        nfeDadosMsg.setExtraElement(element);
        NfeAutorizacaoStub.NfeCabecMsg3 nfeCabecMsg = new NfeAutorizacaoStub.NfeCabecMsg3();
        NfeAutorizacaoStub.NfeCabecMsg cabec = new NfeAutorizacaoStub.NfeCabecMsg();
        cabec.setCUF("35");
        cabec.setVersaoDados("3.10");
        nfeCabecMsg.setNfeCabecMsg(cabec);
        Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.INFO, "Enviando Lote de NFes");
        NfeAutorizacaoStub.NfeAutorizacaoLoteResult recepcao = stub.nfeAutorizacaoLote(nfeDadosMsg, nfeCabecMsg);
        Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.INFO, "Resultado do enviando de NFes");

        element = recepcao.getExtraElement();
        xml = element.toString();

        Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.ALL, xml);
        return xml;
    }
}
