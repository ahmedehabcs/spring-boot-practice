package com.project.project.email.queue;


import com.project.project.email.config.RabbitMqConfig;
import com.project.project.email.dto.EmailMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class EmailProducer {

    private final RabbitTemplate rabbitTemplate;

    public EmailProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendEmail(EmailMessage emailMessage) {
        rabbitTemplate.convertAndSend(RabbitMqConfig.EMAIL_QUEUE, emailMessage);
    }
}
