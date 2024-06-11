package com.seoultech.synergybe.domain.chat.service;

import com.seoultech.synergybe.domain.chat.domain.ChatRoom;
import com.seoultech.synergybe.domain.chat.dto.request.CreateChatRoomRequest;
import com.seoultech.synergybe.domain.chat.dto.response.GetChatRoomResponse;
import com.seoultech.synergybe.domain.chat.jpa_repository.ChatRoomRepository;
import com.seoultech.synergybe.domain.common.generator.IdGenerator;
import com.seoultech.synergybe.domain.common.generator.IdPrefix;
import com.seoultech.synergybe.domain.common.generator.TokenGenerator;
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
    private final TokenGenerator tokenGenerator;
    public void createRoom(CreateChatRoomRequest request) {
        Long chatRoomId = idGenerator.generateId();
        String chatRoomToken = tokenGenerator.generateToken(IdPrefix.CHAT_ROOM);
        User createUser = userService.getUserByToken(request.createUserId());
        User attendUser = userService.getUserByToken(request.attendUserId());

        ChatRoom chatRoom = ChatRoom.builder()
                .id(chatRoomId)
                .chatRoomToken(chatRoomToken)
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
                    newUserIds.add(chatRoom.getCreateUser().getUserToken());
                    newUserIds.add(chatRoom.getAttendUser().getUserToken());
                    return new GetChatRoomResponse(
                            chatRoom.getChatRoomToken(),
                            chatRoom.getName(),
                            newUserIds
                    );
                }
        ).toList();
    }
}
