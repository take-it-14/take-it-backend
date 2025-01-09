package com.takeit.product.domain.entity;

import com.takeit.common.domain.model.BaseEntity;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "p_product")
@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false, unique = true)
    private UUID uuid = UUID.randomUUID();

    @Column(nullable = false)
    private Long sellerId;

    @Column(nullable = false)
    private Long categoryId;

    @Column(nullable = false)
    private String productName;

    private String description;

    @Column(nullable = false)
    private Long price;

    @Column(nullable = false)
    private Integer stock; // 상품 재고 수량

    @Column(nullable = false)
    private Integer limitPerUser; // 1인당 구매 제한 수량

    @Column(nullable = false)
    private LocalDateTime openTime; // 상품 판매 시작 시간

    private LocalDateTime closeTime; // 상품 판매 종료 시간

    @Column(nullable = false)
    private Boolean isActive; // 상품 전시 여부

    private Double stars; // 평점

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "product")
    @Builder.Default
    private List<ProductPhoto> photoList = new ArrayList<>();

    // 상품 생성 메서드
    public static Product create(
            Long sellerId,
            Long categoryId,
            String productName,
            String description,
            Long price,
            Integer stock,
            Integer limitPerUser,
            LocalDateTime openTime,
            LocalDateTime closeTime,
            Boolean isActive
    ) {
        return Product.builder()
                .uuid(UUID.randomUUID())
                .sellerId(sellerId)
                .categoryId(categoryId)
                .productName(productName)
                .description(description)
                .price(price)
                .stock(stock)
                .limitPerUser(limitPerUser)
                .openTime(openTime)
                .closeTime(closeTime)
                .isActive(isActive)
                .build();
    }

    // 상품 수정 메서드
    public void update(
            String productName,
            String description,
            Long price,
            Integer stock,
            Integer limitPerUser,
            LocalDateTime openTime,
            LocalDateTime closeTime,
            Boolean isActive
    ) {
        if (productName != null) this.productName = productName;
        if (description != null) this.description = description;
        if (price != null) this.price = price;
        if (stock != null) this.stock = stock;
        if (limitPerUser != null) this.limitPerUser = limitPerUser;
        if (openTime != null) this.openTime = openTime;
        if (closeTime != null) this.closeTime = closeTime;
        this.isActive = isActive;
    }

    // 상품 삭제 메서드
    public void delete(String deletedBy) {
        this.deletedAt = LocalDateTime.now();
        this.deletedBy = deletedBy;
        this.isDeleted = true;
    }

    public void addPhotos(List<ProductPhoto> productPhotos) {
        photoList = new ArrayList<>(productPhotos);
    }
}
