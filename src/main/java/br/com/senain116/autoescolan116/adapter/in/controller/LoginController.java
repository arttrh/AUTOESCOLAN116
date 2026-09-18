package br.com.senain116.autoescolan116.adapter.in.controller;

import br.com.senain116.autoescolan116.adapter.in.controller.request.usuario.DadosLogin;
import br.com.senain116.autoescolan116.application.core.usecase.LoginService;
import br.com.senain116.autoescolan116.application.port.in.LoginPort;
import br.com.senain116.autoescolan116.config.security.dto.DadosTokenJWT;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginController implements LoginPort {
    private final LoginService service;

    public LoginController(LoginService service) {
        this.service = service;
    }

    @Override
    @PostMapping
    public ResponseEntity<DadosTokenJWT> logar(
            @RequestBody @Valid DadosLogin dados) {
        return ResponseEntity.ok(service.logar(dados));
    }
}