package com.bamito.service.implement;

import com.bamito.dto.request.product.SizeRequest;
import com.bamito.dto.response.PaginationResponse;
import com.bamito.dto.response.product.SizeResponse;
import com.bamito.entity.Size;
import com.bamito.exception.CustomizedException;
import com.bamito.exception.ErrorCode;
import com.bamito.mapper.ISizeMapper;
import com.bamito.repository.ICategoryRepository;
import com.bamito.repository.ISizeRepository;
import com.bamito.service.ISizeService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.bamito.util.CommonFunction.createSortOrder;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SizeServiceImpl implements ISizeService {
    ISizeRepository sizeRepository;
    ICategoryRepository categoryRepository;
    ISizeMapper sizeMapper;

    @Override
    public void createSize(SizeRequest request) {
        if (sizeRepository.existsBySizeId(request.getSizeId()) ||
                sizeRepository.existsBySizeName(request.getCategoryId(), request.getSizeName())) {
            throw new CustomizedException(ErrorCode.SIZE_EXISTED);
        }
        Size size = Size.builder()
                .sizeId(request.getSizeId())
                .sizeName(request.getSizeName())
                .productCategory(categoryRepository.findByCategoryId(request.getCategoryId())
                        .orElseThrow(() -> new CustomizedException(ErrorCode.CATEGORY_NOT_EXISTED))
                )
                .build();
        sizeRepository.save(size);
    }

    @Override
    public Set<SizeResponse> getAllSizesByCategory(String categoryId) {
        return sizeRepository.findAllByProductCategoryCategoryId(categoryId)
                .stream().map(sizeMapper::toDto).collect(Collectors.toSet());
    }

    @Override
    public PaginationResponse<SizeResponse> getAllSizes(int page, int size, String filter, List<String> sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(createSortOrder(sort)));
        Page<Size> sizePage;
        try {
            if (filter != null && !filter.isBlank()) {
                sizePage = sizeRepository.findAllBySizeNameContaining(filter, pageable);
            } else {
                sizePage = sizeRepository.findAll(pageable);
            }
        } catch (RuntimeException e) {
            throw new CustomizedException(ErrorCode.INVALID_SORT_PARAMETER);
        }
        List<Size> sizeList = sizePage.getContent();
        List<SizeResponse> sizeResponseList = sizeMapper.toDtoList(sizeList);

        return PaginationResponse.<SizeResponse>builder()
                .content(sizeResponseList)
                .page(sizePage.getNumber())
                .size(sizePage.getSize())
                .totalElements(sizePage.getTotalElements())
                .totalPage(sizePage.getTotalPages())
                .build();
    }

    @Override
    public void deleteSize(int id) {
        if (!sizeRepository.existsById(id)) throw new CustomizedException(ErrorCode.SIZE_NOT_EXISTED);
        sizeRepository.deleteById(id);
    }

    @Override
    public void updateSize(int id, SizeRequest request) {
        if (sizeRepository.existsBySizeIdAndIdNot(request.getSizeId(), id) ||
                sizeRepository.existsBySizeNameAndIdNotAndProductCategoryCategoryId(request.getSizeName(), id, request.getCategoryId()
                )) {
            throw new CustomizedException(ErrorCode.SIZE_EXISTED);
        }
        Size size = sizeRepository.findById(id)
                .orElseThrow(() -> new CustomizedException(ErrorCode.SIZE_NOT_EXISTED));
        size.setSizeName(request.getSizeName());
        size.setSizeId(request.getSizeId());
        size.setProductCategory(categoryRepository.findByCategoryId(request.getCategoryId())
                .orElseThrow(() -> new CustomizedException(ErrorCode.CATEGORY_NOT_EXISTED)));
        sizeRepository.save(size);
    }
}
