package com.seoultech.synergybe.domain.user.service;

import com.seoultech.synergybe.domain.user.dto.response.UserResponse;
import com.seoultech.synergybe.domain.user.repository.UserRepository;
import com.seoultech.synergybe.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User getUser(String userId) {
        return userRepository.findByUserId(userId);
    }

    public UserResponse getUserInfo(User user) {
        return UserResponse.from(user);
    }
}

