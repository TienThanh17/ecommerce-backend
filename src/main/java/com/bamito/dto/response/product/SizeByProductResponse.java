package com.bamito.dto.response.product;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SizeByProductResponse {
    int id;
    String sizeId;
    String sizeName;
    int quantity;
    int sold;
}
