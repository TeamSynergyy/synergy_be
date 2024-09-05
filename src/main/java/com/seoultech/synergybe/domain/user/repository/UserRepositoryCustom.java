package com.seoultech.synergybe.domain.user.repository;

import com.seoultech.synergybe.domain.user.User;

import java.util.List;

public interface UserRepositoryCustom {
    User findByEmail(String email);
    List<User> findAllByUserToken(List<String> userToken);
    List<User> findAllByUserId(List<Long> userIds);
}
