package com.seoultech.synergybe.domain.user.controller;

import com.seoultech.synergybe.domain.user.service.UserInfo;
import lombok.Builder;
import lombok.Getter;

@Getter
public class GetUserResponse {
    private String userToken;
    private String email;
    private String name;
    private String major;
    private Double temperature;

    @Builder
    public GetUserResponse(UserInfo userInfo) {
        this.userToken = userInfo.getUserToken();
        this.email = userInfo.getEmail();
        this.name = userInfo.getName();
        this.major = userInfo.getMajor();
        this.temperature = userInfo.getTemperature();
    }
}
