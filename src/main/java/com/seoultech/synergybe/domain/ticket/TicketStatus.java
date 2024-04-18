package com.seoultech.synergybe.domain.ticket;

import com.seoultech.synergybe.domain.common.EnumType;

public enum TicketStatus implements EnumType {
    BACKLOG,
    IN_PROGRESS,
    REVIEW,
    DONE
    ;

    @Override
    public String getName() {
        return this.name();
    }
}
