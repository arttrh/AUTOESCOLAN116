package br.com.senain116.autoescolan116.adapter.in.controller;

import br.com.senain116.autoescolan116.adapter.in.controller.request.usuario.DadosAtualizacaoSenha;
import br.com.senain116.autoescolan116.adapter.in.controller.request.usuario.DadosAtualizacaoUsuario;
import br.com.senain116.autoescolan116.adapter.in.controller.request.usuario.DadosCadastroUsuario;
import br.com.senain116.autoescolan116.adapter.in.controller.response.usuario.DadosDetalhamentoUsuario;
import br.com.senain116.autoescolan116.adapter.in.controller.response.usuario.DadosListagemUsuario;
import br.com.senain116.autoescolan116.adapter.in.controller.response.usuario.DadosSucesso;
import br.com.senain116.autoescolan116.application.core.domain.model.Usuario;
import br.com.senain116.autoescolan116.application.core.usecase.UsuarioService;
import br.com.senain116.autoescolan116.application.port.in.ModelDomainController;
import br.com.senain116.autoescolan116.application.port.out.UsuarioRepository;
import br.com.senain116.autoescolan116.exception.type.usuario.SenhaIncorretaException;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/usuarios")
@SecurityRequirement(name = "bearer-key")
public class UsuarioController implements ModelDomainController<
        DadosCadastroUsuario,
        DadosListagemUsuario,
        DadosAtualizacaoUsuario,
        Void,
        DadosDetalhamentoUsuario,
        Long> {
    private UsuarioRepository repository;
    private PasswordEncoder encoder;
    private UsuarioService service;

    public UsuarioController(UsuarioRepository repository, PasswordEncoder encoder, UsuarioService service) {
        this.repository = repository;
        this.encoder = encoder;
        this.service = service;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DadosDetalhamentoUsuario> cadastrar(
            @RequestBody @Valid DadosCadastroUsuario dados,
            UriComponentsBuilder uriBuilder) {
        String senhaCriptografada = encoder.encode(dados.senha());
        DadosDetalhamentoUsuario dto = service.cadastrar(dados);
        URI uri = uriBuilder
                .path("/usuarios/{id}")
                .buildAndExpand(dto.id())
                .toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<DadosListagemUsuario>> listar(
            @ParameterObject @PageableDefault(size = 10, sort = "nome") Pageable paginacao) {
        return ResponseEntity.ok(service.listar(paginacao));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DadosDetalhamentoUsuario> detalhar(
            @PathVariable Long id) {
        return ResponseEntity.ok(service.detalhar(id));
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DadosDetalhamentoUsuario> atualizar(
            @RequestBody @Valid DadosAtualizacaoUsuario dados) {
        return ResponseEntity.ok(service.atualizarUsuario(dados));
    }

    @PatchMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<DadosSucesso> atualizar(@RequestBody @Valid DadosAtualizacaoSenha dados) {
        Usuario usuario = repository.getReferenceById(dados.id());
        if (!encoder.matches(dados.senhaAntiga(), usuario.getSenha())) {
            throw new SenhaIncorretaException("Senha incorreta!");
        }
        String senhaNovaCriptografada = encoder.encode(dados.senhaNova());
        usuario.atualizarSenha(senhaNovaCriptografada);
        repository.save(usuario);
        return ResponseEntity.ok(new DadosSucesso("Senha atualizada com sucesso!"));
    }

    @Override
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}