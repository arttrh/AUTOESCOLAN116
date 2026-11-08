package br.com.senain116.autoescolan116.adapter.in.controller.request.usuario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record DadosCadastroPublico(
        @NotBlank
        @Size(max = 100)
        @Pattern(regexp = "\\S+", message = "não pode conter espaços")
        String login,

        @NotBlank
        @Size(min = 8, max = 72)
        String senha) {
}
