package br.com.senain116.autoescolan116.adapter.out.repository.persistence;

import br.com.senain116.autoescolan116.adapter.out.repository.entity.AlunoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AlunoJpaRepository extends JpaRepository<AlunoEntity, Long> {
    Page<AlunoEntity> findAllByAtivoTrue(Pageable paginacao);

    @Query("""
                select a.ativo
                from Aluno a
                where
                a.id = :id
            """)
    boolean existsByIdAndAtivoTrue(Long id);
}