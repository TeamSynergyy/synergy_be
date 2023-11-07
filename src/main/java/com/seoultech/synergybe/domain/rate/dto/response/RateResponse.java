package com.seoultech.synergybe.domain.rate.dto.response;

import com.seoultech.synergybe.domain.rate.Rate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RateResponse {
    private Long projectId;
    private int score;

    @Builder
    public static RateResponse from(Rate savedRate) {
        return new RateResponse(savedRate.getProject().getId(), savedRate.getScore());
    }
}
