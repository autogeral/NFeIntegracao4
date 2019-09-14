/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.jcomputacao.nfeEmissor;

import br.com.jcomputacao.nfe.ws.WsConnectionConfig;
import br.com.jcomputacao.nfe4.ws.consultaCadastro.CadConsultaCadastro4Stub;
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
        CadConsultaCadastro4Stub stub = new CadConsultaCadastro4Stub();
//        CadConsultaCadastro2Stub.NfeCabecMsgConsultaCadastro2 cadastro = new CadConsultaCadastro2Stub.ConsultaCadastro2();
        OMElement element = AXIOMUtil.stringToOM(xml);
        CadConsultaCadastro4Stub.NfeDadosMsg param = new CadConsultaCadastro4Stub.NfeDadosMsg();
        param.setExtraElement(element);
//        cadastro.setNfeDadosMsg(param);                
//        System.out.println(cadastro.getNfeDadosMsg().getExtraElement().toString());
//        ConsultaCadastro2Result resultado = stub.consultaCadastro2(cadastro, cabecalho);
//        element = resultado.getExtraElement();
        CadConsultaCadastro4Stub.NfeResultMsg resultado = stub.consultaCadastro(param);
        String s = resultado.getExtraElement().toString();
        Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.FINER, s);
        return s;
    }    
}
