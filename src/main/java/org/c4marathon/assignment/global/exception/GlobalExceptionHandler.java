package org.c4marathon.assignment.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.c4marathon.assignment.global.exception.exceptioncode.ExceptionCode;
import org.c4marathon.assignment.global.exception.response.ErrorResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.sql.SQLIntegrityConstraintViolationException;

import static org.c4marathon.assignment.global.exception.exceptioncode.ExceptionCode.INVALID_REQUEST;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ErrorResponse> handleAuthException(final AuthException exception) {

        return ResponseEntity.status(exception.getHttpStatus())
                             .body(new ErrorResponse(exception.getCode(), exception.getMessage()));
    }

    @ExceptionHandler(AccountException.class)
    public ResponseEntity<ErrorResponse> handleAccountException(final AccountException exception) {

        return ResponseEntity.status(exception.getHttpStatus())
                .body(new ErrorResponse(exception.getCode(), exception.getMessage()));
    }

    @ExceptionHandler(TransactionException.class)
    public ResponseEntity<ErrorResponse> handleTransactionException(final TransactionException exception) {

        return ResponseEntity.status(exception.getHttpStatus())
                .body(new ErrorResponse(exception.getCode(), exception.getMessage()));
    }

    @ExceptionHandler(ProductException.class)
    public ResponseEntity<ErrorResponse> handleProductException(final ProductException exception) {

        return ResponseEntity.status(exception.getHttpStatus())
                .body(new ErrorResponse(exception.getCode(), exception.getMessage()));
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleSqlIntegrityConstraintViolationException() {
        ExceptionCode exceptionCode = INVALID_REQUEST;
        return ResponseEntity.status(exceptionCode.getHttpStatus())
                             .body(new ErrorResponse(exceptionCode.getCode(), exceptionCode.getMessage()));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException() {
        ExceptionCode exceptionCode = INVALID_REQUEST;
        return ResponseEntity.status(exceptionCode.getHttpStatus())
                .body(new ErrorResponse(exceptionCode.getCode(), exceptionCode.getMessage()));
    }

}
