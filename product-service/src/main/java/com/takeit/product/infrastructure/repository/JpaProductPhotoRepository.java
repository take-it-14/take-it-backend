package com.takeit.product.infrastructure.repository;

import com.takeit.product.domain.entity.Product;
import com.takeit.product.domain.entity.ProductPhoto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface JpaProductPhotoRepository extends JpaRepository<ProductPhoto, Long> {
    List<ProductPhoto> findByUuidInAndIsDeletedIsFalse(List<UUID> uuids);

    List<ProductPhoto> findAllByProductAndIsDeletedIsFalse(Product product);
}
