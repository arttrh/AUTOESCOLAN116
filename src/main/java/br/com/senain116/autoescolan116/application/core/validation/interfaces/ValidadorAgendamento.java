package br.com.senain116.autoescolan116.application.core.validation.interfaces;

import br.com.senain116.autoescolan116.adapter.in.controller.request.instrucao.DadosAgendamento;

public interface ValidadorAgendamento {
    void validar(DadosAgendamento dados);
}