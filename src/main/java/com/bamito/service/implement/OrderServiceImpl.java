package com.bamito.service.implement;

import com.bamito.dto.request.user.CancelOrderRequest;
import com.bamito.dto.request.user.OrderRequest;
import com.bamito.dto.response.PaginationResponse;
import com.bamito.dto.response.product.ReportProductRes;
import com.bamito.dto.response.product.StatisticResponse;
import com.bamito.dto.response.user.OrderDetailResponse;
import com.bamito.dto.response.user.OrderResponse;
import com.bamito.dto.response.user.OrderStatisticRes;
import com.bamito.dto.response.user.SetProductOrder;
import com.bamito.entity.*;
import com.bamito.exception.CustomizedException;
import com.bamito.exception.ErrorCode;
import com.bamito.mapper.IOrderMapper;
import com.bamito.repository.*;
import com.bamito.service.IFeedbackService;
import com.bamito.service.IOrderService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

import static com.bamito.util.CommonFunction.createSortOrder;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderServiceImpl implements IOrderService {
    IOrderRepository orderRepository;
    IOrderDetailRepos orderDetailsRepos;
    IUserRepository userRepository;
    IVoucherRepository voucherRepository;
    IPaymentRepository paymentRepository;
    ICartDetailRepository cartDetailRepository;
    IProductSizeRepos productSizeRepository;
    IFeedbackRepository feedbackRepository;
    IOrderMapper orderMapper;

    @Override
    public Set<SetProductOrder> getAllProductFeedback(long userId) {
        Set<Order> orders = orderRepository.findAllByUserId(userId);

        return orders.parallelStream()
                .filter(order -> order.getStatus() == 3)
                .flatMap(order -> {
                    Set<OrderDetail> orderDetails = orderDetailsRepos
                            .findAllByOrderIdAndFeedbackStatus(order.getId(), 0);

                    return orderDetails.stream().map(o -> SetProductOrder.builder()
                            .productId(o.getProduct().getProductId())
                            .productName(o.getProduct().getProductName())
                            .sizeId(o.getSize().getSizeId())
                            .sizeName(o.getSize().getSizeName())
                            .orderId(o.getOrder().getId())
                            .imageURL(o.getProduct().getImageURL())
                            .price(o.getProduct().getPrice())
                            .discount(o.getProduct().getDiscount())
                            .quantity(o.getQuantity())
                            .totalPrice(o.getTotalPrice())
                            .build());
                }).collect(Collectors.toSet());
    }

    @Override
    public StatisticResponse getStatistic() {
        Set<String> orderIds = orderRepository.findAllOrderIdByStatus(3);
        Set<OrderStatisticRes> orderStatisticResSet = orderIds.parallelStream()
                .flatMap(order -> {
                    Set<OrderDetail> orderDetail = orderDetailsRepos.findAllByOrderId(order);
                    return orderDetail.stream().map(o -> OrderStatisticRes.builder()
                            .orderId(o.getOrder().getId())
                            .productId(o.getProduct().getProductId())
                            .sizeId(o.getSize().getSizeId())
                            .quantity(o.getQuantity())
                            .totalPrice(o.getTotalPrice())
                            .createAt(o.getCreateDate())
                            .build());
                }).collect(Collectors.toSet());
        long totalRevenue = orderStatisticResSet.stream().map(OrderStatisticRes::getTotalPrice).reduce(0L, Long::sum);
        long totalOrders = orderRepository.count();
        long totalProducts = productSizeRepository.count();
        int totalWaitingOrder = orderRepository.countByStatus(1);
        int totalDeliveryOrder = orderRepository.countByStatus(2);
        int totalSuccessOrder = orderRepository.countByStatus(3);
        int totalCancelOrder = orderRepository.countByStatus(0);
        Set<Map<String, Object>> ordersByType = new HashSet<>();
        ordersByType.add(Map.of("label", "Chờ xác nhận", "quantity", totalWaitingOrder));
        ordersByType.add(Map.of("label", "Đang giao", "quantity", totalDeliveryOrder));
        ordersByType.add(Map.of("label", "Hoàn tất", "quantity", totalSuccessOrder));
        ordersByType.add(Map.of("label", "Đã hủy", "quantity", totalCancelOrder));

        return StatisticResponse.builder()
                .orderStatisticResSet(orderStatisticResSet)
                .totalRevenue(totalRevenue)
                .totalProducts(totalProducts)
                .totalOrders(totalOrders)
                .ordersByType(ordersByType)
                .build();
    }

    @Override
    public PaginationResponse<ReportProductRes> getAllProductReport(int page, int size, String startDate, String endDate) {
        long timestamp = Long.parseLong(startDate);
        Instant instant = Instant.ofEpochMilli(timestamp);
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime localDate = instant.atZone(zoneId).toLocalDateTime();

        long timestamp2 = Long.parseLong(endDate);
        Instant instant2 = Instant.ofEpochMilli(timestamp2);
        LocalDateTime localDate2 = instant2.atZone(zoneId).toLocalDateTime();

        Set<String> orderIds = orderRepository.findAllOrderIdByStatus(3);
        Pageable pageable = PageRequest.of(page, size);
        Page<OrderDetail> orderDetailPage = orderDetailsRepos.findAllByOrderIdsAndTime(orderIds, localDate, localDate2, pageable);
        List<ReportProductRes> reportProductResList = orderDetailPage.getContent().stream().map(od ->
                        ReportProductRes.builder()
                                .productName(od.getProduct().getProductName())
                                .sizeName(od.getSize().getSizeName())
                                .price(od.getProduct().getPrice())
                                .discount(od.getProduct().getDiscount())
                                .quantity(od.getQuantity())
                                .totalPrice(od.getTotalPrice())
                                .createDate(od.getCreateDate().toLocalDate())
                                .build()).toList();
        return PaginationResponse.<ReportProductRes>builder()
                .content(reportProductResList)
                .page(orderDetailPage.getNumber())
                .size(orderDetailPage.getSize())
                .totalElements(orderDetailPage.getTotalElements())
                .totalPage(orderDetailPage.getTotalPages())
                .build();
    }

    @Transactional(rollbackOn = Throwable.class)
    @Override
    public Map<String, String> createOrder(OrderRequest request) {
        String id = UUID.randomUUID().toString();
        Order order = Order.builder()
                .id(id.substring(id.length() - 10))
                .totalPrice(request.getTotalPrice())
                .address(request.getAddress())
                .status(request.getStatus())
                .user(userRepository.findById(request.getUserId())
                        .orElseThrow(() -> new CustomizedException(ErrorCode.ACCOUNT_NOT_EXISTED)))
                .payment(paymentRepository.findById(request.getPayment())
                        .orElseThrow(() -> new CustomizedException(ErrorCode.PAYMENT_NOT_EXISTED)))
                .build();
        if (!request.getVoucherId().isBlank()) {
            Voucher voucher = voucherRepository.findById(request.getVoucherId())
                    .orElseThrow(() -> new CustomizedException(ErrorCode.VOUCHER_NOT_EXISTED));
            order.setVoucher(voucher);
            voucher.setQuantity(voucher.getQuantity() - 1);
            voucherRepository.save(voucher);
        }
        orderRepository.save(order);
        Set<OrderDetail> orderDetails = new HashSet<>();
        Set<CartDetail> cartDetails = cartDetailRepository.findAllByCartId(request.getCartId());
        for (CartDetail cd : cartDetails) {
            OrderDetail orderDetail = OrderDetail.builder()
                    .id(new OrderDetailKey(order.getId(), cd.getProduct().getId(), cd.getSize().getId()))
                    .product(cd.getProduct())
                    .size(cd.getSize())
                    .order(order)
                    .quantity(cd.getQuantity())
                    .totalPrice(cd.getTotalPrice())
                    .build();
            orderDetails.add(orderDetail);
            ProductSize productSize = productSizeRepository.findById(
                            new ProductSizeKey(cd.getProduct().getId(), cd.getSize().getId()))
                    .orElseThrow(() -> new CustomizedException(ErrorCode.PRODUCT_SIZE_NOT_EXISTED));
            productSize.setQuantity(productSize.getQuantity() - cd.getQuantity());
            productSizeRepository.save(productSize);
        }
        orderDetailsRepos.saveAll(orderDetails);
        cartDetailRepository.deleteAll(cartDetails);

        return Map.of("orderId", order.getId());
    }

    @Override
    public PaginationResponse<OrderResponse> getAllOrdersByUser(int page, int size, List<String> sort, long userId, int status) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(createSortOrder(sort)));
        Page<Order> orderPage = orderRepository.findAllByUserIdAndStatus(userId, status, pageable);
        List<Order> orderList = orderPage.getContent();
        List<OrderResponse> orderResponseList = orderMapper.toDtoList(orderList);

        return PaginationResponse.<OrderResponse>builder()
                .content(orderResponseList)
                .page(orderPage.getNumber())
                .size(orderPage.getSize())
                .totalElements(orderPage.getTotalElements())
                .totalPage(orderPage.getTotalPages())
                .build();
    }

    @Override
    public OrderDetailResponse getOrderDetail(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomizedException(ErrorCode.ORDER_NOT_EXISTED));

        Set<OrderDetail> orderDetails = orderDetailsRepos.findAllByOrderId(order.getId());

        Set<SetProductOrder> setProductOrders = orderDetails.stream().map(product -> {
            return SetProductOrder.builder()
                    .productId(product.getProduct().getProductId())
                    .productName(product.getProduct().getProductName())
                    .categoryId(product.getProduct().getProductCategory().getCategoryId())
                    .categoryName(product.getProduct().getProductCategory().getCategoryName())
                    .sizeId(product.getSize().getSizeId())
                    .sizeName(product.getSize().getSizeName())
                    .price(product.getProduct().getPrice())
                    .discount(product.getProduct().getDiscount())
                    .quantity(product.getQuantity())
                    .totalPrice(product.getTotalPrice())
                    .imageURL(product.getProduct().getImageURL())
                    .build();
        }).collect(Collectors.toSet());

        String voucherId = order.getVoucher() != null ? order.getVoucher().getId() : null;
        Integer voucherPrice = order.getVoucher() != null ? order.getVoucher().getDiscount() : 0;

        return OrderDetailResponse.builder()
                .id(order.getId())
                .totalPrice(order.getTotalPrice())
                .status(order.getStatus())
                .username(order.getUser().getUsername())
                .phoneNumber(order.getUser().getPhoneNumber())
                .paymentName(order.getPayment().getName())
                .voucherId(voucherId)
                .voucherPrice(voucherPrice)
                .products(setProductOrders)
                .createDate(order.getCreateDate())
                .address(order.getAddress())
                .build();
    }

    @Override
    public PaginationResponse<OrderResponse> getAllOrdersByStatus(int page, int size, List<String> sort, int status) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(createSortOrder(sort)));
        Page<Order> orderPage = orderRepository.findAllByStatus(status, pageable);
        List<Order> orderList = orderPage.getContent();
        List<OrderResponse> orderResponseList = orderMapper.toDtoList(orderList);

        return PaginationResponse.<OrderResponse>builder()
                .content(orderResponseList)
                .page(orderPage.getNumber())
                .size(orderPage.getSize())
                .totalElements(orderPage.getTotalElements())
                .totalPage(orderPage.getTotalPages())
                .build();
    }

    @Override
    public void cancelOrder(CancelOrderRequest request) {
        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new CustomizedException(ErrorCode.ORDER_NOT_EXISTED));
        order.setStatus(0);
        orderRepository.save(order);
        for (SetProductOrder productOrder : request.getSetProductOrder()) {
            ProductSize productSize = productSizeRepository.findByProductProductIdAndSizeSizeId(
                    productOrder.getProductId(), productOrder.getSizeId()
            );
            productSize.setQuantity(productSize.getQuantity() + productOrder.getQuantity());
            productSizeRepository.save(productSize);
        }
    }

    @Override
    public void deleteOrder(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomizedException(ErrorCode.ORDER_NOT_EXISTED));
        orderRepository.delete(order);
    }

    @Override
    public void deliverOrder(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomizedException(ErrorCode.ORDER_NOT_EXISTED));
        order.setStatus(2);
        orderRepository.save(order);
    }

    @Override
    public void successOrder(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomizedException(ErrorCode.ORDER_NOT_EXISTED));
        order.setStatus(3);
        Set<OrderDetail> orderDetails = orderDetailsRepos.findAllByOrderId(order.getId());
        for (OrderDetail od : orderDetails) {
            ProductSize productSize = productSizeRepository
                    .findByProductProductIdAndSizeSizeId(od.getProduct().getProductId(), od.getSize().getSizeId());
            productSize.setSold(productSize.getSold() + od.getQuantity());
            productSizeRepository.save(productSize);
        }
        orderRepository.save(order);
    }
}
