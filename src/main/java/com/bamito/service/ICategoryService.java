package com.bamito.service;

import com.bamito.dto.request.product.CategoryRequest;
import com.bamito.dto.response.product.CategoryResponse;
import com.bamito.dto.response.PaginationResponse;

import java.util.List;

public interface ICategoryService {
    void createCategory(CategoryRequest request);
    PaginationResponse<CategoryResponse> getAllCategories(Integer page, Integer size, String filter, List<String> sort, Boolean pagination);
    void deleteCategory(int id);
    void updateCategory(int id, CategoryRequest request);
}
