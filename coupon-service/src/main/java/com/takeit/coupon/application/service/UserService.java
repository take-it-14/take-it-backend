package com.takeit.coupon.application.service;

import com.takeit.coupon.application.dto.user.UserDto;

public interface UserService {
    UserDto getUser(String username);
}
