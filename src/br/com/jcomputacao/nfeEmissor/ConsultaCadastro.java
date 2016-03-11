/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.jcomputacao.nfeEmissor;

import br.com.jcomputacao.nfe.ws.WsConnectionConfig;
import br.com.jcomputacao.nfe3.ws.consultaCadastro.CadConsultaCadastro2Stub;
import java.rmi.RemoteException;
import javax.xml.stream.XMLStreamException;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.impl.llom.util.AXIOMUtil;
import org.apache.axis2.AxisFault;

/**
 *
 * @author murilo.lima
 */
public class ConsultaCadastro {
      /**
     "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" 
                + "<ConsCad xmlns=\"http://www.portalfiscal.inf.br/nfe\" versao=\"3.10\">" 
                + "<infCons>"
                + "<xServ>CONS-CAD</xServ>" + "<UF>SP</UF>" 
                + "<CNPJ>71665871000172</CNPJ>"
                + "</infCons>"
                + "</ConsCad>"     
     * @param xml
     * @return
     * @throws DbfException
     */
    
    public String consultar(String xml, String cnpj) throws AxisFault, XMLStreamException, RemoteException {
        WsConnectionConfig.setProperties(cnpj);
        CadConsultaCadastro2Stub stub = new CadConsultaCadastro2Stub();
//        CadConsultaCadastro2Stub.NfeCabecMsgConsultaCadastro2 cadastro = new CadConsultaCadastro2Stub.ConsultaCadastro2();
        OMElement element = AXIOMUtil.stringToOM(xml);
        CadConsultaCadastro2Stub.NfeDadosMsg param = new CadConsultaCadastro2Stub.NfeDadosMsg();
        param.setExtraElement(element);
//        cadastro.setNfeDadosMsg(param);
        CadConsultaCadastro2Stub.NfeCabecMsg0 cabecalho = new CadConsultaCadastro2Stub.NfeCabecMsg0();
        CadConsultaCadastro2Stub.NfeCabecMsg msg = new CadConsultaCadastro2Stub.NfeCabecMsg();
        msg.setCUF("35");
        msg.setVersaoDados("3.10");
        cabecalho.setNfeCabecMsg(msg);
        System.out.println(cabecalho.toString());
//        System.out.println(cadastro.getNfeDadosMsg().getExtraElement().toString());
//        ConsultaCadastro2Result resultado = stub.consultaCadastro2(cadastro, cabecalho);
//        element = resultado.getExtraElement();
        return element.toString();
    }
}
