package com.seoultech.synergybe.domain.follow.dto.request;

import jakarta.validation.constraints.NotBlank;


public record CreateFollowRequest(
        @NotBlank(message = "팔로우타입은 필수 항목입니다.")
        String followType
) {

}