package com.example.auth.service.kafka;


import com.example.auth.dto.mapper.KafkaEventMapper;
import com.example.auth.entity.User;
import com.example.auth.event.UserCreatedEvent;
import com.example.notification.UserRegistrationNotification;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
@Log4j2
public class KafkaEventSenderImpl implements KafkaEventSender {

    private final KafkaEventMapper kafkaEventMapper;
    private final KafkaTemplate<String, UserRegistrationNotification> kafkaTemplate;

    @Async("kafkaTaskExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Override
    public void sendUserRegistrationEvent(UserCreatedEvent userCreatedEvent) {
        User user = userCreatedEvent.getUser();
        try {
            String key = user.getId().toString();
            UserRegistrationNotification event = kafkaEventMapper.toAvro(user);

            kafkaTemplate.send("user-registration-topic", key, event)
                    .whenComplete((result, ex) -> {
                        if (ex != null) {
                            log.error("Ошибка отправки. UserID: {}. Отправка в DLQ...", key);
                            kafkaTemplate.send("user-registration-topic.DLT", key, event);
                        } else {
                            log.info("Успешно. Partition: {}, Offset: {}",
                                    result.getRecordMetadata().partition(),
                                    result.getRecordMetadata().offset());
                        }
                    });
        } catch (Exception e) {
            log.error("Фатальная ошибка маппинга. UserID: {}", user.getId(), e);
        }
    }
}
