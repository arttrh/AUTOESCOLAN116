package br.com.senain116.autoescolan116.adapter.out.repository;

import br.com.senain116.autoescolan116.adapter.out.repository.entity.UsuarioEntity;
import br.com.senain116.autoescolan116.adapter.out.repository.mapper.UsuarioEntityMapper;
import br.com.senain116.autoescolan116.adapter.out.repository.persistence.UsuarioJpaRepository;
import br.com.senain116.autoescolan116.application.core.domain.model.Usuario;
import br.com.senain116.autoescolan116.application.port.out.UsuarioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UsuarioRepositoryImpl implements UsuarioRepository {
    private final UsuarioJpaRepository jpaRepository;
    private final UsuarioEntityMapper entityMapper;

    public UsuarioRepositoryImpl(
            UsuarioJpaRepository jpaRepository,
            UsuarioEntityMapper entityMapper) {
        this.jpaRepository = jpaRepository;
        this.entityMapper = entityMapper;
    }

    @Override
    public UserDetails findByLogin(String username) {
        return jpaRepository.findByLogin(username);
    }

    @Override
    public Page<Usuario> findAllByAtivoTrue(Pageable paginacao) {
        return jpaRepository
                .findAllByAtivoTrue(paginacao)
                .map(entityMapper::toDomain);
    }

    @Override
    public Usuario save(Usuario usuario) {
        UsuarioEntity entity = entityMapper.toEntity(usuario);
        UsuarioEntity saved = jpaRepository.save(entity);
        return entityMapper.toDomain(saved);
    }

    @Override
    public Optional<Usuario> findById(Long id) {
        return jpaRepository
                .findById(id)
                .map(entityMapper::toDomain);
    }
}