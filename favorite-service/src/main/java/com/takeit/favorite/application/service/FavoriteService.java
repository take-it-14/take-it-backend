package com.takeit.favorite.application.service;

import com.takeit.favorite.application.dto.favorite.CreateFavoriteDto;
import com.takeit.favorite.application.dto.favorite.CreateFavoriteResponse;
import com.takeit.favorite.domain.entity.Favorite;
import com.takeit.favorite.domain.repository.FavoriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;

    @Transactional
    public CreateFavoriteResponse addFavorite(CreateFavoriteDto request, String username) {
        // TODO: username으로 User 정보 받아오는 메소드 추가 + 권한 체크
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

        return CreateFavoriteResponse.of(favorite.get().getUuid(), username,  request.getProductId());
    }
}
