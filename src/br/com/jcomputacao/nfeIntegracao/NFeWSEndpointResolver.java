/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.jcomputacao.nfeIntegracao;

import br.com.jcomputacao.nfe.NFeUF;
import br.com.jcomputacao.nfe.NFeUtil;
import java.util.EnumMap;
import java.util.Map;

/**
 *
 * @author murilo.lima
 */
public class NFeWSEndpointResolver {

    private Map<NFeUF, Map<ServicoTipoNfe, String>> mapProducao = new EnumMap<NFeUF, Map<ServicoTipoNfe, String>>(NFeUF.class);
    private Map<NFeUF, Map<ServicoTipoNfe, String>> mapHomologacao = new EnumMap<NFeUF, Map<ServicoTipoNfe, String>>(NFeUF.class);

    public NFeWSEndpointResolver() {
        Map<ServicoTipoNfe, String> map = new EnumMap<ServicoTipoNfe, String>(ServicoTipoNfe.class);
        map.put(ServicoTipoNfe.NfeRecepcao, "https://nfe.sefaz.am.gov.br/services2/services/NfeRecepcao2");
        map.put(ServicoTipoNfe.NfeRetRecepcao, "https://nfe.sefaz.am.gov.br/services2/services/NfeRetRecepcao2");
        map.put(ServicoTipoNfe.NfeCancelamento, "https://nfe.sefaz.am.gov.br/services2/services/NfeCancelamento2");
        map.put(ServicoTipoNfe.NfeInutilizacao, "https://nfe.sefaz.am.gov.br/services2/services/NfeInutilizacao2");
        map.put(ServicoTipoNfe.NfeConsultaProtocolo, "https://nfe.sefaz.am.gov.br/services2/services/NfeConsulta2");
        map.put(ServicoTipoNfe.NfeStatusServico, "https://nfe.sefaz.am.gov.br/services2/services/NfeStatusServico2");
        map.put(ServicoTipoNfe.NfeConsultaCadastro, "https://nfe.sefaz.am.gov.br/services2/services/cadconsultacadastro2");
        map.put(ServicoTipoNfe.RecepcaoEvento, "https://nfe.sefaz.am.gov.br/services2/services/RecepcaoEvento");
        map.put(ServicoTipoNfe.NfeAutorizacao, "https://nfe.sefaz.am.gov.br/services2/services/NfeAutorizacao");
        map.put(ServicoTipoNfe.NfeRetAutorizacao, "https://nfe.sefaz.am.gov.br/services2/services/NfeRetAutorizacao");
        mapProducao.put(NFeUF.AMAZONAS, map);

        map = new EnumMap<ServicoTipoNfe, String>(ServicoTipoNfe.class);
        map.put(ServicoTipoNfe.NfeRecepcao, "https://nfe.sefaz.ba.gov.br/webservices/nfenw/NfeRecepcao2.asmx");
        map.put(ServicoTipoNfe.NfeRetRecepcao, "https://nfe.sefaz.ba.gov.br/webservices/nfenw/NfeRetRecepcao2.asmx");
        map.put(ServicoTipoNfe.NfeCancelamento, "https://nfe.sefaz.ba.gov.br/webservices/nfenw/NfeCancelamento2.asmx");
        map.put(ServicoTipoNfe.NfeInutilizacao, "https://nfe.sefaz.ba.gov.br/webservices/NfeInutilizacao/NfeInutilizacao.asmx");
        map.put(ServicoTipoNfe.NfeConsultaProtocolo, "https://nfe.sefaz.ba.gov.br/webservices/NfeConsulta/NfeConsulta.asmx");
        map.put(ServicoTipoNfe.NfeStatusServico, "https://nfe.sefaz.ba.gov.br/webservices/NfeStatusServico/NfeStatusServico.asmx");
        map.put(ServicoTipoNfe.NfeConsultaCadastro, "https://nfe.sefaz.ba.gov.br/webservices/nfenw/CadConsultaCadastro2.asmx");
        map.put(ServicoTipoNfe.RecepcaoEvento, "https://nfe.sefaz.ba.gov.br/webservices/sre/RecepcaoEvento.asmx");
        map.put(ServicoTipoNfe.NfeAutorizacao, "https://nfe.sefaz.ba.gov.br/webservices/NfeAutorizacao/NfeAutorizacao.asmx");
        map.put(ServicoTipoNfe.NfeRetAutorizacao, "https://nfe.sefaz.ba.gov.br/webservices/NfeRetAutorizacao/NfeRetAutorizacao.asmx");
        mapProducao.put(NFeUF.BAHIA, map);

        //Sefaz Ceará - (CE)  Serviço Versão URL");
        map = new EnumMap<ServicoTipoNfe, String>(ServicoTipoNfe.class);
        map.put(ServicoTipoNfe.NfeRecepcao, "https://nfe.sefaz.ce.gov.br/nfe2/services/NfeRecepcao2");
        map.put(ServicoTipoNfe.NfeRetRecepcao, "https://nfe.sefaz.ce.gov.br/nfe2/services/NfeRetRecepcao2");
        map.put(ServicoTipoNfe.NfeCancelamento, "https://nfe.sefaz.ce.gov.br/nfe2/services/NfeCancelamento2");
        map.put(ServicoTipoNfe.NfeInutilizacao, "https://nfe.sefaz.ce.gov.br/nfe2/services/NfeInutilizacao2");
        map.put(ServicoTipoNfe.NfeConsultaProtocolo, "https://nfe.sefaz.ce.gov.br/nfe2/services/NfeConsulta2");
        map.put(ServicoTipoNfe.NfeStatusServico, "https://nfe.sefaz.ce.gov.br/nfe2/services/NfeStatusServico2");
        map.put(ServicoTipoNfe.NfeConsultaCadastro, "https://nfe.sefaz.ce.gov.br/nfe2/services/CadConsultaCadastro2");
        map.put(ServicoTipoNfe.RecepcaoEvento, "https://nfe.sefaz.ce.gov.br/nfe2/services/RecepcaoEvento");
        map.put(ServicoTipoNfe.NfeAutorizacao, "https://nfe.sefaz.ce.gov.br/nfe2/services/NfeAutorizacao");
        map.put(ServicoTipoNfe.NfeRetAutorizacao, "https://nfe.sefaz.ce.gov.br/nfe2/services/NfeRetAutorizacao");
        mapProducao.put(NFeUF.CEARA, map);

        map = new EnumMap<ServicoTipoNfe, String>(ServicoTipoNfe.class);
        //Sefaz Goias - (GO)  Serviço Versão URL");
        map.put(ServicoTipoNfe.NfeRecepcao, "https://nfe.sefaz.go.gov.br/nfe/services/v2/NfeRecepcao2");
        map.put(ServicoTipoNfe.NfeRetRecepcao, "https://nfe.sefaz.go.gov.br/nfe/services/v2/NfeRetRecepcao2");
        map.put(ServicoTipoNfe.NfeCancelamento, "https://nfe.sefaz.go.gov.br/nfe/services/v2/NfeCancelamento2");
        map.put(ServicoTipoNfe.NfeInutilizacao, "https://nfe.sefaz.go.gov.br/nfe/services/v2/NfeInutilizacao2");
        map.put(ServicoTipoNfe.NfeConsultaProtocolo, "https://nfe.sefaz.go.gov.br/nfe/services/v2/NfeConsulta2");
        map.put(ServicoTipoNfe.NfeStatusServico, "https://nfe.sefaz.go.gov.br/nfe/services/v2/NfeStatusServico2");
        map.put(ServicoTipoNfe.NfeConsultaCadastro, "https://nfe.sefaz.go.gov.br/nfe/services/v2/CadConsultaCadastro2");
        map.put(ServicoTipoNfe.RecepcaoEvento, "https://nfe.sefaz.go.gov.br/nfe/services/v2/NfeRecepcaoEvento");
        map.put(ServicoTipoNfe.NfeAutorizacao, "https://nfe.sefaz.go.gov.br/nfe/services/v2/NfeAutorizacao");
        map.put(ServicoTipoNfe.NfeRetAutorizacao, "https://nfe.sefaz.go.gov.br/nfe/services/v2/NfeRetAutorizacao");
        mapProducao.put(NFeUF.GOIAS, map);

        map = new EnumMap<ServicoTipoNfe, String>(ServicoTipoNfe.class);
        //Sef Minas Gerais - (MG)  Serviço Versão URL");
        map.put(ServicoTipoNfe.NfeRecepcao, "https://nfe.fazenda.mg.gov.br/nfe2/services/NfeRecepcao2");
        map.put(ServicoTipoNfe.NfeRetRecepcao, "https://nfe.fazenda.mg.gov.br/nfe2/services/NfeRetRecepcao2");
        map.put(ServicoTipoNfe.NfeCancelamento, "https://nfe.fazenda.mg.gov.br/nfe2/services/NfeCancelamento2");
        map.put(ServicoTipoNfe.NfeInutilizacao, "https://nfe.fazenda.mg.gov.br/nfe2/services/NfeInutilizacao2");
        map.put(ServicoTipoNfe.NfeConsultaProtocolo, "https://nfe.fazenda.mg.gov.br/nfe2/services/NfeConsulta2");
        map.put(ServicoTipoNfe.NfeStatusServico, "https://nfe.fazenda.mg.gov.br/nfe2/services/NfeStatus2");
        map.put(ServicoTipoNfe.NfeConsultaCadastro, "https://nfe.fazenda.mg.gov.br/nfe2/services/cadconsultacadastro2");
        map.put(ServicoTipoNfe.RecepcaoEvento, "https://nfe.fazenda.mg.gov.br/nfe2/services/RecepcaoEvento");
        map.put(ServicoTipoNfe.NfeAutorizacao, "https://nfe.fazenda.mg.gov.br/nfe2/services/NfeAutorizacao");
        map.put(ServicoTipoNfe.NfeRetAutorizacao, "https://nfe.fazenda.mg.gov.br/nfe2/services/NfeRetAutorizacao");
        mapProducao.put(NFeUF.MINAS, map);

        map = new EnumMap<ServicoTipoNfe, String>(ServicoTipoNfe.class);
        //Sefaz Mato Grosso do Sul - (MS)  Serviço Versão URL");
        map.put(ServicoTipoNfe.NfeRecepcao, "https://nfe.fazenda.ms.gov.br/producao/services2/NfeRecepcao2");
        map.put(ServicoTipoNfe.NfeRetRecepcao, "https://nfe.fazenda.ms.gov.br/producao/services2/NfeRetRecepcao2");
        map.put(ServicoTipoNfe.NfeCancelamento, "https://nfe.fazenda.ms.gov.br/producao/services2/NfeCancelamento2");
        map.put(ServicoTipoNfe.NfeInutilizacao, "https://nfe.fazenda.ms.gov.br/producao/services2/NfeInutilizacao2");
        map.put(ServicoTipoNfe.NfeConsultaProtocolo, "https://nfe.fazenda.ms.gov.br/producao/services2/NfeConsulta2");
        map.put(ServicoTipoNfe.NfeStatusServico, "https://nfe.fazenda.ms.gov.br/producao/services2/NfeStatusServico2");
        map.put(ServicoTipoNfe.NfeConsultaCadastro, "https://nfe.fazenda.ms.gov.br/producao/services2/CadConsultaCadastro2");
        map.put(ServicoTipoNfe.RecepcaoEvento, "https://nfe.fazenda.ms.gov.br/producao/services2/NfeRecepcaoEvento");
        map.put(ServicoTipoNfe.NfeAutorizacao, "https://nfe.fazenda.ms.gov.br/producao/services2/NfeAutorizacao");
        map.put(ServicoTipoNfe.NfeRetAutorizacao, "https://nfe.fazenda.ms.gov.br/producao/services2/NfeRetAutorizacao");
        mapProducao.put(NFeUF.MATO_GROSSO_SUL, map);

        map = new EnumMap<ServicoTipoNfe, String>(ServicoTipoNfe.class);
        //Sefaz Mato Grosso - (MT)  Serviço Versão URL
        map.put(ServicoTipoNfe.NfeRecepcao, "https://nfe.sefaz.mt.gov.br/nfews/v2/services/NfeRecepcao2");
        map.put(ServicoTipoNfe.NfeRetRecepcao, "https://nfe.sefaz.mt.gov.br/nfews/v2/services/NfeRetRecepcao2");
        map.put(ServicoTipoNfe.NfeCancelamento, "https://nfe.sefaz.mt.gov.br/nfews/v2/services/NfeCancelamento2");
        map.put(ServicoTipoNfe.NfeInutilizacao, "https://nfe.sefaz.mt.gov.br/nfews/v2/services/NfeInutilizacao2");
        map.put(ServicoTipoNfe.NfeConsultaProtocolo, "https://nfe.sefaz.mt.gov.br/nfews/v2/services/NfeConsulta2");
        map.put(ServicoTipoNfe.NfeStatusServico, "https://nfe.sefaz.mt.gov.br/nfews/v2/services/NfeStatusServico2");
        map.put(ServicoTipoNfe.NfeConsultaCadastro, "https://nfe.sefaz.mt.gov.br/nfews/CadConsultaCadastro");
        map.put(ServicoTipoNfe.RecepcaoEvento, "https://nfe.sefaz.mt.gov.br/nfews/v2/services/RecepcaoEvento");
        map.put(ServicoTipoNfe.NfeAutorizacao, "https://nfe.sefaz.mt.gov.br/nfews/v2/services/NfeAutorizacao");
        map.put(ServicoTipoNfe.NfeRetAutorizacao, "https://nfe.sefaz.mt.gov.br/nfews/v2/services/NfeRetAutorizacao");
        mapProducao.put(NFeUF.MATO_GROSSO, map);

        map = new EnumMap<ServicoTipoNfe, String>(ServicoTipoNfe.class);
        //Sefaz Pernambuco - (PE)  Serviço Versão URL
        map.put(ServicoTipoNfe.NfeRecepcao, "https://nfe.sefaz.pe.gov.br/nfe-service/services/NfeRecepcao2");
        map.put(ServicoTipoNfe.NfeRetRecepcao, "https://nfe.sefaz.pe.gov.br/nfe-service/services/NfeRetRecepcao2");
        map.put(ServicoTipoNfe.NfeCancelamento, "https://nfe.sefaz.pe.gov.br/nfe-service/services/NfeCancelamento2");
        map.put(ServicoTipoNfe.NfeInutilizacao, "https://nfe.sefaz.pe.gov.br/nfe-service/services/NfeInutilizacao2");
        map.put(ServicoTipoNfe.NfeConsultaProtocolo, "https://nfe.sefaz.pe.gov.br/nfe-service/services/NfeConsulta2");
        map.put(ServicoTipoNfe.NfeStatusServico, "https://nfe.sefaz.pe.gov.br/nfe-service/services/NfeStatusServico2");
        map.put(ServicoTipoNfe.NfeConsultaCadastro, "https://nfe.sefaz.pe.gov.br/nfe-service/services/CadConsultaCadastro2");
        map.put(ServicoTipoNfe.RecepcaoEvento, "https://nfe.sefaz.pe.gov.br/nfe-service/services/RecepcaoEvento");
        map.put(ServicoTipoNfe.NfeAutorizacao, "https://nfe.sefaz.pe.gov.br/nfe-service/services/NfeAutorizacao");
        map.put(ServicoTipoNfe.NfeRetAutorizacao, "https://nfe.sefaz.pe.gov.br/nfe-service/services/NfeRetAutorizacao");
        mapProducao.put(NFeUF.PERNAMBUCO, map);

        map = new EnumMap<ServicoTipoNfe, String>(ServicoTipoNfe.class);
        //Sefaz Paraná - (PR)  Serviço Versão URL
        map.put(ServicoTipoNfe.NfeRecepcao, "https://nfe2.fazenda.pr.gov.br/nfe/NFeRecepcao2");
        map.put(ServicoTipoNfe.NfeRetRecepcao, "https://nfe2.fazenda.pr.gov.br/nfe/NFeRetRecepcao2");
        map.put(ServicoTipoNfe.NfeCancelamento, "https://nfe2.fazenda.pr.gov.br/nfe/NFeCancelamento2");
        map.put(ServicoTipoNfe.NfeInutilizacao, "https://nfe.fazenda.pr.gov.br/nfe/NFeInutilizacao3");
        map.put(ServicoTipoNfe.NfeConsultaProtocolo, "https://nfe.fazenda.pr.gov.br/nfe/NFeConsulta3");
        map.put(ServicoTipoNfe.NfeStatusServico, "https://nfe.fazenda.pr.gov.br/nfe/NFeStatusServico3");
        map.put(ServicoTipoNfe.RecepcaoEvento, "https://nfe.fazenda.pr.gov.br/nfe/NFeRecepcaoEvento");
        map.put(ServicoTipoNfe.NfeAutorizacao, "https://nfe.fazenda.pr.gov.br/nfe/NFeAutorizacao3");
        map.put(ServicoTipoNfe.NfeRetAutorizacao, "https://nfe.fazenda.pr.gov.br/nfe/NFeRetAutorizacao3");
        mapProducao.put(NFeUF.PARANA, map);

        map = new EnumMap<ServicoTipoNfe, String>(ServicoTipoNfe.class);
        //Sefaz Rio Grande do Sul - (RS)  Serviço Versão URL
        map.put(ServicoTipoNfe.NfeRecepcao, "https://nfe.sefaz.rs.gov.br/ws/Nferecepcao/NFeRecepcao2.asmx");
        map.put(ServicoTipoNfe.NfeRetRecepcao, "https://nfe.sefaz.rs.gov.br/ws/NfeRetRecepcao/NfeRetRecepcao2.asmx");
        map.put(ServicoTipoNfe.NfeCancelamento, "https://nfe.sefaz.rs.gov.br/ws/NfeCancelamento/NfeCancelamento2.asmx");
        map.put(ServicoTipoNfe.NfeInutilizacao, "https://nfe.sefaz.rs.gov.br/ws/nfeinutilizacao/nfeinutilizacao2.asmx");
        map.put(ServicoTipoNfe.NfeConsultaProtocolo, "https://nfe.sefaz.rs.gov.br/ws/NfeConsulta/NfeConsulta2.asmx");
        map.put(ServicoTipoNfe.NfeStatusServico, "https://nfe.sefaz.rs.gov.br/ws/NfeStatusServico/NfeStatusServico2.asmx");
        map.put(ServicoTipoNfe.NfeConsultaCadastro, "https://sef.sefaz.rs.gov.br/ws/cadconsultacadastro/cadconsultacadastro2.asmx");
        map.put(ServicoTipoNfe.RecepcaoEvento, "https://nfe.sefaz.rs.gov.br/ws/recepcaoevento/recepcaoevento.asmx");
        map.put(ServicoTipoNfe.NfeAutorizacao, "https://nfe.sefaz.rs.gov.br/ws/NfeAutorizacao/NFeAutorizacao.asmx");
        map.put(ServicoTipoNfe.NfeRetAutorizacao, "https://nfe.sefaz.rs.gov.br/ws/NfeRetAutorizacao/NFeRetAutorizacao.asmx");
        mapProducao.put(NFeUF.RIO_SUL, map);

        map = new EnumMap<ServicoTipoNfe, String>(ServicoTipoNfe.class);
        //Sefaz São Paulo - (SP)  Serviço Versão URL
        map.put(ServicoTipoNfe.NfeRecepcao, "https://nfe.fazenda.sp.gov.br/nfeweb/services/nferecepcao2.asmx");
        map.put(ServicoTipoNfe.NfeRetRecepcao, "https://nfe.fazenda.sp.gov.br/nfeweb/services/nferetrecepcao2.asmx");
        map.put(ServicoTipoNfe.NfeCancelamento, "https://nfe.fazenda.sp.gov.br/nfeweb/services/nfecancelamento2.asmx");
        map.put(ServicoTipoNfe.NfeInutilizacao, "https://nfe.fazenda.sp.gov.br/ws/nfeinutilizacao2.asmx");
        map.put(ServicoTipoNfe.NfeConsultaProtocolo, "https://nfe.fazenda.sp.gov.br/ws/nfeconsulta2.asmx");
        map.put(ServicoTipoNfe.NfeStatusServico, "https://nfe.fazenda.sp.gov.br/ws/nfestatusservico2.asmx");
        map.put(ServicoTipoNfe.NfeConsultaCadastro, "https://nfe.fazenda.sp.gov.br/ws/cadconsultacadastro2.asmx");
        map.put(ServicoTipoNfe.RecepcaoEvento, "https://nfe.fazenda.sp.gov.br/ws/recepcaoevento.asmx");
        map.put(ServicoTipoNfe.NfeAutorizacao, "https://nfe.fazenda.sp.gov.br/ws/nfeautorizacao.asmx");
        map.put(ServicoTipoNfe.NfeRetAutorizacao, "https://nfe.fazenda.sp.gov.br/ws/nferetautorizacao.asmx");
        mapProducao.put(NFeUF.SAO_PAULO, map);

        map = new EnumMap<ServicoTipoNfe, String>(ServicoTipoNfe.class);
        //Estados que utilizam a SVAN - Sefaz Virtual do Ambiente Nacional: ES, MA, PA, PI, RN
        //Sefaz Virtual Ambiente Nacional - (SVAN)  Serviço Versão URL
        map.put(ServicoTipoNfe.NfeRecepcao, "https://www.sefazvirtual.fazenda.gov.br/NfeRecepcao2/NfeRecepcao2.asmx");
        map.put(ServicoTipoNfe.NfeRetRecepcao, "https://www.sefazvirtual.fazenda.gov.br/NfeRetRecepcao2/NfeRetRecepcao2.asmx");
        map.put(ServicoTipoNfe.NfeCancelamento, "https://www.sefazvirtual.fazenda.gov.br/NfeCancelamento2/NfeCancelamento2.asmx");
        map.put(ServicoTipoNfe.NfeInutilizacao, "https://www.sefazvirtual.fazenda.gov.br/NfeInutilizacao2/NfeInutilizacao2.asmx");
        map.put(ServicoTipoNfe.NfeConsultaProtocolo, "https://www.sefazvirtual.fazenda.gov.br/NfeConsulta2/NfeConsulta2.asmx");
        map.put(ServicoTipoNfe.NfeStatusServico, "https://www.sefazvirtual.fazenda.gov.br/NfeStatusServico2/NfeStatusServico2.asmx");
        map.put(ServicoTipoNfe.RecepcaoEvento, "https://www.sefazvirtual.fazenda.gov.br/RecepcaoEvento/RecepcaoEvento.asmx");
        map.put(ServicoTipoNfe.NfeAutorizacao, "https://www.sefazvirtual.fazenda.gov.br/NfeAutorizacao/NfeAutorizacao.asmx");
        map.put(ServicoTipoNfe.NfeRetAutorizacao, "https://www.sefazvirtual.fazenda.gov.br/NfeRetAutorizacao/NfeRetAutorizacao.asmx");
        mapProducao.put(NFeUF.ESPIRITO_SANTO, map);
        mapProducao.put(NFeUF.MARANHAO, map);
        mapProducao.put(NFeUF.PARA, map);
        mapProducao.put(NFeUF.PIAUI, map);
        mapProducao.put(NFeUF.RIO_NORTE, map);

        map = new EnumMap<ServicoTipoNfe, String>(ServicoTipoNfe.class);
        //Sefaz Virtual Rio Grande do Sul - (SVRS)  Serviço Versão URL
        //Estados que utilizam a SVRS - Sefaz Virtual do RS: AC, AL, AM, AP, DF, MS, PB, RJ, RO, RR, SC, SE, TO
        map.put(ServicoTipoNfe.NfeRecepcao, "https://nfe.sefazvirtual.rs.gov.br/ws/Nferecepcao/NFeRecepcao2.asmx");
        map.put(ServicoTipoNfe.NfeRetRecepcao, "https://nfe.sefazvirtual.rs.gov.br/ws/NfeRetRecepcao/NfeRetRecepcao2.asmx");
        map.put(ServicoTipoNfe.NfeCancelamento, "https://nfe.sefazvirtual.rs.gov.br/ws/NfeCancelamento/NfeCancelamento2.asmx");
        map.put(ServicoTipoNfe.NfeInutilizacao, "https://nfe.sefazvirtual.rs.gov.br/ws/nfeinutilizacao/nfeinutilizacao2.asmx");
        map.put(ServicoTipoNfe.NfeConsultaProtocolo, "https://nfe.sefazvirtual.rs.gov.br/ws/NfeConsulta/NfeConsulta2.asmx");
        map.put(ServicoTipoNfe.NfeStatusServico, "https://nfe.sefazvirtual.rs.gov.br/ws/NfeStatusServico/NfeStatusServico2.asmx");
        map.put(ServicoTipoNfe.RecepcaoEvento, "https://nfe.sefazvirtual.rs.gov.br/ws/recepcaoevento/recepcaoevento.asmx");
        map.put(ServicoTipoNfe.NfeAutorizacao, "https://nfe.sefazvirtual.rs.gov.br/ws/NfeAutorizacao/NFeAutorizacao.asmx");
        map.put(ServicoTipoNfe.NfeRetAutorizacao, "https://nfe.sefazvirtual.rs.gov.br/ws/NfeRetAutorizacao/NFeRetAutorizacao.asmx");
        mapProducao.put(NFeUF.ACRE, map);
        mapProducao.put(NFeUF.ALAGOAS, map);
        mapProducao.put(NFeUF.AMAPA, map);
        mapProducao.put(NFeUF.PARAIBA, map);
        mapProducao.put(NFeUF.RONDONIA, map);
        mapProducao.put(NFeUF.ROMAIMA, map);
        mapProducao.put(NFeUF.SANTA_CATARINA, map);
        mapProducao.put(NFeUF.SERGIPE, map);
        mapProducao.put(NFeUF.TOCANTINS, map);

        //		//Sefaz Contingência Ambiente Nacional - (SCAN)  Serviço Versão URL 
//		map.put(ServicoTipoNfe.NfeRecepcao,"https://www.scan.fazenda.gov.br/NfeRecepcao2/NfeRecepcao2.asmx"); 
//		map.put(ServicoTipoNfe.NfeRetRecepcao,"https://www.scan.fazenda.gov.br/NfeRetRecepcao2/NfeRetRecepcao2.asmx");
//		map.put(ServicoTipoNfe.NfeCancelamento,"https://www.scan.fazenda.gov.br/NfeCancelamento2/NfeCancelamento2.asmx"); 
//		map.put(ServicoTipoNfe.NfeInutilizacao,"https://www.scan.fazenda.gov.br/NfeInutilizacao2/NfeInutilizacao2.asmx"); 
//		map.put(ServicoTipoNfe.NfeConsultaProtocolo,"https://www.scan.fazenda.gov.br/NfeConsulta2/NfeConsulta2.asmx"); 
//		map.put(ServicoTipoNfe.NfeStatusServico,"https://www.scan.fazenda.gov.br/NfeStatusServico2/NfeStatusServico2.asmx");
        map = new EnumMap<ServicoTipoNfe, String>(ServicoTipoNfe.class);
        map.put(ServicoTipoNfe.NfeRecepcao, "https://homnfe.sefaz.am.gov.br/services2/services/NfeRecepcao2");
        map.put(ServicoTipoNfe.NfeRetRecepcao, "https://homnfe.sefaz.am.gov.br/services2/services/NfeRetRecepcao2");
        map.put(ServicoTipoNfe.NfeCancelamento, "https://homnfe.sefaz.am.gov.br/services2/services/NfeCancelamento2");
        map.put(ServicoTipoNfe.NfeInutilizacao, "https://homnfe.sefaz.am.gov.br/services2/services/NfeInutilizacao2");
        map.put(ServicoTipoNfe.NfeConsultaProtocolo, "https://homnfe.sefaz.am.gov.br/services2/services/NfeConsulta2");
        map.put(ServicoTipoNfe.NfeStatusServico, "https://homnfe.sefaz.am.gov.br/services2/services/NfeStatusServico2");
        map.put(ServicoTipoNfe.NfeConsultaCadastro, "https://homnfe.sefaz.am.gov.br/services2/services/cadconsultacadastro2");
        map.put(ServicoTipoNfe.RecepcaoEvento, "https://homnfe.sefaz.am.gov.br/services2/services/RecepcaoEvento");
        map.put(ServicoTipoNfe.NfeAutorizacao, "https://homnfe.sefaz.am.gov.br/services2/services/NfeAutorizacao");
        map.put(ServicoTipoNfe.NfeRetAutorizacao, "https://homnfe.sefaz.am.gov.br/services2/services/NfeRetAutorizacao");
        mapHomologacao.put(NFeUF.AMAZONAS, map);

        map = new EnumMap<ServicoTipoNfe, String>(ServicoTipoNfe.class);
        map.put(ServicoTipoNfe.NfeRecepcao, "https://hnfe.sefaz.ba.gov.br/webservices/nfenw/NfeRecepcao2.asmx");
        map.put(ServicoTipoNfe.NfeRetRecepcao, "https://hnfe.sefaz.ba.gov.br/webservices/nfenw/NfeRetRecepcao2.asmx");
        map.put(ServicoTipoNfe.NfeCancelamento, "https://hnfe.sefaz.ba.gov.br/webservices/nfenw/NfeCancelamento2.asmx");
        map.put(ServicoTipoNfe.NfeInutilizacao, "https://hnfe.sefaz.ba.gov.br/webservices/NfeInutilizacao/NfeInutilizacao.asmx");
        map.put(ServicoTipoNfe.NfeConsultaProtocolo, "https://hnfe.sefaz.ba.gov.br/webservices/NfeConsulta/NfeConsulta.asmx");
        map.put(ServicoTipoNfe.NfeStatusServico, "https://hnfe.sefaz.ba.gov.br/webservices/NfeStatusServico/NfeStatusServico.asmx");
        map.put(ServicoTipoNfe.NfeConsultaCadastro, "https://hnfe.sefaz.ba.gov.br/webservices/nfenw/CadConsultaCadastro2.asmx");
        map.put(ServicoTipoNfe.RecepcaoEvento, "https://hnfe.sefaz.ba.gov.br/webservices/sre/RecepcaoEvento.asmx");
        map.put(ServicoTipoNfe.NfeAutorizacao, "https://hnfe.sefaz.ba.gov.br/webservices/NfeAutorizacao/NfeAutorizacao.asmx");
        map.put(ServicoTipoNfe.NfeRetAutorizacao, "https://hnfe.sefaz.ba.gov.br/webservices/NfeRetAutorizacao/NfeRetAutorizacao.asmx");
        mapHomologacao.put(NFeUF.BAHIA, map);

        //Sefaz Ceará - (CE)  Serviço Versão URL");
        map = new EnumMap<ServicoTipoNfe, String>(ServicoTipoNfe.class);
        map.put(ServicoTipoNfe.NfeRecepcao, "https://nfeh.sefaz.ce.gov.br/nfe2/services/NfeRecepcao2");
        map.put(ServicoTipoNfe.NfeRetRecepcao, "https://nfeh.sefaz.ce.gov.br/nfe2/services/NfeRetRecepcao2");
        map.put(ServicoTipoNfe.NfeCancelamento, "https://nfeh.sefaz.ce.gov.br/nfe2/services/NfeCancelamento2");
        map.put(ServicoTipoNfe.NfeInutilizacao, "https://nfeh.sefaz.ce.gov.br/nfe2/services/NfeInutilizacao2");
        map.put(ServicoTipoNfe.NfeConsultaProtocolo, "https://nfeh.sefaz.ce.gov.br/nfe2/services/NfeConsulta2");
        map.put(ServicoTipoNfe.NfeStatusServico, "https://nfeh.sefaz.ce.gov.br/nfe2/services/NfeStatusServico2");
        map.put(ServicoTipoNfe.NfeConsultaCadastro, "https://nfeh.sefaz.ce.gov.br/nfe2/services/CadConsultaCadastro2");
        map.put(ServicoTipoNfe.RecepcaoEvento, "https://nfeh.sefaz.ce.gov.br/nfe2/services/RecepcaoEvento");
        map.put(ServicoTipoNfe.NfeAutorizacao, "https://nfeh.sefaz.ce.gov.br/nfe2/services/NfeAutorizacao");
        map.put(ServicoTipoNfe.NfeRetAutorizacao, "https://nfeh.sefaz.ce.gov.br/nfe2/services/NfeRetAutorizacao");
        mapHomologacao.put(NFeUF.CEARA, map);

        map = new EnumMap<ServicoTipoNfe, String>(ServicoTipoNfe.class);
        //Sefaz Goias - (GO)  Serviço Versão URL");
        map.put(ServicoTipoNfe.NfeRecepcao, "https://homolog.sefaz.go.gov.br/nfe/services/v2/NfeRecepcao2");
        map.put(ServicoTipoNfe.NfeRetRecepcao, "https://homolog.sefaz.go.gov.br/nfe/services/v2/NfeRetRecepcao2");
        map.put(ServicoTipoNfe.NfeCancelamento, "https://homolog.sefaz.go.gov.br/nfe/services/v2/NfeCancelamento2");
        map.put(ServicoTipoNfe.NfeInutilizacao, "https://homolog.sefaz.go.gov.br/nfe/services/v2/NfeInutilizacao2");
        map.put(ServicoTipoNfe.NfeConsultaProtocolo, "https://homolog.sefaz.go.gov.br/nfe/services/v2/NfeConsulta2");
        map.put(ServicoTipoNfe.NfeStatusServico, "https://homolog.sefaz.go.gov.br/nfe/services/v2/NfeStatusServico2");
        map.put(ServicoTipoNfe.NfeConsultaCadastro, "https://homolog.sefaz.go.gov.br/nfe/services/v2/CadConsultaCadastro2");
        map.put(ServicoTipoNfe.RecepcaoEvento, "https://homolog.sefaz.go.gov.br/nfe/services/v2/NfeRecepcaoEvento");
        map.put(ServicoTipoNfe.NfeAutorizacao, "https://homolog.sefaz.go.gov.br/nfe/services/v2/NfeAutorizacao");
        map.put(ServicoTipoNfe.NfeRetAutorizacao, "https://homolog.sefaz.go.gov.br/nfe/services/v2/NfeRetAutorizacao");
        mapHomologacao.put(NFeUF.GOIAS, map);

        map = new EnumMap<ServicoTipoNfe, String>(ServicoTipoNfe.class);
        //Sef Minas Gerais - (MG)  Serviço Versão URL");
        map.put(ServicoTipoNfe.NfeRecepcao, "https://homologacao.fazenda.mg.gov.br/nfe2/services/NfeRecepcao2");
        map.put(ServicoTipoNfe.NfeRetRecepcao, "https://homologacao.fazenda.mg.gov.br/nfe2/services/NfeRetRecepcao2");
        map.put(ServicoTipoNfe.NfeCancelamento, "https://homologacao.fazenda.mg.gov.br/nfe2/services/NfeCancelamento2");
        map.put(ServicoTipoNfe.NfeInutilizacao, "https://homologacao.fazenda.mg.gov.br/nfe2/services/NfeInutilizacao2");
        map.put(ServicoTipoNfe.NfeConsultaProtocolo, "https://homologacao.fazenda.mg.gov.br/nfe2/services/NfeConsulta2");
        map.put(ServicoTipoNfe.NfeStatusServico, "https://homologacao.fazenda.mg.gov.br/nfe2/services/NfeStatus2");
        map.put(ServicoTipoNfe.NfeConsultaCadastro, "https://homologacao.fazenda.mg.gov.br/nfe2/services/cadconsultacadastro2");
        map.put(ServicoTipoNfe.RecepcaoEvento, "https://homologacao.fazenda.mg.gov.br/nfe2/services/RecepcaoEvento");
        map.put(ServicoTipoNfe.NfeAutorizacao, "https://homologacao.fazenda.mg.gov.br/nfe2/services/NfeAutorizacao");
        map.put(ServicoTipoNfe.NfeRetAutorizacao, "https://homologacao.fazenda.mg.gov.br/nfe2/services/NfeRetAutorizacao");
        mapHomologacao.put(NFeUF.MINAS, map);

        map = new EnumMap<ServicoTipoNfe, String>(ServicoTipoNfe.class);
        //Sefaz Mato Grosso do Sul - (MS)  Serviço Versão URL");
        map.put(ServicoTipoNfe.NfeRecepcao, "https://homologacao.fazenda.ms.gov.br/producao/services2/NfeRecepcao2");
        map.put(ServicoTipoNfe.NfeRetRecepcao, "https://homologacao.fazenda.ms.gov.br/producao/services2/NfeRetRecepcao2");
        map.put(ServicoTipoNfe.NfeCancelamento, "https://homologacao.fazenda.ms.gov.br/producao/services2/NfeCancelamento2");
        map.put(ServicoTipoNfe.NfeInutilizacao, "https://homologacao.fazenda.ms.gov.br/producao/services2/NfeInutilizacao2");
        map.put(ServicoTipoNfe.NfeConsultaProtocolo, "https://homologacao.fazenda.ms.gov.br/producao/services2/NfeConsulta2");
        map.put(ServicoTipoNfe.NfeStatusServico, "https://homologacao.fazenda.ms.gov.br/producao/services2/NfeStatusServico2");
        map.put(ServicoTipoNfe.NfeConsultaCadastro, "https://homologacao.fazenda.ms.gov.br/producao/services2/CadConsultaCadastro2");
        map.put(ServicoTipoNfe.RecepcaoEvento, "https://homologacao.fazenda.ms.gov.br/producao/services2/NfeRecepcaoEvento");
        map.put(ServicoTipoNfe.NfeAutorizacao, "https://homologacao.fazenda.ms.gov.br/producao/services2/NfeAutorizacao");
        map.put(ServicoTipoNfe.NfeRetAutorizacao, "https://homologacao.fazenda.ms.gov.br/producao/services2/NfeRetAutorizacao");
        mapHomologacao.put(NFeUF.MATO_GROSSO_SUL, map);

        map = new EnumMap<ServicoTipoNfe, String>(ServicoTipoNfe.class);
        //Sefaz Mato Grosso - (MT)  Serviço Versão URL
        map.put(ServicoTipoNfe.NfeRecepcao, "https://hnfe.sefaz.mt.gov.br/nfews/v2/services/NfeRecepcao2");
        map.put(ServicoTipoNfe.NfeRetRecepcao, "https://hnfe.sefaz.mt.gov.br/nfews/v2/services/NfeRetRecepcao2");
        map.put(ServicoTipoNfe.NfeCancelamento, "https://hnfe.sefaz.mt.gov.br/nfews/v2/services/NfeCancelamento2");
        map.put(ServicoTipoNfe.NfeInutilizacao, "https://hnfe.sefaz.mt.gov.br/nfews/v2/services/NfeInutilizacao2");
        map.put(ServicoTipoNfe.NfeConsultaProtocolo, "https://hnfe.sefaz.mt.gov.br/nfews/v2/services/NfeConsulta2");
        map.put(ServicoTipoNfe.NfeStatusServico, "https://hnfe.sefaz.mt.gov.br/nfews/v2/services/NfeStatusServico2");
        map.put(ServicoTipoNfe.NfeConsultaCadastro, "https://hnfe.sefaz.mt.gov.br/nfews/CadConsultaCadastro");
        map.put(ServicoTipoNfe.RecepcaoEvento, "https://hnfe.sefaz.mt.gov.br/nfews/v2/services/RecepcaoEvento");
        map.put(ServicoTipoNfe.NfeAutorizacao, "https://hnfe.sefaz.mt.gov.br/nfews/v2/services/NfeAutorizacao");
        map.put(ServicoTipoNfe.NfeRetAutorizacao, "https://hnfe.sefaz.mt.gov.br/nfews/v2/services/NfeRetAutorizacao");
        mapHomologacao.put(NFeUF.MATO_GROSSO, map);

        map = new EnumMap<ServicoTipoNfe, String>(ServicoTipoNfe.class);
        //Sefaz Pernambuco - (PE)  Serviço Versão URL
        map.put(ServicoTipoNfe.NfeRecepcao, "https://homologacao.sefaz.pe.gov.br/nfe-service/services/NfeRecepcao2");
        map.put(ServicoTipoNfe.NfeRetRecepcao, "https://homologacao.sefaz.pe.gov.br/nfe-service/services/NfeRetRecepcao2");
        map.put(ServicoTipoNfe.NfeCancelamento, "https://homologacao.sefaz.pe.gov.br/nfe-service/services/NfeCancelamento2");
        map.put(ServicoTipoNfe.NfeInutilizacao, "https://homologacao.sefaz.pe.gov.br/nfe-service/services/NfeInutilizacao2");
        map.put(ServicoTipoNfe.NfeConsultaProtocolo, "https://homologacao.sefaz.pe.gov.br/nfe-service/services/NfeConsulta2");
        map.put(ServicoTipoNfe.NfeStatusServico, "https://homologacao.sefaz.pe.gov.br/nfe-service/services/NfeStatusServico2");
        map.put(ServicoTipoNfe.NfeConsultaCadastro, "https://homologacao.sefaz.pe.gov.br/nfe-service/services/CadConsultaCadastro2");
        map.put(ServicoTipoNfe.RecepcaoEvento, "https://homologacao.sefaz.pe.gov.br/nfe-service/services/RecepcaoEvento");
        map.put(ServicoTipoNfe.NfeAutorizacao, "https://homologacao.sefaz.pe.gov.br/nfe-service/services/NfeAutorizacao");
        map.put(ServicoTipoNfe.NfeRetAutorizacao, "https://homologacao.sefaz.pe.gov.br/nfe-service/services/NfeRetAutorizacao");
        mapHomologacao.put(NFeUF.PERNAMBUCO, map);

        map = new EnumMap<ServicoTipoNfe, String>(ServicoTipoNfe.class);
        //Sefaz Paraná - (PR)  Serviço Versão URL
        map.put(ServicoTipoNfe.NfeRecepcao, "https://nfehomolog.fazenda.pr.gov.br/nfe/NFeRecepcao2");
        map.put(ServicoTipoNfe.NfeRetRecepcao, "https://nfehomolog.fazenda.pr.gov.br/nfe/NFeRetRecepcao2");
        map.put(ServicoTipoNfe.NfeCancelamento, "https://nfehomolog.fazenda.pr.gov.br/nfe/NFeCancelamento2");
        map.put(ServicoTipoNfe.NfeInutilizacao, "https://nfehomolog.fazenda.pr.gov.br/nfe/NFeInutilizacao3");
        map.put(ServicoTipoNfe.NfeConsultaProtocolo, "https://nfehomolog.fazenda.pr.gov.br/nfe/NFeConsulta3");
        map.put(ServicoTipoNfe.NfeStatusServico, "https://nfehomolog.fazenda.pr.gov.br/nfe/NFeStatusServico3");
        map.put(ServicoTipoNfe.RecepcaoEvento, "https://nfehomolog.fazenda.pr.gov.br/nfe/NFeRecepcaoEvento");
        map.put(ServicoTipoNfe.NfeAutorizacao, "https://nfehomolog.fazenda.pr.gov.br/nfe/NFeAutorizacao3");
        map.put(ServicoTipoNfe.NfeRetAutorizacao, "https://nfehomolog.fazenda.pr.gov.br/nfe/NFeRetAutorizacao3");
        mapHomologacao.put(NFeUF.PARANA, map);

        map = new EnumMap<ServicoTipoNfe, String>(ServicoTipoNfe.class);
        //Sefaz Rio Grande do Sul - (RS)  Serviço Versão URL
        map.put(ServicoTipoNfe.NfeRecepcao, "https://homologacao.sefaz.rs.gov.br/ws/Nferecepcao/NFeRecepcao2.asmx");
        map.put(ServicoTipoNfe.NfeRetRecepcao, "https://homologacao.sefaz.rs.gov.br/ws/NfeRetRecepcao/NfeRetRecepcao2.asmx");
        map.put(ServicoTipoNfe.NfeCancelamento, "https://homologacao.sefaz.rs.gov.br/ws/NfeCancelamento/NfeCancelamento2.asmx");
        map.put(ServicoTipoNfe.NfeInutilizacao, "https://homologacao.sefaz.rs.gov.br/ws/nfeinutilizacao/nfeinutilizacao2.asmx");
        map.put(ServicoTipoNfe.NfeConsultaProtocolo, "https://homologacao.sefaz.rs.gov.br/ws/NfeConsulta/NfeConsulta2.asmx");
        map.put(ServicoTipoNfe.NfeStatusServico, "https://homologacao.sefaz.rs.gov.br/ws/NfeStatusServico/NfeStatusServico2.asmx");
        map.put(ServicoTipoNfe.NfeConsultaCadastro, "https://homologacao.sef.sefaz.rs.gov.br/ws/cadconsultacadastro/cadconsultacadastro2.asmx");
        map.put(ServicoTipoNfe.RecepcaoEvento, "https://homologacao.sefaz.rs.gov.br/ws/recepcaoevento/recepcaoevento.asmx");
        map.put(ServicoTipoNfe.NfeAutorizacao, "https://homologacao.sefaz.rs.gov.br/ws/NfeAutorizacao/NFeAutorizacao.asmx");
        map.put(ServicoTipoNfe.NfeRetAutorizacao, "https://homologacao.sefaz.rs.gov.br/ws/NfeRetAutorizacao/NFeRetAutorizacao.asmx");
        mapHomologacao.put(NFeUF.RIO_SUL, map);

        map = new EnumMap<ServicoTipoNfe, String>(ServicoTipoNfe.class);
        //Sefaz São Paulo - (SP)  Serviço Versão URL
        map.put(ServicoTipoNfe.NfeRecepcao, "https://homologacao.nfe.fazenda.sp.gov.br/nfeweb/services/nferecepcao2.asmx");
        map.put(ServicoTipoNfe.NfeRetRecepcao, "https://homologacao.nfe.fazenda.sp.gov.br/nfeweb/services/nferetrecepcao2.asmx");
        map.put(ServicoTipoNfe.NfeCancelamento, "https://homologacao.nfe.fazenda.sp.gov.br/nfeweb/services/nfecancelamento2.asmx");
        map.put(ServicoTipoNfe.NfeInutilizacao, "https://homologacao.nfe.fazenda.sp.gov.br/ws/nfeinutilizacao2.asmx");
        map.put(ServicoTipoNfe.NfeConsultaProtocolo, "https://homologacao.nfe.fazenda.sp.gov.br/ws/nfeconsulta2.asmx");
        map.put(ServicoTipoNfe.NfeStatusServico, "https://homologacao.nfe.fazenda.sp.gov.br/ws/nfestatusservico2.asmx");
        map.put(ServicoTipoNfe.NfeConsultaCadastro, "https://homologacao.nfe.fazenda.sp.gov.br/ws/cadconsultacadastro2.asmx");
        map.put(ServicoTipoNfe.RecepcaoEvento, "https://homologacao.nfe.fazenda.sp.gov.br/ws/recepcaoevento.asmx");
        map.put(ServicoTipoNfe.NfeAutorizacao, "https://homologacao.nfe.fazenda.sp.gov.br/ws/nfeautorizacao.asmx");
        map.put(ServicoTipoNfe.NfeRetAutorizacao, "https://homologacao.nfe.fazenda.sp.gov.br/ws/nferetautorizacao.asmx");
        mapHomologacao.put(NFeUF.SAO_PAULO, map);

        map = new EnumMap<ServicoTipoNfe, String>(ServicoTipoNfe.class);
        //Estados que utilizam a SVAN - Sefaz Virtual do Ambiente Nacional: ES, MA, PA, PI, RN
        //Sefaz Virtual Ambiente Nacional - (SVAN)  Serviço Versão URL
        map.put(ServicoTipoNfe.NfeRecepcao, "https://www.hom.sefazvirtual.fazenda.gov.br/NfeRecepcao2/NfeRecepcao2.asmx");
        map.put(ServicoTipoNfe.NfeRetRecepcao, "https://www.hom.sefazvirtual.fazenda.gov.br/NfeRetRecepcao2/NfeRetRecepcao2.asmx");
        map.put(ServicoTipoNfe.NfeCancelamento, "https://www.hom.sefazvirtual.fazenda.gov.br/NfeCancelamento2/NfeCancelamento2.asmx");
        map.put(ServicoTipoNfe.NfeInutilizacao, "https://www.hom.sefazvirtual.fazenda.gov.br/NfeInutilizacao2/NfeInutilizacao2.asmx");
        map.put(ServicoTipoNfe.NfeConsultaProtocolo, "https://www.hom.sefazvirtual.fazenda.gov.br/NfeConsulta2/NfeConsulta2.asmx");
        map.put(ServicoTipoNfe.NfeStatusServico, "https://www.hom.sefazvirtual.fazenda.gov.br/NfeStatusServico2/NfeStatusServico2.asmx");
        map.put(ServicoTipoNfe.RecepcaoEvento, "https://www.hom.sefazvirtual.fazenda.gov.br/RecepcaoEvento/RecepcaoEvento.asmx");
        map.put(ServicoTipoNfe.NfeAutorizacao, "https://www.hom.sefazvirtual.fazenda.gov.br/NfeAutorizacao/NfeAutorizacao.asmx");
        map.put(ServicoTipoNfe.NfeRetAutorizacao, "https://www.hom.sefazvirtual.fazenda.gov.br/NfeRetAutorizacao/NfeRetAutorizacao.asmx");
        mapHomologacao.put(NFeUF.ESPIRITO_SANTO, map);
        mapHomologacao.put(NFeUF.MARANHAO, map);
        mapHomologacao.put(NFeUF.PARA, map);
        mapHomologacao.put(NFeUF.PIAUI, map);
        mapHomologacao.put(NFeUF.RIO_NORTE, map);

        map = new EnumMap<ServicoTipoNfe, String>(ServicoTipoNfe.class);
        //Sefaz Virtual Rio Grande do Sul - (SVRS)  Serviço Versão URL
        //Estados que utilizam a SVRS - Sefaz Virtual do RS: AC, AL, AM, AP, DF, MS, PB, RJ, RO, RR, SC, SE, TO
        map.put(ServicoTipoNfe.NfeRecepcao, "https://homologacao.nfe.sefazvirtual.rs.gov.br/ws/Nferecepcao/NFeRecepcao2.asmx");
        map.put(ServicoTipoNfe.NfeRetRecepcao, "https://homologacao.nfe.sefazvirtual.rs.gov.br/ws/NfeRetRecepcao/NfeRetRecepcao2.asmx");
        map.put(ServicoTipoNfe.NfeCancelamento, "https://homologacao.nfe.sefazvirtual.rs.gov.br/ws/NfeCancelamento/NfeCancelamento2.asmx");
        map.put(ServicoTipoNfe.NfeInutilizacao, "https://homologacao.nfe.sefazvirtual.rs.gov.br/ws/nfeinutilizacao/nfeinutilizacao2.asmx");
        map.put(ServicoTipoNfe.NfeConsultaProtocolo, "https://homologacao.nfe.sefazvirtual.rs.gov.br/ws/NfeConsulta/NfeConsulta2.asmx");
        map.put(ServicoTipoNfe.NfeStatusServico, "https://homologacao.nfe.sefazvirtual.rs.gov.br/ws/NfeStatusServico/NfeStatusServico2.asmx");
        map.put(ServicoTipoNfe.RecepcaoEvento, "https://homologacao.nfe.sefazvirtual.rs.gov.br/ws/recepcaoevento/recepcaoevento.asmx");
        map.put(ServicoTipoNfe.NfeAutorizacao, "https://homologacao.nfe.sefazvirtual.rs.gov.br/ws/NfeAutorizacao/NFeAutorizacao.asmx");
        map.put(ServicoTipoNfe.NfeRetAutorizacao, "https://homologacao.nfe.sefazvirtual.rs.gov.br/ws/NfeRetAutorizacao/NFeRetAutorizacao.asmx");
        mapHomologacao.put(NFeUF.ACRE, map);
        mapHomologacao.put(NFeUF.ALAGOAS, map);
        mapHomologacao.put(NFeUF.AMAPA, map);
        mapHomologacao.put(NFeUF.PARAIBA, map);
        mapHomologacao.put(NFeUF.RONDONIA, map);
        mapHomologacao.put(NFeUF.ROMAIMA, map);
        mapHomologacao.put(NFeUF.SANTA_CATARINA, map);
        mapHomologacao.put(NFeUF.SERGIPE, map);
        mapHomologacao.put(NFeUF.TOCANTINS, map);

    }

    public String getEndpoing(NFeUF uf, ServicoTipoNfe tipo) {
        Map<NFeUF, Map<ServicoTipoNfe, String>> map;
        if (NFeUtil.getAmbiente() == 1) {
            map = mapProducao;
        } else {
            map = mapHomologacao;
        }
        Map<ServicoTipoNfe, String> servicoMap = map.get(uf);
        if (servicoMap == null) {
            return null;
        }
        return servicoMap.get(tipo);
    }
}
