package org.konkuk.klab.mtot.controller;

import org.konkuk.klab.mtot.dto.response.ErrorResponse;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleException(RuntimeException e){
        ErrorResponse response = new ErrorResponse(500, e.getMessage());
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(500));
    }
}
