package com.seoultech.synergybe.domain.apply.dto.request;

import com.seoultech.synergybe.domain.apply.Apply;
import com.seoultech.synergybe.domain.project.Project;
import com.seoultech.synergybe.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ApplyRequest {
    private String userId;
    private Long projectId;

    public Apply toEntity(User user, Project project) {
        return Apply.builder()
                .user(user)
                .project(project)
                .build();
    }
}
