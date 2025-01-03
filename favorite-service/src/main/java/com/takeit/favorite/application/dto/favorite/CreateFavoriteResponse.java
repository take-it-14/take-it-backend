package com.takeit.favorite.application.dto.favorite;

import com.takeit.favorite.domain.entity.Favorite;
import lombok.*;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
public class CreateFavoriteResponse {
    private UUID favoriteId;
    private String username;
    private UUID productId;

    public static CreateFavoriteResponse of(UUID favoriteId, String username, UUID productId) {
        return CreateFavoriteResponse.builder()
                .favoriteId(favoriteId)
                .username(username)
                .productId(productId)
                .build();
    }
}
