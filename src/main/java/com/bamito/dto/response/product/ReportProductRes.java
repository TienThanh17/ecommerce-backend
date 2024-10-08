package com.bamito.dto.response.product;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReportProductRes {
    String productName;
    String sizeName;
    long price;
    int discount;
    int quantity;
    long totalPrice;
    LocalDate createDate;
}
