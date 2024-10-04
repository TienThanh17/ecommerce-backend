package com.bamito.repository;

import com.bamito.entity.ProductCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ICategoryRepository extends JpaRepository<ProductCategory, Integer> {
    Page<ProductCategory> findAll(Pageable pageable);
    Page<ProductCategory> findAllByCategoryNameContaining(String categoryName, Pageable pageable);

    Optional<ProductCategory> findByCategoryId(String categoryId);
    Boolean existsByCategoryId(String categoryId);
    Boolean existsByCategoryName(String categoryName);

    Boolean existsByCategoryNameAndIdNot(String categoryName, int id);
    Boolean existsByCategoryIdAndIdNot(String categoryId, int id);
}
