package com.takeit.s3.infrastructure.util.dto;

public record S3UploadFile(
        String filename,
        String uri
) {
}
