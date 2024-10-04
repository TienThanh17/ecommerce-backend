package com.bamito.dto.response.user;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SetProductOrder {
    String productId;
    String productName;
    String sizeId;
    String sizeName;
    String imageURL;
    long price;
    int discount;
    int quantity;
    long totalPrice;
    String orderId;
//    int feedbackStatus;
}
