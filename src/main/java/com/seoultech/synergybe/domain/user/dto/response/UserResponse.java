package com.seoultech.synergybe.domain.user.dto.response;

import com.seoultech.synergybe.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

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
    private String minor;
    private Double temperature;
    private String bio;
    private String interestAreas;
    private String skills;
    private String organization;

    public static UserResponse from(User user) {
        return new UserResponse(user.getUserId(), user.getUsername(), user.getEmail(), user.getProfileImageUrl(),
                "", user.getMajor(), user.getMinor(), user.getTemperature(), user.getBio(), user.getInterestAreas(),
                user.getSkills(), user.getOrganization());
    }

    public static Page<UserResponse> from(Page<User> users) {
        return users.map(user -> UserResponse.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .major(user.getMajor())
                .temperature(user.getTemperature())
                .bio(user.getBio())
                .minor(user.getMinor())
                .interestAreas(user.getInterestAreas())
                .skills(user.getSkills())
                .organization(user.getOrganization())
                .build()
        );
    }

    public static List<UserResponse> from(List<User> users) {
        return users.stream()
                .map(user -> UserResponse.builder()
                        .userId(user.getUserId())
                        .username(user.getUsername())
                        .email(user.getEmail())
                        .profileImageUrl(user.getProfileImageUrl())
                        .major(user.getMajor())
                        .temperature(user.getTemperature())
                        .bio(user.getBio())
                        .minor(user.getMinor())
                        .interestAreas(user.getInterestAreas())
                        .skills(user.getSkills())
                        .organization(user.getOrganization())
                        .build()
                ).collect(Collectors.toList());
    }

    public static List<UserResponse> fromEmpty(List<User> users) {
        return users.stream()
                .map(user -> UserResponse.builder().build()).collect(Collectors.toList());
    }
}
