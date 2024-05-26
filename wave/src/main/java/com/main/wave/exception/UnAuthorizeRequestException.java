package com.main.wave.exception;

public class UnAuthorizeRequestException extends RuntimeException {
    @Override
    public String getMessage() {
        return "非法token";
    }
}
