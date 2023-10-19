package com.seoultech.synergybe.domain.projectlike.dto.response;

import com.seoultech.synergybe.domain.projectlike.ProjectLike;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ProjectLikeResponse {
    private String userId;
    private Long projectId;

    public static ProjectLikeResponse from(ProjectLike projectLike) {
        return new ProjectLikeResponse(projectLike.getUser().getUserId(), projectLike.getProject().getId());
    }
}
