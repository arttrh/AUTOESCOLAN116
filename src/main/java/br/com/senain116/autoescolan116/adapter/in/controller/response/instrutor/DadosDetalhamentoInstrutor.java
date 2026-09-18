package br.com.senain116.autoescolan116.adapter.in.controller.response.instrutor;

import br.com.senain116.autoescolan116.adapter.in.controller.request.endereco.DadosEndereco;
import br.com.senain116.autoescolan116.application.core.domain.enums.Especialidade;

public record DadosDetalhamentoInstrutor(
        Long id,
        String nome,
        String email,
        String telefone,
        String cnh,
        Especialidade especialidade,
        DadosEndereco endereco,
        boolean ativo) {
}