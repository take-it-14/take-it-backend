package com.takeit.product.application.dto;

import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

public record CreateProductDto(
        Long sellerId,
        Long categoryId,
        String productName,
        String description,
        Long price,
        Integer stock,
        Integer limitPerUser,
        LocalDateTime openTime,
        LocalDateTime closeTime,
        Boolean isActive,
        List<MultipartFile> files
) {

}
