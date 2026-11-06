package br.com.senain116.autoescolan116.application.port.out;

import br.com.senain116.autoescolan116.application.core.domain.model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UsuarioRepository {
    UserDetails findByLogin(String username);

    Page<Usuario> findAllByAtivoTrue(Pageable paginacao);

    Usuario save(Usuario usuario);

    Optional<Usuario> findById(Long id);
}