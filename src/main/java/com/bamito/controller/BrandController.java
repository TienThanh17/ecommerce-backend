package com.bamito.controller;

import com.bamito.dto.request.product.BrandRequest;
import com.bamito.dto.response.ResponseObject;
import com.bamito.dto.response.product.BrandResponse;
import com.bamito.dto.response.PaginationResponse;
import com.bamito.service.IBrandService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/brand")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class BrandController {
    IBrandService brandService;

    @PostMapping
    public ResponseObject<?> createBrand(@Valid @RequestBody BrandRequest request) {
        brandService.createBrand(request);
        return new ResponseObject<>();
    }

    @GetMapping
    public ResponseObject<PaginationResponse<BrandResponse>> getAllBrands(
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "10", required = false) int size,
            @RequestParam(value ="filter",defaultValue = "",required = false) String filter,
            @RequestParam(value = "sort", defaultValue = "id, desc", required = false) List<String> sort,
            @RequestParam(value = "pagination", defaultValue = "true", required = false) boolean pagination
    ) {
        var result = brandService.getAllBrands(page, size, filter, sort, pagination);

        return ResponseObject.<PaginationResponse<BrandResponse>>builder()
                .data(result)
                .build();
    }

    @PutMapping(path = "/{id}")
    public ResponseObject<?> updateBrand(@Valid @RequestBody BrandRequest request, @PathVariable int id) {
        brandService.updateBrand(id, request);
        return new ResponseObject<>();
    }

    @DeleteMapping("/{id}")
    public ResponseObject<?> deleteBrand(@PathVariable int id) {
        brandService.deleteBrand(id);
        return new ResponseObject<>();
    }
}
