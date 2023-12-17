package com.seoultech.synergybe.domain.notification.controller;

import com.seoultech.synergybe.domain.notification.service.NotificationService;
import com.seoultech.synergybe.system.config.login.LoginUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping(value = "/api/v1")
@RequiredArgsConstructor
@Tag(name = "알림 api")
public class NotificationController {
    private final NotificationService notificationService;

    @Operation(summary = "알림 구독", description = "userId에 대해 알림을 구독합니다.")
    @GetMapping(value = "/subscribe", produces = "text/event-stream")
    public SseEmitter subscribe(@LoginUser String userId, @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId) {
        return notificationService.subscribe(userId, lastEventId);
    }
}
