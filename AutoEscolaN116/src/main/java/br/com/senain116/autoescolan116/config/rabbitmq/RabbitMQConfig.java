package br.com.senain116.autoescolan116.config.rabbitmq;

import br.com.senain116.autoescolan116.application.core.service.EmailNotificacaoService;
import br.com.senain116.autoescolan116.application.port.out.EmailEventPublisher;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.TemplateEngine;

@Configuration
public class RabbitMQConfig {

    public static final String QUEUE = "email.ocorrencia";

    @Bean
    public Queue emailOcorrenciaQueue(){
        return new Queue(QUEUE, true);
    }

    @Bean
    public JacksonJsonMessageConverter jacksonJsonMessageConverter(){
        JacksonJsonMessageConverter converter = new JacksonJsonMessageConverter();
        converter.setAlwaysConvertToInferredType(true);
        return new JacksonJsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jacksonJsonMessageConverter());
        return template;
    }

    //Tranformando EmailNotificacaoService em um Bean
    @Bean
    public EmailNotificacaoService emailNotificacaoService(EmailEventPublisher emailEventPublisher, TemplateEngine templateEngine){
        return new EmailNotificacaoService(emailEventPublisher, templateEngine);
    }
}
