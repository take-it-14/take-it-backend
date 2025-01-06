package com.takeit.favorite.presentation.request;

import com.takeit.favorite.application.dto.favorite.CreateFavoriteDto;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateFavoriteRequest (
        @NotNull UUID productId
){
    public CreateFavoriteDto toDto() {
        return CreateFavoriteDto.from(this.productId);
    }
}
