package com.seoultech.synergybe.domain.chat.repository;

import com.seoultech.synergybe.domain.chat.domain.ChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChatRepository extends MongoRepository<ChatMessage, Long> {
}
