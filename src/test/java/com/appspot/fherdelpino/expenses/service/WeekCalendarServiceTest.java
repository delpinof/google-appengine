package com.appspot.fherdelpino.expenses.service;

import com.appspot.fherdelpino.expenses.model.WeekCalendar;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class WeekCalendarServiceTest {

    private final WeekCalendarService sut = new WeekCalendarService();

    @ParameterizedTest
    @MethodSource("weekDaysDataProvider")
    public void testGetWeekByLocalDate(int week, LocalDate localDate, WeekCalendar expected) {
        var result = sut.getWeek(localDate);
        assertThat(result).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("weekDaysDataProvider")
    public void testGetWeekByYearAndWeekNumber(int week, LocalDate date, WeekCalendar expected) {
        var result = sut.getWeek(date.getYear(), week);
        assertThat(result).isEqualTo(expected);
    }

    public static Stream<Arguments> weekDaysDataProvider() {
        return Stream.of(
                arguments(0, LocalDate.of(2023, 1, 1), WeekCalendar.builder()
                        .from(LocalDate.of(2022, 12, 26))
                        .to(LocalDate.of(2023, 1, 1))
                        .week(0)
                        .year(2023)
                        .build()),
                arguments(1, LocalDate.of(2023, 1, 4), WeekCalendar.builder()
                        .from(LocalDate.of(2023, 1, 2))
                        .to(LocalDate.of(2023, 1, 8))
                        .week(1)
                        .year(2023)
                        .build()),
                arguments(1, LocalDate.of(2023, 1, 2), WeekCalendar.builder()
                        .from(LocalDate.of(2023, 1, 2))
                        .to(LocalDate.of(2023, 1, 8))
                        .week(1)
                        .year(2023)
                        .build()),
                arguments(1, LocalDate.of(2023, 1, 8), WeekCalendar.builder()
                        .from(LocalDate.of(2023, 1, 2))
                        .to(LocalDate.of(2023, 1, 8))
                        .week(1)
                        .year(2023)
                        .build()),
                arguments(51, LocalDate.of(2023, 12, 20), WeekCalendar.builder()
                        .from(LocalDate.of(2023, 12, 18))
                        .to(LocalDate.of(2023, 12, 24))
                        .week(51)
                        .year(2023)
                        .build()),
                arguments(52, LocalDate.of(2023, 12, 31), WeekCalendar.builder()
                        .from(LocalDate.of(2023, 12, 25))
                        .to(LocalDate.of(2023, 12, 31))
                        .week(52)
                        .year(2023)
                        .build())
        );
    }
}
