package com.bamito.mapper;

import com.bamito.dto.response.product.SizeResponse;
import com.bamito.entity.Size;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ISizeMapper extends IEntityMapper<Size, SizeResponse> {
    @Override
    @Mapping(source = "productCategory", target = "categoryResponse")
    @Mapping(source = "id", target = "id")
    public SizeResponse toDto(Size size);
}
