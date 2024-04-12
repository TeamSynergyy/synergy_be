package com.seoultech.synergybe.domain.follow.exception;

import com.seoultech.synergybe.system.exception.NotFoundException;

import static com.seoultech.synergybe.system.exception.ErrorCode.NOT_FOUND;

public class FollowNotFoundException extends NotFoundException {
    public FollowNotFoundException(String message) {
        super(NOT_FOUND, message);
    }
}
