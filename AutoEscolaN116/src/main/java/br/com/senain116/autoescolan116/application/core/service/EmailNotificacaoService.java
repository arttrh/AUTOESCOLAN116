package br.com.senain116.autoescolan116.application.core.service;

import br.com.senain116.autoescolan116.adapter.out.rabbitmq.event.EmailOCorrenciaEvent;
import br.com.senain116.autoescolan116.application.core.domain.model.Instrucao;
import br.com.senain116.autoescolan116.application.port.out.EmailEventPublisher;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class EmailNotificacaoService {
    private final EmailEventPublisher emailEventPublisher;
    private final TemplateEngine templateEngine;
    // private final TemplateEngine templateEngine;
    public EmailNotificacaoService(EmailEventPublisher emailEventPublisher, TemplateEngine templateEngine){
        this.emailEventPublisher = emailEventPublisher;
        this.templateEngine = templateEngine;
    }

    public void enviarNotificacaoInstrucao(Instrucao instrucao, String acao){
        List<String> emails = List.of(
                "contato@example.com",
                instrucao.getAluno().getEmail(),
                instrucao.getInstrutor().getEmail());
        String assunto = "Nova instrucao " + acao;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String dataFormatada = instrucao.getData().format(formatter);

        /*String mensagem = "\nUma nova Instrucao foi " + acao + "!" +
                "\nID: " + instrucao.getId() + "\nAluno: " + instrucao.getAluno().getNome() +
                "\nInstrutor: " + instrucao.getInstrutor().getNome() + "\nData: " + dataFormatada;*/

        Context context = new Context();
        context.setVariable("acao", acao);
        context.setVariable("id", instrucao.getId().toString());
        context.setVariable("aluno", instrucao.getAluno().getNome());
        context.setVariable("instrutor", instrucao.getInstrutor().getNome());
        context.setVariable("data", dataFormatada);

        String mensagem = templateEngine.process("email-notificacao-instrucao", context);

        EmailOCorrenciaEvent event = new EmailOCorrenciaEvent(
                instrucao.getId(),
                emails,
                assunto,
                mensagem);
        emailEventPublisher.publicarOcorrenciaEvent(event);
    }
}
