package com.seoultech.synergybe.domain.user.dto.response;

import com.seoultech.synergybe.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserResponse {
    private String userId;
    private String username;
    private String email;
    private String avatar;
    private String backImage;
    private String major;
    private String temperature;
    private String bio;

    public static UserResponse from(User user) {
        return new UserResponse(user.getUserId(), user.getUsername(), user.getEmail(), user.getProfileImageUrl(),
                "", "", "", "");
    }
}
