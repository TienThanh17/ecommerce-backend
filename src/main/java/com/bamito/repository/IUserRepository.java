package com.bamito.repository;

import com.bamito.entity.Product;
import com.bamito.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

//@NonNullApi
@Repository
public interface IUserRepository extends JpaRepository<User, Long> {
    Page<User> findAll(Pageable pageable);

    @Query("select count(u) > 0 from User u where u.phoneNumber = ?1 and u.id != ?2")
    Boolean existsByPhoneNumber(String phoneNumber, long id);

    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);

    Boolean existsByPhoneNumber(String phoneNumber);

    Boolean existsByPhoneNumberAndIdNot(String phoneNumber, long id);

    Boolean existsByEmailAndIdNot(String email, long id);

    @Query("select u.favoriteProducts from User u where u.id = ?1")
    Set<Product> findAllFavorites(long id);
}
