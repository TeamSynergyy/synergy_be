package com.seoultech.synergybe.domain.project.service;

import com.seoultech.synergybe.domain.common.PageInfo;
import com.seoultech.synergybe.domain.project.Project;
import com.seoultech.synergybe.domain.project.dto.response.GetListProjectResponse;
import com.seoultech.synergybe.domain.project.dto.response.GetProjectResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProjectMapperEntityToDto {
    public static GetListProjectResponse projectListToResponse(List<Project> projectList, boolean hasNext) {
        List<GetProjectResponse> getProjectResponses = projectList.stream()
                .map(
                        result -> new GetProjectResponse(
                                result.getId(),
                                result.getName().getName(),
                                result.getContent().getContent(),
                                result.getField().name(),
                                result.getStatus().getName(),
                                result.getPeriod().getStartAt(),
                                result.getPeriod().getEndAt(),
                                result.getLeaderId().getLeaderId(),
                                result.getLocation().getLocation(),
                                result.getProjectUsers().stream().map(projectUser -> projectUser.getUser().getId()).collect(Collectors.toList())
                        )
                )
                .toList();
        PageInfo pageInfo = PageInfo.of(getProjectResponses.size(), hasNext);

        return new GetListProjectResponse(getProjectResponses, pageInfo);
    }
}
