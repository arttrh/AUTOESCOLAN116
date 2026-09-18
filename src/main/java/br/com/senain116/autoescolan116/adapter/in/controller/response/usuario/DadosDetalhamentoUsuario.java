package br.com.senain116.autoescolan116.adapter.in.controller.response.usuario;

import br.com.senain116.autoescolan116.application.core.domain.enums.Perfil;

public record DadosDetalhamentoUsuario(
        Long id,
        String login,
        boolean ativo,
        Perfil perfil) {
}