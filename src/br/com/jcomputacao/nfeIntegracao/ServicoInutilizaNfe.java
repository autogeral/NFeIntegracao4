/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.jcomputacao.nfeIntegracao;

import br.com.jcomputacao.nfe3.ws.inutilizacao.NfeInutilizacao2Stub;
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
        NfeInutilizacao2Stub stub = new NfeInutilizacao2Stub();
        NfeInutilizacao2Stub.NfeDadosMsg dados = new NfeInutilizacao2Stub.NfeDadosMsg();
        OMElement el = AXIOMUtil.stringToOM(xml);
        dados.setExtraElement(el);
        NfeInutilizacao2Stub.NfeCabecMsg6 cabecalho = new NfeInutilizacao2Stub.NfeCabecMsg6();
        NfeInutilizacao2Stub.NfeCabecMsg cabec = new NfeInutilizacao2Stub.NfeCabecMsg();
        cabec.setVersaoDados("3.10");
        cabec.setCUF("35");
        cabecalho.setNfeCabecMsg(cabec);
        NfeInutilizacao2Stub.NfeInutilizacaoNF2Result inut = stub.nfeInutilizacaoNF2(dados, cabecalho);
        String resultado = inut.getExtraElement().toString();
        return resultado;
    }
}
