package br.com.senain116.autoescolan116.application.core.validation.instrucao;

import br.com.senain116.autoescolan116.adapter.in.controller.request.instrucao.DadosAgendamento;
import br.com.senain116.autoescolan116.application.core.validation.interfaces.ValidadorAgendamento;
import br.com.senain116.autoescolan116.application.port.out.AlunoRepository;
import br.com.senain116.autoescolan116.exception.type.instrucao.ValidacaoException;
import org.springframework.stereotype.Component;

@Component
public class ValidadorAlunoAtivo implements ValidadorAgendamento {
    private final AlunoRepository alunoRepository;

    public ValidadorAlunoAtivo(AlunoRepository alunoRepository) {
        this.alunoRepository = alunoRepository;
    }

    @Override
    public void validar(DadosAgendamento dados) {
        boolean alunoAtivo = alunoRepository.existsByIdAndAtivoTrue(dados.idAluno());
        if (!alunoAtivo) {
            throw new ValidacaoException("Instrução não pode ser agendada para aluno inativo!");
        }
    }
}