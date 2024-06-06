package com.seoultech.synergybe.domain.project.application;

import com.seoultech.synergybe.domain.project.domain.service.ProjectServiceV2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectFacade {
    private final ProjectServiceV2 projectServiceV2;
    public ProjectInfo registerProject(ProjectCommand command) {


    }
}
