package org.pa.balance.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.pa.balance.algo.impl.BiWeeklyAlgo;

import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class BiWeeklyAlgoTest {

    private BiWeeklyAlgo target;

    @BeforeEach
    public void setup() {
        target = new BiWeeklyAlgo();
    }

    @Test
    public void testBiweeklyThursdays() {

        List<LocalDate> res = target.fetch(YearMonth.of(2020, Month.APRIL), null, LocalDate.of(2020, Month.MARCH, 19));

        List<LocalDate> expected = Arrays.asList(
                LocalDate.of(2020, Month.APRIL, 2),
                LocalDate.of(2020, Month.APRIL, 16),
                LocalDate.of(2020, Month.APRIL, 30)
        );

        assertTrue(res.containsAll(expected) && res.size() == expected.size() );

    }

}