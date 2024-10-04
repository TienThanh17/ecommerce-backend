package com.bamito.dto.response.user;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;


@Getter
@Setter
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    long id;
    String username;
    String email;
    String phoneNumber;
    LocalDate dateOfBirth;
    String avatarUrl;
    String role;
    Boolean active;
    Set<String> addresses;

//    LocalDateTime createDate;
//    LocalDateTime lastModified;
//    String createBy;
//    String lastModifiedBy;
}
