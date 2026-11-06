package br.com.senain116.autoescolan116.application.port.out;

import br.com.senain116.autoescolan116.application.core.domain.enums.Especialidade;
import br.com.senain116.autoescolan116.application.core.domain.model.Instrutor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Optional;

public interface InstrutorRepository {
    Page<Instrutor> findAllByAtivoTrue(Pageable paginacao);

    Instrutor escolherInstrutorAleatorioDisponivel(Especialidade especialidade, LocalDateTime data);

    boolean isActiveById(Long id);

    Instrutor save(Instrutor instrutor);

    Optional<Instrutor> findById(Long id);

    boolean existsById(Long id);

    Instrutor getReferenceById(Long id);
}