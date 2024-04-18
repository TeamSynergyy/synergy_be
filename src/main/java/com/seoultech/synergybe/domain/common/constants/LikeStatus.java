package com.seoultech.synergybe.domain.common.constants;

import com.seoultech.synergybe.domain.common.EnumType;

public enum LikeStatus implements EnumType {
    LIKE,
    UN_LIKE
    ;

    @Override
    public String getName() {
        return this.name();
    }
}
