package com.seoultech.synergybe.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CreateUserRequest (
        @NotBlank(message = "이메일은 필수 항목 입니다.")
        String email,
        @NotBlank(message = "비밀번호는 필수 항목입니다.")
        String password,
        @NotBlank(message = "이름은 필수 항목입니다.")
        String name,
        @NotBlank(message = "전공은 필수 항목입니다.")
        String major
) {

}
