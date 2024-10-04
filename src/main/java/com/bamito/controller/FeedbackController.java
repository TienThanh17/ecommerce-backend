package com.bamito.controller;

import com.bamito.dto.request.product.FeedbackRequest;
import com.bamito.dto.request.product.UpdateFeedbackReq;
import com.bamito.dto.response.ResponseObject;
import com.bamito.dto.response.product.FeedbackResponse;
import com.bamito.service.IFeedbackService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/feedback")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class FeedbackController {
    IFeedbackService feedbackService;

    @PostMapping
    public ResponseObject<?> createFeedback (@RequestBody FeedbackRequest request) {
        feedbackService.createFeedback(request);
        return ResponseObject.builder().build();
    }

    @GetMapping
    public ResponseObject<Set<FeedbackResponse>> getAllFeedbacksByProduct (@RequestParam String productId) {
        var result =  feedbackService.getAllFeedbacksByProduct(productId);

        return ResponseObject.<Set<FeedbackResponse>>builder()
                .data(result)
                .build();
    }

    @PatchMapping
    public ResponseObject<?> updateFeedback (@RequestBody UpdateFeedbackReq request) {
        feedbackService.updateFeedback(request);
        return ResponseObject.builder().build();
    }

    @DeleteMapping
    public ResponseObject<?> deleteFeedback (@RequestParam long userId, @RequestParam String productId) {
        feedbackService.deleteFeedback(userId, productId);
        return ResponseObject.builder().build();
    }
}
