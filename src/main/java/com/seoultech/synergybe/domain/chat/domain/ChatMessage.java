package com.seoultech.synergybe.domain.chat.domain;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document(collection = "chat_message")
@Getter
public class ChatMessage {
    @Id
    private String id;

    @Indexed
    private String chatRoomId;

    private String message;

    private String userId;

    private ChatType chatType;

    private String imageName;

    private String imageUrl;

    private LocalDateTime createAt;

    @Builder
    public ChatMessage(String chatRoomId, String message, String userId, ChatType chatType, String imageName, String imageUrl,
                       LocalDateTime createAt) {
        this.chatRoomId = chatRoomId;
        this.message = message;
        this.userId = userId;
        this.chatType = chatType;
        this.imageName = imageName;
        this.imageUrl = imageUrl;
        this.createAt = createAt;
    }
}
