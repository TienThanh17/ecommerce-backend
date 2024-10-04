package com.bamito.mapper;

import com.bamito.dto.response.product.BrandResponse;
import com.bamito.entity.Brand;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IBrandMapper extends IEntityMapper<Brand, BrandResponse>{
}
