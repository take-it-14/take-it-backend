package com.takeit.favorite.presentation.controller;

import com.takeit.common.presentation.dto.CommonResponse;
import com.takeit.favorite.application.dto.favorite.CreateFavoriteResponse;
import com.takeit.favorite.application.service.FavoriteService;
import com.takeit.favorite.presentation.request.CreateFavoriteRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/favorites")
@RequiredArgsConstructor
public class FavoriteController {
    private final FavoriteService favoriteService;

    @PostMapping
    public CommonResponse<CreateFavoriteResponse> createFavorite(@Valid @RequestBody CreateFavoriteRequest request,
                                                                 @RequestHeader(value = "X-Username") String username) {

        return CommonResponse.ofSuccess("찜 하기", favoriteService.createFavorite(request.toDto(), username));
    }

}
