package com.bamito.service.implement;

import com.bamito.dto.request.product.ProductRequest;
import com.bamito.dto.request.product.UpdateProductRequest;
import com.bamito.dto.response.product.ProductResponse;
import com.bamito.dto.response.PaginationResponse;
import com.bamito.dto.response.product.ProductSizeResponse;
import com.bamito.dto.response.product.SizeByProductResponse;
import com.bamito.entity.Product;
import com.bamito.entity.ProductSize;
import com.bamito.exception.CustomizedException;
import com.bamito.exception.ErrorCode;
import com.bamito.mapper.IProductMapper;
import com.bamito.mapper.IProductSizeMapper;
import com.bamito.repository.IBrandRepository;
import com.bamito.repository.IProductRepository;
import com.bamito.repository.ICategoryRepository;
import com.bamito.repository.IProductSizeRepos;
import com.bamito.service.IProductService;
import com.bamito.service.IProductSizeService;
import com.cloudinary.Cloudinary;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static com.bamito.util.CommonFunction.createSortOrder;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductServiceImpl implements IProductService {
    IProductRepository productRepository;
    IBrandRepository brandRepository;
    ICategoryRepository categoryRepository;
    Cloudinary cloudinary;
    IProductMapper productMapper;
    IProductSizeRepos productSizeRepos;

    @Override
    public void createProduct(ProductRequest request) throws IOException {
        if (productRepository.existsByProductId(request.getProductId()) ||
                productRepository.existsByProductName(request.getProductName())
        ) throw new CustomizedException(ErrorCode.PRODUCT_EXISTED);
        Map cloudinaryObject = cloudinary.uploader()
                .upload(request.getImage().getBytes(), Map.of("folder", "bamito"));
        var imageUrl = cloudinaryObject.get("secure_url");
        var imageId = cloudinaryObject.get("public_id");
        Product product = Product.builder()
                .productId(request.getProductId())
                .productName(request.getProductName())
                .price(request.getPrice())
                .imageURL(imageUrl.toString())
                .imageId(imageId.toString())
                .descriptionContent(request.getDescriptionContent())
                .descriptionHTML(request.getDescriptionHTML())
                .productCategory(categoryRepository.findByCategoryId(request.getCategoryId())
                        .orElseThrow(() -> new CustomizedException(ErrorCode.CATEGORY_NOT_EXISTED)))
                .brand(brandRepository.findByBrandId(request.getBrandId())
                        .orElseThrow(() -> new CustomizedException(ErrorCode.BRAND_NOT_EXISTED)))
                .build();
        productRepository.save(product);
    }

    @Override
    public ProductResponse getProduct(String productId) {
        Product product = productRepository.findByProductId(productId)
                .orElseThrow(() -> new CustomizedException(ErrorCode.PRODUCT_NOT_EXISTED));
        Set<ProductSize> sizeSet = productSizeRepos.findByProductId(productId);

        Set<SizeByProductResponse> sizeResponses = sizeSet.stream().map(s -> {
            return SizeByProductResponse.builder()
                    .id(s.getSize().getId())
                    .sizeId(s.getSize().getSizeId())
                    .sizeName(s.getSize().getSizeName())
                    .quantity(s.getQuantity())
                    .sold(s.getSold())
                    .build();
        }).collect(Collectors.toSet());
        ProductResponse productResponse = productMapper.toDto(product);
        productResponse.setSizeResponse(sizeResponses);

        return productResponse;
    }

    @Override
    public PaginationResponse<ProductResponse> getAllProducts(int page, int size, String filter, List<String> sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(createSortOrder(sort)));
        Page<Product> ProductPage;
        try {
            if (filter != null && !filter.isBlank() && !Objects.equals(filter, "null")) {
                ProductPage = productRepository.findAllByProductNameContaining(filter, pageable);
            } else {
                ProductPage = productRepository.findAll(pageable);
            }
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
        List<Product> productList = ProductPage.getContent();
        List<ProductResponse> productResponseList = productMapper.toDtoList(productList);

        return PaginationResponse.<ProductResponse>builder()
                .content(productResponseList)
                .page(ProductPage.getNumber())
                .size(ProductPage.getSize())
                .totalElements(ProductPage.getTotalElements())
                .totalPage(ProductPage.getTotalPages())
                .build();
    }

    @Override
    public PaginationResponse<ProductResponse> getProductsByCategory(int page, int size, Map<String, Object> filter, List<String> sort, String categoryId) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(createSortOrder(sort)));
        Page<Product> productPage;
        try {
            if (!filter.isEmpty()) {
                List<Integer> priceRange = (List<Integer>) filter.get("price");
                List<String> brandIds = (List<String>) filter.get("brandId");
                if (brandIds.isEmpty()) {
                    productPage = productRepository.findAllByPriceBetweenAndProductCategoryCategoryId(priceRange.getFirst(), priceRange.getLast(), categoryId, pageable);
                } else {
                    productPage = productRepository.findAllByBrandBrandIdInAndPriceBetweenAndProductCategoryCategoryId(brandIds, priceRange.getFirst(), priceRange.getLast(), categoryId, pageable);
                }
            } else {
                productPage = productRepository.findAllByProductCategoryCategoryId(categoryId, pageable);
            }
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
        List<Product> productList = productPage.getContent();
        List<ProductResponse> productResponseList = productMapper.toDtoList(productList);

        return PaginationResponse.<ProductResponse>builder()
                .content(productResponseList)
                .page(productPage.getNumber())
                .size(productPage.getSize())
                .totalElements(productPage.getTotalElements())
                .totalPage(productPage.getTotalPages())
                .build();
    }

    @Override
    public PaginationResponse<ProductResponse> getProductsByUserFavorite(int page, int size, String filter, List<String> sort, long userId) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(createSortOrder(sort)));
        Page<Product> ProductPage;
        try {
            if (!filter.isBlank() && !Objects.equals(filter, "null")) {
                ProductPage = productRepository.findAllFavoriteByUserByProductName(userId, filter, pageable);
            } else {
                ProductPage = productRepository.findAllFavoriteByUser(userId, pageable);
            }
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
        List<Product> productList = ProductPage.getContent();
        List<ProductResponse> productResponseList = productMapper.toDtoList(productList);

        return PaginationResponse.<ProductResponse>builder()
                .content(productResponseList)
                .page(ProductPage.getNumber())
                .size(ProductPage.getSize())
                .totalElements(ProductPage.getTotalElements())
                .totalPage(ProductPage.getTotalPages())
                .build();
    }

    @Override
    public PaginationResponse<ProductResponse> getSaleProducts(int page, int size, List<String> sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(createSortOrder(sort)));
        Page<Product> ProductPage = productRepository.findAllSaleProducts(pageable);
        List<Product> productList = ProductPage.getContent();
        List<ProductResponse> productResponseList = productMapper.toDtoList(productList);

        return PaginationResponse.<ProductResponse>builder()
                .content(productResponseList)
                .page(ProductPage.getNumber())
                .size(ProductPage.getSize())
                .totalElements(ProductPage.getTotalElements())
                .totalPage(ProductPage.getTotalPages())
                .build();
    }

    @Override
    public void deleteProduct(int id) throws IOException {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new CustomizedException(ErrorCode.PRODUCT_NOT_EXISTED));
        if (product.getImageId() != null) {
            cloudinary.uploader().destroy(product.getImageId(), Map.of());
        }
        productRepository.deleteById(id);
    }

    @Override
    public void updateProduct(int id, UpdateProductRequest request) throws IOException {
        if (productRepository.existsByProductIdAndIdNot(request.getProductId(), id) ||
                productRepository.existsByProductNameAndIdNot(request.getProductName(), id)
        ) throw new CustomizedException(ErrorCode.PRODUCT_EXISTED);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new CustomizedException(ErrorCode.PRODUCT_NOT_EXISTED));

        if (request.getImage() != null && !request.getImage().isEmpty()) {
            cloudinary.uploader().destroy(product.getImageId(), Map.of());
            Map cloudinaryObject = cloudinary.uploader()
                    .upload(request.getImage().getBytes(), Map.of("folder", "bamito"));
            var imageUrl = cloudinaryObject.get("secure_url");
            var imageId = cloudinaryObject.get("public_id");
            product.setImageURL(imageUrl.toString());
            product.setImageId(imageId.toString());
        }
        if (request.getProductId() != null && !request.getProductId().isBlank()) {
            product.setProductId(request.getProductId());
        }
        if (request.getProductName() != null && !request.getProductName().isBlank()) {
            product.setProductName(request.getProductName());
        }
        if (request.getDescriptionContent() != null && !request.getDescriptionContent().isBlank()) {
            product.setDescriptionContent(request.getDescriptionContent());
        }
        if (request.getDescriptionHTML() != null && !request.getDescriptionHTML().isBlank()) {
            product.setDescriptionHTML(request.getDescriptionHTML());
        }
        if (Objects.nonNull(request.getPrice())) {
            product.setPrice(request.getPrice());
        }
        if (Objects.nonNull(request.getDiscount())) {
            product.setDiscount(request.getDiscount());
        }
        if (request.getCategoryId() != null && !request.getCategoryId().isBlank()) {
            product.setProductCategory(categoryRepository.findByCategoryId(request.getCategoryId())
                    .orElseThrow(() -> new CustomizedException(ErrorCode.CATEGORY_NOT_EXISTED)));
        }
        if (request.getBrandId() != null && !request.getBrandId().isBlank()) {
            product.setBrand(brandRepository.findByBrandId(request.getBrandId())
                    .orElseThrow(() -> new CustomizedException(ErrorCode.BRAND_NOT_EXISTED)));
        }
        productRepository.save(product);
    }
}
