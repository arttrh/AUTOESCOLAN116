package br.com.senain116.autoescolan116.adapter.in.controller;

import br.com.senain116.autoescolan116.adapter.in.controller.request.instrucao.DadosAgendamento;
import br.com.senain116.autoescolan116.adapter.in.controller.response.instrucao.DadosDetalhamentoAgendamento;
import br.com.senain116.autoescolan116.application.core.usecase.AgendaDeInstrucoes;
import br.com.senain116.autoescolan116.application.port.in.InstrucaoPort;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/instrucoes")
@SecurityRequirement(name = "bearer-key")
public class InstrucaoController implements InstrucaoPort {
    private final AgendaDeInstrucoes agenda;

    public InstrucaoController(AgendaDeInstrucoes agenda) {
        this.agenda = agenda;
    }

    @Override
    @PostMapping
    public ResponseEntity<DadosDetalhamentoAgendamento> agendar(
            @RequestBody @Valid DadosAgendamento dados) {
        return ResponseEntity.ok(agenda.agendarInstrucao(dados));
    }
}