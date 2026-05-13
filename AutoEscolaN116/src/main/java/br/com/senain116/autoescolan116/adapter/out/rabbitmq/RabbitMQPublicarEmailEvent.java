package br.com.senain116.autoescolan116.adapter.out.rabbitmq;

import br.com.senain116.autoescolan116.adapter.out.rabbitmq.event.EmailOCorrenciaEvent;
import br.com.senain116.autoescolan116.application.port.out.EmailEventPublisher;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQPublicarEmailEvent implements EmailEventPublisher {
    public final RabbitTemplate rabbitTemplate;

    public RabbitMQPublicarEmailEvent(RabbitTemplate rabbitTemplate){
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void publicarOcorrenciaEvent(EmailOCorrenciaEvent event) {
        rabbitTemplate.convertAndSend("email.ocorrencia", event);
    }
}
