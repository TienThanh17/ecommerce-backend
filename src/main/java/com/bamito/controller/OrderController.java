package com.bamito.controller;

import com.bamito.dto.request.user.CancelOrderRequest;
import com.bamito.dto.request.user.OrderRequest;
import com.bamito.dto.response.PaginationResponse;
import com.bamito.dto.response.ResponseObject;
import com.bamito.dto.response.product.ReportProductRes;
import com.bamito.dto.response.product.StatisticResponse;
import com.bamito.dto.response.user.OrderDetailResponse;
import com.bamito.dto.response.user.OrderResponse;
import com.bamito.dto.response.user.SetProductOrder;
import com.bamito.service.IOrderService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/order")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class OrderController {
    IOrderService orderService;

    @PostMapping
    public ResponseObject<Map<String, String>> createOrder(@Valid @RequestBody OrderRequest request) {
        var result = orderService.createOrder(request);
        return ResponseObject.<Map<String, String>>builder()
                .data(result)
                .build();
    }

    @GetMapping("get-product-feedback")
    public ResponseObject<Set<SetProductOrder>> getAllProductFeedback(@RequestParam long userId) {
        var result = orderService.getAllProductFeedback(userId);

        return ResponseObject.<Set<SetProductOrder>>builder()
                .data(result)
                .build();
    }

    @GetMapping("get-by-user")
    public ResponseObject<PaginationResponse<OrderResponse>> getAllByUser
            (
                    @RequestParam(value = "page", defaultValue = "0", required = false) int page,
                    @RequestParam(value = "size", defaultValue = "10", required = false) int size,
                    @RequestParam(value = "sort", defaultValue = "id, desc", required = false) List<String> sort,
                    @RequestParam(value = "userId") long userId,
                    @RequestParam(value = "status") int status
            ) {
        var result = orderService.getAllOrdersByUser(page, size, sort, userId, status);

        return ResponseObject.<PaginationResponse<OrderResponse>>builder()
                .data(result)
                .build();
    }

    @GetMapping("get-by-status")
    public ResponseObject<PaginationResponse<OrderResponse>> getAllByStatus
            (
                    @RequestParam(value = "page", defaultValue = "0", required = false) int page,
                    @RequestParam(value = "size", defaultValue = "10", required = false) int size,
                    @RequestParam(value = "sort", defaultValue = "id, desc", required = false) List<String> sort,
                    @RequestParam(value = "status") int status
            ) {
        var result = orderService.getAllOrdersByStatus(page, size, sort, status);

        return ResponseObject.<PaginationResponse<OrderResponse>>builder()
                .data(result)
                .build();
    }

    @GetMapping("get-product-report")
    public ResponseObject<PaginationResponse<ReportProductRes>> getAllProductReport
            (
                    @RequestParam(value = "page", defaultValue = "0", required = false) int page,
                    @RequestParam(value = "size", defaultValue = "10", required = false) int size,
                    @RequestParam(value = "startTime") String startTime,
                    @RequestParam(value = "endTime") String endTime
            ) {
        var result = orderService.getAllProductReport(page, size, startTime, endTime);

        return ResponseObject.<PaginationResponse<ReportProductRes>>builder()
                .data(result)
                .build();
    }

    @GetMapping("get-detail")
    public ResponseObject<OrderDetailResponse> getOrderDetail(@RequestParam String orderId) {
        var result = orderService.getOrderDetail(orderId);

        return ResponseObject.<OrderDetailResponse>builder()
                .data(result)
                .build();
    }

    @PatchMapping("cancel")
    public ResponseObject<?> cancelOrder(@Valid @RequestBody CancelOrderRequest request) {
        orderService.cancelOrder(request);

        return ResponseObject.builder().build();
    }

    @DeleteMapping("delete")
    public ResponseObject<?> deleteOrder(@RequestParam String orderId) {
        orderService.deleteOrder(orderId);

        return ResponseObject.builder().build();
    }

    @PatchMapping("deliver")
    public ResponseObject<?> deliverOrder(@RequestParam String orderId) {
        orderService.deliverOrder(orderId);

        return ResponseObject.builder().build();
    }

    @PatchMapping("success")
    public ResponseObject<?> successOrder(@RequestParam String orderId) {
        orderService.successOrder(orderId);

        return ResponseObject.builder().build();
    }

    @GetMapping("get-statistic")
    public ResponseObject<StatisticResponse> getStatistic() {
        var result = orderService.getStatistic();

        return ResponseObject.<StatisticResponse>builder()
                .data(result)
                .build();
    }
}
