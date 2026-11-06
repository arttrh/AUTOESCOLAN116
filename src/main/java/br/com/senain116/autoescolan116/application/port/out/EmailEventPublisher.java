package br.com.senain116.autoescolan116.application.port.out;

import br.com.senain116.autoescolan116.adapter.out.rabbitmq.event.EmailOcorrenciaEvent;

public interface EmailEventPublisher {
    void publicarOcorrenciaEvent(EmailOcorrenciaEvent event);
}