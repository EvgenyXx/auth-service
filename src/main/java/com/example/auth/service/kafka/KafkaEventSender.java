package com.example.auth.service.kafka;




public interface KafkaEventSender<T> {
    void send(T t);

}