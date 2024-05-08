package com.seoultech.synergybe.domain.chat.dto.request;

import com.seoultech.synergybe.domain.chat.domain.ChatType;
import jakarta.validation.constraints.NotBlank;

public record ChatMessageRequest(
        @NotBlank
        Long chatRoomId,

        @NotBlank
        String userId,

        @NotBlank
        String message,

        @NotBlank
        ChatType chatType,

        String imageName,
        String imageUrl
) {
}
