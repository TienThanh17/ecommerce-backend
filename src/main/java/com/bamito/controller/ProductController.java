package com.bamito.controller;

import com.bamito.dto.request.product.ProductRequest;
import com.bamito.dto.request.product.UpdateProductRequest;
import com.bamito.dto.response.ResponseObject;
import com.bamito.dto.response.PaginationResponse;
import com.bamito.dto.response.product.ProductResponse;
import com.bamito.service.IProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/product")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ProductController {
    IProductService productService;

    @PostMapping(path = "", consumes = "multipart/form-data")
    public ResponseObject<?> createProductFormData(@Valid @ModelAttribute ProductRequest request)
            throws IOException {
        productService.createProduct(request);
        return new ResponseObject<>();
    }

    @PostMapping(path = "", consumes = "application/json")
    public ResponseObject<?> createProductJson(@Valid @RequestBody ProductRequest request)
            throws IOException {
        productService.createProduct(request);
        return new ResponseObject<>();
    }

    @GetMapping
    public ResponseObject<PaginationResponse<ProductResponse>> getAllProducts
            (
                    @RequestParam(value = "page", defaultValue = "0", required = false) int page,
                    @RequestParam(value = "size", defaultValue = "10", required = false) int size,
                    @RequestParam(value = "filter", defaultValue = "", required = false) String filter,
                    @RequestParam(value = "sort", defaultValue = "id, desc", required = false) List<String> sort
            ) {
        var result = productService.getAllProducts(page, size, filter, sort);

        return ResponseObject.<PaginationResponse<ProductResponse>>builder()
                .data(result)
                .build();
    }

    @GetMapping("get-all-by-category")
    public ResponseObject<PaginationResponse<ProductResponse>> getProductsByCategory
            (
                    @RequestParam(value = "page", defaultValue = "0", required = false) int page,
                    @RequestParam(value = "size", defaultValue = "10", required = false) int size,
                    @RequestParam(value = "filter", defaultValue = "", required = false) String filter,
                    @RequestParam(value = "sort", defaultValue = "id, desc", required = false) String sort,
                    @RequestParam(value = "categoryId") String categoryId
            ) throws JsonProcessingException {
        // Parse chuỗi JSON thành Map
        Map<String, Object> filterMap = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();
        if (!filter.isBlank()) {
            filterMap = objectMapper.readValue(filter, new TypeReference<Map<String, Object>>() {
            });
        }
        List<String> sortList = new ArrayList<>();
        if (!sort.isBlank()) {
            String sortFormat = sort.replace("[", "")
                    .replace("]", "")
                    .replace("\"", "");
            System.out.println("sort format " + sortFormat);
            sortList = Arrays.asList(sortFormat.split(",\\s*")); // Tách chuỗi bằng dấu phẩy và loại bỏ khoảng trắng dư thừa
        }
        var result = productService.getProductsByCategory(page, size, filterMap, sortList, categoryId);

        return ResponseObject.<PaginationResponse<ProductResponse>>builder()
                .data(result)
                .build();
    }

    @GetMapping("get-all-by-user")
    public ResponseObject<PaginationResponse<ProductResponse>> getFavoriteByUser
            (
                    @RequestParam(value = "page", defaultValue = "0", required = false) int page,
                    @RequestParam(value = "size", defaultValue = "10", required = false) int size,
                    @RequestParam(value = "filter", defaultValue = "", required = false) String filter,
                    @RequestParam(value = "sort", defaultValue = "id, desc", required = false) List<String> sort,
                    @RequestParam(value = "userId") long userId
            ) {
        var result = productService.getProductsByUserFavorite(page, size, filter, sort, userId);

        return ResponseObject.<PaginationResponse<ProductResponse>>builder()
                .data(result)
                .build();
    }

    @GetMapping("get-all-sale")
    public ResponseObject<PaginationResponse<ProductResponse>> getSaleProducts
            (
                    @RequestParam(value = "page", defaultValue = "0", required = false) int page,
                    @RequestParam(value = "size", defaultValue = "10", required = false) int size,
                    @RequestParam(value = "sort", defaultValue = "id, desc", required = false) List<String> sort
            ) {
        var result = productService.getSaleProducts(page, size, sort);

        return ResponseObject.<PaginationResponse<ProductResponse>>builder()
                .data(result)
                .build();
    }

    @PatchMapping(path = "/{id}", consumes = "application/json")
    public ResponseObject<?> updateProductJson(@Valid @RequestBody UpdateProductRequest request, @PathVariable int id)
            throws IOException {
        productService.updateProduct(id, request);
        return new ResponseObject<>();
    }

    @PatchMapping(path = "/{id}", consumes = "multipart/form-data")
    public ResponseObject<?> updateProductFormData(@Valid @ModelAttribute UpdateProductRequest request, @PathVariable int id)
            throws IOException {
        productService.updateProduct(id, request);
        return new ResponseObject<>();
    }

    @DeleteMapping("/{id}")
    public ResponseObject<?> deleteBrand(@PathVariable int id) throws IOException {
        productService.deleteProduct(id);
        return new ResponseObject<>();
    }

    @GetMapping("/{id}")
    public ResponseObject<ProductResponse> getProduct(@PathVariable String id) {
        var result = productService.getProduct(id);
        return ResponseObject.<ProductResponse>builder()
                .data(result)
                .build();
    }
}
