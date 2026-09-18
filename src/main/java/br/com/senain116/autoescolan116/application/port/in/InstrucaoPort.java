package br.com.senain116.autoescolan116.application.port.in;

import br.com.senain116.autoescolan116.adapter.in.controller.request.instrucao.DadosAgendamento;
import br.com.senain116.autoescolan116.adapter.in.controller.response.instrucao.DadosDetalhamentoAgendamento;
import org.springframework.http.ResponseEntity;

public interface InstrucaoPort {
    ResponseEntity<DadosDetalhamentoAgendamento> agendar(DadosAgendamento dados);
}