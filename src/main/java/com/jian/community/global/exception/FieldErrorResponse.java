package com.jian.community.global.exception;

public record FieldErrorResponse(ErrorCode code, String message, String field) {
}
