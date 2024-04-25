package com.seoultech.synergybe.domain.apply.service;

import com.seoultech.synergybe.domain.apply.Apply;
import com.seoultech.synergybe.domain.apply.dto.response.GetApplyResponse;
import com.seoultech.synergybe.domain.apply.dto.response.GetApplyUserResponse;
import com.seoultech.synergybe.domain.apply.dto.response.GetListApplyResponse;
import com.seoultech.synergybe.domain.apply.dto.response.GetListApplyUserResponse;
import com.seoultech.synergybe.domain.user.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ApplyMapperEntityToDto {
    public static GetListApplyResponse applyListToResponse(
            List<Apply> applyList
    ) {
        List<GetApplyResponse> getApplyResponses = applyList.stream()
                .map(result -> new GetApplyResponse(
                        result.getId(),
                        result.getStatus()))
                .toList();
        return new GetListApplyResponse(getApplyResponses);
    }

    public static GetListApplyUserResponse userListToResponse(
            List<User> userList
    ) {
        List<GetApplyUserResponse> getApplyUserResponses = userList.stream()
                .map(GetApplyUserResponse::new)
                .toList();
        return new GetListApplyUserResponse(getApplyUserResponses);
    }
}
