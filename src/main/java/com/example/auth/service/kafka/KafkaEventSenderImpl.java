package com.example.auth.service.kafka;


import com.example.auth.dto.mapper.KafkaEventMapper;
import com.example.auth.entity.User;
import com.example.notification.UserRegistrationNotification;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class KafkaEventSenderImpl implements KafkaEventSender {

    private final KafkaEventMapper kafkaEventMapper;
    private final KafkaTemplate<String, UserRegistrationNotification> kafkaTemplate;

    @Override
    public void sendUserRegistrationEvent(User user) {
        try {
            UserRegistrationNotification event = kafkaEventMapper.toAvro(user);
            kafkaTemplate.send("user-registration-topic", event)
                    .whenComplete((result,ex) -> {
                        if (ex != null) {
                            log.error("Ошибка отправки в Kafka для пользователя ID: {}", event.toString(), ex);
                        } else {
                            log.info("Событие отправлено для пользователя ID: {}", event.toString());
                        }
                    });
        } catch (Exception e) {
            log.error("Ошибка события для пользователя ID: {}", user.getId(), e);
        }
    }
}
