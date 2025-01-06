package com.takeit.favorite.application.dto.favorite;

import java.util.UUID;

public record CreateFavoriteDto(
        UUID productId
){

    public static CreateFavoriteDto from(UUID productId) {
        return new CreateFavoriteDto(productId);
    }
}
