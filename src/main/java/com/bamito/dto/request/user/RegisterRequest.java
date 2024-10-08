package com.bamito.dto.request.user;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegisterRequest {
    @NotBlank(message = "Invalid username")
    String username;
    @NotBlank(message = "Invalid password")
    String password;
    @NotBlank(message = "Invalid email")
    String email;
}
