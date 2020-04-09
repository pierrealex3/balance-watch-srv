package org.pa.balance.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class ErrorHandlingControllerAdvice {

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    ResponseEntity<String> onConstraintViolationException(ConstraintViolationException e) {
        return new ResponseEntity<>("constraint violation here : " + e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
