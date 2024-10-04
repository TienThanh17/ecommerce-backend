package com.bamito.controller;

import com.bamito.dto.request.product.ProductSizeRequest;
import com.bamito.dto.response.ResponseObject;
import com.bamito.dto.response.PaginationResponse;
import com.bamito.dto.response.product.ProductSizeResponse;
import com.bamito.service.IProductSizeService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product-size")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ProductSizeController {
    IProductSizeService productSizeService;

    @PostMapping
    public ResponseObject<?> createProductSize(@Valid @RequestBody ProductSizeRequest request) {
        productSizeService.createProductSize(request);
        return new ResponseObject<>();
    }

    @GetMapping
    public ResponseObject<PaginationResponse<ProductSizeResponse>> getAllProductSizes(
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "10", required = false) int size,
            @RequestParam(value = "sort", defaultValue = "id, desc", required = false) List<String> sort,
            @RequestParam(value = "productId") String productId
    ) {
        var result = productSizeService.getAllProductSizes(page, size, sort, productId);

        return ResponseObject.<PaginationResponse<ProductSizeResponse>>builder()
                .data(result)
                .build();
    }

    @PatchMapping
    public ResponseObject<?> updateProductSize(@Valid @RequestBody ProductSizeRequest request) {
        productSizeService.updateProductSize(request);
        return new ResponseObject<>();
    }

    @DeleteMapping
    public ResponseObject<?> deleteBrand(@RequestParam int sizeId, @RequestParam int productId) {
        productSizeService.deleteProductSize(sizeId, productId);
        return new ResponseObject<>();
    }
}
