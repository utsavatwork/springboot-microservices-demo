package com.utsav.order_service.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {
  
  @ExceptionHandler(OrderNotFoundException.class)
  public ResponseEntity<ApiErrorResponse> orderNotFoundException(OrderNotFoundException ex, HttpServletRequest request) {
    return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request.getRequestURI());
  }

  private ResponseEntity<ApiErrorResponse> buildResponse(
      HttpStatus status, String message, String path) {
    ApiErrorResponse response = 
        new ApiErrorResponse(LocalDateTime.now(), status.value(), status.getReasonPhrase(), message, path);
    return ResponseEntity.status(status).body(response);
  }
}
