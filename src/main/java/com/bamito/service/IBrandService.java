package com.bamito.service;

import com.bamito.dto.request.product.BrandRequest;
import com.bamito.dto.response.product.BrandResponse;
import com.bamito.dto.response.PaginationResponse;

import java.util.List;

public interface IBrandService {
    void createBrand(BrandRequest request);

    PaginationResponse<BrandResponse> getAllBrands(int page, int size, String filter, List<String> sort, boolean pagination);

    void deleteBrand(int id);

    void updateBrand(int id, BrandRequest request);
}
