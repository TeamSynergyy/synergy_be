package com.seoultech.synergybe.domain.apply.dto.response;

import com.seoultech.synergybe.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class ListApplyUserResponse {
    private List<User> users;
    private int count;

    public static ListApplyUserResponse from(List<User> users) {
        return new ListApplyUserResponse(users, users.size());
    }
}
