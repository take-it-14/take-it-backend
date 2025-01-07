package com.takeit.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다. (%s)"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "권한이 없습니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "해당 작업에 대한 권한이 없습니다."),

    // favorite
    FAVORITE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 찜 정보입니다."),

      // auth
    USERNAME_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 존재하는 username 입니다."),
    EMAIL_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 존재하는 email 입니다."),
    PASSWORD_VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "비밀번호는 대문자, 소문자, 숫자, 특수문자를 포함한 8자 이상, 15자 이하여야 합니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 사용자를 찾을 수 없습니다."),
    PASSWORD_NOT_MATCHED(HttpStatus.NOT_FOUND, "비밀번호가 일치하지 않습니다."),

    // review
    FILE_UPLOAD_ERROR(HttpStatus.BAD_REQUEST , "파일 업로드에 실패했습니다"),
    TOO_MANY_PHOTOS(HttpStatus.BAD_REQUEST, "너무 많은 사진을 업로드했습니다."),
    REVIEW_NOT_FOUND(HttpStatus.BAD_REQUEST, "리뷰를 찾을 수 없습니다."),

    FILE_DELETE_ERROR(HttpStatus.BAD_REQUEST, "파일 삭제에 실패했습니다."),

    ;
    private final HttpStatus status;
    private final String description;

    ErrorCode(HttpStatus status, String description) {
        this.status = status;
        this.description = description;
    }
}
