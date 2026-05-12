package br.com.senain116.autoescolan116.config.security.filter;

import br.com.senain116.autoescolan116.adapter.out.repository.entity.UsuarioEntity;
import br.com.senain116.autoescolan116.adapter.out.repository.persistence.UsuarioJpaRepository;
import br.com.senain116.autoescolan116.config.security.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {
    private TokenService tokenService;

    private UsuarioJpaRepository repository;

    public SecurityFilter(TokenService tokenService, UsuarioJpaRepository repository){
        this.tokenService = tokenService;
        this.repository = repository;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        String tokenJWT = recuperarToken(request);

        if (tokenJWT != null) {
            String subject = tokenService.getSubject(tokenJWT);
            UsuarioEntity usuario = repository.findByLogin(subject).orElseThrow();
            UsernamePasswordAuthenticationToken authentication
                    = new UsernamePasswordAuthenticationToken
                    (usuario,
                    null,
                            usuario.getAuthorities()
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private String recuperarToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null) {
            return authorizationHeader.replace("Bearer ", "");
        }
        return null;
    }
}