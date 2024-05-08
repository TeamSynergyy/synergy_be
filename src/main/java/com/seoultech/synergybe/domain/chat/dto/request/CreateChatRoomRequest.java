package com.seoultech.synergybe.domain.chat.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CreateChatRoomRequest(
        @NotBlank(message = "채팅방 생성 유저 ID는 필수값입니다.")
        String createUserId,
        @NotBlank(message = "채팅방 참석 유저 ID는 필수값입니다.")
        String attendUserId,
        String roomName

) {
}
