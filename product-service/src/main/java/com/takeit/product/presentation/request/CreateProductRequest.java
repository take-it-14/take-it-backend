package com.takeit.product.presentation.request;

import com.takeit.product.application.dto.CreateProductDto;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

public record CreateProductRequest(
        @NotNull
        Long sellerId,
        @NotNull
        Long categoryId,
        @NotNull
        String productName,
        String description,
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
        Boolean isActive,
        List<MultipartFile> photos
) {
    public CreateProductDto toDto() {
        return new CreateProductDto(
                sellerId,
                categoryId,
                productName,
                description,
                price,
                stock,
                limitPerUser,
                openTime,
                closeTime,
                isActive,
                photos
        );
    }
}
