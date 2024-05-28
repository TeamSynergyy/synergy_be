package com.seoultech.synergybe.domain.chat.dto.response;

import java.util.List;

public record GetChatRoomResponse(
        Long chatRoomId,
        String chatRoomName,
        List<String> userIds
) {
}
