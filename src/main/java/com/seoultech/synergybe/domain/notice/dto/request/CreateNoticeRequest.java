package com.seoultech.synergybe.domain.notice.dto.request;


import jakarta.validation.constraints.NotBlank;

public record CreateNoticeRequest(
        @NotBlank(message = "프로젝트 ID는 필수항목입니다.")
        String projectId,
        @NotBlank(message = "공지사항 내용은 필수항목입니다.")
        String content
) {

//    public Notice toEntity(String content, Project project) {
//        return Notice.builder()
//                .content(content)
//                .project(project)
//                .build();
//    }
}
