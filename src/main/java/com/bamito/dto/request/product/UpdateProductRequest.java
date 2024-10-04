package com.bamito.dto.request.product;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateProductRequest {
    String productId;
    String productName;
    Integer price;
    Integer discount;
    MultipartFile image;
    String brandId;
    String categoryId;
    String descriptionContent;
    String descriptionHTML;
}
