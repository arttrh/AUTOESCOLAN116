package br.com.senain116.autoescolan116.adapter.out.rabbitmq.event;

import java.util.List;

public record EmailOCorrenciaEvent(
        Long ocorrencia,
        List<String> emails,
        String assunto,
        String mensagem) {
}
