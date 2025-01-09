package com.takeit.product.presentation.request;

import com.takeit.product.application.dto.product.UpdateProductDto;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record UpdateProductRequest(
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
        List<UUID> deletePhotos,
        List<MultipartFile> files
) {
    public UpdateProductDto toDto() {
        return new UpdateProductDto(
                productName,
                description,
                price,
                stock,
                limitPerUser,
                openTime,
                closeTime,
                isActive,
                deletePhotos,
                files
        );
    }
}
