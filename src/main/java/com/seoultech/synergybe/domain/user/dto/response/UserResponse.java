package com.seoultech.synergybe.domain.user.dto.response;

import com.seoultech.synergybe.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

@AllArgsConstructor
@Getter
@Builder
public class UserResponse {
    private String userId;
    private String username;
    private String email;
    private String profileImageUrl;
    private String backImage;
    private String major;
    private String temperature;
    private String bio;

    public static UserResponse from(User user) {
        return new UserResponse(user.getUserId(), user.getUsername(), user.getEmail(), user.getProfileImageUrl(),
                "", "", "", "");
    }

    public static Page<UserResponse> from(Page<User> users) {
        return users.map(user -> UserResponse.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .major(user.getMajor())
                .temperature(user.getTemperature())
                .bio(user.getBio())
                .build()
        );
    }
}
