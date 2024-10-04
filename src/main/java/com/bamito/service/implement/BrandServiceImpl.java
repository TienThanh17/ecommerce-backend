package com.bamito.service.implement;

import static com.bamito.util.CommonFunction.createSortOrder;

import com.bamito.dto.request.product.BrandRequest;
import com.bamito.dto.response.product.BrandResponse;
import com.bamito.dto.response.PaginationResponse;
import com.bamito.entity.Brand;
import com.bamito.exception.CustomizedException;
import com.bamito.exception.ErrorCode;
import com.bamito.mapper.IBrandMapper;
import com.bamito.repository.IBrandRepository;
import com.bamito.service.IBrandService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BrandServiceImpl implements IBrandService {
    IBrandRepository brandRepository;
    IBrandMapper brandMapper;

    @Override
    public PaginationResponse<BrandResponse> getAllBrands(int page, int size, String filter, List<String> sort, boolean pagination) {
        if(!pagination) {
            List<Brand> brandList = brandRepository.findAll();
            List<BrandResponse> brandResponseList = brandMapper.toDtoList(brandList);
            return PaginationResponse.<BrandResponse>builder()
                    .content(brandResponseList)
                    .build();
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by(createSortOrder(sort)));
        Page<Brand> brandPage;
        try {
            if (!filter.isBlank()) {
                brandPage = brandRepository.findAllByBrandNameContaining(filter, pageable);
            } else {
                brandPage = brandRepository.findAll(pageable);
            }
        } catch (RuntimeException e) {
            throw new CustomizedException(ErrorCode.INVALID_SORT_PARAMETER);
        }
        List<Brand> brandList = brandPage.getContent();
        List<BrandResponse> brandResponseList = brandMapper.toDtoList(brandList);

        return PaginationResponse.<BrandResponse>builder()
                .content(brandResponseList)
                .page(brandPage.getNumber())
                .size(brandPage.getSize())
                .totalElements(brandPage.getTotalElements())
                .totalPage(brandPage.getTotalPages())
                .build();
    }


    @Override
    public void deleteBrand(int id) {
        if (!brandRepository.existsById(id)) {
            throw new CustomizedException(ErrorCode.BRAND_NOT_EXISTED);
        }
        brandRepository.deleteById(id);
    }

    @Override
    public void updateBrand(int id, BrandRequest request) {
        if (brandRepository.existsByBrandIdAndIdNot(request.getBrandId(), id) ||
                brandRepository.existsByBrandNameAndIdNot(request.getBrandName(), id)
        ) {
            throw new CustomizedException(ErrorCode.BRAND_EXISTED);
        }
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new CustomizedException(ErrorCode.BRAND_NOT_EXISTED));
        brand.setBrandId(request.getBrandId());
        brand.setBrandName(request.getBrandName());
        brandRepository.save(brand);
    }

    @Override
    public void createBrand(BrandRequest request) {
        if (brandRepository.existsByBrandId(request.getBrandId()) ||
                brandRepository.existsByBrandName(request.getBrandName())
        ) {
            throw new CustomizedException(ErrorCode.BRAND_EXISTED);
        }
        Brand brand = Brand.builder()
                .brandId(request.getBrandId())
                .brandName(request.getBrandName())
                .build();
        brandRepository.save(brand);
    }
}
