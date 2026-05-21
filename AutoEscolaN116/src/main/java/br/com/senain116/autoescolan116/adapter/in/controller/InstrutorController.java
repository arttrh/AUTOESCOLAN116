package br.com.senain116.autoescolan116.adapter.in.controller;

import br.com.senain116.autoescolan116.adapter.in.controller.request.instrutor.DadosAtualizacaoInstrutor;
import br.com.senain116.autoescolan116.adapter.in.controller.request.instrutor.DadosCadastroInstrutor;
import br.com.senain116.autoescolan116.adapter.in.controller.response.instrutor.DadosDetalhamentoInstrutor;
import br.com.senain116.autoescolan116.adapter.in.controller.response.instrutor.DadosListagemInstrutor;
import br.com.senain116.autoescolan116.application.core.domain.model.Instrutor;
import br.com.senain116.autoescolan116.application.core.usecase.InstrutorService;
import br.com.senain116.autoescolan116.application.port.in.ModelDomainController;
import br.com.senain116.autoescolan116.application.port.out.InstrutorRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/instrutores")
@SecurityRequirement(name = "bearer-key")
public class InstrutorController implements ModelDomainController<
        DadosCadastroInstrutor,
        DadosListagemInstrutor,
        DadosAtualizacaoInstrutor,
        Void,
        DadosDetalhamentoInstrutor,
        Long> {
        private InstrutorRepository repository;
        private InstrutorService service;

        public InstrutorController(InstrutorRepository repository, InstrutorService service) {
            this.repository = repository;
            this.service = service;
        }

    @Override
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<DadosDetalhamentoInstrutor> cadastrar(
            @RequestBody DadosCadastroInstrutor dados, UriComponentsBuilder uriBuilder) {
        DadosDetalhamentoInstrutor dto = service.cadastrar(dados);
        URI uri = uriBuilder
                .path("/instrutores/{id}")
                .buildAndExpand(dto.id())
                .toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Page<DadosListagemInstrutor>> listar(
            @ParameterObject @PageableDefault(size = 10, sort = "nome") Pageable paginacao) {
        return ResponseEntity.ok(service.listar(paginacao));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<DadosDetalhamentoInstrutor> detalhar(
            @PathVariable Long id) {
        Instrutor instrutor = repository.getReferenceById(id);
        return ResponseEntity.ok(service.detalhar(id));
    }

    @PutMapping
    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
        public ResponseEntity<DadosDetalhamentoInstrutor> atualizar(
            @RequestBody @Valid DadosAtualizacaoInstrutor dados) {
        return ResponseEntity.ok(service.atualizar(dados));
    }

    @DeleteMapping("/{id}")
    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
