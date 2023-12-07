package com.seoultech.synergybe.domain.notification.dto.response;

import com.seoultech.synergybe.domain.notification.Notification;
import com.seoultech.synergybe.domain.notification.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class NotificationResponse {
    private String content;
    private boolean isRead;
    private NotificationType type;
    private Long entityId;

    public static NotificationResponse from(Notification notification) {
        return NotificationResponse.builder()
                .content(notification.getContent())
                .isRead(notification.getIsRead())
                .type(notification.getType())
                .entityId(notification.getEntityId())
                .build();
    }
}
