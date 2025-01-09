package com.takeit.favorite.utils;

import com.takeit.favorite.application.dto.user.UserDto;

public class AccessValidator {

    public static boolean isMaster(UserDto user) {
        return user.role().equals("MASTER");
    }
    public static boolean isManager(UserDto user) {
        return user.role().equals("MANAGER");
    }
    public static boolean isRequesterAuthorized(String username, String requesterUsername) {
        return username.equals(requesterUsername);
    }
}
