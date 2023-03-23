package com.appspot.fherdelpino.expenses.controller;

import com.appspot.fherdelpino.expenses.error.ExpenseNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AdviceController {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ExpenseNotFoundException.class)
    public void handleNotFound() {
        //nothing to do
    }
}
