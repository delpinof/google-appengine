package com.appspot.fherdelpino.expenses.controller;

import com.appspot.fherdelpino.expenses.model.WeekCalendar;
import com.appspot.fherdelpino.expenses.service.WeekCalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
@RequestMapping("/expense/week")
public class WeekController {

    @Autowired
    private WeekCalendarService service;

    @GetMapping
    @ResponseBody
    public List<WeekCalendar> getWeeks(@RequestParam(required = false) Optional<Integer> pastWeeks) {
        return pastWeeks
                .filter(n -> n > 0)
                .map(n -> IntStream.range(0, n))
                .map(intRage -> intRage
                        .map(operand -> operand * 7)
                        .boxed()
                        .map(number -> LocalDate.now().minusDays(number))
                        .map(service::getWeek)
                        .collect(Collectors.toList())
                )
                .orElse(List.of(service.getWeek(LocalDate.now())));
    }

}
