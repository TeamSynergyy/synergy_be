package com.seoultech.synergybe.domain.apply.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateApplyRequest(
        @NotBlank(message = "userId는 필수 항목입니다.")
        String userId
) {
}
