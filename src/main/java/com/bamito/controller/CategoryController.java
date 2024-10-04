package com.bamito.controller;

import com.bamito.dto.request.product.CategoryRequest;
import com.bamito.dto.response.ResponseObject;
import com.bamito.dto.response.product.CategoryResponse;
import com.bamito.dto.response.PaginationResponse;
import com.bamito.service.ICategoryService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class CategoryController {
    ICategoryService categoryService;

    @PostMapping
    public ResponseObject<?> createBrand(@Valid @RequestBody CategoryRequest request) {
        categoryService.createCategory(request);
        return new ResponseObject<>();
    }

    @GetMapping
    public ResponseObject<PaginationResponse<CategoryResponse>> getAllCategories
            (
            @RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
            @RequestParam(value = "size", defaultValue = "10", required = false) Integer size,
            @RequestParam(value ="filter", defaultValue = "", required = false) String filter,
            @RequestParam(value = "sort", defaultValue = "id, desc", required = false) List<String> sort,
            @RequestParam(value = "pagination", defaultValue = "true", required = false) Boolean pagination
    ) {
        var result = categoryService.getAllCategories(page, size, filter, sort, pagination);

        return ResponseObject.<PaginationResponse<CategoryResponse>>builder()
                .data(result)
                .build();
    }

    @PutMapping(path = "/{id}")
    public ResponseObject<?> updateBrand(@Valid @RequestBody CategoryRequest request, @PathVariable int id) {
        categoryService.updateCategory(id, request);
        return new ResponseObject<>();
    }

    @DeleteMapping("/{id}")
    public ResponseObject<?> deleteBrand(@PathVariable int id) {
        categoryService.deleteCategory(id);
        return new ResponseObject<>();
    }
}
