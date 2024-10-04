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
public class OrderRequest {
    @NotBlank(message = "cartId must not be blank")
    String cartId;
    @NotNull(message = "userId must not be blank")
    Long userId;

    String voucherId;

    @NotNull(message = "totalPrice must not be blank")
    Integer totalPrice;
    @NotBlank(message = "address must not be blank")
    String address;
    @NotNull(message = "status must not be blank")
    Integer status;
    @NotBlank(message = "payment must not be blank")
    String payment;
}
