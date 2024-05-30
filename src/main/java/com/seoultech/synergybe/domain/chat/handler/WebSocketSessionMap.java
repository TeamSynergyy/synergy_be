package com.seoultech.synergybe.domain.chat.handler;

import com.seoultech.synergybe.domain.chat.exception.WebSocketBadRequestException;
import com.seoultech.synergybe.system.exception.ErrorCode;
import lombok.Getter;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;


import java.util.HashMap;
import java.util.Map;

@Getter
@Component
public class WebSocketSessionMap {
    private final HashMap<String, WebSocketSessionList> websocketListHashMap;

    public WebSocketSessionMap() {
        this.websocketListHashMap = new HashMap<>();
    }

    // 세션을 이용하여 HashMap의 키 값을 가져오는 메서드
    public String getKeyFromSession(WebSocketSession session) {
        for (Map.Entry<String, WebSocketSessionList> entry : websocketListHashMap.entrySet()) {
            if (entry.getValue().contains(session)) {
                return entry.getKey();
            }
        }
        throw new WebSocketBadRequestException(ErrorCode.BAD_REQUEST, "채팅방 세션을 찾을 수 없습니다.");
    }
}
