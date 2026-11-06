package br.com.senain116.autoescolan116.application.port.out;

import br.com.senain116.autoescolan116.application.core.domain.model.Aluno;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface AlunoRepository {
    Page<Aluno> findAllByAtivoTrue(Pageable paginacao);

    boolean existsByIdAndAtivoTrue(Long id);

    Aluno save(Aluno aluno);

    Optional<Aluno> findById(Long id);

    boolean existsById(Long id);

    Aluno getReferenceById(Long id);
}