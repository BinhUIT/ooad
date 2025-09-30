package com.example.ooad.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.ooad.dto.response.GlobalResponse;

@RestControllerAdvice
public class ExceptionHandlerController {
    @ExceptionHandler(RuntimeException.class) 
    public ResponseEntity<GlobalResponse<Void>> handleException(Exception e) {
        GlobalResponse<Void> response = com.example.ooad.exception.ExceptionHandler.getResponseFromException(e);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }
}
