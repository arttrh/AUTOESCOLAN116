package br.com.senain116.autoescolan116.config.security.filter;
import br.com.senain116.autoescolan116.adapter.out.repository.persistence.UsuarioJpaRepository;
import br.com.senain116.autoescolan116.config.security.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
@Component
public class SecurityFilter extends OncePerRequestFilter {
    private final TokenService tokens;
    private final UsuarioJpaRepository usuarios;
    public SecurityFilter(TokenService tokens, UsuarioJpaRepository usuarios) {
        this.tokens = tokens;
        this.usuarios = usuarios;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain chain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if (header != null) {
            String login;
            try {
                if (!header.startsWith("Bearer "))
                    throw new IllegalArgumentException();
                login = tokens.getSubject(header.substring(7));
            } catch (
                com.auth0.jwt.exceptions.JWTVerificationException | IllegalArgumentException ex) {
                response.sendError(401, "Sessão inválida");
                return;
            }
            UserDetails usuario = usuarios.findByLogin(login);
            if (usuario == null || !usuario.isEnabled()) {
                response.sendError(401, "Sessão inválida");
                return;
            }
            SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities()));
        }
        chain.doFilter(request, response);
    }
}
