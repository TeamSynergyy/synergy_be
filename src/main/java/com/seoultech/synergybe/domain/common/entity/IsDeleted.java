package com.seoultech.synergybe.domain.common.entity;

import jakarta.persistence.Column;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class IsDeleted {
    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;

    public IsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public void updateDeleted() {
        this.isDeleted = true;
    }
}
