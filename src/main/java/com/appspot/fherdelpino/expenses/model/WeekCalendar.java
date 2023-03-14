package com.appspot.fherdelpino.expenses.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class WeekCalendar {
    private int year;
    private int week;
    private LocalDate from;
    private LocalDate to;

}
