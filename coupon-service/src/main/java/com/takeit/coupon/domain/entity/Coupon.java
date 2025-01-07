package com.takeit.coupon.domain.entity;

import com.takeit.common.domain.model.BaseEntity;
import com.takeit.coupon.domain.type.CouponType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
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
    @Builder.Default
    private int minAmount = 0;

    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "expiration_date")
    @Builder.Default
    private int expirationDate = 365 * 10;

}
