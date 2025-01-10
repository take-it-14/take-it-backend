package com.takeit.favorite.application.dto.favorite;

import java.util.UUID;

public record CreateFavoriteDto(
        String username,
        UUID productId
){

    public static CreateFavoriteDto of(String username, UUID productId) {
        return new CreateFavoriteDto(username, productId);
    }
}
