package com.seoultech.synergybe.system.exception;

public class InvalidAccessException extends RuntimeException {
    private static final String MESSAGE = "잘못된 접근입니다";

    public InvalidAccessException() {
        super(MESSAGE);
    }
}
