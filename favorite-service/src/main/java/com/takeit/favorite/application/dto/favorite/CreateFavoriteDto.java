package com.takeit.favorite.application.dto.favorite;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Getter
@Builder(access = AccessLevel.PRIVATE)
public class CreateFavoriteDto {
    private UUID productId;

    public static CreateFavoriteDto from(UUID productId) {
        return CreateFavoriteDto.builder()
                .productId(productId)
                .build();
    }
}
