package com.seoultech.synergybe.domain.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UpdateUserRequest {
    private String username;
    private String bio;
    private String major;
    private String minor;
    private String interestAreas;
    private String skills;
    private String organization;
}
