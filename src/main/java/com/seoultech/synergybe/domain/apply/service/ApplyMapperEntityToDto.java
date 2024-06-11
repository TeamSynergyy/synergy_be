package com.seoultech.synergybe.domain.apply.service;

import com.seoultech.synergybe.domain.apply.Apply;
import com.seoultech.synergybe.domain.apply.dto.response.GetApplyResponse;
import com.seoultech.synergybe.domain.apply.dto.response.GetApplyUserResponse;
import com.seoultech.synergybe.domain.apply.dto.response.GetListApplyResponse;
import com.seoultech.synergybe.domain.apply.dto.response.GetListApplyUserResponse;
import com.seoultech.synergybe.domain.common.PageInfo;
import com.seoultech.synergybe.domain.user.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ApplyMapperEntityToDto {
    public static GetListApplyResponse applyListToResponse(
            List<Apply> applyList
    ) {
        List<GetApplyResponse> getApplyResponses = applyList.stream()
                .map(result -> new GetApplyResponse(
                        result.getApplyToken(),
                        result.getStatus()))
                .toList();
        PageInfo pageInfo = PageInfo.of(applyList.size());
        return new GetListApplyResponse(getApplyResponses, pageInfo);
    }

    public static GetListApplyUserResponse userListToResponse(
            List<User> userList
    ) {
        List<GetApplyUserResponse> getApplyUserResponses = userList.stream()
                .map(GetApplyUserResponse::new)
                .toList();
        PageInfo pageInfo = PageInfo.of(getApplyUserResponses.size());
        return new GetListApplyUserResponse(getApplyUserResponses, pageInfo);
    }
}
