package com.seoultech.synergybe.domain.user.service;

import com.seoultech.synergybe.domain.user.dto.response.UserResponse;
import com.seoultech.synergybe.domain.user.repository.UserRepository;
import com.seoultech.synergybe.domain.user.User;
import com.seoultech.synergybe.system.exception.NotExistUserException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User getUser(String userId) {
        return userRepository.findByUserId(userId);
    }

    public UserResponse getMyInfo(User user) {
        return UserResponse.from(user);
    }

    public UserResponse getUserInfo(String userId) {
        return UserResponse.from(this.findUserById(userId));
    }

    public User findUserById(String userId) {
        return userRepository.findByUserId(userId);
    }

    public List<User> getUsers(String userIds) {
        return userRepository.findAllByUserId(userIds);
    }
}

