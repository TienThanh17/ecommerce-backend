package com.bamito.dto.request.user;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateUserRequest {
    @NotBlank(message = "username must not be blank")
    String username;
    @NotBlank(message = "password must not be blank")
    String password;
    @NotBlank(message = "email must not be blank")
    String email;
    @NotBlank(message = "role must not be blank")
    String role;
    @NotBlank(message = "phoneNumber must not be blank")
    String phoneNumber;
    @NotBlank(message = "address must not be blank")
    String address;
}
