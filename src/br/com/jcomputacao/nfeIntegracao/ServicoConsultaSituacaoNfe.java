/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.jcomputacao.nfeIntegracao;

import br.com.jcomputacao.nfe.NFeUF;
import br.com.jcomputacao.nfe4.ws.consultaProtocolo.NFeConsultaProtocolo4Stub;
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
public class ServicoConsultaSituacaoNfe {

    public String executar(String xml) throws AxisFault, XMLStreamException, RemoteException {
        return executar(xml, NFeUF.SAO_PAULO);
    }

    public String executar(String xml, NFeUF uf) throws AxisFault, XMLStreamException, RemoteException {
        NFeConsultaProtocolo4Stub stub = new NFeConsultaProtocolo4Stub();
        OMElement el = AXIOMUtil.stringToOM(xml);
        NFeConsultaProtocolo4Stub.NfeDadosMsg dados = new NFeConsultaProtocolo4Stub.NfeDadosMsg();                
        dados.setExtraElement(el);

        NFeConsultaProtocolo4Stub.NfeResultMsg resultado = stub.nfeConsultaNF(dados);
        String s = resultado.getExtraElement().toString();
        Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.FINER, s);
        return s;
    }
}
