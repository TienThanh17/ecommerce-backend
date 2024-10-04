package com.bamito.dto.request.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateFeedbackReq {
    @NotBlank(message = "productId must not be blank")
    String productId;
    @NotNull(message = "userId must not be null")
    Long userId;
    String description;
    Integer rating;
}
