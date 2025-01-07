package com.takeit.coupon.domain.entity;

import com.takeit.common.domain.model.BaseEntity;
import com.takeit.coupon.domain.type.CouponType;
import com.takeit.coupon.presentation.request.CreateCouponRequest;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "p_coupon")
@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class Coupon extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "uuid")
    @Builder.Default
    private UUID uuid = UUID.randomUUID();

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private CouponType type;

    @Column(name = "discount_value")
    private int discountValue;

    @Column(name = "min_amount")
    private int minAmount;

    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "expiration_date")
    private int expirationDate;

    public static Coupon of(CreateCouponRequest request, Long categoryId) {
        return Coupon.builder()
                .name(request.name())
                .type(request.type())
                .categoryId(categoryId)
                .discountValue(request.discountValue())
                .minAmount(request.minAmount() != null ? request.minAmount() : 0)
                .startDate(request.startDate())
                .endDate(request.endDate())
                .expirationDate(request.expirationDate() == null ? 3650 : request.expirationDate())
                .build();
    }
}
