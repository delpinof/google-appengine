package com.appspot.fherdelpino.expenses.error;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ExpenseNotFoundException extends RuntimeException {

    public ExpenseNotFoundException(String message) {
        super(message);
    }
}
