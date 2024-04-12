package com.seoultech.synergybe.system.exception;

public abstract class NotFoundException extends SynergyException {
    protected NotFoundException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
