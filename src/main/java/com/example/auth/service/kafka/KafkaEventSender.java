package com.example.auth.service.kafka;

import com.example.auth.entity.User;

public interface KafkaEventSender {

    void sendUserRegistrationEvent(User user);
}
