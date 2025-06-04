package com.example.auth.service.kafka;


import com.example.auth.event.UserCreatedEvent;

public interface KafkaEventSender {

    void sendUserRegistrationEvent(UserCreatedEvent userCreatedEvent);
}
