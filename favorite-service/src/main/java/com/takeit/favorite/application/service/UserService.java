package com.takeit.favorite.application.service;

import com.takeit.favorite.application.dto.user.UserDto;

import java.util.List;

public interface UserService {
    UserDto getUser(String username);

    List<UserDto> getUsers(List<Long> idList);
}
