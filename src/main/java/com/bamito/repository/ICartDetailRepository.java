package com.bamito.repository;

import com.bamito.entity.CartDetail;
import com.bamito.entity.CartDetailKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.Set;

public interface ICartDetailRepository extends JpaRepository<CartDetail, CartDetailKey> {
    Integer countByCart_Id(String cartId);

    @Query("select cd from CartDetail cd where cd.cart.id = ?1 and cd.product.productId = ?2 and cd.size.sizeId = ?3")
    Optional<CartDetail> findByStringId(String cartId, String productId, String sizeId);

    @Query("select cd from CartDetail cd where cd.id.cartId = ?1")
    Set<CartDetail> findAllByCartId(String cartId);
}
