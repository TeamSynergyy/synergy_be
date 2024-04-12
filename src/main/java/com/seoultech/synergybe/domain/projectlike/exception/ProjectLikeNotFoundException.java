package com.seoultech.synergybe.domain.projectlike.exception;

import com.seoultech.synergybe.system.exception.NotFoundException;

import static com.seoultech.synergybe.system.exception.ErrorCode.NOT_FOUND;

public class ProjectLikeNotFoundException extends NotFoundException {
    public ProjectLikeNotFoundException(String message) {
        super(NOT_FOUND, message);
    }
}
