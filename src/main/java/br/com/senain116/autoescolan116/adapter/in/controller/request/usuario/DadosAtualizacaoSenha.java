package br.com.senain116.autoescolan116.adapter.in.controller.request.usuario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record DadosAtualizacaoSenha(
        @NotNull
        Long id,

        @NotBlank
        String senhaAntiga,

        @NotBlank
        @Pattern(regexp = "\\d{8}")
        String senhaNova) {
}