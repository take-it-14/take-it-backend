package com.takeit.product.application.service;

import com.querydsl.core.types.Predicate;
import com.takeit.common.exception.CustomException;
import com.takeit.common.exception.ErrorCode;
import com.takeit.product.application.dto.CreateProductDto;
import com.takeit.product.application.dto.ProductDetailResponse;
import com.takeit.product.application.dto.ProductPageResponse;
import com.takeit.product.application.dto.ProductResponse;
import com.takeit.product.application.dto.UpdateProductDto;
import com.takeit.product.domain.entity.Product;
import com.takeit.product.domain.repository.ProductRepository;
import java.util.UUID;
import java.util.Date;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.takeit.product.application.dto.ProductDailyStatResponse;
import com.takeit.product.domain.entity.ProductDailyStat;
import com.takeit.product.domain.repository.ProductDailyStatRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

	private final ProductDailyStatRepository productDailyStatRepository;
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
        Product product = productRepository.findByUuidAndIsDeletedFalse(productId).orElseThrow(
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
        Product product = productRepository.findByUuidAndIsDeletedFalse(productId).orElseThrow(
                () -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND)
        );

        product.delete(username);

        return ProductResponse.from(product);
    }

    // 상품 단건 조회
    @Transactional(readOnly = true)
    public ProductDetailResponse getProduct(UUID productId) {
        Product product = productRepository.findByUuidAndIsDeletedFalse(productId).orElseThrow(
                () -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND)
        );

        return ProductDetailResponse.from(product);
    }

    // 상품 목록
    @Transactional(readOnly = true)
    public ProductPageResponse getProducts(Predicate predicate, Pageable pageable) {
        Page<Product> userPage = productRepository.findAll(predicate, pageable);

        if (userPage.isEmpty()) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        return ProductPageResponse.from(userPage);
    }

	public Page<ProductDailyStatResponse> getProductDailyStat(UUID productId, Date startDate, Date endDate, Pageable pageable) {
		// TODO: product UUID->ID 변환 요청
		Long newProductId=1L;

		Page<ProductDailyStat> productDailyStats = productDailyStatRepository.findByProductIdAndBaseDateBetween(newProductId, startDate, endDate, pageable);

		return productDailyStats.map(
			productDailyStat -> ProductDailyStatResponse.of(productDailyStat, productId)
		);
	}
}
