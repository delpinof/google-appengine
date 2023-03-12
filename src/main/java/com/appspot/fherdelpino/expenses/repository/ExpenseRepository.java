package com.appspot.fherdelpino.expenses.repository;

import com.appspot.fherdelpino.expenses.model.Expense;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepository extends MongoRepository<Expense, String> {
    @Query("{date:{$gte:?0,$lte:?1}}")
    List<Expense> findByDates(LocalDate from, LocalDate to);
}
