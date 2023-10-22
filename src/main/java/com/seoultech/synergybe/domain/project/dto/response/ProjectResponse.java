package com.seoultech.synergybe.domain.project.dto.response;

import com.seoultech.synergybe.domain.project.Project;
import com.seoultech.synergybe.domain.project.ProjectField;
import com.seoultech.synergybe.domain.project.ProjectStatus;
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

//    private int likes;


    public static ProjectResponse from(Project project) {
        return new ProjectResponse(project.getId(), project.getName(), project.getContent(), project.getField(),
                project.getStatus(), project.getStartAt(), project.getEndAt(), project.getLeaderId(), project.getLocation());
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
                        .build())
                .collect(Collectors.toList());
    }
}
