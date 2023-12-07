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

    /**
     * 클라이언트가 구독을 위해 호출하는 메서드
     *
     * @param userId - 구독하는 클라이언트의 사용자 Id
     * @param lastEventId
     * @return SseEmitter - 서버에서 보낸 이벤트 Emitter
     */
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
            // Completion Callback 등록
            emitter.onCompletion(() -> {
                // 연결이 종료될 때 처리
                emitterRepository.deleteById(id);
                System.out.println("Connection closed: " + id);
            });
        } catch (IOException exception) {
            emitterRepository.deleteById(id);
            throw new RuntimeException("연결 오류");
        }
    }

    public void send(User receiver, NotificationType type, String content, Long id) {
        Notification notification = createNotification(receiver, type, content, id);
        String userId = receiver.getUserId();

        // 로그인 한 유저의 SseEmitter 모두 가져오기
        Map<String, SseEmitter> sseEmitters = emitterRepository.findAllEmitterStartWithByUserId(userId);
        System.out.println("see Emitter 객체" + sseEmitters.toString());
        System.out.println(sseEmitters.size());
        sseEmitters.forEach(
                (key, emitter) -> {
                    System.out.println("saveCache 저장/ 데이터 캐시 저장(유실된 데이터 처리하기 위함)");
                    emitterRepository.saveEventCache(key, notification);
                    System.out.println("데이터 전송");
                    sendToClient(emitter, key, NotificationResponse.from(notification));
                }
        );
    }

    private Notification createNotification(User receiver, NotificationType type, String content, Long id) {
        return Notification.builder()
                .user(receiver)
                .type(type)
                .content(content)
                .entityId(id)
                .build();
    }
}
