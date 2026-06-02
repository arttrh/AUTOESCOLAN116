package br.com.senain116.autoescolan116.application.core.validation.instrucao;

import br.com.senain116.autoescolan116.adapter.in.controller.request.instrucao.DadosAgendamento;
import br.com.senain116.autoescolan116.application.core.validation.interfaces.ValidadorAgendamento;
import br.com.senain116.autoescolan116.application.port.out.InstrucaoRepository;
import br.com.senain116.autoescolan116.exception.type.instrucao.ValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidadorInstrutorDisponivel implements ValidadorAgendamento {
    @Autowired
    private InstrucaoRepository instrucaoRepository;

    @Override
    public void validar(DadosAgendamento dados) {
        boolean ocupado = instrucaoRepository.existsByInstrutorIdAndData(dados.idInstrutor(), dados.data());

        if (ocupado) {
            throw new ValidacaoException("Instrutor ocupado na data e horário informados");
        }
    }
}