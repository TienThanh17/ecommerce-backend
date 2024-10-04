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
public class CartDetailRequest {
    @NotNull(message = "user id must not be null")
    Long userId;
    @NotNull(message = "product id must not be null")
    Object productId;
    @NotNull(message = "size id must not be null")
    Object sizeId;
    @NotNull(message = "quantity must not be null")
    Integer quantity;
    @NotNull(message = "totalPrice must not be null")
    Integer totalPrice;
}