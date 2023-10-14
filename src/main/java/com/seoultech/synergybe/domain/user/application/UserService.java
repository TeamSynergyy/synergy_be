package com.seoultech.synergybe.domain.user.application;

import com.seoultech.synergybe.domain.user.dao.UserRepository;
import com.seoultech.synergybe.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User getUser(String userId) {
        return userRepository.findByUserId(userId);
    }
}

