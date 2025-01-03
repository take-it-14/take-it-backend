package com.takeit.favorite.domain.repository;

import com.takeit.favorite.domain.entity.Favorite;

public interface FavoriteRepository {
    Favorite save(Favorite favorite);
}
