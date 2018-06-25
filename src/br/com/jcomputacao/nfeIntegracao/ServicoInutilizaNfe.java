/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.jcomputacao.nfeIntegracao;

import br.com.jcomputacao.nfe4.ws.inutilizacao.NFeInutilizacao4Stub;
import java.rmi.RemoteException;
import javax.xml.stream.XMLStreamException;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.impl.llom.util.AXIOMUtil;
import org.apache.axis2.AxisFault;

/**
 *
 * @author murilo.lima
 */
public class ServicoInutilizaNfe {

    public String inutilizar(String xml) throws AxisFault, XMLStreamException, RemoteException {
        NFeInutilizacao4Stub stub = new NFeInutilizacao4Stub();
        OMElement el = AXIOMUtil.stringToOM(xml);

        NFeInutilizacao4Stub.NfeDadosMsg dados = new NFeInutilizacao4Stub.NfeDadosMsg();        
        dados.setExtraElement(el);
        
        NFeInutilizacao4Stub.NfeResultMsg inut = stub.nfeInutilizacaoNF(dados);
        String resultado = inut.getExtraElement().toString();
        return resultado;
    }
}
