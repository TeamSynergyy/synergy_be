package com.seoultech.synergybe.domain.chat.handler;

import lombok.Getter;
import org.springframework.stereotype.Component;


import java.util.HashMap;

@Getter
@Component
public class WebSocketSessionMap {
    private HashMap<Long, WebSocketSessionList> websocketListHashMap = new HashMap<>();

    public void createWebSocketSessionList(Long chatRoomId, WebSocketSessionList webSocketSessionList) {
        websocketListHashMap.put(chatRoomId, webSocketSessionList);
    }
}
