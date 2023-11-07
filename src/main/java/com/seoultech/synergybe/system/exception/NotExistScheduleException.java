package com.seoultech.synergybe.system.exception;

public class NotExistScheduleException extends RuntimeException{
    private static final String MESSAGE = "존재하지 않는 일정입니다";

    public NotExistScheduleException() {
        super(MESSAGE);
    }
}
