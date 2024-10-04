package com.bamito.dto.request.product;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BrandRequest {
    @NotBlank(message = "brand id must not be blank")
    String brandId;
    @NotBlank(message = "brand name must not be blank")
    String brandName;
}
