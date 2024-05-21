package com.seoultech.synergybe.domain.user.repository;

import com.seoultech.synergybe.domain.user.User;
import com.seoultech.synergybe.domain.user.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

// data access layer 로써 data에 대한 검증을 여기서 진행할 수 있도록 함
@Component
@RequiredArgsConstructor
public class UserReader {
    private final UserRepository userRepository;

    public User read(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("존재하지 않은 유저입니다."));
    }
}
