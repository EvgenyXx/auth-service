package com.example.auth.dto.mapper;

import com.example.auth.entity.User;
import com.example.notification.UserRegistrationNotification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Mapper(componentModel = "spring")
public interface KafkaEventMapper {

    @Mapping(target = "createdAt", expression = "java(formatDate(user.getCreatedAt()))")
    UserRegistrationNotification toAvro(User user);

    default String formatDate(LocalDateTime date) {
        return date != null
                ? date.atZone(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT)
                : null;
    }
}