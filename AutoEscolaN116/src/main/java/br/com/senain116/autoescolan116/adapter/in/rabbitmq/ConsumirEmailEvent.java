package br.com.senain116.autoescolan116.adapter.in.rabbitmq;

import br.com.senain116.autoescolan116.adapter.in.rabbitmq.mapper.EmailEventMapper;
import br.com.senain116.autoescolan116.adapter.in.rabbitmq.message.MensagemEmailEvent;
import br.com.senain116.autoescolan116.adapter.out.rabbitmq.event.EmailOCorrenciaEvent;
import br.com.senain116.autoescolan116.application.port.out.EmailSender;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class ConsumirEmailEvent {
    private final EmailEventMapper emailEventMapper;
    private final EmailSender emailSender;

    public ConsumirEmailEvent(EmailEventMapper emailEventMapper, EmailSender emailSender){
        this.emailEventMapper = emailEventMapper;
        this.emailSender = emailSender;
    }

    @RabbitListener(queues = "email.ocorrencia")
    public void receberMensagem(MensagemEmailEvent mensagem){
        EmailOCorrenciaEvent event = emailEventMapper.toEvent(mensagem);
        for (String email : event.emails()){
            emailSender.enviar(email,
                    event.assunto(),
                    event.mensagem());
        }
    }
}
