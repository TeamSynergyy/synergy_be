package com.seoultech.synergybe.domain.user.service;

import com.seoultech.synergybe.domain.user.User;
import lombok.Getter;

@Getter
public class UserInfo {
    private String userToken;
    private String email;
    private String name;
    private String major;
    private Double temperature;

    public UserInfo(User user) {
        this.userToken = user.getUserToken();
        this.email = user.getEmail().getEmail();
        this.name = user.getName().getName();
        this.major = user.getMajor().getMajor();
        this.temperature = user.getTemperature().getTemperature();
    }

}
