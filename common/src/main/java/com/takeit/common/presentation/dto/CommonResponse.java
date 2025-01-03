package com.takeit.common.presentation.dto;

import com.takeit.common.exception.ErrorCode;

public record CommonResponse<T> (
        String status,
        String message,
        T data
){
    public static CommonResponse<Void> ofError(ErrorCode code) {
        return new CommonResponse<>("fail", code.getDescription(), null);
    }

    public static CommonResponse<Void> ofError(String message) {
        return new CommonResponse<>("fail", message, null);
    }
    public static <T> CommonResponse<T> ofSuccess(String message, T data) {
        return new CommonResponse<>( "success", message, data);
    }
}
