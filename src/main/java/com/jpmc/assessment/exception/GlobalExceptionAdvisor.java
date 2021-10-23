package com.jpmc.assessment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionAdvisor extends ResponseEntityExceptionHandler {

  @ExceptionHandler(Exception.class)
  public ResponseEntity<String> handleGeneralException(
      Exception ex) {
    return new ResponseEntity<>(ex.getMessage() == null ? "Something went wrong!" : ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<String> handleNotFoundException(
      NotFoundException ex) {
    return new ResponseEntity<>(ex.getMessage() == null ? "Could not find the requested resource!" : ex.getMessage(), HttpStatus.NOT_FOUND);
  }
}