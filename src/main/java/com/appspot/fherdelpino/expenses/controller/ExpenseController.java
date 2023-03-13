package com.appspot.fherdelpino.expenses.controller;

import com.appspot.fherdelpino.error.ExpenseNotFoundException;
import com.appspot.fherdelpino.expenses.model.Expense;
import com.appspot.fherdelpino.expenses.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/expense")
public class ExpenseController {

    @Autowired
    ExpenseRepository expenseRepository;

    @PostMapping
    @ResponseBody
    public Expense createExpense(@RequestBody Expense expense) {
        return expenseRepository.save(expense);
    }

    @GetMapping("/all")
    @ResponseBody
    public List<Expense> getAll() {
        return expenseRepository.findAll();
    }

    @GetMapping
    @ResponseBody
    public List<Expense> getByDates(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
                                    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return expenseRepository.findByDates(from, to);
    }

    @DeleteMapping("/{id}")
    public void deleteExpense(@PathVariable String id) {
        expenseRepository.findById(id)
                .ifPresentOrElse(
                        expense -> expenseRepository.deleteById(expense.getId()),
                        () -> {
                            throw new ExpenseNotFoundException();
                        }
                );
    }

    @GetMapping("/{id}")
    @ResponseBody
    public Expense getExpense(@PathVariable String id) {
        return expenseRepository.findById(id)
                .orElseThrow(() -> new ExpenseNotFoundException());
    }

}
