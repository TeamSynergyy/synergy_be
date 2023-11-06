package com.seoultech.synergybe.domain.notification.dto.response;

import com.seoultech.synergybe.domain.notification.Notification;
import com.seoultech.synergybe.domain.notification.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class NotificationResponse {
    private Long notificationId;

    private String content;

    private NotificationType type;

    public static NotificationResponse from(Notification notification) {
        return new NotificationResponse(notification.getId(), notification.getContent(), notification.getType());
    }
}
