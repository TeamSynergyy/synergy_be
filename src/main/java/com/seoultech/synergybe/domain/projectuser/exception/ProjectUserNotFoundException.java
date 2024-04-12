package com.seoultech.synergybe.domain.projectuser.exception;

import com.seoultech.synergybe.system.exception.NotFoundException;

import static com.seoultech.synergybe.system.exception.ErrorCode.NOT_FOUND;

public class ProjectUserNotFoundException extends NotFoundException {
    public ProjectUserNotFoundException(String message) {
        super(NOT_FOUND, message);
    }
}
