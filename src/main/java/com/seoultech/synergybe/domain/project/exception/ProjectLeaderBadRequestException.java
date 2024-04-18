package com.seoultech.synergybe.domain.project.exception;

import com.seoultech.synergybe.system.exception.BadRequestException;

import static com.seoultech.synergybe.system.exception.ErrorCode.BAD_REQUEST;

public class ProjectLeaderBadRequestException extends BadRequestException {
    public ProjectLeaderBadRequestException(String message) {
        super(BAD_REQUEST, message);
    }
}
