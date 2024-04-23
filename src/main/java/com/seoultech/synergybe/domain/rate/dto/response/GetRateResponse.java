package com.seoultech.synergybe.domain.rate.dto.response;

import lombok.Builder;


@Builder
public record GetRateResponse(
        String projectId,
        String rateId,
        String giveUserId,
        String receiveUserId,
        int score
) {
//
//    public static RateResponse from(Rate savedRate) {
//        return new RateResponse(savedRate.getProject().getId(), savedRate.getId(), savedRate.getGiveUser().getUserId(),
//                savedRate.getReceiveUser().getUserId(), savedRate.getScore());
//    }
//
//    public static List<RateResponse> from(List<Rate> rates) {
//        return rates.stream()
//                .map(rate -> RateResponse.builder()
//                        .projectId(rate.getProject().getId())
//                        .rateId(rate.getId())
//                        .giveUserId(rate.getGiveUser().getUserId())
//                        .receiveUserId(rate.getReceiveUser().getUserId())
//                        .score(rate.getScore())
//                        .build())
//                .collect(Collectors.toList());
//    }
}
