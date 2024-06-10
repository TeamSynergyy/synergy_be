package com.seoultech.synergybe.domain.chat.service;

import com.seoultech.synergybe.domain.chat.domain.ChatRoom;
import com.seoultech.synergybe.domain.chat.dto.request.CreateChatRoomRequest;
import com.seoultech.synergybe.domain.chat.dto.response.GetChatRoomResponse;
import com.seoultech.synergybe.domain.chat.jpa_repository.ChatRoomRepository;
import com.seoultech.synergybe.domain.common.generator.IdGenerator;
import com.seoultech.synergybe.domain.common.generator.IdPrefix;
import com.seoultech.synergybe.domain.user.User;
import com.seoultech.synergybe.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final UserService userService;
    private final ChatRoomRepository chatRoomRepository;
    private final IdGenerator idGenerator;
    public void createRoom(CreateChatRoomRequest request) {
        String chatRoomId = idGenerator.generateId(IdPrefix.CHAT_ROOM);
        User createUser = userService.getUser(request.createUserId());
        User attendUser = userService.getUser(request.attendUserId());

        ChatRoom chatRoom = ChatRoom.builder()
                .id(chatRoomId)
                .createUser(createUser)
                .attendUser(attendUser)
                .name(request.roomName())
                .build();


        chatRoomRepository.save(chatRoom);
    }

    public List<GetChatRoomResponse> getChatRoomsByUserId(String userId) {
        List<ChatRoom> chatRooms = chatRoomRepository.findAllByCreateUserIdOrAttendUserId(userId);
        return chatRooms.stream().map(
                chatRoom -> {
                    // 기존 userIds 리스트를 복사하고, 새로운 userId를 추가
                    List<String> newUserIds = new ArrayList<>();
                    newUserIds.add(chatRoom.getCreateUser().getId());
                    newUserIds.add(chatRoom.getAttendUser().getId());
                    return new GetChatRoomResponse(
                            chatRoom.getId(),
                            chatRoom.getName(),
                            newUserIds
                    );
                }
        ).toList();
    }
}
