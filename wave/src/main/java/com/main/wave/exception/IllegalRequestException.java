package com.main.wave.exception;

public class IllegalRequestException extends RuntimeException{

    @Override
    public String getMessage() {
        return "非法请求";
    }
}
