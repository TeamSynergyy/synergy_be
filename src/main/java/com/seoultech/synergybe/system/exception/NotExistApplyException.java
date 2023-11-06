package com.seoultech.synergybe.system.exception;

public class NotExistApplyException extends RuntimeException {
    private static final String MESSAGE = "존재하지 않는 지원입니다";

    public NotExistApplyException() {
        super(MESSAGE);
    }
}
