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
public class ProductSizeRequest {
    @NotNull(message = "sizeId name must not be null")
    int sizeId;
    @NotNull(message = "productId name must not be null")
    int productId;
    @NotNull(message = "quantity name must not be null")
    int quantity;
}
