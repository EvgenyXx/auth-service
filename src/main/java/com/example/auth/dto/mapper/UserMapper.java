package com.example.auth.dto.mapper;

import com.example.auth.dto.*;
import com.example.auth.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toRegisterEntity(UserRegisterRequest userRegisterRequest);

    UserRegisterResponse toRegisterDto(User user);

    @Mapping(target = "accessToken", source = "authTokens.accessToken")
    UserRegisterResponse toRegisterResponse(User user, AuthTokens authTokens);

    @Mapping(target = "accessToken", source = "authTokens.accessToken")
    LoginResponse toLoginDto(User user,AuthTokens authTokens);
}
