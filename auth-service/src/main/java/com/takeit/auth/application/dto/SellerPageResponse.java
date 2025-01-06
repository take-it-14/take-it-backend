package com.takeit.auth.application.dto;

import com.takeit.auth.domain.entity.SellerInfo;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.web.PagedModel;

public record SellerPageResponse(
    SellerPage sellerPage
) {
    public static SellerPageResponse from(Page<SellerInfo> sellerPage) {
        return new SellerPageResponse(new SellerPage(sellerPage));
    }

    public static class SellerPage extends PagedModel<SellerPage.SellerInfo> {

        public SellerPage(Page<com.takeit.auth.domain.entity.SellerInfo> sellerPage) {
            super(
                    new PageImpl<>(
                            SellerInfo.from(sellerPage.getContent()),
                            sellerPage.getPageable(),
                            sellerPage.getTotalElements()
                    )
            );
        }

        public static record SellerInfo(
                String businessNumber,
                String businessName,
                String phoneNumber,
                String address,
                String username,
                String nickname,
                String email
        ) {
            public static List<SellerInfo> from(List<com.takeit.auth.domain.entity.SellerInfo> sellerInfoList) {
                return sellerInfoList.stream()
                        .map(SellerInfo::from)
                        .toList();
            }

            public static SellerInfo from(com.takeit.auth.domain.entity.SellerInfo sellerInfo) {
                return new SellerInfo(
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
    }
}
