package com.bamito.mapper;

import com.bamito.dto.response.user.OrderResponse;
import com.bamito.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IOrderMapper extends IEntityMapper<Order, OrderResponse> {

    @Override
    @Mapping(source = "payment.name", target = "paymentName")
    @Mapping(source = "voucher.id", target = "voucherId")
    @Mapping(source = "createDate", target = "createDate")
    OrderResponse toDto(Order order);
}
