package com.takeit.favorite.presentation.controller;

import com.takeit.common.presentation.dto.CommonResponse;
import com.takeit.favorite.application.dto.favorite.CreateFavoriteResponse;
import com.takeit.favorite.application.dto.favorite.FavoriteResponse;
import com.takeit.favorite.application.service.FavoriteService;
import com.takeit.favorite.presentation.request.CreateFavoriteRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/favorites")
@RequiredArgsConstructor
public class FavoriteController {
    private final FavoriteService favoriteService;

    @PostMapping
    public CommonResponse<CreateFavoriteResponse> createFavorite(@Valid @RequestBody CreateFavoriteRequest request,
                                                                 @RequestHeader(value = "X-Username") String requesterUsername) {

        return CommonResponse.ofSuccess("찜 하기", favoriteService.addFavorite(request.toDto(), requesterUsername));
    }

    @DeleteMapping("/{favoriteId}")
    public CommonResponse<?> cancelFavorite(@PathVariable UUID favoriteId,
                                            @RequestHeader(value = "X-Username") String requesterUsername) {

        favoriteService.cancelFavorite(favoriteId, requesterUsername);

        return CommonResponse.ofSuccess("찜 제거", null);
    }

    @GetMapping("/user/{username}")
    public CommonResponse<PagedModel<FavoriteResponse>> getUserFavorites(@PathVariable String username,
                                                                         @RequestHeader(value = "X-Username") String requesterUsername,
                                                                         Pageable pageable) {
        return CommonResponse.ofSuccess("유저 찜 목록 조회", favoriteService.getUserFavorites(username, requesterUsername, pageable));
    }
}
