package com.takeit.product.application.service;

import com.takeit.common.exception.CustomException;
import com.takeit.common.exception.ErrorCode;
import com.takeit.product.application.dto.CreateProductDto;
import com.takeit.product.application.dto.ProductResponse;
import com.takeit.product.application.dto.UpdateProductDto;
import com.takeit.product.domain.entity.Product;
import com.takeit.product.domain.repository.ProductRepository;
import jakarta.transaction.Transactional;
import java.util.UUID;
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

    // 상품 수정
    @Transactional
    public ProductResponse updateProduct(UUID productId, UpdateProductDto dto) {
        Product product = productRepository.findByUuid(productId).orElseThrow(
                () -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND)
        );

        product.update(
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

        return ProductResponse.from(product);
    }

    // 상품 삭제
    @Transactional
    public ProductResponse deleteProduct(UUID productId, String username) {
        Product product = productRepository.findByUuid(productId).orElseThrow(
                () -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND)
        );

        product.delete(username);

        return ProductResponse.from(product);
    }
}
