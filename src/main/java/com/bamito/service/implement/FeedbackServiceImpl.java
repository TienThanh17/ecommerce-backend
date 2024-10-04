package com.bamito.service.implement;

import com.bamito.dto.request.product.FeedbackRequest;
import com.bamito.dto.request.product.UpdateFeedbackReq;
import com.bamito.dto.response.product.FeedbackResponse;
import com.bamito.entity.*;
import com.bamito.exception.CustomizedException;
import com.bamito.exception.ErrorCode;
import com.bamito.repository.*;
import com.bamito.service.IFeedbackService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FeedbackServiceImpl implements IFeedbackService {
    IFeedbackRepository feedbackRepository;
    IProductRepository productRepository;
    IUserRepository userRepository;
    IOrderDetailRepos orderDetailsRepos;

    @Override
    public void createFeedback(FeedbackRequest request) {
        Product product = productRepository.findByProductId(request.getProductId())
                .orElseThrow(() -> new CustomizedException(ErrorCode.PRODUCT_NOT_EXISTED));
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new CustomizedException(ErrorCode.ACCOUNT_NOT_EXISTED));
        Feedback feedback = Feedback.builder()
                .id(new FeedbackKey(user.getId(), product.getId()))
                .product(product)
                .user(user)
                .description(request.getDescription())
                .rating(request.getRating())
                .build();
        OrderDetail orderDetail = orderDetailsRepos
                .findByOrderIdAndProductProductIdAndSizeSizeId(request.getOrderId(), request.getProductId(), request.getSizeId())
                .orElseThrow(() -> new CustomizedException(ErrorCode.ORDER_DETAIL_NOT_EXISTED));
        orderDetail.setFeedbackStatus(1);
        orderDetailsRepos.save(orderDetail);
        // sum / count = rating
        long countFeedback = feedbackRepository.countAllByProductProductId(product.getProductId());
        long sum = product.getRating() * countFeedback;
        long rating = (sum + feedback.getRating()) / (countFeedback + 1);
        product.setRating((int) rating);
        productRepository.save(product);
        feedbackRepository.save(feedback);
    }

    @Override
    public void updateFeedback(UpdateFeedbackReq request) {
        Feedback feedback = feedbackRepository
                .findByUserIdAndProductProductId(request.getUserId(), request.getProductId())
                .orElseThrow(() -> new CustomizedException(ErrorCode.FEEDBACK_NOT_EXISTED));
        feedback.setRating(request.getRating());
        feedback.setDescription(request.getDescription());

        Product product = productRepository.findByProductId(request.getProductId())
                .orElseThrow(() -> new CustomizedException(ErrorCode.PRODUCT_NOT_EXISTED));
        // sum / count = rating
        long countFeedback = feedbackRepository.countAllByProductProductId(product.getProductId());
        long sum = feedbackRepository.sumRating(request.getUserId(), request.getProductId()) == null
                ? 0 : feedbackRepository.sumRating(request.getUserId(), request.getProductId());
        long rating = (sum + feedback.getRating()) / countFeedback;
        product.setRating((int) rating);
        productRepository.save(product);

        feedbackRepository.save(feedback);
    }

    @Override
    public void deleteFeedback(long userId, String productId) {
        Feedback feedback = feedbackRepository
                .findByUserIdAndProductProductId(userId, productId)
                .orElseThrow(() -> new CustomizedException(ErrorCode.FEEDBACK_NOT_EXISTED));

        Product product = productRepository.findByProductId(productId)
                .orElseThrow(() -> new CustomizedException(ErrorCode.PRODUCT_NOT_EXISTED));

        long countFeedback = feedbackRepository.countAllByProductProductId(productId);
        long sum = feedbackRepository.sumRating(userId, productId) == null
                ? 0 : feedbackRepository.sumRating(userId, productId);
        long checkZero = (countFeedback - 1) == 0 ? 1 : (countFeedback - 1);
        long rating = sum / checkZero;
        product.setRating((int) rating);
        productRepository.save(product);
        feedbackRepository.delete(feedback);
    }

    @Override
    public Set<FeedbackResponse> getAllFeedbacksByProduct(String productId) {
        Set<Feedback> feedbacks = feedbackRepository.findAllByProductProductId(productId);

        return feedbacks.stream().map(feedback -> FeedbackResponse.builder()
                        .userId(feedback.getUser().getId())
                        .username(feedback.getUser().getUsername())
                        .email(feedback.getUser().getEmail())
                        .avatarURL(feedback.getUser().getAvatarUrl())
                        .productId(feedback.getProduct().getProductId())
                        .description(feedback.getDescription())
                        .rating(feedback.getRating())
                        .updatedDate(feedback.getLastModified() != null ? feedback.getLastModified().toString() : null)
                        .build())
                .collect(Collectors.toSet());
    }
}
