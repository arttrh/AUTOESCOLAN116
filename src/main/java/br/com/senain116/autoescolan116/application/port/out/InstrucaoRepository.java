package br.com.senain116.autoescolan116.application.port.out;

import br.com.senain116.autoescolan116.application.core.domain.model.Instrucao;

import java.time.LocalDateTime;

public interface InstrucaoRepository {
    boolean existsByAlunoIdAndDataBetween(Long id, LocalDateTime inicio, LocalDateTime fim);

    boolean existsByInstrutorIdAndData(Long id, LocalDateTime data);

    java.util.List<Instrucao> listarEntre(LocalDateTime inicio, LocalDateTime fim);

    Instrucao save(Instrucao instrucao);
}