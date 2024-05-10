package com.seoultech.synergybe.domain.user.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;

@Builder
public record GetUserAccountResponse(
        String userId,
        String email,
        String name,
        String major,
        Double temperature
) {
    @QueryProjection
    public GetUserAccountResponse(String userId, String email, String name, String major, Double temperature) {
        this.userId = userId;
        this.email = email;
        this.name = name;
        this.major = major;
        this.temperature = temperature;
    }
}
