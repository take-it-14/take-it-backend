package com.takeit.favorite.application.service;

import com.takeit.common.exception.CustomException;
import com.takeit.common.exception.ErrorCode;
import com.takeit.favorite.application.dto.favorite.CreateFavoriteDto;
import com.takeit.favorite.application.dto.favorite.CreateFavoriteResponse;
import com.takeit.favorite.application.dto.favorite.FavoriteResponse;
import com.takeit.favorite.domain.entity.Favorite;
import com.takeit.favorite.domain.repository.FavoriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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

    public PagedModel<FavoriteResponse> getUserFavorites(String username, Pageable pageable) {
        // TODO: username으로 User 정보 받아오는 메소드 추가 + 권한체크(customer: 본인 것만 가능, manager,master: 아무나 가능)
        Long userId = 1L;

        Page<Favorite> favoritePage = favoriteRepository.getUserFavorites(userId, pageable);
        List<Long> productIds = favoritePage.getContent().stream().map(Favorite::getProductId).toList();

        // TODO: prouductIds 를 통해 product 가져오기
        UUID productId = UUID.randomUUID();

        return new PagedModel<>(
                new PageImpl<>(
                        favoritePage.getContent().stream()
                                .map(favorite -> new FavoriteResponse(favorite.getUuid(), productId))
                                .toList(),
                        favoritePage.getPageable(),
                        favoritePage.getTotalElements())
        );
    }
}
