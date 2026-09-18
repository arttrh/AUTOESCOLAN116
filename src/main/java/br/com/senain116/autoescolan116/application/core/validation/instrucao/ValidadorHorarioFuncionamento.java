package br.com.senain116.autoescolan116.application.core.validation.instrucao;

import br.com.senain116.autoescolan116.adapter.in.controller.request.instrucao.DadosAgendamento;
import br.com.senain116.autoescolan116.application.core.validation.interfaces.ValidadorAgendamento;
import br.com.senain116.autoescolan116.exception.type.instrucao.ValidacaoException;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;

@Component
public class ValidadorHorarioFuncionamento implements ValidadorAgendamento {
    @Override
    public void validar(DadosAgendamento dados) {
        boolean domingo = dados.data().getDayOfWeek().equals(DayOfWeek.SUNDAY);
        boolean preAbertura = dados.data().getHour() < 6;
        boolean posFechamento = dados.data().getHour() > (21 - 1);

        if (domingo || preAbertura || posFechamento) {
            throw new ValidacaoException("Tentativa de agendamento fora do horário de funcionamento!");
        }
    }
}