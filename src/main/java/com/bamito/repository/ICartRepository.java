package com.bamito.repository;

import com.bamito.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ICartRepository extends JpaRepository<Cart, String> {
    @Query("select c.id from Cart c where c.user.id = ?1")
    String findCartIdByUserId(long userId);

    Optional<Cart> findByUserId(long userId);
}
