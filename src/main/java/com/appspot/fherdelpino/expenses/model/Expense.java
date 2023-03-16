package com.appspot.fherdelpino.expenses.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document("expenses")
@Data
public class Expense {

    @Id
    private String id;
    private String name;
    private Double amount;
    @JsonFormat(pattern = "yyyy-M-d")
    private LocalDate date;
    private String description;
}
