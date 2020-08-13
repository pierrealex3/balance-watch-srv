package org.pa.balance.algo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.pa.balance.algo.impl.DayOfMonthAlgo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.YearMonth;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@Import({FrequencyGeneratorLocalizer.class, DayOfMonthAlgo.class })
class FrequencyGeneratorLocalizerTest
{
    @Autowired
    private FrequencyGeneratorLocalizer localizer;

    @Test
    void test_dayOfMonth_algo_happyPath() {
        final String algoSpec = "dayOfMonth=22";
        AbstractFrequencyGenerator gen = localizer.localize(algoSpec);
        assertEquals(DayOfMonthAlgo.class, gen.getClass());
        assertDoesNotThrow(() -> {
            List<LocalDateTime> transactionDateList = gen.generate(algoSpec, YearMonth.of(2020, Month.JULY), null);
            assertEquals(1, transactionDateList.size());
            assertEquals(LocalDateTime.of(2020, Month.JULY, 22, 0, 0), transactionDateList.get(0));
        });
    }

}