package br.com.senain116.autoescolan116.adapter.in.controller.response.aluno;

import br.com.senain116.autoescolan116.adapter.in.controller.request.endereco.DadosEndereco;

public record DadosDetalhamentoAluno(
        Long id,
        String nome,
        String email,
        String telefone,
        String cpf,
        DadosEndereco endereco,
        boolean ativo) {
}