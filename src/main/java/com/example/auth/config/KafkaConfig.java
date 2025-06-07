package com.example.auth.config;

import com.example.auth.anatation.UserRegistrationDlq;
import com.example.auth.anatation.UserRegistrationTopic;
import com.example.auth.event.UserCreatedEvent;
import com.example.notification.UserRegistrationNotification;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.function.Function;

@Configuration
public class KafkaConfig {

    @Value("${spring.kafka.topics.user-registration}")
    private String userRegistrationTopicName;

    @Value("${spring.kafka.topics.user-registration-dlq}")
    private String userRegistrationDlqTopicName;

    @Bean
    @UserRegistrationTopic
    public String userRegistrationTopic() {
        return this.userRegistrationTopicName;
    }

    @Bean
    @UserRegistrationDlq
    public String userRegistrationDlqTopic() {
        return this.userRegistrationDlqTopicName;
    }

    @Bean
    public Function<UserCreatedEvent, UserRegistrationNotification> userRegistrationEventMapper() {
        return event -> UserRegistrationNotification.newBuilder()
                .setId(event.user().getId().toString())
                .setEmail(event.user().getEmail())
                .setNumberPhone(event.user().getNumberPhone())
                .setCreatedAt(LocalDate.now().toString())
                .setFirstname(event.user().getFirstname())
                .build();
    }



}