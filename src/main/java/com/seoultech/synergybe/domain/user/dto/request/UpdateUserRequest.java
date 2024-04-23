package com.seoultech.synergybe.domain.user.dto.request;


import jakarta.validation.constraints.NotBlank;

public record UpdateUserRequest(
        @NotBlank(message = "이메일은 필수 항목 입니다.")
        String email,
        @NotBlank(message = "이름은 필수 항목 입니다.")
        String name,
        @NotBlank(message = "전공은 필수 항목 입니다.")
        String major
) {
}
