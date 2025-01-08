package com.takeit.product.application.dto;

import com.takeit.product.domain.entity.Product;
import com.takeit.product.domain.entity.ProductPhoto;

import java.util.List;
import java.util.UUID;

public record ProductResponse(
        UUID ProductId,
        List<ProductPhotoDto> photos
) {
    public static ProductResponse from(Product product) {
        return new ProductResponse(
            product.getUuid(),
            product.getPhotoList().stream().map(ProductPhotoDto::from).toList()
        );
    }

    public static ProductResponse of(Product product, List<ProductPhoto> photos) {
        return new ProductResponse(
                product.getUuid(),
                photos.stream().map(ProductPhotoDto::from).toList()
        );
    }

    public record ProductPhotoDto(
            UUID id,
            String url
    ) {
        public static ProductPhotoDto from(ProductPhoto photo) {
            return new ProductPhotoDto(
                photo.getUuid(),
                photo.getUri()
            );
        }
    }
}
