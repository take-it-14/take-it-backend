package com.takeit.review.application.service;

import com.takeit.review.application.dto.user.UserDto;

public interface UserService {
    UserDto getUser(String username);
}
