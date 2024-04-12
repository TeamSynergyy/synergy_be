package com.seoultech.synergybe.domain.schedule.exception;

import com.seoultech.synergybe.system.exception.NotFoundException;

import static com.seoultech.synergybe.system.exception.ErrorCode.NOT_FOUND;

public class ScheduleNotFoundException extends NotFoundException {
    public ScheduleNotFoundException(String message) {
        super(NOT_FOUND, message);
    }
}
