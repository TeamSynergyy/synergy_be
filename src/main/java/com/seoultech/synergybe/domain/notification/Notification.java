package com.seoultech.synergybe.domain.notification;

import com.seoultech.synergybe.domain.user.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long id;

    private String content;

    private Boolean isRead;
    private Long entityId;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public void setRead(Boolean isRead) {
        this.isRead = isRead;
    }

    @Builder
    public Notification(User user, String content, NotificationType type, Long entityId) {
        this.user = user;
        this.content = content;
        this.isRead = false;
        this.type = type;
        this.entityId = entityId;
    }
}
