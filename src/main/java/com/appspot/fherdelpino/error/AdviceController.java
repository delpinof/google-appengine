package com.appspot.fherdelpino.error;

import com.appspot.fherdelpino.security.error.UserConflictException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AdviceController {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntityNotFoundException.class)
    public void handleEntityNotFound() {
        //nothing to do
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(UserConflictException.class)
    public void handleConflict() {
        //nothing to do
    }
}
