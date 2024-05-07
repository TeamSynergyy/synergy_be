package com.seoultech.synergybe.domain.chat.handler;

import lombok.Builder;
import lombok.Getter;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;

@Getter
public class WebSocketSessionList {
    private List<WebSocketSession> webSocketSessions;

    @Builder
    public WebSocketSessionList(List<WebSocketSession> webSocketSessions) {
        this.webSocketSessions = webSocketSessions;
    }
}
