package com.takeit.product.application.service;

import com.takeit.product.application.dto.user.UserDto;

public interface UserService {
    UserDto getUser(String username);
}
