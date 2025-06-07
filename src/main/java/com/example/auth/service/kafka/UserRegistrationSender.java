package com.example.auth.service.kafka;

import com.example.auth.anatation.UserRegistrationDlq;
import com.example.auth.anatation.UserRegistrationTopic;
import com.example.auth.event.UserCreatedEvent;
import com.example.notification.UserRegistrationNotification;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.function.Function;

@Service
public class UserRegistrationSender extends
        com.example.auth.service.kafka.AbstractKafkaSender<UserCreatedEvent, UserRegistrationNotification> implements
        com.example.auth.service.kafka.KafkaEventSender<UserCreatedEvent> {

    public UserRegistrationSender(
            KafkaTemplate<String, UserRegistrationNotification> kafkaTemplate,
            Function<UserCreatedEvent, UserRegistrationNotification> mapper,
            @UserRegistrationTopic String mainTopic, // Внедряем конкретный топик
            @UserRegistrationDlq String dlqTopic
    ) {
        super(kafkaTemplate, mapper, mainTopic, dlqTopic);
    }

    @Async("kafkaTaskExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Override
    public void send(UserCreatedEvent event) {
        sendEvent(event.user().getId().toString(), event);
    }
}
