package com.seoultech.synergybe.domain.rate.dto.request;

import com.seoultech.synergybe.domain.project.Project;
import com.seoultech.synergybe.domain.rate.Rate;
import com.seoultech.synergybe.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RateRequest {
    private Long projectId;
    private String receiveUserId;
    private String content;
    private Integer score;

    public Rate toEntity(RateRequest request, Project project, User giveUser, User receiveUser) {
        return Rate.builder()
                .project(project)
                .giveUser(giveUser)
                .receiveUser(receiveUser)
                .content(content)
                .score(request.score)
                .build();
    }
}
