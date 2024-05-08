package com.seoultech.synergybe.domain.chat.dto.response;

public record GetChatRoomResponse(
        Long chatRoomId,
        String chatRoomName
) {
}
