package com.jian.community.domain.exception;

public class UnauthorizedWriterException extends RuntimeException {
    public UnauthorizedWriterException(String message) {
        super(message);
    }
}
