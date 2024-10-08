package com.bamito.dto.request.user;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ForgotPasswordRequest {
    @NotBlank(message = "email must not be blank")
    String email;
    @NotBlank(message = "password must not be blank")
    String password;
    @NotBlank(message = "Otp must not be blank")
    String otp;
}
