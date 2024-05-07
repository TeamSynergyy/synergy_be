package com.seoultech.synergybe.domain.chat.dto.request;

import com.seoultech.synergybe.domain.chat.domain.ChatType;

public record ChatMessageRequest(
        Long chatRoomId,
//        String userId,
        String message,
        ChatType chatType
//        String imageName,
//        String imageUrl
) {
}
