/*
 * Copyright (C) 2010  J. Computacao LTDA
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * If it is your itention to get support for a commercial version of this
 * application get in touch with J. Computacao LTDA at http://www.jcomputacao.com.br
 *
 * Este programa ï¿½ software livre: voce pode redistribui-lo e/ou modifica-lo
 * sob os termos da Licenca Pulica Generica (GNU GPL) como publicado pela
 * Free Software Foundation, na versao 3 da licenca ou alguma versao superior.
 *
 * This programa e distribuido na esperanca que seja util,
 * mas SEM QUALQUER GARANTIA; sem sequer a garantia implicita
 * de COMERCIALIZACAO or APTIDAO PARA UMA FINALIDADE PARTICULAR.
 * Leia a GNU GPL para mais detalhes.
 *
 * Voce deveria receber uma copia da GNU GPL junto com este programa
 * se nao o recebeu leia em <http://www.gnu.org/licenses/>.
 *
 * Se for sua intencao obter suporte para uma versao comercial desta
 * aplicacao entre em contato com J. Computacao LTDA em http://www.jcomputacao.com.br
 *
 */
package br.com.jcomputacao.nfeIntegracao;

import br.com.jcomputacao.exception.DbfDatabaseException;
import br.com.jcomputacao.model.beans.LojaBean;
import br.com.jcomputacao.nfe.NFeUtil;
import br.com.jcomputacao.nfe.ws.WsConnectionConfig;
import br.com.jcomputacao.nfe4.ws.statusServico.NFeStatusServico4Stub;
import br.com.jcomputacao.nfeEmissor.Servico;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.stream.XMLStreamException;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.impl.llom.util.AXIOMUtil;

/**
 * 07/03/2016 12:23:35
 *
 * @author murilo.lima
 */
public class ServicoConsultaStatusServicoSefaz extends Servico {

    public String consultar() throws XMLStreamException, RemoteException, DbfDatabaseException {
        String cnpj = obtemCnpjEmitente(LojaBean.getLojaAtual());
        WsConnectionConfig.setProperties(cnpj);

        NFeStatusServico4Stub stub = new NFeStatusServico4Stub();
        NFeStatusServico4Stub.NfeDadosMsg dados = new NFeStatusServico4Stub.NfeDadosMsg();

        String s = "<consStatServ xmlns=\"http://www.portalfiscal.inf.br/nfe\" versao=\"4.00\"><tpAmb>"
                + NFeUtil.getAmbiente() + "</tpAmb><cUF>35</cUF><xServ>STATUS</xServ></consStatServ>";
        OMElement el = AXIOMUtil.stringToOM(s);
        dados.setExtraElement(el);
        
        NFeStatusServico4Stub.NfeResultMsg retorno = stub.nfeStatusServicoNF(dados);
        s = retorno.getExtraElement().toString().replace("><", ">\n<");
        Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.FINER, s);
        return s;
    }
}
