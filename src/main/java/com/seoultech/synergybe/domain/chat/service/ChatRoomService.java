package com.seoultech.synergybe.domain.chat.service;

import com.seoultech.synergybe.domain.chat.domain.ChatRoom;
import com.seoultech.synergybe.domain.chat.dto.request.CreateChatRoomRequest;
import com.seoultech.synergybe.domain.chat.handler.ChatHandler;
import com.seoultech.synergybe.domain.chat.handler.WebSocketSessionList;
import com.seoultech.synergybe.domain.chat.handler.WebSocketSessionMap;
import com.seoultech.synergybe.domain.chat.repository.ChatRoomRepository;
import com.seoultech.synergybe.domain.user.User;
import com.seoultech.synergybe.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final UserService userService;
    private final ChatRoomRepository chatRoomRepository;
    private final WebSocketSessionMap webSocketSessionMap;
    private final ChatHandler chatHandler;

    public void createRoom(CreateChatRoomRequest request) {
        User createUser = userService.getUser(request.createUserId());
        User attendUser = userService.getUser(request.attendUserId());

        ChatRoom chatRoom = ChatRoom.builder()
                .createUser(createUser)
                .attendUser(attendUser)
                .name(request.roomName())
                .build();

        // todo
        // session 생성

        List<WebSocketSession> webSocketSessions = new ArrayList<>();
        WebSocketSessionList webSocketSessionList = WebSocketSessionList.builder()
                .webSocketSessions(webSocketSessions)
                .build();

        Long chatRoomId = chatRoom.getId();
//        webSocketSessionMap.createWebSocketSessionList(chatRoomId, webSocketSessionList);

        // session createUser 연결
//        chatHandler.afterConnectionEstablished();


        chatRoomRepository.save(chatRoom);
    }
}
