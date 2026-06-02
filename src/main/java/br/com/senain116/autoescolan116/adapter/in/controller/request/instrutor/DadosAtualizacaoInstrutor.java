package br.com.senain116.autoescolan116.adapter.in.controller.request.instrutor;

import br.com.senain116.autoescolan116.adapter.in.controller.request.endereco.DadosEndereco;
import br.com.senain116.autoescolan116.application.core.domain.enums.Especialidade;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record DadosAtualizacaoInstrutor(
        @NotNull
        Long id,
        String nome,

        @Email
        String email,

        @Pattern(regexp = "\\(\\d{2}\\) \\d{5}-\\d{4}")
        String telefone,
        Especialidade especialidade,
        DadosEndereco endereco) {
}