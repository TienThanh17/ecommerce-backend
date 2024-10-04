package com.bamito.dto.response.product;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SizeResponse {
    int id;
    String sizeId;
    String sizeName;
    CategoryResponse categoryResponse;
}
