package com.seoultech.synergybe.domain.user.application;

import com.seoultech.synergybe.domain.common.RandomNumber;
import com.seoultech.synergybe.domain.common.paging.ListResponse;
import com.seoultech.synergybe.domain.email.MailService;
import com.seoultech.synergybe.domain.user.User;
import com.seoultech.synergybe.domain.user.controller.GetUserResponse;
import com.seoultech.synergybe.domain.user.dto.request.CreateUserRequest;
import com.seoultech.synergybe.domain.user.dto.request.UpdateUserRequest;
import com.seoultech.synergybe.domain.user.dto.request.ValidateNumberRequest;
import com.seoultech.synergybe.domain.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserFacade {
    private final UserService userService;
    private final MailService mailService;
    private final RandomNumber randomNumber;

    public void sendValidationNumber(String email) {
        userService.checkEmailDuplicate(email);
        String authNumber = randomNumber.generateRandomNumber();
        mailService.sendValidateEmail(email, authNumber);
    }

    public void validateNumber(ValidateNumberRequest request) {
        userService.validateNumber(request);
    }

    public String createUser(CreateUserRequest request) {
        String userToken = userService.createUser(request);

        return userToken;
    }

    public GetUserResponse getUserInfo(String userToken) {
        GetUserResponse response = userService.getUserInfo(userToken);

        return response;
    }

    public Page<User> searchAllUsers(String search, Pageable pageable) {
        Page<User> response = userService.searchAllUsers(search, pageable);

        return response;
    }

    public void updateMyInfo(String userToken, UpdateUserRequest request) {
        userService.updateMyInfo(userToken, request);
    }

    public ListResponse<GetUserResponse> getSimilarUsers(String userToken, Long end) {
        ListResponse<GetUserResponse> response = userService.getSimilarUserListByUser(userToken, end);

        return response;
    }

    public void generateAccessTokenByRefreshToken(HttpServletRequest request, HttpServletResponse response) {
        userService.generateAccessTokenByRefreshToken(request, response);
    }
}
