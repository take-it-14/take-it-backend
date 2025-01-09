package com.takeit.favorite.application.service;

import com.takeit.favorite.application.dto.user.UserDto;

public interface UserService {
    UserDto getUser(String username);
}
