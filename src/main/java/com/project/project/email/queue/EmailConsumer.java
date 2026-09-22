package com.project.project.email.queue;

import com.project.project.email.config.RabbitMqConfig;
import com.project.project.email.dto.EmailMessage;
import com.project.project.email.service.EmailService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class EmailConsumer {

    private final EmailService emailService;

    public EmailConsumer(EmailService emailService) {
        this.emailService = emailService;
    }

    @RabbitListener(queues = RabbitMqConfig.EMAIL_QUEUE)
    public void consume(EmailMessage message) throws Exception {
        System.out.println("Received email message: " + message);
        Thread.sleep(10000);

        emailService.sendEmail(
                message.to(),
                message.subject(),
                message.template(),
                message.variables()
        );
    }
}