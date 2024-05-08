package com.seoultech.synergybe.domain.chat.mongo_repository;

import com.seoultech.synergybe.domain.chat.domain.ChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {
    @Query("{chatRoomId :?0}")
    List<ChatMessage> findChatMessagesByChatRoomIdOrderByCreateAtAsc(Long chatRoomId);
}
