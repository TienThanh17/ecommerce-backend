package com.bamito.repository;

import com.bamito.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface IOrderRepository extends JpaRepository<Order, String> {
    Page<Order> findAllByUserIdAndStatus(long userId, int status, Pageable pageable);
    Page<Order> findAllByStatus(int status, Pageable pageable);
    Set<Order> findAllByUserId(long userId);
    @Query("select o.id from Order o where o.status = ?1")
    Set<String> findAllOrderIdByStatus(int status);
    int countByStatus(int status);
}
