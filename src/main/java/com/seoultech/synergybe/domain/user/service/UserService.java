package com.seoultech.synergybe.domain.user.service;

import com.seoultech.synergybe.domain.common.paging.ListResponse;
import com.seoultech.synergybe.domain.user.User;
import com.seoultech.synergybe.domain.user.dto.request.CreateUserRequest;
import com.seoultech.synergybe.domain.user.dto.request.ValidateNumberRequest;
import com.seoultech.synergybe.domain.user.dto.response.GetUserAccountResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface UserService {
    String createUser(CreateUserRequest request);

    void checkEmailDuplicate(String email);

    void validateNumber(ValidateNumberRequest request);

    User getUserByToken(String userToken);

    User getUserById(Long userId);

    GetUserAccountResponse getUserInfo(String userId);

    List<User> getUsers(List<Long> userIds);

    Page<User> searchAllUsers(String keyword, Pageable pageable);

    Specification<User> search(String keyword);

    void updateMyInfo(String userId, String email, String name, String major);

    ListResponse<GetUserAccountResponse> getSimilarUserListByUser(String userId, Long end);

    void generateAccessTokenByRefreshToken(HttpServletRequest request, HttpServletResponse response);

    List<Long> getUserIds(List<String> userToken);
}
