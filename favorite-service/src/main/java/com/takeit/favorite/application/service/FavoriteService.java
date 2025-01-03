package com.takeit.favorite.application.service;

import com.takeit.favorite.application.dto.favorite.CreateFavoriteDto;
import com.takeit.favorite.application.dto.favorite.CreateFavoriteResponse;
import com.takeit.favorite.domain.entity.Favorite;
import com.takeit.favorite.domain.repository.FavoriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;

    @Transactional
    public CreateFavoriteResponse createFavorite(CreateFavoriteDto request, String username) {
        // TODO: username으로 User 정보 받아오는 메소드 추가 + 권한 체크
        Long userId = 1L;
        // TODO: UUID로 Product 정보 받아오는 메소드 추가
        Long productId = 1L;

        Favorite favorite = favoriteRepository.save(
                Favorite.create(productId, userId)
        );

        return CreateFavoriteResponse.of(favorite.getUuid(), username,  request.getProductId());
    }
}
