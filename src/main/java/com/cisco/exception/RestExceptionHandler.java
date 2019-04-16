package com.cisco.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {IllegalArgumentException.class})
    public ResponseEntity<Object> handleIllegalArgumentException(RuntimeException ex) {
        return ResponseEntity.badRequest().body(new ErrorResponse(Arrays.asList(ex.getMessage())));
    }

    @ExceptionHandler(value = {ConstraintViolationException.class})
    protected ResponseEntity handleConstraintViolationException(ConstraintViolationException ex) {
        try {
            List<String> messages = ex.getConstraintViolations().stream().map(v -> v.getPropertyPath() + ": " + v.getMessage()).collect(Collectors.toList());
            return new ResponseEntity(new ErrorResponse(messages), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity(new ErrorResponse(Arrays.asList(e.getMessage())), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
