package com.bamito.service;

import com.bamito.dto.request.user.CancelOrderRequest;
import com.bamito.dto.request.user.OrderRequest;
import com.bamito.dto.response.PaginationResponse;
import com.bamito.dto.response.product.ReportProductRes;
import com.bamito.dto.response.product.StatisticResponse;
import com.bamito.dto.response.user.OrderDetailResponse;
import com.bamito.dto.response.user.OrderResponse;
import com.bamito.dto.response.user.SetProductOrder;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IOrderService {
    Map<String, String> createOrder(OrderRequest request);
    PaginationResponse<OrderResponse> getAllOrdersByUser(int page, int size, List<String> sort, long userId, int status);
    PaginationResponse<OrderResponse> getAllOrdersByStatus(int page, int size, List<String> sort, int status);
    OrderDetailResponse getOrderDetail(String orderId);
    void cancelOrder(CancelOrderRequest request);
    void deleteOrder(String orderId);
    void deliverOrder(String orderId);
    void successOrder(String orderId);
    Set<SetProductOrder> getAllProductFeedback(long userId);
    StatisticResponse getStatistic();
    PaginationResponse<ReportProductRes> getAllProductReport(int page, int size, String startDate, String endDate);
}
