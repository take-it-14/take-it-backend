package com.takeit.product.infrastructure.repository;

import com.takeit.product.domain.entity.Product;
import com.takeit.product.domain.entity.ProductPhoto;
import com.takeit.product.domain.repository.ProductPhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ProductPhotoRepositoryImpl implements ProductPhotoRepository {
    private final JpaProductPhotoRepository jpaProductPhotoRepository;

    @Override
    public List<ProductPhoto> saveAll(List<ProductPhoto> photos) {
        return jpaProductPhotoRepository.saveAll(photos);
    }

    @Override
    public List<ProductPhoto> findByUuidInAndIsDeletedIsFalse(List<UUID> uuids) {
        return jpaProductPhotoRepository.findByUuidInAndIsDeletedIsFalse(uuids);
    }

    @Override
    public List<ProductPhoto> findAllByProductAndIsDeletedIsFalse(Product product) {
        return jpaProductPhotoRepository.findAllByProductAndIsDeletedIsFalse(product);
    }
}
