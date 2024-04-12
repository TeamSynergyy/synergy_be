package com.seoultech.synergybe.domain.postlike.exception;

import com.seoultech.synergybe.system.exception.NotFoundException;

import static com.seoultech.synergybe.system.exception.ErrorCode.NOT_FOUND;

public class PostLikeNotFoundException extends NotFoundException {
    public PostLikeNotFoundException(String message) {
        super(NOT_FOUND, message);
    }
}
