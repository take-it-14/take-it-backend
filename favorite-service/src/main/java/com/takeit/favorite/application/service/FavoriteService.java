package com.takeit.favorite.application.service;

import com.takeit.common.exception.CustomException;
import com.takeit.common.exception.ErrorCode;
import com.takeit.favorite.application.dto.favorite.CreateFavoriteDto;
import com.takeit.favorite.application.dto.favorite.CreateFavoriteResponse;
import com.takeit.favorite.domain.entity.Favorite;
import com.takeit.favorite.domain.repository.FavoriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;

    @Transactional
    public CreateFavoriteResponse addFavorite(CreateFavoriteDto request, String username) {
        // TODO: username으로 User 정보 받아오는 메소드 추가 + 권한체크 customer: 본인 것만 가능, manager,master: 아무나 가능)
        Long userId = 1L;
        // TODO: UUID로 Product 정보 받아오는 메소드 추가
        Long productId = 1L;

        Optional<Favorite> favorite = favoriteRepository.findByProductIdAndUserId(productId, userId);
        if (favorite.isPresent()) {
            favorite.get().restore();
        } else {
            favorite = Optional.of(favoriteRepository.save(
                    Favorite.create(productId, userId)
            ));
        }

        return CreateFavoriteResponse.of(favorite.get().getUuid(), username,  request.productId());
    }

    @Transactional
    public void cancelFavorite(UUID favoriteId, String username) {
        // TODO: username으로 User 정보 받아오는 메소드 추가 + 권한체크(customer: 본인 것만 가능, manager,master: 아무나 가능)
        Favorite favorite = favoriteRepository.findByUuidIsDeleteFalse(favoriteId)
                .orElseThrow(() -> new CustomException(ErrorCode.FAVORITE_NOT_FOUND));

        favorite.cancel(username);

    }
}
