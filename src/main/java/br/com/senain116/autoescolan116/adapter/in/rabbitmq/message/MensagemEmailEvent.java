package br.com.senain116.autoescolan116.adapter.in.rabbitmq.message;

import java.util.List;

public record MensagemEmailEvent(
        Long ocorrencia,
        List<String> emails,
        String assunto,
        String mensagem) {
}