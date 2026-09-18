package br.com.senain116.autoescolan116.application.core.validation.instrucao;

import br.com.senain116.autoescolan116.adapter.in.controller.request.instrucao.DadosAgendamento;
import br.com.senain116.autoescolan116.application.core.validation.interfaces.ValidadorAgendamento;
import br.com.senain116.autoescolan116.exception.type.instrucao.ValidacaoException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ValidadorHorarioInteiro implements ValidadorAgendamento {
    @Override
    public void validar(DadosAgendamento dados) {
        LocalDateTime agendamento = dados.data();

        if (agendamento.getMinute() != 0 || agendamento.getSecond() != 0) {
            throw new ValidacaoException("Apenas horarios inteiros são permitidos (ex: 08:00, 09:00, ...)!");
        }
    }
}