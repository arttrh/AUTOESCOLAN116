package br.com.senain116.autoescolan116.adapter.out.email;

import br.com.senain116.autoescolan116.application.port.out.EmailSender;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class SMTPEmailSender implements EmailSender {
    private final JavaMailSender mailSender;

    public SMTPEmailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void enviar(String destinatario, String assunto, String conteudo) {
        try{
           /* SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(destinatario);
            message.setSubject(assunto);
            message.setText(conteudo);
            mailSender.send(message);*/
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "utf-8");
            helper.setTo(destinatario);
            helper.setSubject(assunto);
            helper.setText(conteudo, true); // true indica que o conteúdo é HTML
            mailSender.send(mimeMessage);
            System.out.println("Email enviado com sucesso para: " + destinatario);
        } catch (Exception e){
            System.out.println("Erro ao enviar email: " + destinatario);
            e.printStackTrace();
        }
    }
}
