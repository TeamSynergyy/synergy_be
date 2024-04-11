package com.seoultech.synergybe.domain.user.dto.response;

import com.seoultech.synergybe.domain.user.User;

public record CreateUserResponse (String userId) {
    public static CreateUserResponse from(User user) {
        return new CreateUserResponse(user.getUserId());
    }

}
