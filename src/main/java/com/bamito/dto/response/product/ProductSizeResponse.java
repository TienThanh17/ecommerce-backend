package com.bamito.dto.response.product;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductSizeResponse {
    SizeResponse sizeResponse;
    ProductResponse productResponse;
    int quantity;
    int sold;
}
