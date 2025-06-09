package com.example.auth.service.kafka;

import com.example.auth.anatation.PasswordResentTokenDlgTopic;
import com.example.auth.anatation.PasswordResentTokenTopic;
import com.example.auth.event.PasswordResentEvent;
import com.yourcompany.notification.events.PasswordResetEvent;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;



import java.util.function.Function;

@Service
@Log4j2
public class PasswordResentSender extends
        com.example.auth.service.kafka.AbstractKafkaSender<PasswordResentEvent, PasswordResetEvent> implements
        com.example.auth.service.kafka.KafkaEventSender<PasswordResentEvent> {

    public PasswordResentSender(KafkaTemplate<String, PasswordResetEvent> kafkaTemplate,
                                Function<PasswordResentEvent, PasswordResetEvent> mapper,
                                @PasswordResentTokenTopic String mainTopic,
                                @PasswordResentTokenDlgTopic String dlqTopic) {
        super(kafkaTemplate, mapper, mainTopic, dlqTopic);
    }


    @EventListener
    @Override
    public void send(PasswordResentEvent event) {
        log.info("Sending password reset event for user: {}", event.user().getEmail());
        try {
            sendEvent(event.user().getEmail(), event); // Используем email как ключ
        } catch (Exception e) {
            log.error("Failed to send password reset event", e);
            throw e;
        }
    }


}
