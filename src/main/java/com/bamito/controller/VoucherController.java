package com.bamito.controller;

import com.bamito.dto.request.product.UpdateVoucherRequest;
import com.bamito.dto.request.product.VoucherRequest;
import com.bamito.dto.response.ResponseObject;
import com.bamito.dto.response.PaginationResponse;
import com.bamito.dto.response.product.VoucherResponse;
import com.bamito.service.IVoucherService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/voucher")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class VoucherController {
    IVoucherService voucherService;

    @PostMapping(path = "", consumes = "multipart/form-data")
    public ResponseObject<?> createVoucherFormData(@Valid @ModelAttribute VoucherRequest request)
            throws IOException {
        voucherService.createVoucher(request);
        return new ResponseObject<>();
    }

    @PostMapping(path = "", consumes = "application/json")
    public ResponseObject<?> createVoucherJson(@Valid @RequestBody VoucherRequest request)
            throws IOException {
        voucherService.createVoucher(request);
        return new ResponseObject<>();
    }

    @GetMapping
    public ResponseObject<PaginationResponse<VoucherResponse>> getAllVouchers
            (
                    @RequestParam(value = "page", defaultValue = "0", required = false) int page,
                    @RequestParam(value = "size", defaultValue = "10", required = false) int size,
                    @RequestParam(value = "filter", defaultValue = "", required = false) String filter,
                    @RequestParam(value = "sort", defaultValue = "id, desc", required = false) List<String> sort,
                    @RequestParam(value = "pagination", defaultValue = "true", required = false) boolean pagination
            ) {
        var result = voucherService.getAllVouchers(page, size, filter, sort, pagination);

        return ResponseObject.<PaginationResponse<VoucherResponse>>builder()
                .data(result)
                .build();
    }

    @GetMapping("get-all-user-voucher")
    public ResponseObject<Set<VoucherResponse>> getAllUserVouchers() {
        var result = voucherService.getAllUserVouchers();
        return ResponseObject.<Set<VoucherResponse>>builder()
                .data(result)
                .build();
    }

    @PatchMapping(path = "/{id}", consumes = "application/json")
    public ResponseObject<?> updateProductJson(@Valid @RequestBody UpdateVoucherRequest request, @PathVariable String id)
            throws IOException {
        voucherService.updateVoucher(id, request);
        return new ResponseObject<>();
    }

    @PatchMapping(path = "/{id}", consumes = "multipart/form-data")
    public ResponseObject<?> updateProductFormData(@Valid @ModelAttribute UpdateVoucherRequest request, @PathVariable String id)
            throws IOException {
        voucherService.updateVoucher(id, request);
        return new ResponseObject<>();
    }

    @DeleteMapping("/{id}")
    public ResponseObject<?> deleteBrand(@PathVariable String id) throws IOException {
        voucherService.deleteVoucher(id);
        return new ResponseObject<>();
    }
}
