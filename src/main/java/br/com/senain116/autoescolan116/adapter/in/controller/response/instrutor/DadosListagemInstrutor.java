package br.com.senain116.autoescolan116.adapter.in.controller.response.instrutor;

import br.com.senain116.autoescolan116.application.core.domain.enums.Especialidade;

public record DadosListagemInstrutor(
        Long id,
        String nome,
        String email,
        Especialidade especialidade) {
}