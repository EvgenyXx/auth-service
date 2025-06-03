package com.example.auth.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;



@Data
@Builder
public class UserRegisterRequest {

    private static final String BAD_REQUEST_MESSAGE = "Поле обязательно для заполнения";



    @NotBlank(message = BAD_REQUEST_MESSAGE)
    @Pattern(regexp = "^(\\+7|8)[\\s\\-()]?\\d{3}[\\s\\-()]?\\d{3}[\\s\\-]?\\d{2}[\\s\\-]?\\d{2}$",
            message = "Номер должен начинаться с +7 или 8. Пример: '+79181234567'")
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
    @Pattern(regexp = "^[а-яА-Я]+$", message = "Имя должно содержать только русские буквы")
    private String firstname;
}
