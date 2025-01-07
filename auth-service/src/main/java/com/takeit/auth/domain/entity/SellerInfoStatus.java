package com.takeit.auth.domain.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SellerInfoStatus {
    PENDING("PENDING", "대기"),
    APPROVED("APPROVED", "승인"),
    REJECTED("REJECTED", "거절");

    private final String statusName;
    private final String description;

    public static SellerInfoStatus from(String statusName) {
        for (SellerInfoStatus status : SellerInfoStatus.values()) {
            if (status.statusName.equals(statusName)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid statusName: " + statusName);
    }

}
