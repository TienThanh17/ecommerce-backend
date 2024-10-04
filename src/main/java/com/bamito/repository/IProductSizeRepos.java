package com.bamito.repository;

import com.bamito.entity.ProductSize;
import com.bamito.entity.ProductSizeKey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface IProductSizeRepos extends JpaRepository<ProductSize, ProductSizeKey> {
    @Query("SELECT ps FROM ProductSize ps WHERE ps.product.productId = :productId")
    Page<ProductSize> findByProductId(@Param("productId") String productId, Pageable pageable);

    @Query("SELECT ps FROM ProductSize ps WHERE ps.product.productId = :productId")
    Set<ProductSize> findByProductId(@Param("productId") String productId);

    ProductSize findByProductProductIdAndSizeSizeId(String productId, String sizeId);

    @Query("SELECT ps.quantity FROM ProductSize ps WHERE ps.size.id = ?1")
    Integer findQuantityBySizeId(int sizeId);
}