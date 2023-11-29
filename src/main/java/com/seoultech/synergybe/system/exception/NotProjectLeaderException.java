package com.seoultech.synergybe.system.exception;

public class NotProjectLeaderException extends RuntimeException {
    private static final String MESSAGE = "프로젝트 리더가 아닙니다.";

    public NotProjectLeaderException() {
        super(MESSAGE);
    }
}
