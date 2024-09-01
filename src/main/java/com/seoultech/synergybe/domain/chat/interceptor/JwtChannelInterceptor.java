package com.seoultech.synergybe.domain.chat.interceptor;

import com.seoultech.synergybe.domain.user.service.UserServiceImpl;
import com.seoultech.synergybe.system.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class JwtChannelInterceptor implements ChannelInterceptor {
    private final UserServiceImpl userService;
    private final JwtUtil jwtUtil;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        System.out.println("message : " + message);
        System.out.println("message header ; " + message.getHeaders());
        System.out.println("token : " + accessor.getNativeHeader("Authorization"));
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            jwtUtil.validateToken(Objects.requireNonNull(accessor.getFirstNativeHeader("Authorization")).substring(7));
        }
        return message;
    }
}
