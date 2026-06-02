package br.com.senain116.autoescolan116.adapter.in.controller.request.usuario;

import br.com.senain116.autoescolan116.application.core.domain.enums.Perfil;
import jakarta.validation.constraints.NotNull;

public record DadosAtualizacaoUsuario(
        @NotNull
        Long id,
        String login,
        Perfil perfil) {
}