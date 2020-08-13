package org.pa.balance.algo;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.List;

public abstract class AbstractFrequencyGenerator
{
    public List<LocalDateTime> generate(String algo, YearMonth ym, LocalTime time) throws DateGenValidationException {
        return process(algo, ym, time);
    }

    protected abstract List<LocalDateTime> process(String algo, YearMonth ym, LocalTime time) throws DateGenValidationException;

    protected abstract PatternWrapper getPatternWrapper();
}
