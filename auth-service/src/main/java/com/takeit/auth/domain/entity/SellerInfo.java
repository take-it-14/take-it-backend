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
import java.time.LocalDateTime;
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
    private String businessNumber;

    @Column(nullable = false, length = 100)
    private String businessName;

    @Column(nullable = false, length = 20)
    private String phoneNumber;

    @Column(nullable = false)
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SellerInfoStatus status;

    // 판매자정보 생성 메서드
    public static SellerInfo create(
            User user,
            String businessNumber,
            String businessName,
            String phoneNumber,
            String address,
            SellerInfoStatus status
    ) {
        return SellerInfo.builder()
                .user(user)
                .businessNumber(businessNumber)
                .businessName(businessName)
                .phoneNumber(phoneNumber)
                .address(address)
                .status(status)
                .build();
    }

    // 판매자정보 수정 메서드
    public void update(
            String businessNumber,
            String businessName,
            String phoneNumber,
            String address,
            SellerInfoStatus status
    ) {
        if (businessNumber != null) this.businessNumber = businessNumber;
        if (businessName != null) this.businessName = businessName;
        if (phoneNumber != null) this.phoneNumber = phoneNumber;
        if (address != null) this.address = address;
        if (status != null) this.status = status;
    }

    // 판매자정보 삭제 메서드
    public void delete(String deletedBy) {
        this.deletedAt = LocalDateTime.now();
        this.deletedBy = deletedBy;
        this.isDeleted = true;
    }
}
