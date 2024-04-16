package com.seoultech.synergybe.domain.apply;

import com.seoultech.synergybe.domain.common.EnumType;

public enum ApplyStatus implements EnumType {
    NEW,
    REJECTED,
    ACCEPT,
    COMPLETED
    ;

    @Override
    public String getName() {
        return this.name();
    }
}
