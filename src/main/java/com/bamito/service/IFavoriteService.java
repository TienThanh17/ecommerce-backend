package com.bamito.service;

import com.bamito.dto.request.user.FavoriteRequest;

import java.util.Set;

public interface IFavoriteService {
    void addFavourite(FavoriteRequest request);
    void removeFavourite(long userId, int productId);
    Set<String> getAllFavoritesByUser(long userId);
}
