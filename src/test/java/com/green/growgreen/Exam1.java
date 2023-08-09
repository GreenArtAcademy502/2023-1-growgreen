package com.green.growgreen;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.YearMonth;

@Slf4j
public class Exam1 {

    @Test
    public void test() {
        YearMonth today = YearMonth.now();
        log.info("today: {}", today);
        LocalDate thisMonStart = today.atDay(1);
        LocalDate thisMonEnd = today.atEndOfMonth();

        log.info("thisMonStart: {}", thisMonStart);
        log.info("thisMonEnd: {}", thisMonEnd);
    }
}


