package com.takeit.product.domain.repository;

import com.takeit.product.domain.entity.Product;
import com.takeit.product.domain.entity.ProductPhoto;

import java.util.List;
import java.util.UUID;

public interface ProductPhotoRepository {
    List<ProductPhoto> saveAll(List<ProductPhoto> product);

    List<ProductPhoto> findByUuidInAndIsDeletedIsFalse(List<UUID> uuids);

    List<ProductPhoto> findAllByProductAndIsDeletedIsFalse(Product product);
}
