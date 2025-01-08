package com.takeit.product.application.service;

import com.takeit.product.application.dto.CreateProductDto;
import com.takeit.product.application.dto.ProductResponse;
import com.takeit.product.domain.entity.Product;
import com.takeit.product.domain.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    // 상품 등록
    @Transactional
    public ProductResponse createProduct(CreateProductDto dto) {
        Product product = Product.create(
                dto.sellerId(),
                dto.categoryId(),
                dto.productName(),
                dto.description(),
                dto.imageUrl(),
                dto.price(),
                dto.stock(),
                dto.limitPerUser(),
                dto.openTime(),
                dto.closeTime(),
                dto.isActive()
        );
        productRepository.save(product);

        return ProductResponse.from(product);
    }
}
