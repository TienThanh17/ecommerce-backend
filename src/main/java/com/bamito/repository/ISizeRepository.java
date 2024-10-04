package com.bamito.repository;

import com.bamito.entity.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface ISizeRepository extends JpaRepository<Size, Integer> {
    Page<Size> findAll(Pageable pageable);

    Page<Size> findAllBySizeNameContaining(String sizeName, Pageable pageable);

    Optional<Size> findBySizeId(String sizeId);

    Set<Size> findAllByProductCategoryCategoryId(String categoryId);

    @Query("select s from Size s join s.cartDetails c where c.id.sizeId = s.id and c.cart.id = ?1")
    Set<Size> findAllByCart(String cartId);

    Boolean existsBySizeId(String sizeId);

    @Query("select count(s) > 0 from Size s where s.productCategory.categoryId = ?1 and s.sizeName = ?2")
    Boolean existsBySizeName(String categoryId, String name);

    Boolean existsBySizeIdAndIdNot(String sizeId, int id);

    Boolean existsBySizeNameAndIdNotAndProductCategoryCategoryId(String sizeName, int id, String categoryId);
}
