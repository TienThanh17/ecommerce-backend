package com.bamito.service;

import com.bamito.dto.request.product.UpdateVoucherRequest;
import com.bamito.dto.request.product.VoucherRequest;
import com.bamito.dto.response.PaginationResponse;
import com.bamito.dto.response.product.VoucherResponse;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface IVoucherService {
    void createVoucher(VoucherRequest request) throws IOException;

    PaginationResponse<VoucherResponse> getAllVouchers(int page, int size, String filter, List<String> sort, boolean pagination);

    Set<VoucherResponse> getAllUserVouchers();

    void deleteVoucher(String id) throws IOException;

    void updateVoucher(String id, UpdateVoucherRequest request) throws IOException;
}
