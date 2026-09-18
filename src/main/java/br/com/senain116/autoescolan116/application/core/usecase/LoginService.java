package br.com.senain116.autoescolan116.application.core.usecase;

import br.com.senain116.autoescolan116.adapter.in.controller.request.usuario.DadosLogin;
import br.com.senain116.autoescolan116.adapter.out.repository.entity.UsuarioEntity;
import br.com.senain116.autoescolan116.adapter.out.repository.mapper.UsuarioEntityMapper;
import br.com.senain116.autoescolan116.adapter.out.repository.persistence.UsuarioJpaRepository;
import br.com.senain116.autoescolan116.application.core.domain.model.Usuario;
import br.com.senain116.autoescolan116.config.security.dto.DadosTokenJWT;
import br.com.senain116.autoescolan116.config.security.service.TokenService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class LoginService {
    private final AuthenticationManager manager;
    private final TokenService tokenService;
    private final UsuarioEntityMapper entityMapper;
    private final UsuarioJpaRepository jpaRepository;

    public LoginService(
            AuthenticationManager manager,
            TokenService tokenService,
            UsuarioEntityMapper entityMapper,
            UsuarioJpaRepository jpaRepository) {
        this.manager = manager;
        this.tokenService = tokenService;
        this.entityMapper = entityMapper;
        this.jpaRepository = jpaRepository;
    }

    public DadosTokenJWT logar(DadosLogin dados) {
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(
                        dados.login(),
                        dados.senha()
                );
        Authentication authentication = manager.authenticate(token);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String login = Objects.requireNonNull(userDetails).getUsername();
        UsuarioEntity entity = (UsuarioEntity) jpaRepository.findByLogin(login);
        Usuario usuario = entityMapper.toDomain(entity);
        String tokenJWT = tokenService.gerarToken(usuario);
        return new DadosTokenJWT(tokenJWT);
    }
}