package com.seoultech.synergybe.domain.chat.domain;

import com.seoultech.synergybe.domain.user.User;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "chat_message")
@Getter
@Builder
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Indexed
    private Long chatRoomId;

    private String message;

    private String userId;

    private Integer readCount;

    private ChatType chatType;

    private String imageName;

    private String imageUrl;
}
