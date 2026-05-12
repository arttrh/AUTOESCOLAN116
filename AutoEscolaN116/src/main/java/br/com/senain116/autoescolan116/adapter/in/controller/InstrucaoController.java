package br.com.senain116.autoescolan116.adapter.in.controller;

import br.com.senain116.autoescolan116.adapter.in.controller.request.instrucao.DadosAgendamento;
import br.com.senain116.autoescolan116.adapter.in.controller.response.instrucao.DadosDetalhamentoAgendamento;
import br.com.senain116.autoescolan116.adapter.in.controller.response.instrucao.DadosListagemInstrucao;
import br.com.senain116.autoescolan116.application.core.usecase.AgendaDeInstrucoes;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/instrucoes")
public class InstrucaoController {

    private AgendaDeInstrucoes agenda;

    public InstrucaoController(AgendaDeInstrucoes agenda){
        this.agenda = agenda;
    }

    @PostMapping
    public ResponseEntity<DadosDetalhamentoAgendamento> cadastrarInstrucao(
            @RequestBody @Valid DadosAgendamento dados) {
        return ResponseEntity.ok(agenda.agendar(dados));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public Page<DadosListagemInstrucao> listarInstrucao(@ParameterObject @PageableDefault(size = 10, sort = "name") Pageable paginacao) {
        return ResponseEntity.ok(agenda.listar(paginacao)).getBody();
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/{id}")
    public ResponseEntity<DadosDetalhamentoAgendamento> detalhar(Long id) {
        return ResponseEntity.ok(agenda.detalhar(id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN', 'USER')")
   public ResponseEntity<Void> excluirInstrucao(@PathVariable Long id){
       agenda.excluirAgendamento(id);
       return ResponseEntity.noContent().build();
   }
}