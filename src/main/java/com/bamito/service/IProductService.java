package com.bamito.service;

import com.bamito.dto.request.product.ProductRequest;
import com.bamito.dto.request.product.UpdateProductRequest;
import com.bamito.dto.response.PaginationResponse;
import com.bamito.dto.response.product.ProductResponse;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface IProductService {
    void createProduct(ProductRequest request) throws IOException;

    ProductResponse getProduct(String productId);

    PaginationResponse<ProductResponse> getAllProducts(int page, int size, String filter, List<String> sort);

    PaginationResponse<ProductResponse> getProductsByCategory(int page, int size, Map<String, Object> filter, List<String> sort, String categoryId);

    PaginationResponse<ProductResponse> getProductsByUserFavorite(int page, int size, String filter, List<String> sort, long userId);

    PaginationResponse<ProductResponse> getSaleProducts(int page, int size, List<String> sort);

    void deleteProduct(int id) throws IOException;

    void updateProduct(int id, UpdateProductRequest request) throws IOException;
}
