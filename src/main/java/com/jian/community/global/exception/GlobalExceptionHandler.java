package com.jian.community.global.exception;

import com.jian.community.application.exception.InvalidCredentialsException;
import com.jian.community.application.exception.ResourceNotFoundException;
import com.jian.community.application.exception.UserAlreadyExistsException;
import com.jian.community.domain.exception.*;
import com.jian.community.infrastructure.exception.FileStorageException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Optional;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public FieldErrorResponse handleMethodArgumentNotValid(BindException e) {
        Optional<FieldError> fieldError = e.getBindingResult().getFieldErrors().stream()
                .findFirst();

        // 필드 에러가 없고 글로벌 에러만 있는 경우
        if (fieldError.isEmpty()) {
            String globalMessage = e.getBindingResult().getGlobalErrors().stream()
                    .findFirst()
                    .map(ObjectError::getDefaultMessage)
                    .orElse(ErrorMessage.INVALID_INPUT);

            return new FieldErrorResponse(ErrorCode.INVALID_USER_INPUT, globalMessage, null);
        }

        String field = fieldError.get().getField();
        String validation = fieldError.get().getCode();
        String message = fieldError.get().getDefaultMessage();
        ErrorCode code = "NotBlank".equals(validation)
                ? ErrorCode.USER_INPUT_REQUIRED
                : ErrorCode.INVALID_USER_INPUT;

        return new FieldErrorResponse(code, message, field);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequestException() {
        return new ErrorResponse(ErrorCode.INVALID_CREDENTIALS, ErrorMessage.INVALID_CREDENTIALS);
    }

    @ExceptionHandler(SessionExpiredException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleSessionExpiredException() {
        return new ErrorResponse(ErrorCode.AUTHENTICATION_REQUIRED, ErrorMessage.INVALID_SESSION);
    }

    @ExceptionHandler(UnauthorizedWriterException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleForbiddenException(UnauthorizedWriterException e) {
        return new ErrorResponse(ErrorCode.ACCESS_DENIED, e.getMessage());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleResourceNotFound(ResourceNotFoundException e) {
        return new ErrorResponse(ErrorCode.RESOURCE_NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleUserAlreadyExistsException(UserAlreadyExistsException e) {
        return new ErrorResponse(ErrorCode.USER_ALREADY_EXISTS, e.getMessage());
    }

    @ExceptionHandler(FileStorageException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleFileStorageException(FileStorageException e) {
        return new ErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    @ExceptionHandler(DataConversionException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleDataConversionException() {
        return new ErrorResponse(
                ErrorCode.INTERNAL_SERVER_ERROR,
                ErrorMessage.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleUnexpectedException() {
        return new ErrorResponse(
                ErrorCode.INTERNAL_SERVER_ERROR,
                ErrorMessage.INTERNAL_SERVER_ERROR
        );
    }
}

