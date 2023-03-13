package com.appspot.fherdelpino.error;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ExpenseNotFoundException extends RuntimeException {

    public ExpenseNotFoundException(String message) {
        super(message);
    }
}
