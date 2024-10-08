package com.bamito.dto.response.product;

import com.bamito.dto.response.user.OrderStatisticRes;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Map;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StatisticResponse {
    Set<OrderStatisticRes> orderStatisticResSet;
    long totalRevenue;
    long totalOrders;
    long totalProducts;
    Set<Map<String, Object>> ordersByType;
}
