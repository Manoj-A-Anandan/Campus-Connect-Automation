package com.campusconnect.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import com.campusconnect.dto.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse> handleResourceNotFound(ResourceNotFoundException ex, WebRequest request) {
        return new ResponseEntity<>(ApiResponse.error(ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiResponse> handleValidationException(ValidationException ex, WebRequest request) {
        return new ResponseEntity<>(ApiResponse.error(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleGlobalException(Exception ex, WebRequest request) {
        return new ResponseEntity<>(ApiResponse.error("An error occurred: " + ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
