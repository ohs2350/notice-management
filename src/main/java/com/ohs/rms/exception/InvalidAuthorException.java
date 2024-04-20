package com.ohs.rms.exception;

public class InvalidAuthorException extends RuntimeException {

    public InvalidAuthorException() {
        super("권한이 없습니다.");
    }
}
