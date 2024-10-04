package com.bamito.dto.request.user;

import com.bamito.dto.response.user.SetProductOrder;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CancelOrderRequest {
    @NotBlank(message = "orderId must not be blank")
    String orderId;
    @NotNull(message = "setProductOrder must not be null")
    Set<SetProductOrder> setProductOrder;
}
