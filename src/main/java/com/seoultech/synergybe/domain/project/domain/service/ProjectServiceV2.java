package com.seoultech.synergybe.domain.project.domain.service;

import org.springframework.stereotype.Service;

@Service
public interface ProjectServiceV2 {
    void createProject();

    void validateProjectUser();

    void validateProjectLeader();

    void updateProject();

    void deleteProject();

    void search();
}
