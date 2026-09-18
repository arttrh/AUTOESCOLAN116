package br.com.senain116.autoescolan116.application.core.validation.instrucao;

import br.com.senain116.autoescolan116.adapter.in.controller.request.instrucao.DadosAgendamento;
import br.com.senain116.autoescolan116.application.core.validation.interfaces.ValidadorAgendamento;
import br.com.senain116.autoescolan116.application.port.out.InstrutorRepository;
import br.com.senain116.autoescolan116.exception.type.instrucao.ValidacaoException;
import org.springframework.stereotype.Component;

@Component
public class ValidadorInstrutorAtivo implements ValidadorAgendamento {
    private final InstrutorRepository instrutorRepository;

    public ValidadorInstrutorAtivo(InstrutorRepository instrutorRepository) {
        this.instrutorRepository = instrutorRepository;
    }

    @Override
    public void validar(DadosAgendamento dados) {
        boolean instrutorAtivo = instrutorRepository.isActiveById(dados.idInstrutor());
        if (!instrutorAtivo) {
            throw new ValidacaoException("Instrução não pode ser agendada com instrutor inativo!");
        }
    }
}