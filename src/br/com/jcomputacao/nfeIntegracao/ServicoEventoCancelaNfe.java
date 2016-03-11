/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.jcomputacao.nfeIntegracao;

import br.com.jcomputacao.nfe3.ws.recepcaoEvento.RecepcaoEventoStub;
import java.rmi.RemoteException;
import javax.xml.stream.XMLStreamException;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.impl.llom.util.AXIOMUtil;
import org.apache.axis2.AxisFault;

/**
 *
 * @author murilo.lima
 */
public class ServicoEventoCancelaNfe {

    public String executar(String xml) throws AxisFault, XMLStreamException, RemoteException {
        RecepcaoEventoStub stub = new RecepcaoEventoStub();
        RecepcaoEventoStub.NfeCabecMsg7 cabecalho = new RecepcaoEventoStub.NfeCabecMsg7();
        RecepcaoEventoStub.NfeCabecMsg cabec = new RecepcaoEventoStub.NfeCabecMsg();
        cabec.setCUF("35");
        cabec.setVersaoDados("1.00");
        cabecalho.setNfeCabecMsg(cabec);

        RecepcaoEventoStub.NfeDadosMsg mensagem = new RecepcaoEventoStub.NfeDadosMsg();
        OMElement element = AXIOMUtil.stringToOM(xml);
        mensagem.setExtraElement(element);

        RecepcaoEventoStub.NfeRecepcaoEventoResult resultado = stub.nfeRecepcaoEvento(mensagem, cabecalho);
        element = resultado.getExtraElement();
        return element.toString();
    }
}
