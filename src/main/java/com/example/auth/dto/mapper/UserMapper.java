package com.example.auth.dto.mapper;

import com.example.auth.dto.*;
import com.example.auth.entity.Role;
import com.example.auth.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toRegisterEntity(UserRegisterRequest userRegisterRequest);

    UserRegisterResponse toRegisterDto(User user);

    @Mapping(target = "accessToken", source = "authTokens.accessToken")
    UserRegisterResponse toRegisterResponse(User user, AuthTokens authTokens);

    @Mapping(target = "accessToken", source = "authTokens.accessToken")
    LoginResponse toLoginDto(User user,AuthTokens authTokens);


    @Mapping(target = "roles", source = "roles", qualifiedByName = "roleToString")
    UserDto toSearchDto(User user);

    @Named("roleToString")
    default String roleToString(Role role) {
        return role.getName();
    }
}
