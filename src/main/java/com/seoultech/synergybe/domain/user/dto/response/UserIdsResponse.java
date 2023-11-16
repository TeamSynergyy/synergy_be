package com.seoultech.synergybe.domain.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class UserIdsResponse {
    private List<String> userIds;

    public static UserIdsResponse from(List<String> userIds) {
        return new UserIdsResponse(userIds);
    }
}
