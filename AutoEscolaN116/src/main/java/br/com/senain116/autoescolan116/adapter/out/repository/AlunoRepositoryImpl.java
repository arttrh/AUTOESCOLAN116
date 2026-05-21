package br.com.senain116.autoescolan116.adapter.out.repository;

import br.com.senain116.autoescolan116.adapter.out.repository.entity.AlunoEntity;
import br.com.senain116.autoescolan116.adapter.out.repository.mapper.AlunoEntityMapper;
import br.com.senain116.autoescolan116.adapter.out.repository.persistence.AlunoJpaRepository;
import br.com.senain116.autoescolan116.application.core.domain.model.Aluno;
import br.com.senain116.autoescolan116.application.port.out.AlunoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AlunoRepositoryImpl implements AlunoRepository {
    private final AlunoJpaRepository jpaRepository;
    private final AlunoEntityMapper entityMapper;

    public AlunoRepositoryImpl(
            AlunoJpaRepository jpaRepository,
            AlunoEntityMapper entityMapper) {
        this.jpaRepository = jpaRepository;
        this.entityMapper = entityMapper;
    }

    @Override
    public Page<Aluno> findAllByAtivoTrue(Pageable paginacao) {
        return jpaRepository
                .findAllByAtivoTrue(paginacao)
                .map(entityMapper::toDomain);
    }

    @Override
    public boolean existsByIdAndAtivoTrue(Long id) {
        Boolean resultado = jpaRepository.existsByIdAndAtivoTrue(id);
        return resultado != null && resultado;
    }

    @Override
    public Aluno save(Aluno aluno) {
        AlunoEntity entity = entityMapper.toEntity(aluno);
        AlunoEntity saved = jpaRepository.save(entity);
        return entityMapper.toDomain(saved);
    }

    @Override
    public Optional<Aluno> findById(Long id) {
        return jpaRepository
                .findById(id)
                .map(entityMapper::toDomain);
    }
}