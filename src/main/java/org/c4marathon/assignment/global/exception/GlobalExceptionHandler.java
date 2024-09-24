package org.c4marathon.assignment.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

   /* @ExceptionHandler(MethodArgumentNotValidException.class)
    public String validationError(MethodArgumentNotValidException exception) {
        BindingResult bindingResult = exception.getBindingResult();


    }*/

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {

        ErrorResponse errorResponse = new ErrorResponse(e.getStatus(), e.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(e.getStatus()));
    }

}
