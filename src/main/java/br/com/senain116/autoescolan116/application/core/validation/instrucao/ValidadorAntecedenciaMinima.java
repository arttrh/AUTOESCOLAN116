package br.com.senain116.autoescolan116.application.core.validation.instrucao;

import br.com.senain116.autoescolan116.adapter.in.controller.request.instrucao.DadosAgendamento;
import br.com.senain116.autoescolan116.application.core.validation.interfaces.ValidadorAgendamento;
import br.com.senain116.autoescolan116.exception.type.instrucao.ValidacaoException;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

@Component
public class ValidadorAntecedenciaMinima implements ValidadorAgendamento {
    @Override
    public void validar(DadosAgendamento dados) {
        LocalDateTime agendamento = dados.data();
        LocalDateTime agora = LocalDateTime.now();

        long antecedencia = Duration.between(agora, agendamento).toMinutes();

        if (antecedencia < 30) {
            throw new ValidacaoException("Instrução deve ser agendada com antecedência mínima de 30 minutos!");
        }
    }
}