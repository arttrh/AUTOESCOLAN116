package br.com.senain116.autoescolan116.adapter.in.controller.response.usuario;

import br.com.senain116.autoescolan116.application.core.domain.enums.Perfil;

public record DadosListagemUsuario(
        Long id,
        String login,
        Perfil perfil) {
}