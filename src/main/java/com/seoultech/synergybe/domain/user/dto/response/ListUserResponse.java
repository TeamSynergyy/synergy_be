package com.seoultech.synergybe.domain.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class ListUserResponse {
    private List<UserResponse> content;

    public static ListUserResponse from(List<UserResponse> userResponses) {
        return new ListUserResponse(userResponses);
    }
}
