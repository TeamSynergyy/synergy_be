package com.seoultech.synergybe.domain.user.application;

import com.seoultech.synergybe.domain.email.MailService;
import com.seoultech.synergybe.domain.user.dto.request.CreateUserRequest;
import com.seoultech.synergybe.domain.user.dto.request.ValidateNumberRequest;
import com.seoultech.synergybe.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserFacade {
    private final UserService userService;
    private final MailService mailService;

    public void sendValidationNumber(String email) {
        userService.checkEmailDuplicate(email);
        mailService.sendValidateEmail(email);
    }

    public void validateNumber(ValidateNumberRequest request) {
        userService.validateNumber(request);
    }

    public String createUser(CreateUserRequest request) {
        String userToken = userService.createUser(request);

        return userToken;
    }
}
