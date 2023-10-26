package com.seoultech.synergybe.domain.project.dto.response;

import com.seoultech.synergybe.domain.project.Project;
import com.seoultech.synergybe.domain.project.ProjectField;
import com.seoultech.synergybe.domain.project.ProjectStatus;
import com.seoultech.synergybe.domain.projectuser.ProjectUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.geo.Point;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
@Builder
public class ProjectResponse {
    private Long projectId;

    private String name;

    private String content;

    private ProjectField field;

    private ProjectStatus status;

    private LocalDateTime startAt;

    private LocalDateTime endAt;

    private String leaderId;

    private Point location;

    private List<String> teamUserIds;


    public static ProjectResponse from(Project project) {
        return new ProjectResponse(project.getId(), project.getName(), project.getContent(), project.getField(),
                project.getStatus(), project.getStartAt(), project.getEndAt(), project.getLeaderId(), project.getLocation(),
                project.getProjectUsers().stream().map(projectUser -> projectUser.getUser().getUserId()).collect(Collectors.toList()));
    }

    public static Page<ProjectResponse> from(Page<Project> projects) {
        return projects.map(project -> ProjectResponse.builder()
                .projectId(project.getId())
                .name(project.getName())
                .content(project.getContent())
                .field(project.getField())
                .status(project.getStatus())
                .startAt(project.getStartAt())
                .endAt(project.getEndAt())
                .leaderId(project.getLeaderId())
                .teamUserIds(project.getProjectUsers().stream().map(projectUser -> projectUser.getUser().getUserId()).collect(Collectors.toList()))
                .build()
        );
    }

    public static List<ProjectResponse> from(List<Project> projects) {
        return projects.stream()
                .map(project -> ProjectResponse.builder()
                        .projectId(project.getId())
                        .name(project.getName())
                        .content(project.getContent())
                        .field(project.getField())
                        .status(project.getStatus())
                        .startAt(project.getStartAt())
                        .endAt(project.getEndAt())
                        .leaderId(project.getLeaderId())
                        .teamUserIds(project.getProjectUsers().stream().map(projectUser -> projectUser.getUser().getUserId()).collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());
    }
}
