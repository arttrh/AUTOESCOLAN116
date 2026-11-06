package br.com.senain116.autoescolan116.adapter.out.repository;

import br.com.senain116.autoescolan116.adapter.out.repository.entity.InstrucaoEntity;
import br.com.senain116.autoescolan116.adapter.out.repository.mapper.InstrucaoEntityMapper;
import br.com.senain116.autoescolan116.adapter.out.repository.persistence.InstrucaoJpaRepository;
import br.com.senain116.autoescolan116.application.core.domain.model.Instrucao;
import br.com.senain116.autoescolan116.application.port.out.InstrucaoRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class InstrucaoRepositoryImpl implements InstrucaoRepository {
    private final InstrucaoJpaRepository jpaRepository;
    private final InstrucaoEntityMapper entityMapper;

    public InstrucaoRepositoryImpl(
            InstrucaoJpaRepository jpaRepository,
            InstrucaoEntityMapper entityMapper) {
        this.jpaRepository = jpaRepository;
        this.entityMapper = entityMapper;
    }

    @Override
    public boolean existsByAlunoIdAndDataBetween(
            Long id,
            LocalDateTime inicio,
            LocalDateTime fim) {
        return jpaRepository.existsByAlunoIdAndDataBetween(
                id,
                inicio,
                fim
        );
    }

    @Override
    public boolean existsByInstrutorIdAndData(
            Long id,
            LocalDateTime data) {
        return jpaRepository.existsByInstrutorIdAndData(
                id,
                data
        );
    }

    public java.util.List<Instrucao> listarEntre(LocalDateTime inicio, LocalDateTime fim) {
        return jpaRepository.findByDataGreaterThanEqualAndDataLessThanOrderByDataAsc(inicio,fim).stream().map(entityMapper::toDomain).toList();
    }

    @Override
    public Instrucao save(Instrucao instrucao) {
        InstrucaoEntity entity = entityMapper.toEntity(instrucao);
        InstrucaoEntity saved = jpaRepository.save(entity);
        return entityMapper.toDomain(saved);
    }
}