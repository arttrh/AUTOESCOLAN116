package br.com.senain116.autoescolan116.adapter.out.repository.mapper;

import br.com.senain116.autoescolan116.adapter.out.repository.entity.UsuarioEntity;
import br.com.senain116.autoescolan116.application.core.domain.model.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioEntityMapper {
    public Usuario toDomain(UsuarioEntity entity) {
        return new Usuario(
                entity.getId(),
                entity.getLogin(),
                entity.getSenha(),
                entity.isAtivo(),
                entity.getPerfil()
        );
    }

    public UsuarioEntity toEntity(Usuario usuario) {
        return new UsuarioEntity(
                usuario.getId(),
                usuario.getLogin(),
                usuario.getSenha(),
                usuario.isAtivo(),
                usuario.getPerfil()
        );
    }
}