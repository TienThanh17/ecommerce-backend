package com.bamito.controller;

import com.bamito.dto.request.product.CartDetailRequest;
import com.bamito.dto.response.ResponseObject;
import com.bamito.dto.response.product.CartResponse;
import com.bamito.service.ICartService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class CartController {
    ICartService cartService;

    @GetMapping
    public ResponseObject<String> getCart(@RequestParam long userId) {
        var result = cartService.getCart(userId);

        return ResponseObject.<String>builder()
                .data(result)
                .build();
    }

    @GetMapping("/get-cart-detail")
    public ResponseObject<CartResponse> getProductCart(@RequestParam long userId) {
        var result = cartService.getProductCart(userId);

        return ResponseObject.<CartResponse>builder()
                .data(result)
                .build();
    }

    @PostMapping("add-product")
    public ResponseObject<?> addProductToCart(@Valid @RequestBody CartDetailRequest request) {
        cartService.addProductToCart(request);

        return ResponseObject.builder().build();
    }

    @PutMapping("update-product")
    public ResponseObject<?> updateProductCart(@Valid @RequestBody CartDetailRequest request) {
        cartService.updateProductCart(request);

        return ResponseObject.builder().build();
    }

    @DeleteMapping("delete-product")
    public ResponseObject<?> deleteProductCart(
            @RequestParam long userId,
            @RequestParam String productId,
            @RequestParam String sizeId
    ) {
        cartService.deleteProductCart(userId, productId, sizeId);

        return ResponseObject.builder().build();
    }
}
