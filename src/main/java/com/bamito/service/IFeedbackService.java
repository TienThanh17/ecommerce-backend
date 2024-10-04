package com.bamito.service;

import com.bamito.dto.request.product.FeedbackRequest;
import com.bamito.dto.request.product.UpdateFeedbackReq;
import com.bamito.dto.response.product.FeedbackResponse;
import com.bamito.entity.Feedback;

import java.util.Set;

public interface IFeedbackService {
    Set<FeedbackResponse> getAllFeedbacksByProduct(String productId);
    void createFeedback(FeedbackRequest request);
    void updateFeedback(UpdateFeedbackReq request);
    void deleteFeedback(long userId, String productId);
}
