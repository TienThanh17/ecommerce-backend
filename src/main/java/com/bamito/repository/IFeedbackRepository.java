package com.bamito.repository;

import com.bamito.entity.Feedback;
import com.bamito.entity.FeedbackKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.Set;

public interface IFeedbackRepository extends JpaRepository<Feedback, FeedbackKey> {
    Set<Feedback> findAllByProductProductId(String productId);
    Optional<Feedback> findByUserIdAndProductProductId(long userId, String productId);
    int countAllByProductProductId(String productId);
    @Query("select sum(f.rating) from Feedback f where f.user.id != ?1 and f.product.productId = ?2")
    Integer sumRating(long userId, String productId);
}