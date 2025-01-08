package com.takeit.product.presentation.request;

import com.takeit.product.application.dto.CreateProductDto;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record CreateProductRequest(
        @NotNull
        Long sellerId,
        @NotNull
        Long categoryId,
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
    public CreateProductDto toDto() {
        return new CreateProductDto(
                sellerId,
                categoryId,
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
