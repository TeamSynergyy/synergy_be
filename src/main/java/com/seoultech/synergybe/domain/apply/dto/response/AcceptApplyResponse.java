package com.seoultech.synergybe.domain.apply.dto.response;

import com.seoultech.synergybe.domain.apply.Apply;
import com.seoultech.synergybe.domain.apply.ApplyStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AcceptApplyResponse {
    private Long applyId;
    private ApplyStatus status;

    public static AcceptApplyResponse from(Apply apply) {
        return new AcceptApplyResponse(apply.getId(), apply.getStatus());
    }
}
