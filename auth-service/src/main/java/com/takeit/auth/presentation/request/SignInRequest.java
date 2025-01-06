package com.takeit.auth.presentation.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record SignInRequest (

    @NotNull
    @Pattern(regexp = "^[a-z0-9]{4,10}$", message = "username은 소문자와 숫자로 구성된 4자 이상, 10자 이하여야 합니다.")
    String username,

    @NotNull
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,15}$",
            message = "password는 대문자, 소문자, 숫자, 특수문자를 포함한 8자 이상, 15자 이하여야 합니다."
    )
    String password
) {

}
