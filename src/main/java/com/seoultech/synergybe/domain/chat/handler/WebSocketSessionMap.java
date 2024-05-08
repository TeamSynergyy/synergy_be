package com.seoultech.synergybe.domain.chat.handler;

import lombok.Getter;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;


import java.util.HashMap;
import java.util.Map;

@Getter
@Component
public class WebSocketSessionMap {
    private final HashMap<Long, WebSocketSessionList> websocketListHashMap;

    public WebSocketSessionMap() {
        this.websocketListHashMap = new HashMap<>();
    }

    // 세션을 이용하여 HashMap의 키 값을 가져오는 메서드
    public Long getKeyFromSession(WebSocketSession session) {
        for (Map.Entry<Long, WebSocketSessionList> entry : websocketListHashMap.entrySet()) {
            if (entry.getValue().contains(session)) {
                return entry.getKey();
            }
        }
        return null; // 세션을 찾지 못한 경우
    }
}
