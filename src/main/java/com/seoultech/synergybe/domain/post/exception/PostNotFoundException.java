package com.seoultech.synergybe.domain.post.exception;

import com.seoultech.synergybe.system.exception.NotFoundException;

import static com.seoultech.synergybe.system.exception.ErrorCode.NOT_FOUND;

public class PostNotFoundException extends NotFoundException {
    public PostNotFoundException(String message) {
        super(NOT_FOUND, message);
    }
}
