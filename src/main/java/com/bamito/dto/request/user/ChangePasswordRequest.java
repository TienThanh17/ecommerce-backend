package com.bamito.dto.request.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChangePasswordRequest {
    @NotNull(message = "id must not be null")
    long id;
    @NotBlank(message = "password must not be blank")
    String oldPassword;
    @NotBlank(message = "new password must not be blank")
    String newPassword;
}
