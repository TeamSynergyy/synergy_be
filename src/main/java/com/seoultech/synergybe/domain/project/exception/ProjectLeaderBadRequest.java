package com.seoultech.synergybe.domain.project.exception;

import com.seoultech.synergybe.system.exception.BadRequestException;

import static com.seoultech.synergybe.system.exception.ErrorCode.BAD_REQUEST;

public class ProjectLeaderBadRequest extends BadRequestException {
    public ProjectLeaderBadRequest(String message) {
        super(BAD_REQUEST, message);
    }
}
