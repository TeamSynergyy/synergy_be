package com.seoultech.synergybe.domain.project.exception;

import com.seoultech.synergybe.system.exception.NotFoundException;

import static com.seoultech.synergybe.system.exception.ErrorCode.NOT_FOUND;

public class ProjectNotFoundException extends NotFoundException {
    public ProjectNotFoundException(String message) {
        super(NOT_FOUND, message);
    }
}
