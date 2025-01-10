package com.takeit.favorite.application.service;

import com.takeit.common.exception.CustomException;
import com.takeit.common.exception.ErrorCode;
import com.takeit.favorite.application.dto.favorite.CreateFavoriteDto;
import com.takeit.favorite.application.dto.favorite.CreateFavoriteResponse;
import com.takeit.favorite.application.dto.favorite.FavoriteResponse;
import com.takeit.favorite.application.dto.product.ProductDto;
import com.takeit.favorite.application.dto.user.UserDto;
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
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.takeit.favorite.utils.AccessValidator.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final UserService userService;
    private final ProductService productService;

    @Transactional
    public CreateFavoriteResponse addFavorite(CreateFavoriteDto request, String requesterUsername) {
        UserDto userDto = userService.getUser(requesterUsername);
        if(!isMaster(userDto) && !isManager(userDto) && !isRequesterAuthorized(request.username(), requesterUsername)) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }
        Long userId = userDto.id();

        ProductDto productDto = productService.getProduct(request.productId());
        Long productId = productDto.id();

        Optional<Favorite> favorite = favoriteRepository.findByProductIdAndUserId(productId, userId);
        if (favorite.isPresent()) {
            favorite.get().restore();
        } else {
            favorite = Optional.of(favoriteRepository.save(
                    Favorite.create(productId, userId)
            ));
        }

        return CreateFavoriteResponse.of(favorite.get().getUuid(), requesterUsername,  request.productId());
    }

    @Transactional
    public void cancelFavorite(UUID favoriteId, String requesterUsername) {
        Favorite favorite = favoriteRepository.findByUuidIsDeleteFalse(favoriteId)
                .orElseThrow(() -> new CustomException(ErrorCode.FAVORITE_NOT_FOUND));

        UserDto userDto = userService.getUser(requesterUsername);
        if(!isMaster(userDto) && !isManager(userDto) && favorite.getUserId().equals(userDto.id())) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        favorite.cancel(requesterUsername);
    }

    public PagedModel<FavoriteResponse> getUserFavorites(String username, String requesterUsername, Pageable pageable) {
        UserDto userDto = userService.getUser(username);
        if(!isMaster(userDto) && !isManager(userDto) && !isRequesterAuthorized(username, requesterUsername)) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }
        Long userId = userDto.id();

        Page<Favorite> favoritePage = favoriteRepository.getUserFavorites(userId, pageable);
        List<Long> productIds = favoritePage.getContent().stream().map(Favorite::getProductId).toList();

        Map<Long, UUID> productIdToUuidMap = productService.getProducts(productIds).stream()
                .collect(Collectors.toMap(ProductDto::id, ProductDto::uuid));

        return new PagedModel<>(
                new PageImpl<>(
                        favoritePage.getContent().stream()
                                .map(favorite -> new FavoriteResponse(favorite.getUuid(), productIdToUuidMap.get(favorite.getProductId())))
                                .toList(),
                        favoritePage.getPageable(),
                        favoritePage.getTotalElements())
        );
    }
}
