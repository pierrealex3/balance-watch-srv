package org.pa.balance.algo;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

public abstract class AbstractDateTranslator {

    public abstract List<LocalDate> fetch(YearMonth yearMonth, String variant, LocalDate refDate);

}
