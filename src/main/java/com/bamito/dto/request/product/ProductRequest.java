package com.bamito.dto.request.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductRequest {
    @NotBlank(message = "productId must not be blank")
    String productId;
    @NotBlank(message = "productName must not be blank")
    String productName;
    @NotNull(message = "price must not be null")
    int price;
    @NotNull(message = "image must not be null")
    MultipartFile image;
    @NotBlank(message = "brandId must not be blank")
    String brandId;
    @NotBlank(message = "categoryId must not be blank")
    String categoryId;
    String descriptionContent;
    String descriptionHTML;
}
