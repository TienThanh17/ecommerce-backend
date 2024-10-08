package com.bamito.repository;

import com.bamito.entity.Order;
import com.bamito.entity.OrderDetail;
import com.bamito.entity.OrderDetailKey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

public interface IOrderDetailRepos extends JpaRepository<OrderDetail, OrderDetailKey> {
    @Query("select od from OrderDetail od where od.order.id in ?1 and od.createDate > ?2 and od.createDate < ?3")
    Page<OrderDetail> findAllByOrderIdsAndTime(Set<String> orderIds, LocalDateTime startTime, LocalDateTime endTime, Pageable pageable);

    Set<OrderDetail> findAllByOrderId(String orderId);

    Set<OrderDetail> findAllByOrderIdAndFeedbackStatus(String orderId, int feedbackStatus);

    Optional<OrderDetail> findByOrderIdAndProductProductIdAndSizeSizeId(String orderId, String productId, String sizeId);
}
