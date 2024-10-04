package com.bamito.service;

import com.bamito.dto.request.product.SizeRequest;
import com.bamito.dto.response.PaginationResponse;
import com.bamito.dto.response.product.SizeResponse;

import java.util.List;
import java.util.Set;

public interface ISizeService {
    void createSize(SizeRequest request);

    PaginationResponse<SizeResponse> getAllSizes(int page, int size, String filter, List<String> sort);

    Set<SizeResponse> getAllSizesByCategory(String category);

    void deleteSize(int id);

    void updateSize(int id, SizeRequest request);
}
