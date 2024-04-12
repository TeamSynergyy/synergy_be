package com.seoultech.synergybe.system.exception;

public class BadRequestException extends SynergyException {
    protected BadRequestException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
