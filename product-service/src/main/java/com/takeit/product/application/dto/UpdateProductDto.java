package com.takeit.product.application.dto;

import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record UpdateProductDto(
        String productName,
        String description,
        Long price,
        Integer stock,
        Integer limitPerUser,
        LocalDateTime openTime,
        LocalDateTime closeTime,
        Boolean isActive,
        List<UUID> deletePhotos,
        List<MultipartFile> files
) {

}
