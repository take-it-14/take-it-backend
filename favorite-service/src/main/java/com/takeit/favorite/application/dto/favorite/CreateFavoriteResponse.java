package com.takeit.favorite.application.dto.favorite;

import java.util.UUID;

public record CreateFavoriteResponse(
        UUID favoriteId,
        String username,
        UUID productId
) {

    public static CreateFavoriteResponse of(UUID favoriteId, String username, UUID productId) {
        return new CreateFavoriteResponse(favoriteId, username, productId);
    }
}
