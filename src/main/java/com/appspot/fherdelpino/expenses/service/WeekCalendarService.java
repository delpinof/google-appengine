package com.appspot.fherdelpino.expenses.service;

import com.appspot.fherdelpino.expenses.model.WeekCalendar;
import lombok.extern.slf4j.Slf4j;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.time.temporal.WeekFields;

@Slf4j
public class WeekCalendarService {

    private final WeekFields broadcastCalendar;

    public WeekCalendarService() {
        broadcastCalendar = WeekFields.of(DayOfWeek.MONDAY, 7);
    }

    public WeekCalendar getWeek(int year, int weekOfYear) {
        LocalDate firstDayOfYear = LocalDate.of(year, 1, 1);
        int firstWeekOfYear = firstDayOfYear.get(broadcastCalendar.weekOfYear());
        LocalDate dateAtWeekNumber = firstDayOfYear.plusWeeks(weekOfYear - firstWeekOfYear);
        return WeekCalendar.builder()
                .week(weekOfYear)
                .year(year)
                .from(getWeekStartDate(dateAtWeekNumber))
                .to(getWeekEndDate(dateAtWeekNumber))
                .build();
    }

    public WeekCalendar getWeek(LocalDate localDate) {
        int weekOfYear = localDate.get(broadcastCalendar.weekOfYear());
        int year = localDate.get(ChronoField.YEAR);
        return WeekCalendar.builder()
                .week(weekOfYear)
                .year(year)
                .from(getWeekStartDate(localDate))
                .to(getWeekEndDate(localDate))
                .build();
    }

    private LocalDate getWeekStartDate(LocalDate localDate) {
        int dayOfWeek = localDate.get(broadcastCalendar.dayOfWeek());
        return localDate.minusDays(dayOfWeek - 1);
    }

    private LocalDate getWeekEndDate(LocalDate localDate) {
        int dayOfWeek = localDate.get(broadcastCalendar.dayOfWeek());
        return localDate.plusDays(7 - dayOfWeek);
    }
}
