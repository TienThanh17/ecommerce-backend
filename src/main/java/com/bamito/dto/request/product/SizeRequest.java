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
public class SizeRequest {
    @NotBlank(message = "sizeId must not be blank")
    String sizeId;
    @NotBlank(message = "sizeName must not be blank")
    String sizeName;
    @NotNull(message = "categoryId must not be blank")
    String categoryId;
}
