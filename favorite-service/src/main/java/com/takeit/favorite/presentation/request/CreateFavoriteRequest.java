package com.takeit.favorite.presentation.request;

import com.takeit.favorite.application.dto.favorite.CreateFavoriteDto;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateFavoriteRequest {
    @NotNull
    private UUID productId;

    public CreateFavoriteDto toDto() {
        return CreateFavoriteDto.from(this.productId);
    }
}
