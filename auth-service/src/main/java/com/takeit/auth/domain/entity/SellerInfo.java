package com.takeit.auth.domain.entity;

import com.takeit.common.domain.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "p_seller_info")
@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class SellerInfo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false, length = 10)
    private String business_number;

    @Column(nullable = false, length = 100)
    private String business_name;

    @Column(nullable = false, length = 10)
    private String phone_number;

    @Column(nullable = false)
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SellerInfoStatus status;

    // 판매자정보 생성 메서드
    public static SellerInfo create(
            String business_number,
            String business_name,
            String phone_number,
            String address,
            SellerInfoStatus status
    ) {
        return SellerInfo.builder()
                .business_number(business_number)
                .business_name(business_name)
                .phone_number(phone_number)
                .address(address)
                .status(status)
                .build();
    }

    // 판매자정보 수정 메서드
    public void update(
            String business_number,
            String business_name,
            String phone_number,
            String address,
            SellerInfoStatus status
    ) {
        if (business_number != null) this.business_number = business_number;
        if (business_name != null) this.business_name = business_name;
        if (phone_number != null) this.phone_number = phone_number;
        if (address != null) this.address = address;
        if (status != null) this.status = status;
    }
}
