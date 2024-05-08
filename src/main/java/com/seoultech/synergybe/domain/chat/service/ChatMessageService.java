package com.seoultech.synergybe.domain.chat.service;

import com.seoultech.synergybe.domain.chat.domain.ChatMessage;
import com.seoultech.synergybe.domain.chat.domain.ChatType;
import com.seoultech.synergybe.domain.chat.dto.request.ChatMessageRequest;
import com.seoultech.synergybe.domain.chat.mongo_repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;

    public void saveChat(ChatMessageRequest chatMessageRequest) {
        // mongoDB는 UTC로 저장되므로 +9를 해주어 한국시간과 맞춘다
//        LocalDateTime createAt = LocalDateTime.now().plusHours(9);
        LocalDateTime createAt = LocalDateTime.now();

        if (chatMessageRequest.chatType().equals(ChatType.TEXT)) {
            ChatMessage chatMessage = ChatMessage.builder()
                    .chatRoomId(chatMessageRequest.chatRoomId())
                    .userId(chatMessageRequest.userId())
                    .message(chatMessageRequest.message())
                    .chatType(chatMessageRequest.chatType())
                    .createAt(createAt)
                    .build();

            log.info("chatMessage Id" + chatMessage.getId());
            log.info("chatMessage message " + chatMessage.getMessage());

            chatMessageRepository.save(chatMessage);

        } else if (chatMessageRequest.chatType().equals(ChatType.IMAGE)) {
            ChatMessage chatMessage = ChatMessage.builder()
                    .chatRoomId(chatMessageRequest.chatRoomId())
                    .userId(chatMessageRequest.userId())
                    .imageName(chatMessageRequest.imageName())
                    .imageUrl(chatMessageRequest.imageUrl())
                    .chatType(chatMessageRequest.chatType())
                    .createAt(createAt)
                    .build();
            chatMessageRepository.save(chatMessage);
        }
    }

    public List<ChatMessage> getChatListByChatRoomId(Long chatRoomId) {

        return chatMessageRepository.findChatMessagesByChatRoomIdOrderByCreateAtAsc(chatRoomId);
    }
}
