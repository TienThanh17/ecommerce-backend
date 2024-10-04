package com.bamito.service.implement;

import com.bamito.dto.request.product.ProductSizeRequest;
import com.bamito.dto.response.PaginationResponse;
import com.bamito.dto.response.product.ProductSizeResponse;
import com.bamito.entity.ProductSize;
import com.bamito.entity.ProductSizeKey;
import com.bamito.exception.CustomizedException;
import com.bamito.exception.ErrorCode;
import com.bamito.mapper.IProductSizeMapper;
import com.bamito.repository.IProductRepository;
import com.bamito.repository.IProductSizeRepos;
import com.bamito.repository.ISizeRepository;
import com.bamito.service.IProductSizeService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
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
public class ProdSizeServiceImpl implements IProductSizeService {
    IProductSizeRepos productSizeRepos;
    IProductRepository productRepository;
    ISizeRepository sizeRepository;
    IProductSizeMapper productSizeMapper;

    @Override
    public void createProductSize(ProductSizeRequest request) {
        ProductSizeKey id = new ProductSizeKey(request.getProductId(), request.getSizeId());
        if (productSizeRepos.existsById(id))
            throw new CustomizedException(ErrorCode.PRODUCT_SIZE_EXISTED);
        ProductSize productSize = ProductSize.builder()
                .id(id)
                .product(productRepository.findById(request.getProductId())
                        .orElseThrow(() -> new CustomizedException(ErrorCode.PRODUCT_NOT_EXISTED)))
                .size(sizeRepository.findById(request.getSizeId())
                        .orElseThrow(() -> new CustomizedException(ErrorCode.SIZE_NOT_EXISTED)))
                .quantity(request.getQuantity())
                .sold(0)
                .build();
        productSizeRepos.save(productSize);
    }

    @Override
    public PaginationResponse<ProductSizeResponse> getAllProductSizes(int page, int size, List<String> sort, String productId) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(createSortOrder(sort)));
        Page<ProductSize> productSizePage = productSizeRepos.findByProductId(productId, pageable);

        List<ProductSize> productSizeList = productSizePage.getContent();
        List<ProductSizeResponse> productSizeResponseList = productSizeMapper.toDtoList(productSizeList);

        return PaginationResponse.<ProductSizeResponse>builder()
                .content(productSizeResponseList)
                .page(productSizePage.getNumber())
                .size(productSizePage.getSize())
                .totalElements(productSizePage.getTotalElements())
                .totalPage(productSizePage.getTotalPages())
                .build();
    }

    @Override
    public void deleteProductSize(int sizeId, int productId) {
        ProductSizeKey id = new ProductSizeKey(productId, sizeId);
        if (!productSizeRepos.existsById(id))
            throw new CustomizedException(ErrorCode.PRODUCT_SIZE_NOT_EXISTED);
        productSizeRepos.deleteById(id);
    }

    @Override
    public void updateProductSize(ProductSizeRequest request) {
        ProductSizeKey id = new ProductSizeKey(request.getProductId(), request.getSizeId());
        ProductSize productSize = productSizeRepos.findById(id)
                .orElseThrow(() -> new CustomizedException(ErrorCode.PRODUCT_SIZE_NOT_EXISTED));
        productSize.setQuantity(request.getQuantity());
        productSizeRepos.save(productSize);
    }
}
