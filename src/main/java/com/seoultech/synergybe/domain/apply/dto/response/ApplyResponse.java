package com.seoultech.synergybe.domain.apply.dto.response;

import com.seoultech.synergybe.domain.apply.Apply;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
@Builder
public class ApplyResponse {
    private String userId;
    private Long projectId;

    public static ApplyResponse from(Apply apply) {
        return new ApplyResponse(apply.getUser().getUserId(), apply.getProject().getId());
    }

    public static List<ApplyResponse> from(List<Apply> applies) {
        return applies.stream()
                .map(apply -> ApplyResponse.builder()
                        .userId(apply.getUser().getUserId())
                        .projectId(apply.getProject().getId())
                        .build())
                .collect(Collectors.toList());
    }
}
