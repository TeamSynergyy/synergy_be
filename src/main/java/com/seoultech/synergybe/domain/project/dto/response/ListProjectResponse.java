package com.seoultech.synergybe.domain.project.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class ListProjectResponse {
    private List<ProjectResponse> content;

    public static ListProjectResponse from(List<ProjectResponse> projectResponses) {
        return new ListProjectResponse(projectResponses);
    }
}
