package com.seoultech.synergybe.domain.project.domain;

import com.seoultech.synergybe.domain.common.EnumType;

public enum ProjectStatus implements EnumType {
    NEW,
    RECRUITMENT,
    IN_PROGRESS,
    COMPLETED
    ;

    @Override
    public String getName() {
        return this.name();
    }
}
