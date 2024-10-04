package com.bamito.repository;

import com.bamito.entity.Brand;
import io.micrometer.common.lang.NonNullApi;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IBrandRepository extends JpaRepository<Brand, Integer> {
    Page<Brand> findAll(Pageable pageable);
    Page<Brand> findAllByBrandNameContaining(String brandName, Pageable pageable);

    Optional<Brand> findByBrandId(String brandId);
    Boolean existsByBrandName(String name);
    Boolean existsByBrandId(String brandId);

    Boolean existsByBrandNameAndIdNot(String name, int id);
    Boolean existsByBrandIdAndIdNot(String brandId, int id);
}