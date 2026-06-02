package br.com.senain116.autoescolan116.adapter.in.controller.request.instrutor;

import br.com.senain116.autoescolan116.adapter.in.controller.request.endereco.DadosEndereco;
import br.com.senain116.autoescolan116.application.core.domain.enums.Especialidade;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record DadosCadastroInstrutor(
        @NotBlank
        String nome,

        @NotBlank
        @Email
        String email,

        @NotBlank
        @Pattern(regexp = "\\(\\d{2}\\) \\d{5}-\\d{4}")
        String telefone,

        @NotBlank
        @Pattern(regexp = "\\d{9,11}")//Primeira maneira de padrão numérico
        String cnh,

        @NotNull
        Especialidade especialidade,

        @Valid
        DadosEndereco endereco) {
}