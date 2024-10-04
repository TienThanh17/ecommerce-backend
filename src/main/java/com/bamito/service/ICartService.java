package com.bamito.service;

import com.bamito.dto.request.product.CartDetailRequest;
import com.bamito.dto.response.product.CartResponse;

public interface ICartService {
    void createCart(long userId, String cartId);
    String getCart(long userId);
    void addProductToCart(CartDetailRequest request);
    CartResponse getProductCart(long userId);
    void updateProductCart(CartDetailRequest request);
    void deleteProductCart(long userId, String productId, String sizeId);
}