package com.bamito.service;

import com.bamito.dto.request.product.ProductSizeRequest;
import com.bamito.dto.response.PaginationResponse;
import com.bamito.dto.response.product.ProductSizeResponse;

import java.util.List;

public interface IProductSizeService {
    void createProductSize(ProductSizeRequest request);

    PaginationResponse<ProductSizeResponse> getAllProductSizes(int page, int size, List<String> sort, String productId);

    void deleteProductSize(int sizeId, int productId);

    void updateProductSize(ProductSizeRequest productSizeRequest);
}