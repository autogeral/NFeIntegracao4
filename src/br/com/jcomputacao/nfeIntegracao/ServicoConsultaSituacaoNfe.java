/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.jcomputacao.nfeIntegracao;

import br.com.jcomputacao.nfe.NFeUF;
import br.com.jcomputacao.nfe3.ws.consultaProtocolo.NfeConsulta2Stub;
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
        NfeConsulta2Stub stub = new NfeConsulta2Stub();
        NfeConsulta2Stub.NfeDadosMsg dados = new NfeConsulta2Stub.NfeDadosMsg();
        NfeConsulta2Stub.NfeCabecMsg2 cabec = new NfeConsulta2Stub.NfeCabecMsg2();
        NfeConsulta2Stub.NfeCabecMsg param = new NfeConsulta2Stub.NfeCabecMsg();
        param.setVersaoDados("3.10");
        param.setCUF(uf.getCodigo());
        cabec.setNfeCabecMsg(param);

        OMElement el = AXIOMUtil.stringToOM(xml);
        dados.setExtraElement(el);

        NfeConsulta2Stub.NfeConsultaNF2Result resultado = stub.nfeConsultaNF2(dados, cabec);
        String s = resultado.getExtraElement().toString();
        Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.FINER, s);
        return s;
    }
}
