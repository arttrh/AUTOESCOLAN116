package br.com.senain116.autoescolan116.adapter.out.repository.persistence;

import br.com.senain116.autoescolan116.adapter.out.repository.entity.InstrucaoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface InstrucaoJpaRepository extends JpaRepository<InstrucaoEntity, Long> {
    java.util.List<InstrucaoEntity> findByDataGreaterThanEqualAndDataLessThanOrderByDataAsc(LocalDateTime inicio, LocalDateTime fim);

    boolean existsByAlunoIdAndDataBetween(Long id, LocalDateTime inicio, LocalDateTime fim);

    boolean existsByInstrutorIdAndData(Long id, LocalDateTime data);
}