package com.example.auth.dto.mapper;

import com.example.auth.dto.UserRegisterRequest;
import com.example.auth.dto.UserRegisterResponse;
import com.example.auth.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toRegisterEntity(UserRegisterRequest userRegisterRequest);

    UserRegisterResponse toRegisterDto(User user);
}
