package com.seoultech.synergybe.domain.notification.controller;

import com.seoultech.synergybe.domain.notification.service.NotificationService;
import com.seoultech.synergybe.system.config.login.LoginUser;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping(value = "/subscribe", produces = "text/event-stream")
    public SseEmitter subscribe(@LoginUser String userId, @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId) {
        return notificationService.subscribe(userId, lastEventId);
    }
}
