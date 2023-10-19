package com.seoultech.synergybe.domain.apply.dto.response;

import com.seoultech.synergybe.domain.apply.Apply;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ApplyResponse {
    private String userId;
    private Long projectId;

    public static ApplyResponse from(Apply apply) {
        return new ApplyResponse(apply.getUser().getUserId(), apply.getProject().getId());
    }
}
