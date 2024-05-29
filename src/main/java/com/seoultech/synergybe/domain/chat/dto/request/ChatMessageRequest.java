package com.seoultech.synergybe.domain.chat.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.seoultech.synergybe.domain.chat.domain.ChatType;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public record ChatMessageRequest(
        @NotBlank
        String chatRoomId,

        @NotBlank
        String userId,

        @NotBlank
        String message,

        @NotBlank
        ChatType chatType,
        @NotBlank
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime createAt,
        String imageName,
        String imageUrl
) {
}
