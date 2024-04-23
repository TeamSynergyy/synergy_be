package com.seoultech.synergybe.domain.rate.dto.request;

import com.seoultech.synergybe.domain.project.Project;
import com.seoultech.synergybe.domain.rate.Rate;
import com.seoultech.synergybe.domain.user.User;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

public record CreateRateRequest(
        @NotBlank(message = "프로젝트 ID는 필수항목입니다.")
        String projectId,
        @NotBlank(message = "받는 사람의 유저 ID는 필수항목입니다.")
        String receiveUserId,
        @NotBlank(message = "평점 리뷰 내용은 필수항목입니다.")
        String content,
        @NotBlank(message = "평점은 필수항목입니다.")
        Integer score
) {

//    public Rate toEntity(CreateRateRequest request, Project project, User giveUser, User receiveUser) {
//        return Rate.builder()
//                .project(project)
//                .giveUser(giveUser)
//                .receiveUser(receiveUser)
//                .content(content)
//                .score(request.score)
//                .build();
//    }
}
