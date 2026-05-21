package br.com.senain116.autoescolan116.adapter.in.rabbitmq.mapper;

import br.com.senain116.autoescolan116.adapter.in.rabbitmq.message.MensagemEmailEvent;
import br.com.senain116.autoescolan116.adapter.out.rabbitmq.event.EmailOCorrenciaEvent;
import org.springframework.stereotype.Component;

@Component
public class EmailEventMapper {
    public EmailOCorrenciaEvent toEvent(MensagemEmailEvent msg){
        return new EmailOCorrenciaEvent(
                msg.ocorrencia(),
                msg.emails(),
                msg.assunto(),
                msg.mensagem()
        );
    }
}
