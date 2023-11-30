package com.seoultech.synergybe.domain.rate.dto.response;

import com.seoultech.synergybe.domain.rate.Rate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
@Builder
public class RateResponse {
    private Long projectId;
    private Long rateId;
    private String giveUserId;
    private String receiveUserId;
    private int score;

    public static RateResponse from(Rate savedRate) {
        return new RateResponse(savedRate.getProject().getId(), savedRate.getId(), savedRate.getGiveUser().getUserId(),
                savedRate.getReceiveUser().getUserId(), savedRate.getScore());
    }

    public static List<RateResponse> from(List<Rate> rates) {
        return rates.stream()
                .map(rate -> RateResponse.builder()
                        .projectId(rate.getProject().getId())
                        .rateId(rate.getId())
                        .giveUserId(rate.getGiveUser().getUserId())
                        .receiveUserId(rate.getReceiveUser().getUserId())
                        .score(rate.getScore())
                        .build())
                .collect(Collectors.toList());
    }
}
