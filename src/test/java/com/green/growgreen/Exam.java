package com.green.growgreen;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.Locale;

@Slf4j
public class Exam {

    @Test
    public void test() {
        YearMonth today = YearMonth.of(2023, 06);
        LocalDate todayStart = today.atDay(1);
        LocalDate todayEnd = today.atEndOfMonth();

        LocalDate start = todayStart.plusDays(-todayStart.getDayOfWeek().getValue());
        LocalDate end = todayEnd.plusDays((6-todayEnd.getDayOfWeek().getValue()));

        int weeks = (todayEnd.getDayOfMonth() - todayEnd.getDayOfWeek().getValue() + 13)/7;

        log.info("start: {}", start.toString());
        log.info("end: {}, 마지막 주차: {}", end.toString(), weeks);

        log.info("todayStart: {}, 요일: {}", todayStart.toString(), todayStart.getDayOfWeek().getValue());
        log.info("todayEnd: {}, 요일: {}", todayEnd.toString(), todayEnd.getDayOfWeek().getValue());
    }
}
