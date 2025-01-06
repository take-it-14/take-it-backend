package com.takeit.auth.application.dto;

import com.takeit.auth.domain.entity.SellerInfo;

public record SellerResponse(
    String businessNumber,
    String businessName,
    String phoneNumber,
    String address,
    String username,
    String nickname,
    String email
) {
    public static SellerResponse from(SellerInfo sellerInfo) {
        return new SellerResponse(
                sellerInfo.getBusinessNumber(),
                sellerInfo.getBusinessName(),
                sellerInfo.getPhoneNumber(),
                sellerInfo.getAddress(),
                sellerInfo.getUser().getUsername(),
                sellerInfo.getUser().getNickname(),
                sellerInfo.getUser().getEmail()
        );
    }
}
