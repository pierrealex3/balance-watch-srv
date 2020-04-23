package org.pa.balance.algo.impl;

import org.pa.balance.algo.AbstractDateTranslator;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Validated
@Component("BI_WEEKLY")
public class BiWeeklyAlgo extends AbstractDateTranslator {

    @Override
    public List<LocalDate> fetch(YearMonth yearMonth, String variant, LocalDate refDate) {

        List<LocalDate> res = new ArrayList<>();
        LocalDate startPoint = refDate.plusWeeks(2);

        while ( startPoint.getMonthValue() == yearMonth.getMonthValue() ) {
            res.add(startPoint);
            startPoint = startPoint.plusWeeks(2);
        }

        return res;
    }
}
