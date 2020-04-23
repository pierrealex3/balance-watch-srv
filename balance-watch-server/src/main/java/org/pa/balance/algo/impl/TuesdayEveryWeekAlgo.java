package org.pa.balance.algo.impl;

import org.pa.balance.algo.AbstractDateTranslator;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Null;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.LinkedList;
import java.util.List;

@Validated
@Component("TUE_WEEKLY")
public class TuesdayEveryWeekAlgo extends AbstractDateTranslator {

    @Override
    public List<LocalDate> fetch(YearMonth yearMonth, @Null String variant, @Null  LocalDate refDate) {

        List<LocalDate> res = new LinkedList<>();
        LocalDate currentDay = yearMonth.atDay(1);

        while( currentDay.getMonthValue() == yearMonth.getMonthValue() ) {
            if (currentDay.getDayOfWeek() == DayOfWeek.TUESDAY) {
                res.add(currentDay);
            }
            currentDay = currentDay.plusDays(1);
        }

        return res;
    }
}
