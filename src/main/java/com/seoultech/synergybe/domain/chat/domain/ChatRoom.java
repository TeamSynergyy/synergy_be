package com.seoultech.synergybe.domain.chat.domain;

import com.seoultech.synergybe.domain.user.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "chat_room")
@Getter
@NoArgsConstructor
public class ChatRoom {
    @Id
    @Column(name = "chat_room_id")
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "create_user_id", nullable = false)
    private User createUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attend_user_id", nullable = false)
    private User attendUser;

    @Column(name = "name")
    private String name;


    @Builder
    public ChatRoom(String id, User createUser, User attendUser, String name) {
        this.id = id;
        this.createUser = createUser;
        this.attendUser = attendUser;
        this.name = name;
    }

}
