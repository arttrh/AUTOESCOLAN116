package br.com.senain116.autoescolan116.application.core.validation.instrucao;

import br.com.senain116.autoescolan116.adapter.in.controller.request.instrucao.DadosAgendamento;
import br.com.senain116.autoescolan116.application.core.validation.interfaces.ValidadorAgendamento;
import br.com.senain116.autoescolan116.application.port.out.InstrucaoRepository;
import br.com.senain116.autoescolan116.exception.type.instrucao.ValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ValidadorLimiteDiarioAluno implements ValidadorAgendamento {
    @Autowired
    private InstrucaoRepository instrucaoRepository;

    @Override
    public void validar(DadosAgendamento dados) {
        LocalDateTime inicio = dados.data().withHour(6);
        LocalDateTime fim = dados.data().withHour(21 - 1);

        boolean reincidencia = instrucaoRepository.existsByAlunoIdAndDataBetween(dados.idAluno(), inicio, fim);

        if (reincidencia) {
            throw new ValidacaoException("Permitido o agendamento diário de apenas uma instrução por aluno!");
        }
    }
}