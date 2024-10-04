package com.bamito.repository;

import com.bamito.entity.Brand;
import com.bamito.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface IProductRepository extends JpaRepository<Product, Integer> {
    Page<Product> findAll(Pageable pageable);

    Page<Product> findAllByProductNameContaining(String name, Pageable pageable);

    Page<Product> findAllByProductCategoryCategoryId(String categoryId, Pageable pageable);

    Page<Product> findAllByBrandBrandIdInAndPriceBetweenAndProductCategoryCategoryId(Collection<String> brand_brandId, long price, long price2, String productCategory_categoryId, Pageable pageable);

    Page<Product> findAllByPriceBetweenAndProductCategoryCategoryId(long price, long price2, String productCategory_categoryId, Pageable pageable);

    @Query("select p from Product p where p.discount > 0")
    Page<Product> findAllSaleProducts(Pageable pageable);

    @Query("select p from Product p join p.cartDetails c where c.id.productId = p.id and c.cart.id = ?1")
    Set<Product> findAllByCart(String cartId);

    @Query("SELECT p FROM User u JOIN u.favoriteProducts p WHERE u.id = ?1 AND p.productName LIKE %?2%")
    Page<Product> findAllFavoriteByUserByProductName(long userId, String productName, Pageable pageable);

    @Query("SELECT p FROM User u JOIN u.favoriteProducts p WHERE u.id = ?1")
    Page<Product> findAllFavoriteByUser(long userId, Pageable pageable);

    Optional<Product> findByProductId(String productId);

    Boolean existsByProductIdAndIdNot(String productId, int id);

    Boolean existsByProductNameAndIdNot(String productName, int id);

    Boolean existsByProductId(String id);

    Boolean existsByProductName(String name);
}