package org.pa.balance.algo.impl;

import org.pa.balance.algo.AbstractFrequencyGenerator;
import org.pa.balance.algo.DateGenValidationException;
import org.pa.balance.error.InternalException;
import org.pa.balance.transactiont.entity.SpanEntity;
import org.springframework.stereotype.Component;

import java.time.*;
import java.time.temporal.TemporalAdjusters;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class DayOfWeekAtPositionWithinMonthAlgo extends AbstractFrequencyGenerator
{
    Pattern level1Pattern = Pattern.compile("^dayOfWeek=.*\\|at\\|rank=.*$");
    Pattern level2Pattern = Pattern.compile(String.format("^dayOfWeek=%s%s\\|at\\|rank=([12345]{1}|LAST)$", FULL_DOW_PTN, OPT_TIME_PTN));

    @Override
    protected List<LocalDateTime> process(String algo, YearMonth ym, List<SpanEntity> spanList) throws DateGenValidationException
    {
        Matcher m = level2Pattern.matcher(algo);

        if (!m.find()) {
            throw new InternalException(String.format("Algo: (%s) Pattern: (%s) does not match algo spec: (%s)", getClass().getSimpleName(), level2Pattern, algo));
        }

        DayOfWeek dow = DayOfWeek.valueOf(m.group(1));
        TimeMatchRes time = getTime(m.group(2));
        String rank = m.group(3);

        LocalDate d = ym.atDay(1);

        if ("LAST".equals(rank))
            d = d.with(TemporalAdjusters.lastInMonth(dow));
        else
            d = d.with(TemporalAdjusters.dayOfWeekInMonth(Integer.parseInt(rank), dow));    // this may throw as not all months have 5 MONDAYS, for example.

        LocalDateTime t = LocalDateTime.of(d, LocalTime.of(time.getHours(), time.getMinutes()));

        return Arrays.asList(t);
    }

    @Override
    protected Pattern getLevel1Pattern()
    {
        return level1Pattern;
    }

}
