package org.pa.balance.algo;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.pa.balance.algo.impl.*;
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
@Import({
        FrequencyGeneratorLocalizer.class,
        DayOfMonthAlgo.class,
        DayOfYearAlgo.class,
        WeeklyAlgo.class,
        DayOfWeekAfterDayOfMonthAlgo.class,
        DayOfMonthWithPossibleReportAlgo.class
})
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
    void test_weekly_algo_everyweek_dateGen() {
        SpanEntity se = new SpanEntity();
        se.setStartDate(LocalDate.of(2020, Month.SEPTEMBER, 9));
        se.setEndDate(LocalDate.MAX);
        List<SpanEntity> l = Arrays.asList(se);
        final String algoSpec = "dayOfWeek=TUESDAY|everyWeek";
        AbstractFrequencyGenerator gen = localizer.localize(algoSpec);
        assertEquals(WeeklyAlgo.class, gen.getClass());
        assertDoesNotThrow(() -> {
            List<LocalDateTime> transactionDateList = gen.generate(algoSpec, YearMonth.of(2020, Month.SEPTEMBER), l);
            assertEquals(3, transactionDateList.size());
            assertTrue(transactionDateList.contains(LocalDateTime.of(2020, Month.SEPTEMBER, 15, 0, 0)));
            assertTrue(transactionDateList.contains(LocalDateTime.of(2020, Month.SEPTEMBER, 22, 0, 0)));
            assertTrue(transactionDateList.contains(LocalDateTime.of(2020, Month.SEPTEMBER, 29, 0, 0)));
        });
    }

    @Test
    void test_weekly_algo_everyxweeks_dateGen() {
        SpanEntity se = new SpanEntity();
        se.setStartDate(LocalDate.of(2020, Month.AUGUST, 27));
        se.setEndDate(LocalDate.MAX);
        List<SpanEntity> l = Arrays.asList(se);
        final String algoSpec = "dayOfWeek=THURSDAY|everyXWeeks=2";
        AbstractFrequencyGenerator gen = localizer.localize(algoSpec);
        assertEquals(WeeklyAlgo.class, gen.getClass());
        assertDoesNotThrow(() -> {
            List<LocalDateTime> transactionDateList = gen.generate(algoSpec, YearMonth.of(2020, Month.SEPTEMBER), l);
            assertEquals(2, transactionDateList.size());
            assertTrue(transactionDateList.contains(LocalDateTime.of(2020, Month.SEPTEMBER, 10, 0, 0)));
            assertTrue(transactionDateList.contains(LocalDateTime.of(2020, Month.SEPTEMBER, 24, 0, 0)));
        });
    }

    @Test
    void test_weekly_algo_everyxweeks_dateGen_withTime() {
        SpanEntity se = new SpanEntity();
        se.setStartDate(LocalDate.of(2020, Month.AUGUST, 27));
        se.setEndDate(LocalDate.MAX);
        List<SpanEntity> l = Arrays.asList(se);
        final String algoSpec = "dayOfWeek=THURSDAY|everyXWeeks=2;time=3h33m";
        AbstractFrequencyGenerator gen = localizer.localize(algoSpec);
        assertEquals(WeeklyAlgo.class, gen.getClass());
        assertDoesNotThrow(() -> {
            List<LocalDateTime> transactionDateList = gen.generate(algoSpec, YearMonth.of(2020, Month.SEPTEMBER), l);
            assertEquals(2, transactionDateList.size());
            assertTrue(transactionDateList.contains(LocalDateTime.of(2020, Month.SEPTEMBER, 10, 3, 33)));
            assertTrue(transactionDateList.contains(LocalDateTime.of(2020, Month.SEPTEMBER, 24, 3, 33)));
        });
    }

    @Test
    void test_weekly_algo_everyxweeks_monthInSpanBlackout() {
        SpanEntity se = new SpanEntity();
        se.setStartDate(LocalDate.of(2020, Month.JANUARY, 1));
        se.setEndDate(LocalDate.of(2020, Month.AUGUST, 31));
        SpanEntity se2 = new SpanEntity();
        se2.setStartDate(LocalDate.of(2020, Month.OCTOBER, 1));
        se2.setEndDate(LocalDate.MAX);
        List<SpanEntity> l = Arrays.asList(se, se2);
        final String algoSpec = "dayOfWeek=THURSDAY|everyXWeeks=2";
        AbstractFrequencyGenerator gen = localizer.localize(algoSpec);
        assertEquals(WeeklyAlgo.class, gen.getClass());
        assertDoesNotThrow(() -> {
            List<LocalDateTime> transactionDateList = gen.generate(algoSpec, YearMonth.of(2020, Month.SEPTEMBER), l);
            assertEquals(0, transactionDateList.size());
        });
    }

    @Test
    void test_DayOfWeekAfterDayOfMonthAlgo_dateGen() {
        SpanEntity se = new SpanEntity();
        se.setStartDate(LocalDate.of(2020, Month.AUGUST, 27));
        se.setEndDate(LocalDate.MAX);
        List<SpanEntity> spanList = Arrays.asList(se);

        final String algoSpec = "dayOfWeek=MONDAY|after|dayOfMonth=15";
        AbstractFrequencyGenerator gen = this.localizer.localize(algoSpec);
        assertEquals(DayOfWeekAfterDayOfMonthAlgo.class, gen.getClass());
        assertDoesNotThrow(() -> {
            List<LocalDateTime> res = gen.generate(algoSpec, YearMonth.of(2020, Month.SEPTEMBER), spanList);
            assertEquals(1, res.size());
            assertTrue(res.contains(LocalDateTime.of(2020, Month.SEPTEMBER, 21, 0, 0)));
        });

    }

    @Test
    void test_DayOfMonthWithPossibleReportAlgo_dateGenWithReport() {
        SpanEntity se = new SpanEntity();
        se.setStartDate(LocalDate.of(2020, Month.AUGUST, 27));
        se.setEndDate(LocalDate.MAX);
        List<SpanEntity> spanList = Arrays.asList(se);

        final String algoSpec = "dayOfMonth=12|report|from1=SATURDAY;to1=MONDAY;from2=SUNDAY;to2=MONDAY";
        AbstractFrequencyGenerator gen = this.localizer.localize(algoSpec);
        assertEquals(DayOfMonthWithPossibleReportAlgo.class, gen.getClass());
        assertDoesNotThrow(() -> {
            List<LocalDateTime> res = gen.generate(algoSpec, YearMonth.of(2020, Month.SEPTEMBER), spanList);
            assertEquals(1, res.size());
            assertTrue(res.contains(LocalDateTime.of(2020, Month.SEPTEMBER, 14, 0, 0)));
        });
    }

    @Test
    void test_DayOfMonthWithPossibleReportAlgo_dateGenNoReport() {
        SpanEntity se = new SpanEntity();
        se.setStartDate(LocalDate.of(2020, Month.AUGUST, 27));
        se.setEndDate(LocalDate.MAX);
        List<SpanEntity> spanList = Arrays.asList(se);

        final String algoSpec = "dayOfMonth=11|report|from1=SATURDAY;to1=MONDAY;from2=SUNDAY;to2=MONDAY";
        AbstractFrequencyGenerator gen = this.localizer.localize(algoSpec);
        assertEquals(DayOfMonthWithPossibleReportAlgo.class, gen.getClass());
        assertDoesNotThrow(() -> {
            List<LocalDateTime> res = gen.generate(algoSpec, YearMonth.of(2020, Month.SEPTEMBER), spanList);
            assertEquals(1, res.size());
            assertTrue(res.contains(LocalDateTime.of(2020, Month.SEPTEMBER, 11, 0, 0)));
        });
    }

    @Test
    void test_DayOfMonthWithPossibleReportAlgo_NoReportSpec_dateGenNoReport() {
        SpanEntity se = new SpanEntity();
        se.setStartDate(LocalDate.of(2020, Month.AUGUST, 27));
        se.setEndDate(LocalDate.MAX);
        List<SpanEntity> spanList = Arrays.asList(se);

        final String algoSpec = "dayOfMonth=12|report|";
        AbstractFrequencyGenerator gen = this.localizer.localize(algoSpec);
        assertEquals(DayOfMonthWithPossibleReportAlgo.class, gen.getClass());
        assertDoesNotThrow(() -> {
            List<LocalDateTime> res = gen.generate(algoSpec, YearMonth.of(2020, Month.SEPTEMBER), spanList);
            assertEquals(1, res.size());
            assertTrue(res.contains(LocalDateTime.of(2020, Month.SEPTEMBER, 12, 0, 0)));
        });
    }

    @Test
    void test_DayOfMonthWithPossibleReportAlgo_dateGenWithReport_withTime() {
        SpanEntity se = new SpanEntity();
        se.setStartDate(LocalDate.of(2020, Month.AUGUST, 27));
        se.setEndDate(LocalDate.MAX);
        List<SpanEntity> spanList = Arrays.asList(se);

        final String algoSpec = "dayOfMonth=12;time=9h30m|report|from1=SATURDAY;to1=MONDAY;from2=SUNDAY;to2=MONDAY";
        AbstractFrequencyGenerator gen = this.localizer.localize(algoSpec);
        assertEquals(DayOfMonthWithPossibleReportAlgo.class, gen.getClass());
        assertDoesNotThrow(() -> {
            List<LocalDateTime> res = gen.generate(algoSpec, YearMonth.of(2020, Month.SEPTEMBER), spanList);
            assertEquals(1, res.size());
            assertTrue(res.contains(LocalDateTime.of(2020, Month.SEPTEMBER, 14, 9, 30)));
        });
    }

}