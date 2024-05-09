package com.seoultech.synergybe.domain.chat.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seoultech.synergybe.domain.chat.domain.ChatType;
import com.seoultech.synergybe.domain.chat.dto.request.ChatMessageRequest;
import com.seoultech.synergybe.domain.chat.exception.WebSocketBadRequestException;
import com.seoultech.synergybe.domain.chat.service.ChatMessageService;
import com.seoultech.synergybe.domain.user.User;
import com.seoultech.synergybe.domain.user.service.UserService;
import com.seoultech.synergybe.system.exception.ErrorCode;
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
public class ChatHandler extends TextWebSocketHandler {
    private WebSocketSessionMap webSocketSessionMap; // 채팅방별 세션리스트 모음
    private ObjectMapper objectMapper;
    private ChatMessageService chatMessageService;
    private UserService userService;



    // 채팅 핸들러 생성
    public ChatHandler(ObjectMapper objectMapper, ChatMessageService chatMessageService, UserService userService) {
        this.webSocketSessionMap = new WebSocketSessionMap();
        this.objectMapper = objectMapper;
        this.chatMessageService = chatMessageService;
        this.userService = userService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    }

    // todo
    // 좀 더 책임을 나누자

    /**
     * 전체 로직
     * - TextMessage에서 payLoad를 가져옴
     * - payLoad 값을 통해 objectMapper를 사용하여 ChatMessageRequest Dto 로 변환
     * - payLoad에 담긴 userId를 통해 user 검증
     * - payLoad에 담긴 chatRoomId를 통해 websocketList 탐색
     *   - 만약 없으면 생성
     * - chatType이 ENTER일 경우 session 에 add
     * - chatType이 TEXT일 경우 sendMessage(), saveMessage() 호출
     * - websocket connection을 close할 경우 해당 session을 websocket List에서 remove
     * @param session
     * @param message
     * @throws Exception
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payLoad = message.getPayload();
        log.info("pay load {}", payLoad);

        // 채팅 Dto로 변환
        ChatMessageRequest chatMessageRequest = objectMapper.readValue(payLoad, ChatMessageRequest.class);
        log.info("session {}", chatMessageRequest.toString());

        // 유저 검증
        User user = userService.getUser(chatMessageRequest.userId());
        if (user == null) {
            throw new WebSocketBadRequestException(ErrorCode.BAD_REQUEST, "유효하지 않은 유저의 메세지 요청입니다.");
        }


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
            // afterConnectionEstablished -> 이 메소들 사용해서 session add는 안되는지 다시 고민
//            afterConnectionEstablished(session);
            log.info("before add session List size : {}", webSocketSessionList.getWebSocketSessions().size());
            // 현재 들어온 세션을 해당 채팅방 세션리스트에 추가
            webSocketSessionList.getWebSocketSessions().add(session);
            log.info("session add | session Id : {}", session.getId());

        } else if (chatMessageRequest.chatType().equals(ChatType.TEXT)) {
            log.info("websocket Session List size : {}",webSocketSessionList.getWebSocketSessions().size());

            // 채팅 전송
            sendMessageToChatRoom(chatMessageRequest, webSocketSessionList);

            // 한사람에 대해서만 저장을 해야함
            saveMessage(chatMessageRequest);

        } else if (chatMessageRequest.chatType().equals(ChatType.IMAGE)) {
            // todo
            // 이미지 혹은 영상 처리
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

        log.info("session remove | session Id : {}", session.getId());
        log.info("after connection closed session List size : {}",webSocketSessions.size());
    }

    private List<WebSocketSession> getSessionListByChatRoomId(Long chatRoomId) {
        return webSocketSessionMap.getWebsocketListHashMap().get(chatRoomId).getWebSocketSessions();
    }

    private void sendMessageToChatRoom(ChatMessageRequest chatMessageRequest, WebSocketSessionList webSocketSessionList) {
        for (WebSocketSession session : webSocketSessionList.getWebSocketSessions()) {
            sendMessage(session, chatMessageRequest.message());
        }
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
