package com.example.auth.config;

import com.example.auth.anatation.PasswordResentTokenDlgTopic;
import com.example.auth.anatation.PasswordResentTokenTopic;
import com.example.auth.anatation.UserRegistrationDlq;
import com.example.auth.anatation.UserRegistrationTopic;
import com.example.auth.event.PasswordResentEvent;
import com.example.auth.event.UserCreatedEvent;
import com.example.notification.UserRegistrationNotification;
import com.yourcompany.notification.events.PasswordResetEvent;
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

    @Value("${spring.kafka.topics.password-reset}")
    private String passwordResentTokenTopicName;

    @Value("${spring.kafka.topics.password-reset-dlq}")
    private String passwordResentTokenDlgTopicName;

    @PasswordResentTokenTopic
    @Bean
    public String getPasswordResentTokenTopic(){
        return this.passwordResentTokenTopicName;
    }

    @PasswordResentTokenDlgTopic
    @Bean
    public String getUserRegistrationDlqTopic(){
        return this.userRegistrationDlqTopicName;
    }


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

    @Bean
    public Function<PasswordResentEvent, PasswordResetEvent>passwordResetEventFunction(){
        return event -> PasswordResetEvent.newBuilder()
                .setEmail(event.user().getEmail())
                .setToken(event.token())
                .setExpirationTime(event.expirationTime())
                .setFirstname(event.user().getFirstname())
                .setResetLinkTemplate(event.resetLinkTemplate())
                .build();
    }



}