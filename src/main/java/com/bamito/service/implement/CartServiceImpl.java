package com.bamito.service.implement;

import com.bamito.dto.request.product.CartDetailRequest;
import com.bamito.dto.response.product.CartResponse;
import com.bamito.dto.response.product.ProductCartResponse;
import com.bamito.entity.*;
import com.bamito.exception.CustomizedException;
import com.bamito.exception.ErrorCode;
import com.bamito.repository.*;
import com.bamito.service.ICartService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartServiceImpl implements ICartService {
    IUserRepository userRepository;
    ICartRepository cartRepository;
    ICartDetailRepository cartDetailRepository;
    IProductRepository productRepository;
    ISizeRepository sizeRepository;
    IProductSizeRepos productSizeRepository;

    @Override
    public void addProductToCart(CartDetailRequest request) {
        int productId = (Integer) request.getProductId();
        int sizeId = (Integer) request.getSizeId();
        Cart cart = cartRepository.findByUserId(request.getUserId())
                .orElseThrow(() -> new CustomizedException(ErrorCode.CART_NOT_EXISTED));
        CartDetailKey id = new CartDetailKey(cart.getId(), productId, sizeId);
        if (cartDetailRepository.existsById(id))
            throw new CustomizedException(ErrorCode.CART_DETAIL_EXISTED);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new CustomizedException(ErrorCode.PRODUCT_NOT_EXISTED));
        Size size = sizeRepository.findById(sizeId)
                .orElseThrow(() -> new CustomizedException(ErrorCode.SIZE_NOT_EXISTED));
        CartDetail cartDetail = CartDetail.builder()
                .id(id)
                .product(product)
                .size(size)
                .cart(cart)
                .quantity(request.getQuantity())
                .totalPrice(request.getTotalPrice())
                .build();
        cartDetailRepository.save(cartDetail);
    }

    @Override
    public CartResponse getProductCart(long userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomizedException(ErrorCode.CART_NOT_EXISTED));
        int productCount = cartDetailRepository.countByCart_Id(cart.getId());
        Set<Product> products = productRepository.findAllByCart(cart.getId());
        Set<Size> sizes = sizeRepository.findAllByCart(cart.getId());

        Map<String, Size> sizeMap = sizes.stream()
                .collect(Collectors.toMap(size -> size.getProductCategory().getCategoryId(), size -> size));

        Set<ProductCartResponse> productCartResponses = products.stream()
                .filter(product -> sizeMap.containsKey(product.getProductCategory().getCategoryId())) // Lọc product có size tương ứng
                .map(product -> {
                    Size size = sizeMap.get(product.getProductCategory().getCategoryId());

                    CartDetailKey id = new CartDetailKey(cart.getId(), product.getId(), size.getId());
                    CartDetail cartDetail = cartDetailRepository.findById(id)
                            .orElseThrow(() -> new CustomizedException(ErrorCode.CART_DETAIL_NOT_EXISTED));

                    int quantityInStock = productSizeRepository.findQuantityBySizeId(size.getId());

                    return ProductCartResponse.builder()
                            .productId(product.getProductId())
                            .productName(product.getProductName())
                            .categoryId(product.getProductCategory().getCategoryId())
                            .categoryName(product.getProductCategory().getCategoryName())
                            .sizeId(size.getSizeId())
                            .sizeName(size.getSizeName())
                            .discount(product.getDiscount())
                            .price(product.getPrice())
                            .imageURL(product.getImageURL())
                            .quantity(cartDetail.getQuantity())
                            .totalPrice(cartDetail.getTotalPrice())
                            .quantityInStock(quantityInStock)
                            .build();
                })
                .collect(Collectors.toSet());

        return new CartResponse(productCartResponses, productCount);
    }

    @Override
    public void updateProductCart(CartDetailRequest request) {
        String productId = (String) request.getProductId();
        String sizeId = (String) request.getSizeId();
        Cart cart = cartRepository.findByUserId(request.getUserId())
                .orElseThrow(() -> new CustomizedException(ErrorCode.CART_NOT_EXISTED));
        CartDetail cartDetail = cartDetailRepository.findByStringId(cart.getId(), productId, sizeId)
                .orElseThrow(() -> new CustomizedException(ErrorCode.CART_DETAIL_NOT_EXISTED));
        cartDetail.setQuantity(request.getQuantity());
        cartDetail.setTotalPrice(request.getTotalPrice());
        cartDetailRepository.save(cartDetail);
    }

    @Override
    public void deleteProductCart(long userId, String productId, String sizeId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomizedException(ErrorCode.CART_NOT_EXISTED));
        CartDetail cartDetail = cartDetailRepository.findByStringId(cart.getId(), productId, sizeId)
                .orElseThrow(() -> new CustomizedException(ErrorCode.CART_DETAIL_NOT_EXISTED));
        cartDetailRepository.delete(cartDetail);
    }

    @Override
    public void createCart(long userId, String cartId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomizedException(ErrorCode.ACCOUNT_NOT_EXISTED));
        Cart cart = Cart.builder()
                .id(cartId)
                .user(user)
                .totalPrice(0)
                .build();

        cartRepository.save(cart);
    }

    @Override
    public String getCart(long userId) {
        String id = cartRepository.findCartIdByUserId(userId);
        if (Objects.isNull(id)) {
            String cartId = UUID.randomUUID().toString().substring(0, 8);
            createCart(userId, cartId);
            return cartId;
        }
        return id;
    }

}
