package com.example.auth.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;



@Data
@Builder
public class UserRegisterRequest {

    private static final String BAD_REQUEST_MESSAGE = "Поле обязательно для заполнения";


    @Size(min = 11, max = 11, message =
            "Номер телефона введен не верно! Попробуйте еще! Пример '89181447133' ")
    @NotBlank(message = BAD_REQUEST_MESSAGE)
    private String numberPhone;

    @Email(message =
            "Электронная почта введена не верно! Попробуй еще раз!")

    @NotBlank(message = BAD_REQUEST_MESSAGE)
    private String email;


    @NotBlank(message = BAD_REQUEST_MESSAGE)
    @Size(min = 6,max = 20,message =
    "Длина пароля должна быть не меньше '6' символов и не больше '100'! ")
    private String password;

    @NotBlank(message = BAD_REQUEST_MESSAGE)
    private String firstname;
}
