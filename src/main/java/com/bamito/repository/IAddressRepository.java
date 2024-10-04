package com.bamito.repository;

import com.bamito.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface IAddressRepository extends JpaRepository<Address, Long> {
    Address findByUserId(Long userId);
    @Query("select a.address from Address a where a.user.id = ?1")
    Set<String> findAllByUserId(Long userId);
}
