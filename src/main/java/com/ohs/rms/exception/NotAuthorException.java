package com.ohs.rms.exception;

public class NotAuthorException extends RuntimeException {

    public NotAuthorException() {
        super("권한이 없습니다.");
    }
}
