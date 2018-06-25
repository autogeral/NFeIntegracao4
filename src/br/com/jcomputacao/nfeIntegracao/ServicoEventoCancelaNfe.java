/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.jcomputacao.nfeIntegracao;

import br.com.jcomputacao.nfe4.ws.recepcaoEvento.NFeRecepcaoEvento4Stub;
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
        NFeRecepcaoEvento4Stub stub = new NFeRecepcaoEvento4Stub();        
        OMElement element = AXIOMUtil.stringToOM(xml);
        NFeRecepcaoEvento4Stub.NfeDadosMsg mensagem = new NFeRecepcaoEvento4Stub.NfeDadosMsg();        
        mensagem.setExtraElement(element);        

        NFeRecepcaoEvento4Stub.NfeResultMsg resultado = stub.nfeRecepcaoEvento(mensagem);
        element = resultado.getExtraElement();
        return element.toString();
    }
}
