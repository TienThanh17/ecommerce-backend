package com.bamito.mapper;

import com.bamito.dto.response.product.CategoryResponse;
import com.bamito.entity.ProductCategory;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ICategoryMapper extends IEntityMapper<ProductCategory, CategoryResponse> {
}
