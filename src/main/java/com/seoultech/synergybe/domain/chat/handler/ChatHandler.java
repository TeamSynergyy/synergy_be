package com.seoultech.synergybe.domain.chat.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seoultech.synergybe.domain.chat.domain.ChatType;
import com.seoultech.synergybe.domain.chat.dto.request.ChatMessageRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatHandler extends TextWebSocketHandler {
    private static List<WebSocketSession> webSocketSessions = new ArrayList<>();
    private static WebSocketSessionMap webSocketSessionMap = new WebSocketSessionMap();
    private final ObjectMapper objectMapper;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        webSocketSessions.add(session);
        log.info("session add : " + session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payLoad = message.getPayload();
        log.info("pay load {}", payLoad);

        ChatMessageRequest chatMessageRequest = objectMapper.readValue(payLoad, ChatMessageRequest.class);
        log.info("session {}", chatMessageRequest.toString());

        Long chatRoomId = chatMessageRequest.chatRoomId();
        Map<Long, WebSocketSessionList> chatRoomSessionMap = webSocketSessionMap.getWebsocketListHashMap();


        if (!chatRoomSessionMap.containsKey(chatRoomId)) {
            WebSocketSessionList webSocketSessionList = WebSocketSessionList.builder()
                    .webSocketSessions(new ArrayList<>())
                    .build();

            chatRoomSessionMap.put(chatRoomId, webSocketSessionList);
        }

        WebSocketSessionList webSocketSessionList = chatRoomSessionMap.get(chatRoomId);

        if (chatMessageRequest.chatType().equals(ChatType.ENTER)) {
            webSocketSessionList.getWebSocketSessions().add(session);
        }
        if (webSocketSessionList.getWebSocketSessions().size() >= 3) {
            removeClosedSession(webSocketSessionList.getWebSocketSessions());
        }

        sendMessageToChatRoom(chatMessageRequest, webSocketSessionList);

//        for (WebSocketSession socketSession : webSocketSessions) {
//            socketSession.sendMessage(message);
//        }
    }



    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        webSocketSessions.remove(session);
        log.info("session remove : " + session.getId());
    }

    public void removeClosedSession(List<WebSocketSession> socketSessions) {
        socketSessions.removeIf(sess -> !webSocketSessions.contains(sess));
    }

    private void sendMessageToChatRoom(ChatMessageRequest chatMessageRequest, WebSocketSessionList webSocketSessionList) {
        webSocketSessionList.getWebSocketSessions().parallelStream().forEach(sess -> sendMessage(sess, chatMessageRequest.message()));

    }

    private <T> void sendMessage(WebSocketSession session, T message) {
        try {
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }
}
