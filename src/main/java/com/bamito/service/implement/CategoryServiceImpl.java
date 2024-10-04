package com.bamito.service.implement;

import com.bamito.dto.request.product.CategoryRequest;
import com.bamito.dto.response.product.CategoryResponse;
import com.bamito.dto.response.PaginationResponse;
import com.bamito.entity.ProductCategory;
import com.bamito.exception.CustomizedException;
import com.bamito.exception.ErrorCode;
import com.bamito.mapper.ICategoryMapper;
import com.bamito.repository.ICategoryRepository;
import com.bamito.service.ICategoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.bamito.util.CommonFunction.createSortOrder;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryServiceImpl implements ICategoryService {
    ICategoryRepository categoryRepository;
    ICategoryMapper categoryMapper;

    @Override
    public void createCategory(CategoryRequest request) {
        if (categoryRepository.existsByCategoryId(request.getCategoryId()) ||
                categoryRepository.existsByCategoryName(request.getCategoryName())) {
            throw new CustomizedException(ErrorCode.CATEGORY_EXISTED);
        }
        ProductCategory productCategory = ProductCategory.builder()
                .categoryId(request.getCategoryId())
                .categoryName(request.getCategoryName())
                .build();
        categoryRepository.save(productCategory);
    }

    @Override
    public PaginationResponse<CategoryResponse> getAllCategories(Integer page, Integer size, String filter, List<String> sort, Boolean pagination) {
        if (!pagination) {
            List<ProductCategory> categoryList = categoryRepository.findAll();
            List<CategoryResponse> brandResponseList = categoryMapper.toDtoList(categoryList);
            return PaginationResponse.<CategoryResponse>builder()
                    .content(brandResponseList)
                    .build();
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by(createSortOrder(sort)));
        Page<ProductCategory> productCategories;
        try {
            if (filter != null && !filter.isBlank()) {
                productCategories = categoryRepository.findAllByCategoryNameContaining(filter, pageable);
            } else {
                productCategories = categoryRepository.findAll(pageable);
            }
        } catch (RuntimeException e) {
            throw new CustomizedException(ErrorCode.INVALID_SORT_PARAMETER);
        }
        List<ProductCategory> categoryList = productCategories.getContent();
        List<CategoryResponse> categoryResponseList = categoryMapper.toDtoList(categoryList);

        return PaginationResponse.<CategoryResponse>builder()
                .content(categoryResponseList)
                .page(productCategories.getNumber())
                .size(productCategories.getSize())
                .totalElements(productCategories.getTotalElements())
                .totalPage(productCategories.getTotalPages())
                .build();
    }

    @Override
    public void deleteCategory(int id) {
        if (!categoryRepository.existsById(id)) {
            throw new CustomizedException(ErrorCode.CATEGORY_NOT_EXISTED);
        }
        categoryRepository.deleteById(id);
    }

    @Override
    public void updateCategory(int id, CategoryRequest request) {
        if (categoryRepository.existsByCategoryIdAndIdNot(request.getCategoryId(), id) ||
                categoryRepository.existsByCategoryNameAndIdNot(request.getCategoryName(), id)
        ) {
            throw new CustomizedException(ErrorCode.CATEGORY_EXISTED);
        }
        ProductCategory productCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new CustomizedException(ErrorCode.CATEGORY_NOT_EXISTED));
        productCategory.setCategoryId(request.getCategoryId());
        productCategory.setCategoryName(request.getCategoryName());
        categoryRepository.save(productCategory);
    }
}
