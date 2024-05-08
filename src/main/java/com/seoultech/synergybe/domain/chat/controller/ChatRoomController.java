package com.seoultech.synergybe.domain.chat.controller;

import com.seoultech.synergybe.domain.chat.domain.ChatRoom;
import com.seoultech.synergybe.domain.chat.dto.request.CreateChatRoomRequest;
import com.seoultech.synergybe.domain.chat.dto.response.GetChatRoomResponse;
import com.seoultech.synergybe.domain.chat.service.ChatRoomService;
import com.seoultech.synergybe.system.config.login.LoginUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/rooms")
public class ChatRoomController {
    private final ChatRoomService chatRoomService;

    @PostMapping
    public ResponseEntity<Void> createChatRoom(@Valid @RequestBody CreateChatRoomRequest request) {
        chatRoomService.createRoom(request);

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<GetChatRoomResponse>> getChatRoomList(@LoginUser String userId) {

        return ResponseEntity.status(HttpStatus.OK).body(chatRoomService.getChatRoomsByUserId(userId));
    }
}
