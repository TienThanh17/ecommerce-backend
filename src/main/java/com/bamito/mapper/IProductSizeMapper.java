package com.bamito.mapper;

import com.bamito.dto.response.product.ProductSizeResponse;
import com.bamito.entity.ProductSize;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface IProductSizeMapper extends IEntityMapper<ProductSize, ProductSizeResponse> {
    @Override
    @Mapping(source = "product", target = "productResponse")
    @Mapping(source = "size", target = "sizeResponse")
    ProductSizeResponse toDto(ProductSize productSize);
}
