package com.seoultech.synergybe.system.exception;

public class InvalidUpdateTicketException extends RuntimeException {
    private static final String MESSAGE = "티켓 업데이트에 실패하였습니다.";

    public InvalidUpdateTicketException() {
        super(MESSAGE);
    }
}
