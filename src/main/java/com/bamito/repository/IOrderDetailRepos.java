package com.bamito.repository;

import com.bamito.entity.OrderDetail;
import com.bamito.entity.OrderDetailKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface IOrderDetailRepos extends JpaRepository<OrderDetail, OrderDetailKey> {
    Set<OrderDetail> findAllByOrderId(String orderId);

    Set<OrderDetail> findAllByOrderIdAndFeedbackStatus(String orderId, int feedbackStatus);

    Optional<OrderDetail> findByOrderIdAndProductProductIdAndSizeSizeId(String orderId, String productId, String sizeId);
}
