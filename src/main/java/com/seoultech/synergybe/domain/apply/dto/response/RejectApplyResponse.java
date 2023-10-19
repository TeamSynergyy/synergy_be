package com.seoultech.synergybe.domain.apply.dto.response;

import com.seoultech.synergybe.domain.apply.Apply;
import com.seoultech.synergybe.domain.apply.ApplyStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RejectApplyResponse {
    private Long applyId;

    private ApplyStatus status;

    public static RejectApplyResponse from(Apply apply) {
        return new RejectApplyResponse(apply.getId(), apply.getStatus());
    }
}
