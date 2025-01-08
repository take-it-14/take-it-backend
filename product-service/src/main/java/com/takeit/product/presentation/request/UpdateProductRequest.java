package com.takeit.product.presentation.request;

import com.takeit.product.application.dto.UpdateProductDto;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record UpdateProductRequest(
        @NotNull
        String productName,
        String description,
        String imageUrl,
        @NotNull
        Long price,
        @NotNull
        Integer stock,
        @NotNull
        Integer limitPerUser,
        @NotNull
        LocalDateTime openTime,
        LocalDateTime closeTime,
        @NotNull
        Boolean isActive
) {
    public UpdateProductDto toDto() {
        return new UpdateProductDto(
                productName,
                description,
                imageUrl,
                price,
                stock,
                limitPerUser,
                openTime,
                closeTime,
                isActive
        );
    }
}
