package com.seoultech.synergybe.domain.chat.service;

import com.seoultech.synergybe.domain.chat.domain.ChatRoom;
import com.seoultech.synergybe.domain.chat.dto.request.CreateChatRoomRequest;
import com.seoultech.synergybe.domain.chat.dto.response.GetChatRoomResponse;
import com.seoultech.synergybe.domain.chat.jpa_repository.ChatRoomRepository;
import com.seoultech.synergybe.domain.user.User;
import com.seoultech.synergybe.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final UserService userService;
    private final ChatRoomRepository chatRoomRepository;
    public void createRoom(CreateChatRoomRequest request) {
        User createUser = userService.getUser(request.createUserId());
        User attendUser = userService.getUser(request.attendUserId());

        ChatRoom chatRoom = ChatRoom.builder()
                .createUser(createUser)
                .attendUser(attendUser)
                .name(request.roomName())
                .build();


        chatRoomRepository.save(chatRoom);
    }

    public List<GetChatRoomResponse> getChatRoomsByUserId(String userId) {
        List<ChatRoom> chatRooms = chatRoomRepository.findAllByCreateUserIdOrAttendUserId(userId);

        List<GetChatRoomResponse> getChatRoomResponses = chatRooms.stream().map(
                chatRoom -> new GetChatRoomResponse(
                        chatRoom.getId(), chatRoom.getName()
                )
        ).toList();

        return getChatRoomResponses;
    }
}
