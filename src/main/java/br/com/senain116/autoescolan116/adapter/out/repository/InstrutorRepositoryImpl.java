package br.com.senain116.autoescolan116.adapter.out.repository;

import br.com.senain116.autoescolan116.adapter.out.repository.entity.InstrutorEntity;
import br.com.senain116.autoescolan116.adapter.out.repository.mapper.InstrutorEntityMapper;
import br.com.senain116.autoescolan116.adapter.out.repository.persistence.InstrutorJpaRepository;
import br.com.senain116.autoescolan116.application.core.domain.enums.Especialidade;
import br.com.senain116.autoescolan116.application.core.domain.model.Instrutor;
import br.com.senain116.autoescolan116.application.port.out.InstrutorRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class InstrutorRepositoryImpl implements InstrutorRepository {
    private final InstrutorJpaRepository jpaRepository;
    private final InstrutorEntityMapper entityMapper;

    public InstrutorRepositoryImpl(
            InstrutorJpaRepository jpaRepository,
            InstrutorEntityMapper entityMapper) {
        this.jpaRepository = jpaRepository;
        this.entityMapper = entityMapper;
    }

    @Override
    public Page<Instrutor> findAllByAtivoTrue(
            Pageable paginacao) {
        return jpaRepository
                .findAllByAtivoTrue(paginacao)
                .map(entityMapper::toDomain);
    }

    @Override
    public Instrutor escolherInstrutorDisponivel(
            Especialidade especialidade,
            LocalDateTime data) {
        InstrutorEntity entity = jpaRepository
                .escolherInstrutorDisponivel(
                        especialidade,
                        data
                );
        return entity==null ? null : entityMapper.toDomain(entity);
    }

    @Override
    public boolean isActiveById(Long id) {
        return jpaRepository.isActiveById(id);
    }

    @Override
    public Instrutor save(Instrutor instrutor) {
        InstrutorEntity entity = entityMapper.toEntity(instrutor);
        InstrutorEntity saved = jpaRepository.save(entity);
        return entityMapper.toDomain(saved);
    }

    @Override
    public Optional<Instrutor> findById(Long id) {
        return jpaRepository
                .findById(id)
                .map(entityMapper::toDomain);
    }

    @Override
    public boolean existsById(Long id) {
        return jpaRepository.existsById(id);
    }

    @Override
    public Instrutor getReferenceById(Long id) {
        InstrutorEntity entity = jpaRepository.getReferenceById(id);
        return entityMapper.toDomain(entity);
    }
}
