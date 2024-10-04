package com.bamito.controller;

import com.bamito.dto.request.product.SizeRequest;
import com.bamito.dto.response.ResponseObject;
import com.bamito.dto.response.PaginationResponse;
import com.bamito.dto.response.product.SizeResponse;
import com.bamito.service.ISizeService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/size")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class SizeController {
    ISizeService sizeService;

    @PostMapping
    public ResponseObject<?> createBrand(@Valid @RequestBody SizeRequest request) {
        sizeService.createSize(request);
        return new ResponseObject<>();
    }

    @GetMapping("/get-all-by-category")
    public ResponseObject<Set<SizeResponse>> getAllByCategory(@RequestParam String categoryId) {
        var result = sizeService.getAllSizesByCategory(categoryId);
        return ResponseObject.<Set<SizeResponse>>builder()
                .data(result)
                .build();
    }

    @GetMapping
    public ResponseObject<PaginationResponse<SizeResponse>> getAllSizes(
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "10", required = false) int size,
            @RequestParam(value ="filter",defaultValue = "", required = false) String filter,
            @RequestParam(value = "sort", defaultValue = "id, desc", required = false) List<String> sort
    ) {
        var result = sizeService.getAllSizes(page, size, filter, sort);

        return ResponseObject.<PaginationResponse<SizeResponse>>builder()
                .data(result)
                .build();
    }

    @PutMapping(path = "/{id}")
    public ResponseObject<?> updateBrand(@Valid @RequestBody SizeRequest request, @PathVariable int id) {
        sizeService.updateSize(id, request);
        return new ResponseObject<>();
    }

    @DeleteMapping("/{id}")
    public ResponseObject<?> deleteBrand(@PathVariable int id) {
        sizeService.deleteSize(id);
        return new ResponseObject<>();
    }
}
