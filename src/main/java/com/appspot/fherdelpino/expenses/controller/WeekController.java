package com.appspot.fherdelpino.expenses.controller;

import com.appspot.fherdelpino.expenses.model.WeekCalendar;
import com.appspot.fherdelpino.expenses.service.WeekCalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.nonNull;

@CrossOrigin
@RestController
@RequestMapping("/expense/week")
public class WeekController {

    @Autowired
    private WeekCalendarService service;

    @GetMapping
    @ResponseBody
    public List<WeekCalendar> getWeeks(@RequestParam(required = false) Integer pastWeeks) {
        List<WeekCalendar> weeks = new ArrayList<>();
        WeekCalendar currentWeek = service.getWeek(LocalDate.now());
        weeks.add(currentWeek);
        for (int i = 1; nonNull(pastWeeks) && i <= pastWeeks; i++) {
            weeks.add(service.getWeek(currentWeek.getYear(), currentWeek.getWeek() - i));
        }
        return weeks;
    }

}
