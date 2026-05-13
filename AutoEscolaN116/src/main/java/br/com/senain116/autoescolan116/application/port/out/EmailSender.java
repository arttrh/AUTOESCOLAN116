package br.com.senain116.autoescolan116.application.port.out;

public interface EmailSender {
    void enviar(String destinatario, String assunto, String conteudo);
}
