package br.com.senain116.autoescolan116.application.core.service;

import br.com.senain116.autoescolan116.adapter.out.rabbitmq.event.EmailOCorrenciaEvent;
import br.com.senain116.autoescolan116.application.core.domain.model.Instrucao;
import br.com.senain116.autoescolan116.application.port.out.EmailEventPublisher;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class EmailNotificacaoService {
    private final EmailEventPublisher emailEventPublisher;
    // private final TemplateEngine templateEngine;
    public EmailNotificacaoService(EmailEventPublisher emailEventPublisher){
        this.emailEventPublisher = emailEventPublisher;
    }

    public void enviarNotificacaoInstrucao(Instrucao instrucao, String acao){
        List<String> emails = List.of(
                "contato@example.com",
                instrucao.getAluno().getEmail(),
                instrucao.getInstrutor().getEmail());
        String assunto = "Nova instrucao " + acao;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String dataFormatada = instrucao.getData().format(formatter);

        String mensagem = "\nUma nova Instrucao foi " + acao + "!" +
                "\nID: " + instrucao.getId() + "\nAluno: " + instrucao.getAluno().getNome() +
                "\nInstrutor: " + instrucao.getInstrutor().getNome() + "\nData: " + dataFormatada;

        EmailOCorrenciaEvent event = new EmailOCorrenciaEvent(
                instrucao.getId(),
                emails,
                assunto,
                mensagem);
        emailEventPublisher.publicarOcorrenciaEvent(event);
    }
}
