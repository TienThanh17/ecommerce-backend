package com.bamito.dto.request.product;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryRequest {
    @NotBlank(message = "categoryId must not be blank")
    String categoryId;
    @NotBlank(message = "categoryName must not be blank")
    String categoryName;
}
