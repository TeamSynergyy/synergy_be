package com.seoultech.synergybe.domain.projectlike.dto.response;

import com.seoultech.synergybe.domain.projectlike.ProjectLike;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
public record ProjectLikeResponse(
        String userId,
        String projectId
) {

//    public static ProjectLikeResponse from(ProjectLike projectLike) {
//        return new ProjectLikeResponse(projectLike.getUser().getUserId(), projectLike.getProject().getId());
//    }
}
