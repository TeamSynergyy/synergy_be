package com.seoultech.synergybe.domain.chat.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seoultech.synergybe.domain.chat.domain.ChatType;
import com.seoultech.synergybe.domain.chat.dto.request.ChatMessageRequest;
import com.seoultech.synergybe.domain.chat.service.ChatMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
//@RequiredArgsConstructor
public class ChatHandler extends TextWebSocketHandler {
//    private static List<WebSocketSession> webSocketSessions = new ArrayList<>();
    private WebSocketSessionMap webSocketSessionMap; // 채팅방별 세션리스트 모음
    private ObjectMapper objectMapper;
    private ChatMessageService chatMessageService;



    // 채팅 핸들러 생성
    public ChatHandler(ObjectMapper objectMapper, ChatMessageService chatMessageService) {
        this.webSocketSessionMap = new WebSocketSessionMap();
        this.objectMapper = objectMapper;
        this.chatMessageService = chatMessageService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
//        webSocketSessions.add(session);
        Long chatRoomId = webSocketSessionMap.getKeyFromSession(session);

        // 여기서 request를 어떻게 알지 ?
        if (chatRoomId == null) {
            // 채팅방 생성
//            chatRoomService.createRoom();
        }


//        log.info("session add : " + session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payLoad = message.getPayload();
        log.info("pay load {}", payLoad);

        // 채팅 Dto로 변환
        ChatMessageRequest chatMessageRequest = objectMapper.readValue(payLoad, ChatMessageRequest.class);
        log.info("session {}", chatMessageRequest.toString());

        // payload에 chatroomId 가져옴
        Long chatRoomId = chatMessageRequest.chatRoomId();

        Map<Long, WebSocketSessionList> chatRoomSessionMap = webSocketSessionMap.getWebsocketListHashMap();


        // 현재 채팅방의 세션이 존재하는지 체크
        if (!chatRoomSessionMap.containsKey(chatRoomId)) {
            // 만약 없다면 세션 생성
            WebSocketSessionList webSocketSessionList = WebSocketSessionList.builder()
                    .webSocketSessions(new ArrayList<>())
                    .build();

            // 생성한 세션에 채팅방 Id와 세션을 Map에 저장
            chatRoomSessionMap.put(chatRoomId, webSocketSessionList);
        }

        // 해당 채팅방에 해당하는 세션을 Map에서 가져옴
        WebSocketSessionList webSocketSessionList = chatRoomSessionMap.get(chatRoomId);

        // 만약 입장하는 경우라면 (Type이 Enter 라면)
        if (chatMessageRequest.chatType().equals(ChatType.ENTER)) {
//            afterConnectionEstablished(session);
            // 현재 들어온 세션을 해당 채팅방 세션리스트에 추가
            webSocketSessionList.getWebSocketSessions().add(session);
            log.info("session add / session Id : {}", session.getId());
        }

        // 만약 텍스트를 보낸다면
        if (chatMessageRequest.chatType().equals(ChatType.TEXT)) {
            // 채팅 전송
            sendAndSaveMessageToChatRoom(chatMessageRequest, webSocketSessionList);
        }

    }



    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        Long chatRoomId = webSocketSessionMap.getKeyFromSession(session);



        // 얘가 호출되니까 여기서 removeClosedSession을 호출해야지
        // 그런데 roomId로 해당하는 채팅방의 채팅세션리스트들을 가져와야하는데 ?
        // 그 내용을 넣어줄 수 있을까 ?

        // 채팅방 리스트 가져오기 내가 가지고 있는 정보는 현재 session 정보임
        List<WebSocketSession> webSocketSessions = getSessionListByChatRoomId(chatRoomId);
        webSocketSessions.remove(session);

        log.info("session remove / session Id : {}", session.getId());
    }

    private List<WebSocketSession> getSessionListByChatRoomId(Long chatRoomId) {
        return webSocketSessionMap.getWebsocketListHashMap().get(chatRoomId).getWebSocketSessions();
    }

    private void sendAndSaveMessageToChatRoom(ChatMessageRequest chatMessageRequest, WebSocketSessionList webSocketSessionList) {
        for (WebSocketSession session : webSocketSessionList.getWebSocketSessions()) {
            sendMessage(session, chatMessageRequest.message());
            saveMessage(chatMessageRequest);
        }

//        webSocketSessionList.getWebSocketSessions().parallelStream().forEach(sess -> sendMessage(sess, chatMessageRequest.message()));

    }

    private void saveMessage(ChatMessageRequest chatMessageRequest) {
        chatMessageService.saveChat(chatMessageRequest);
    }

    private <T> void sendMessage(WebSocketSession session, T message) {
        try {
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }
}
