package com.seoultech.synergybe.domain.notification.service;

import com.seoultech.synergybe.domain.notification.Notification;
import com.seoultech.synergybe.domain.notification.NotificationType;
import com.seoultech.synergybe.domain.notification.dto.response.NotificationResponse;
import com.seoultech.synergybe.domain.notification.repository.EmitterRepository;
import com.seoultech.synergybe.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;

    private final EmitterRepository emitterRepository;

    public SseEmitter subscribe(String userId, String lastEventId) {
        String id = userId + "_" + System.currentTimeMillis();

        SseEmitter emitter = emitterRepository.save(id, new SseEmitter(DEFAULT_TIMEOUT));

        this.sendToClient(emitter, id, "EventStream Created. [memberId = " + userId + "]");

        if (!lastEventId.isEmpty()) {
            Map<String, Object> events = emitterRepository.findAllEventCacheStartWithByUserId(userId);
            events.entrySet().stream()
                    .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
                    .forEach(entry -> sendToClient(emitter, entry.getKey(), entry.getValue()));
            System.out.println("isEmpty");
        }
        return emitter;
    }

    private void sendToClient(SseEmitter emitter, String id, Object data) {
        try {
            emitter.send(SseEmitter.event()
                    .id(id)
                    .name("sse")
                    .data(data));
            System.out.println("id : " + id + " ||      data : " + data);
        } catch (IOException exception) {
            emitterRepository.deleteById(id);
            throw new RuntimeException("연결 오류");
        }
    }

    public void send(User receiver, NotificationType type, String content, Long id) {
        Notification notification = Notification.builder()
                .user(receiver).type(type).content(content).entityId(id).build();
        String userId = receiver.getUserId();

        Map<String, SseEmitter> sseEmitters = emitterRepository.findAllEmitterStartWithByUserId(userId);
        System.out.println(sseEmitters.size());
        System.out.println("see Emitter 객체" + sseEmitters.toString());
        sseEmitters.forEach(
                (key, emitter) -> {
                    emitterRepository.saveEventCache(key, notification);
                    System.out.println("saveCache 저장/ 데이터 캐시 저장(유실된 데이터 처리하기 위함)");
                    sendToClient(emitter, key, NotificationResponse.from(notification));
                    System.out.println("데이터 전송");
                }
        );
    }

    private Notification createNotification(User receiver, String content) {
        return Notification.builder()
                .user(receiver)
                .content(content)
                .isRead(false)
                .build();
    }
}
