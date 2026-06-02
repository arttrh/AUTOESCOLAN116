package br.com.senain116.autoescolan116.adapter.in.controller.request.aluno;

import br.com.senain116.autoescolan116.adapter.in.controller.request.endereco.DadosEndereco;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record DadosAtualizacaoAluno(
        @NotNull
        Long id,
        String nome,

        @Email
        String email,

        @Pattern(regexp = "\\(\\d{2}\\) \\d{5}-\\d{4}")
        String telefone,
        DadosEndereco endereco) {
}