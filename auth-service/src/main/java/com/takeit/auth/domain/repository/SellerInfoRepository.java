package com.takeit.auth.domain.repository;

import com.takeit.auth.domain.entity.SellerInfo;
import com.takeit.auth.domain.entity.SellerInfoStatus;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SellerInfoRepository {
    SellerInfo save(SellerInfo sellerInfo);

    Page<SellerInfo> findByStatusAndIsDeletedFalseOrderByIdAsc(SellerInfoStatus status, Pageable pageable);

    Optional<SellerInfo> findByIdAndIsDeletedFalse(Long SellerInfoId);
}
