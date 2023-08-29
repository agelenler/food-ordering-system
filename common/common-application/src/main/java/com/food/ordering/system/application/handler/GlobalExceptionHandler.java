package com.food.ordering.system.application.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value = {Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDTO handleException(Exception exception) {
        log.error(exception.getMessage(), exception);
        return ErrorDTO.builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .message("Unexpected error!")
                .build();
    }

    @ResponseBody
    @ExceptionHandler(value = {ValidationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleException(ValidationException validationException) {
       ErrorDTO errorDTO;
       if (validationException instanceof ConstraintViolationException) {
           String violations = extractViolationsFromException((ConstraintViolationException) validationException);
           log.error(violations, validationException);
           errorDTO = ErrorDTO.builder()
                   .code(HttpStatus.BAD_REQUEST.getReasonPhrase())
                   .message(violations)
                   .build();
       } else {
           String exceptionMessage = validationException.getMessage();
           log.error(exceptionMessage, validationException);
           errorDTO = ErrorDTO.builder()
                   .code(HttpStatus.BAD_REQUEST.getReasonPhrase())
                   .message(exceptionMessage)
                   .build();
       }
       return errorDTO;
    }

    private String extractViolationsFromException(ConstraintViolationException validationException) {
        return validationException.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining("--"));
    }

}
