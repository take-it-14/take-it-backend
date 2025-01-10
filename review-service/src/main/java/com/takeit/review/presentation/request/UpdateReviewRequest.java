package com.takeit.review.presentation.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public record UpdateReviewRequest(
        @Min(value = 1, message = "1 이상 입력할 수 있습니다.")
        @Max(value = 5, message = "5 이하 입력할 수 있습니다.")
        int stars,
        @Length(max = 255, message = "255자 이하로 입력할 수 있습니다.")
        String comment,
        List<MultipartFile> files,
        List<UUID> deleteFileNames
) {
}
