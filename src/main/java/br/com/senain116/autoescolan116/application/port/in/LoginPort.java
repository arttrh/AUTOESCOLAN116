package br.com.senain116.autoescolan116.application.port.in;

import br.com.senain116.autoescolan116.adapter.in.controller.request.usuario.DadosLogin;
import br.com.senain116.autoescolan116.config.security.dto.DadosTokenJWT;
import org.springframework.http.ResponseEntity;

public interface LoginPort {
    ResponseEntity<DadosTokenJWT> logar(DadosLogin dados);
}