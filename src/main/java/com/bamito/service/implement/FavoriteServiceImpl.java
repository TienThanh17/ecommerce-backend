package com.bamito.service.implement;

import com.bamito.dto.request.user.FavoriteRequest;
import com.bamito.entity.Product;
import com.bamito.entity.User;
import com.bamito.exception.CustomizedException;
import com.bamito.exception.ErrorCode;
import com.bamito.repository.IProductRepository;
import com.bamito.repository.IUserRepository;
import com.bamito.service.IFavoriteService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FavoriteServiceImpl implements IFavoriteService {
    IUserRepository userRepository;
    IProductRepository productRepository;

    @Override
    public Set<String> getAllFavoritesByUser(long userId) {
        Set<String> favorites = userRepository.findAllFavorites(userId).stream()
                .map(Product::getProductId).collect(Collectors.toSet());

        return favorites;
    }

    @Override
    public void addFavourite(FavoriteRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new CustomizedException(ErrorCode.ACCOUNT_NOT_EXISTED));
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new CustomizedException(ErrorCode.PRODUCT_NOT_EXISTED));
        boolean isSuccess = user.getFavoriteProducts().add(product);
        if (!isSuccess) throw new CustomizedException(ErrorCode.FAVORITE_EXISTED);
        userRepository.save(user);
    }

    @Override
    public void removeFavourite(long userId, int productId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomizedException(ErrorCode.ACCOUNT_NOT_EXISTED));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new CustomizedException(ErrorCode.PRODUCT_NOT_EXISTED));
        boolean isSuccess = user.getFavoriteProducts().remove(product);
        if (!isSuccess) throw new CustomizedException(ErrorCode.FAVORITE_NOT_EXISTED);
        userRepository.save(user);
    }
}
