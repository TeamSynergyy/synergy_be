package com.seoultech.synergybe.domain.notice.dto.request;

import com.seoultech.synergybe.domain.notice.Notice;
import com.seoultech.synergybe.domain.project.Project;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class NoticeRequest {
    private Long projectId;
    private Long noticeId;
    private String content;

    public Notice toEntity(String content, Project project) {
        return Notice.builder()
                .content(content)
                .project(project)
                .build();
    }
}
