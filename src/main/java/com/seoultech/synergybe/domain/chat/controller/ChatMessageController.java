package com.seoultech.synergybe.domain.chat.controller;

import com.seoultech.synergybe.domain.chat.domain.ChatMessage;
import com.seoultech.synergybe.domain.chat.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
public class ChatMessageController {
    private final ChatMessageService chatMessageService;

    @GetMapping("/{chatRoomId}")
    public List<ChatMessage> getChatList(@PathVariable("chatRoomId") String chatRoomId) {

        return chatMessageService.getChatListByChatRoomId(chatRoomId);
    }
}
