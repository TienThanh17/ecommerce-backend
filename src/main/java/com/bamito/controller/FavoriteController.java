package com.bamito.controller;

import com.bamito.dto.request.user.FavoriteRequest;
import com.bamito.dto.response.ResponseObject;
import com.bamito.service.IFavoriteService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/favorite")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class FavoriteController {
    IFavoriteService favoriteService;

    @GetMapping
    public ResponseObject<?> getAllFavorites(@RequestParam long userId) {
        var result =  favoriteService.getAllFavoritesByUser(userId);
        return ResponseObject.builder()
                .data(result)
                .build();
    }

    @PostMapping
    public ResponseObject<?> addFavorite(@Valid @RequestBody FavoriteRequest request) {
        favoriteService.addFavourite(request);
        return ResponseObject.builder().build();
    }

    @DeleteMapping
    public ResponseObject<?> deleteFavorite(@RequestParam long userId, @RequestParam int productId) {
        favoriteService.removeFavourite(userId, productId);
        return ResponseObject.builder().build();
    }
}
