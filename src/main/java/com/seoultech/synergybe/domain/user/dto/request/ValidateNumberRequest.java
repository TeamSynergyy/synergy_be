package com.seoultech.synergybe.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ValidateNumberRequest (
        @NotBlank(message = "이메일은 필수 항목 입니다.")
        String email,
        @NotBlank(message = "인증번호는 필수 항목입니다.")
        String validationNumber
) {

}
