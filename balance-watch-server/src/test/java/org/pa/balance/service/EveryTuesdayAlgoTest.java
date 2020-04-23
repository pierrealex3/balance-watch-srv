package org.pa.balance.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.pa.balance.algo.impl.TuesdayEveryWeekAlgo;

import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class EveryTuesdayAlgoTest {

    private TuesdayEveryWeekAlgo target;

    @BeforeEach
    public void setup() {
        target = new TuesdayEveryWeekAlgo();
    }

    @Test
    public void testFetch() {

        YearMonth ym = YearMonth.of(2020, Month.APRIL);

        List<LocalDate> tuesdayList = target.fetch(ym, null, null);

        List<LocalDate> expectedList = Arrays.asList(
                LocalDate.of(2020, Month.APRIL, 7),
                LocalDate.of(2020, Month.APRIL, 14),
                LocalDate.of(2020, Month.APRIL, 21),
                LocalDate.of(2020, Month.APRIL, 28)
        );

        assertTrue( tuesdayList.containsAll(expectedList) && tuesdayList.size() == expectedList.size() );
    }

}