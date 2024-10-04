package com.bamito.mapper;

import com.bamito.dto.response.product.ProductResponse;
import com.bamito.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IProductMapper extends IEntityMapper<Product, ProductResponse> {
    @Override
    @Mapping(source = "productCategory", target = "categoryResponse")
    @Mapping(source = "brand", target = "brandResponse")
    ProductResponse toDto(Product product);
}
