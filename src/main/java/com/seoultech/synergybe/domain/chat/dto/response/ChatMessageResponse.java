package com.seoultech.synergybe.domain.chat.dto.response;

import com.seoultech.synergybe.domain.chat.domain.ChatType;

import java.time.LocalDateTime;

public record ChatMessageResponse(
        String id,
        String chatRoomId,
        String userId,
        String message,
        ChatType chatType,
        LocalDateTime createAt,
        String imageName,
        String imageUrl
) {
}
