package com.bamito.dto.response.product;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductCartResponse {
    String productId;
    String productName;
    String categoryId;
    String categoryName;
    String imageURL;
    String sizeId;
    String sizeName;
    long price;
    int discount;
    int quantity;
    long totalPrice;
    int quantityInStock;
}
