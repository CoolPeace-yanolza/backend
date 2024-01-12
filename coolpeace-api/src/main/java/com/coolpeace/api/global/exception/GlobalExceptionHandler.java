package com.coolpeace.api.global.exception;

import com.coolpeace.core.exception.ApplicationException;
import com.coolpeace.core.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorMessage> handleApplicationException(ApplicationException applicationException) {
        ErrorCode errorCode = applicationException.getErrorCode();
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(ErrorMessage.from(errorCode));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorMessage> handleHttpMessageNotReadableException(HttpMessageNotReadableException httpMessageNotReadableException) {
        ErrorCode errorCode = ErrorCode.INVALID_REQUEST_PARAM;
        Throwable cause = httpMessageNotReadableException.getCause();
        log.error(cause.getMessage(), cause);
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(ErrorMessage.from(errorCode, errorCode.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> handleMethodArgumentNotValidException(MethodArgumentNotValidException methodArgumentNotValidException) {
        ErrorCode errorCode = ErrorCode.INVALID_REQUEST_PARAM;
        BindingResult bindingResult = methodArgumentNotValidException.getBindingResult();
        String errorMessage = bindingResult.getFieldErrors().stream().findFirst().orElseThrow().getDefaultMessage();
        return ResponseEntity.status(errorCode.getHttpStatus())
                        .body(ErrorMessage.from(errorCode, errorMessage));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> handleInternalServerException(Exception exception) {
        log.error(exception.getMessage(), exception);
        ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(ErrorMessage.from(errorCode));
    }
}
