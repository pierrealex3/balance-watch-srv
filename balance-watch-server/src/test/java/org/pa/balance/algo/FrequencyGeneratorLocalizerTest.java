package org.pa.balance.algo;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.pa.balance.algo.impl.DayOfMonthAlgo;
import org.pa.balance.algo.impl.DayOfYearAlgo;
import org.pa.balance.algo.impl.WeeklyAlgo;
import org.pa.balance.transactiont.entity.SpanEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@Import({FrequencyGeneratorLocalizer.class, DayOfMonthAlgo.class, DayOfYearAlgo.class, WeeklyAlgo.class })
class FrequencyGeneratorLocalizerTest
{
    @Autowired
    private FrequencyGeneratorLocalizer localizer;

    private static List<SpanEntity> SPAN_LIST_ACTIVE_ALL_2020 = new ArrayList<>();

    @BeforeAll
    public static void setup() {

        SpanEntity s = new SpanEntity();
        s.setStartDate(LocalDate.of(2020, Month.JANUARY, 1));
        s.setEndDate(LocalDate.MAX);
        SPAN_LIST_ACTIVE_ALL_2020.add(s);
    }


    @Test
    void test_dayOfMonth_algo_happyPath() {
        final String algoSpec = "dayOfMonth=22";
        AbstractFrequencyGenerator gen = localizer.localize(algoSpec);
        assertEquals(DayOfMonthAlgo.class, gen.getClass());
        assertDoesNotThrow(() -> {
            List<LocalDateTime> transactionDateList = gen.generate(algoSpec, YearMonth.of(2020, Month.JULY), SPAN_LIST_ACTIVE_ALL_2020);
            assertEquals(1, transactionDateList.size());
            assertEquals(LocalDateTime.of(2020, Month.JULY, 22, 0, 0), transactionDateList.get(0));
        });
    }

    @Test
    void test_dayOfMonth_algo_withTime() {
        final String algoSpec = "dayOfMonth=22;time=2h13m";
        AbstractFrequencyGenerator gen = localizer.localize(algoSpec);
        assertEquals(DayOfMonthAlgo.class, gen.getClass());
        assertDoesNotThrow(() -> {
            List<LocalDateTime> transactionDateList = gen.generate(algoSpec, YearMonth.of(2020, Month.JULY), SPAN_LIST_ACTIVE_ALL_2020);
            assertEquals(1, transactionDateList.size());
            assertEquals(LocalDateTime.of(2020, Month.JULY, 22, 2, 13), transactionDateList.get(0));
        });
    }

    @Test
    void test_dayOfYear_algo_dateGen() {
        final String algoSpec = "dayOfYear=9-25";
        AbstractFrequencyGenerator gen = localizer.localize(algoSpec);
        assertEquals(DayOfYearAlgo.class, gen.getClass());
        assertDoesNotThrow( () -> {
            List<LocalDateTime> transactionDateList = gen.generate(algoSpec, YearMonth.of(2020, Month.SEPTEMBER), SPAN_LIST_ACTIVE_ALL_2020);
            assertEquals(1, transactionDateList.size());
            assertEquals(LocalDateTime.of(2020, Month.SEPTEMBER, 25, 0, 0), transactionDateList.get(0));
        });
    }

    @Test
    void test_dayOfYear_algo_withTime() {
        final String algoSpec = "dayOfYear=9-25;time=23h59m";
        AbstractFrequencyGenerator gen = localizer.localize(algoSpec);
        assertEquals(DayOfYearAlgo.class, gen.getClass());
        assertDoesNotThrow( () -> {
            List<LocalDateTime> transactionDateList = gen.generate(algoSpec, YearMonth.of(2020, Month.SEPTEMBER), SPAN_LIST_ACTIVE_ALL_2020);
            assertEquals(1, transactionDateList.size());
            assertEquals(LocalDateTime.of(2020, Month.SEPTEMBER, 25, 23, 59), transactionDateList.get(0));
        });
    }

    @Test
    void test_dayOfYear_algo_invalidTime() {
        final String algoSpec = "dayOfYear=9-25;time=28h59m";
        AbstractFrequencyGenerator gen = localizer.localize(algoSpec);
        assertEquals(DayOfYearAlgo.class, gen.getClass());
        assertThrows( DateGenValidationException.class, () -> {
            List<LocalDateTime> transactionDateList = gen.generate(algoSpec, YearMonth.of(2020, Month.SEPTEMBER), SPAN_LIST_ACTIVE_ALL_2020);
        });
    }

    @Test
    void test_dayOfYear_algo_noDateGenOk() {
        final String algoSpec = "dayOfYear=10-25";
        AbstractFrequencyGenerator gen = localizer.localize(algoSpec);
        assertEquals(DayOfYearAlgo.class, gen.getClass());
        assertDoesNotThrow( () -> {
            List<LocalDateTime> transactionDateList = gen.generate(algoSpec, YearMonth.of(2020, Month.SEPTEMBER), SPAN_LIST_ACTIVE_ALL_2020);
            assertEquals(0, transactionDateList.size());
        });
    }

    @Test
    void test_weekly_algo_dateGen() {
        SpanEntity se = new SpanEntity();
        se.setStartDate(LocalDate.of(2020, Month.SEPTEMBER, 9));
        List<SpanEntity> l = Arrays.asList(se);
        final String algoSpec = "dayOfWeek=TUESDAY|everyWeek";
        AbstractFrequencyGenerator gen = localizer.localize(algoSpec);
        assertEquals(WeeklyAlgo.class, gen.getClass());
        assertDoesNotThrow(() -> {
            List<LocalDateTime> transactionDateList = gen.generate(algoSpec, YearMonth.of(2020, Month.SEPTEMBER), l);
            assertEquals(3, transactionDateList.size());
            assertEquals(LocalDateTime.of(2020, Month.SEPTEMBER, 15, 0, 0), transactionDateList.get(0));
            assertEquals(LocalDateTime.of(2020, Month.SEPTEMBER, 22, 0, 0), transactionDateList.get(1));
            assertEquals(LocalDateTime.of(2020, Month.SEPTEMBER, 29, 0, 0), transactionDateList.get(2));
        });
    }

}