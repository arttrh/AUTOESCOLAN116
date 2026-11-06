package br.com.senain116.autoescolan116.config.security.dto;
import br.com.senain116.autoescolan116.application.core.domain.enums.Perfil;
public record DadosTokenJWT(String tokenJWT, Long id, String login, Perfil perfil) {}
