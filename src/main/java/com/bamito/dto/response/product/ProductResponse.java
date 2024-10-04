package com.bamito.dto.response.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductResponse {
    int id;
    String productId;
    String productName;
    int price;
    int discount;
    int rating;
    String imageURL;
    String descriptionContent;
    String descriptionHTML;
    BrandResponse brandResponse;
    CategoryResponse categoryResponse;
    Set<SizeByProductResponse> sizeResponse;
}
