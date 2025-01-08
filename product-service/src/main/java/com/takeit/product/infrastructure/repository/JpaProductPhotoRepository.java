package com.takeit.product.infrastructure.repository;

import com.takeit.product.domain.entity.ProductPhoto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaProductPhotoRepository extends JpaRepository<ProductPhoto, Long> {
}
