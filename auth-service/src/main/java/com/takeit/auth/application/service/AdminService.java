package com.takeit.auth.application.service;

import com.takeit.auth.application.dto.SellerPageResponse;
import com.takeit.auth.application.dto.SellerResponse;
import com.takeit.auth.domain.entity.SellerInfo;
import com.takeit.auth.domain.entity.SellerInfoStatus;
import com.takeit.auth.domain.repository.SellerInfoRepository;
import com.takeit.common.exception.CustomException;
import com.takeit.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminService {

    private final SellerInfoRepository sellerInfoRepository;

    // 승인 요청된 판매자 목록 조회
    public SellerPageResponse getSellers(Pageable pageable) {
        Page<SellerInfo> sellerPage = sellerInfoRepository.findByStatusAndIsDeletedFalseOrderByIdAsc(SellerInfoStatus.PENDING, pageable);

        if (sellerPage.isEmpty()) {
            throw new CustomException(ErrorCode.SELLERINFO_NOT_FOUND);
        }

        return SellerPageResponse.from(sellerPage);
    }

    // 판매자 상태 승인
    @Transactional
    public SellerResponse approveSeller(Long sellerInfoId) {
        // 판매자 정보 확인
        SellerInfo sellerInfo = sellerInfoRepository.findByIdAndIsDeletedFalse(sellerInfoId).orElseThrow(
                () -> new CustomException(ErrorCode.SELLERINFO_NOT_FOUND)
        );

        sellerInfo.update(null, null, null, null, SellerInfoStatus.APPROVED);

        return SellerResponse.from(sellerInfo);
    }
}
