package com.takeit.product.application.service;

import com.querydsl.core.types.Predicate;
import com.takeit.common.exception.CustomException;
import com.takeit.common.exception.ErrorCode;
import com.takeit.product.application.dto.*;
import com.takeit.product.domain.entity.Product;
import com.takeit.product.domain.entity.ProductDailyStat;
import com.takeit.product.domain.entity.ProductPhoto;
import com.takeit.product.domain.repository.ProductDailyStatRepository;
import com.takeit.product.domain.repository.ProductPhotoRepository;
import com.takeit.product.domain.repository.ProductRepository;
import com.takeit.s3.infrastructure.util.FileUpload;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.takeit.common.exception.ErrorCode.FILE_UPLOAD_ERROR;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

	private final ProductDailyStatRepository productDailyStatRepository;
    private final ProductRepository productRepository;
    private final ProductPhotoRepository productPhotoRepository;
    private final FileUpload fileUpload;

    // 상품 등록
    @Transactional
    public ProductResponse createProduct(CreateProductDto dto) {
        Product product = productRepository.save(Product.create(
                dto.sellerId(),
                dto.categoryId(),
                dto.productName(),
                dto.description(),
                dto.price(),
                dto.stock(),
                dto.limitPerUser(),
                dto.openTime(),
                dto.closeTime(),
                dto.isActive()
        ));

        List<ProductPhoto> photos = new ArrayList<>();
        if(dto.files() != null && !dto.files().isEmpty()) {
            // todo : 파일 개수 제한 로직
            try {
                photos = productPhotoRepository.saveAll(
                        fileUpload.uploadMultipleFile(dto.files(), "product")
                                .stream().map(file -> ProductPhoto.create(file, product)).toList()
                );

            } catch (IOException e) {
                throw new CustomException(FILE_UPLOAD_ERROR);
            }
        }

        return ProductResponse.of(product, photos);
    }

    // 상품 수정
    @Transactional
    public ProductResponse updateProduct(UUID productId, UpdateProductDto dto, String username) {

        Product product = productRepository.findByUuidAndIsDeletedFalse(productId).orElseThrow(
                () -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND)
        );

        product.update(
                dto.productName(),
                dto.description(),
                dto.price(),
                dto.stock(),
                dto.limitPerUser(),
                dto.openTime(),
                dto.closeTime(),
                dto.isActive()
        );

        product = productRepository.save(product);

        int deleteFileSize = 0;
        if(dto.deletePhotos() != null && !dto.deletePhotos().isEmpty()) {
            List<ProductPhoto> deletePhotos = productPhotoRepository.findByUuidInAndIsDeletedIsFalse(dto.deletePhotos());

            deletePhotos.forEach(productPhoto -> productPhoto.delete(username));
            deleteFileSize = deletePhotos.size();
            productPhotoRepository.saveAll(deletePhotos);
        }

        if(dto.files() != null && !dto.files().isEmpty()) {
            // todo : 파일 개수 제한 로직
//            int count = productPhotoRepository.findAllByProductAndIsDeletedIsFalse(product).size();
//
//            // 총 보여질 사진 개수 (현재 저장되어 있는 사진 개수 - 지울 사진 개수 + 새로 등록할 사진)
//            if(count - deleteFileSize + dto.files().size() > 3) {
//                throw new CustomException(TOO_MANY_PHOTOS);
//            }

            try {
                Product finalProduct = product;
                productPhotoRepository.saveAll(
                        fileUpload.uploadMultipleFile(dto.files(), "product")
                                .stream().map(file -> ProductPhoto.create(file, finalProduct)).toList()
                );

            } catch (IOException e) {
                throw new CustomException(FILE_UPLOAD_ERROR);
            }
        }

        List<ProductPhoto> newPhotos = productPhotoRepository.findAllByProductAndIsDeletedIsFalse(product);

        return ProductResponse.of(product, newPhotos);
    }

    // 상품 삭제
    @Transactional
    public ProductResponse deleteProduct(UUID productId, String username) {
        Product product = productRepository.findByUuidAndIsDeletedFalse(productId).orElseThrow(
                () -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND)
        );

        product.delete(username);

        List<ProductPhoto> deletePhotos = productPhotoRepository.findAllByProductAndIsDeletedIsFalse(product);
        deletePhotos.forEach(productPhoto -> productPhoto.delete(username));
        productPhotoRepository.saveAll(deletePhotos);

        return ProductResponse.of(product, deletePhotos);
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

    public ProductEntityResponse getProductEntity(UUID productId) {
        return ProductEntityResponse.from(productRepository.findByUuidAndIsDeletedFalse(productId)
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND)));
    }

    public List<ProductEntityResponse> getProductEntities(List<Long> idList, Predicate predicate) {
        return productRepository.getProductEntities(idList, predicate).stream()
                .map(ProductEntityResponse::from)
                .toList();
    }
}
