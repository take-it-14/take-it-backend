package com.takeit.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다. (%s)"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "권한이 없습니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "해당 작업에 대한 권한이 없습니다."),



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
