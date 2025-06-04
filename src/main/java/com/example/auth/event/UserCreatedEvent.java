package com.example.auth.event;

import com.example.auth.entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class UserCreatedEvent {

    private final User user;
}
