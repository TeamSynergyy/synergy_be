package com.seoultech.synergybe.domain.project.infrastructure;

import com.seoultech.synergybe.domain.project.domain.Project;

import java.util.List;

public interface ProjectRepositoryCustom {
    List<Project> findAllByCreateAtAndLimit(Long offset);

    int countTotalProjectSize();
}
