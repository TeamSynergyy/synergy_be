package com.seoultech.synergybe.domain.chat.handler;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayList;
import java.util.List;

@Component
public class WebSocketSessionList {
    private List<WebSocketSession> webSocketSessions = new ArrayList<>();

    public void createWebSocketSession() {

    }
}
