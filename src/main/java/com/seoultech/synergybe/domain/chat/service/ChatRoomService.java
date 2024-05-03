package com.seoultech.synergybe.domain.chat.service;

import com.seoultech.synergybe.domain.chat.domain.ChatRoom;
import com.seoultech.synergybe.domain.chat.dto.request.CreateChatRoomRequest;
import com.seoultech.synergybe.domain.chat.repository.ChatRoomRepository;
import com.seoultech.synergybe.domain.user.User;
import com.seoultech.synergybe.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final UserService userService;
    private final ChatRoomRepository chatRoomRepository;

    public void createRoom(CreateChatRoomRequest request) {
        User createUser = userService.getUser(request.createUserId());
        User attendUser = userService.getUser(request.createUserId());
        // todo
        // project User인지 구분

        ChatRoom chatRoom = ChatRoom.builder()
                .createUser(createUser)
                .attendUser(attendUser)
                .name(request.roomName())
                .build();

        chatRoomRepository.save(chatRoom);


    }
}
