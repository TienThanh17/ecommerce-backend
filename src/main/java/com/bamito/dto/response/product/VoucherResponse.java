package com.bamito.dto.response.product;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VoucherResponse {
    String id;
    int discount;
    int quantity;
    String startTime;
    String endTime;
    String imageURL;
}
