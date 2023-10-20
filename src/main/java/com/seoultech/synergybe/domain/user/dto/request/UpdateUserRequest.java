package com.seoultech.synergybe.domain.user.dto.request;

import com.seoultech.synergybe.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UpdateUserRequest {
    private String username;
    private String bio;
    private String major;
}
