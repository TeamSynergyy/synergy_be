package com.seoultech.synergybe.domain.project.interfaces.dto.response;

import lombok.Builder;
import org.springframework.data.geo.Point;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record GetProjectResponse(
        String projectToken,
        String name,
        String content,
        String field,
        String status,
        LocalDateTime startAt,
        LocalDateTime endAt,
        String leaderId,

        Point location,
        List<String> teamUserIds
) {


//    public static ProjectResponse from(Project project) {
//        return new ProjectResponse(project.getId(), project.getName(), project.getContent(), project.getField(),
//                project.getStatus(), project.getStartAt(), project.getEndAt(), project.getLeaderId(), project.getLocation(),
//                project.getProjectUsers().stream().map(projectUser -> projectUser.getUser().getUserId()).collect(Collectors.toList()));
//    }
//
//    public static Page<ProjectResponse> from(Page<Project> projects) {
//        return projects.map(project -> ProjectResponse.builder()
//                .projectId(project.getId())
//                .name(project.getName())
//                .content(project.getContent())
//                .field(project.getField())
//                .status(project.getStatus())
//                .startAt(project.getStartAt())
//                .endAt(project.getEndAt())
//                .leaderId(project.getLeaderId())
//                .teamUserIds(project.getProjectUsers().stream().map(projectUser -> projectUser.getUser().getUserId()).collect(Collectors.toList()))
//                .build()
//        );
//    }
//
//    public static List<ProjectResponse> from(List<Project> projects) {
//        return projects.stream()
//                .map(project -> ProjectResponse.builder()
//                        .projectId(project.getId())
//                        .name(project.getName())
//                        .content(project.getContent())
//                        .field(project.getField())
//                        .status(project.getStatus())
//                        .startAt(project.getStartAt())
//                        .endAt(project.getEndAt())
//                        .leaderId(project.getLeaderId())
//                        .teamUserIds(project.getProjectUsers().stream().map(projectUser -> projectUser.getUser().getUserId()).collect(Collectors.toList()))
//                        .build())
//                .collect(Collectors.toList());
//    }
//
//    public static List<ProjectResponse> fromEmpty(List<Project> projects) {
//        return projects.stream()
//                .map(project -> ProjectResponse.builder().build()).collect(Collectors.toList());
//    }
}
