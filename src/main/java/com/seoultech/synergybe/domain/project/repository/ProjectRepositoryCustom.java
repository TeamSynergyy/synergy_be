package com.seoultech.synergybe.domain.project.repository;

import com.seoultech.synergybe.domain.project.Project;

import java.util.List;

public interface ProjectRepositoryCustom {
    List<Project> findAllByCreateAtAndLimit(Long offset);

    int countTotalProjectSize();
}
