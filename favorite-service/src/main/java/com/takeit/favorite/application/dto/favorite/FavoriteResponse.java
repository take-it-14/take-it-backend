package com.takeit.favorite.application.dto.favorite;

import java.util.UUID;

public record FavoriteResponse(
        UUID id,
        UUID productId
) {
    public static FavoriteResponse of(UUID favoriteId, UUID productId) {
        return new FavoriteResponse(favoriteId, productId);
    }
}
