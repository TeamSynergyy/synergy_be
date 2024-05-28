package com.seoultech.synergybe.domain.chat.dto.response;

import com.seoultech.synergybe.domain.chat.domain.ChatType;

public record ChatMessageResponse(
        String id,
        Long chatRoomId,
        String userId,
        String message,
        ChatType chatType,
        String imageName,
        String imageUrl
) {
}
