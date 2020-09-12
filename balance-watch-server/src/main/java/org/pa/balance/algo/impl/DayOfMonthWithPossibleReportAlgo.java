package org.pa.balance.algo.impl;

import org.pa.balance.algo.AbstractFrequencyGenerator;
import org.pa.balance.algo.DateGenValidationException;
import org.pa.balance.error.InternalException;
import org.pa.balance.transactiont.entity.SpanEntity;
import org.springframework.stereotype.Component;

import java.time.*;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class DayOfMonthWithPossibleReportAlgo extends AbstractFrequencyGenerator
{

    Pattern level1Pattern = Pattern.compile("^dayOfMonth=.*|report|.*$");
    Pattern level2Pattern = Pattern.compile(String.format("^dayOfMonth=(\\d+)%s\\|report|(?:from1=%s;to1=%s(?:;from2=%s;to2=%s(?:;from3=%s;to3=%s)?)?)?$",
            OPT_TIME_PTN, FULL_DOW_PTN, FULL_DOW_PTN, FULL_DOW_PTN, FULL_DOW_PTN, FULL_DOW_PTN, FULL_DOW_PTN));

    @Override
    protected List<LocalDateTime> process(String algo, YearMonth ym, List<SpanEntity> spanList) throws DateGenValidationException
    {
        Matcher m = level2Pattern.matcher(algo);
        if (m.find()) {
            Integer dom = Integer.parseInt(m.group(1));
            TimeMatchRes time = getTime(m.group(2));

            LocalDate d = ym.atDay(dom);
            LocalDate res = d;

            if (m.find())
            {
                DayOfWeek from1 = Optional.ofNullable(m.group(3)).map(DayOfWeek::valueOf).orElseGet(() -> null);
                DayOfWeek to1 = Optional.ofNullable(m.group(4)).map(DayOfWeek::valueOf).orElseGet(() -> null);
                DayOfWeek from2 = Optional.ofNullable(m.group(5)).map(DayOfWeek::valueOf).orElseGet(() -> null);
                DayOfWeek to2 = Optional.ofNullable(m.group(6)).map(DayOfWeek::valueOf).orElseGet(() -> null);
                DayOfWeek from3 = Optional.ofNullable(m.group(7)).map(DayOfWeek::valueOf).orElseGet(() -> null);
                DayOfWeek to3 = Optional.ofNullable(m.group(8)).map(DayOfWeek::valueOf).orElseGet(() -> null);

                res = getReportedIfRequired(d, from1, to1);
                if (res.equals(d)) {
                    res = getReportedIfRequired(d, from2, to2);
                    if (res.equals(d)) {
                        res = getReportedIfRequired(d, from3, to3);
                    }
                }
            }

            LocalDateTime ress = LocalDateTime.of(res, LocalTime.of(time.getHours(), time.getMinutes()));
            return List.of(ress);
        } else {
            throw new InternalException(String.format("Algo: (%s) Pattern: (%s) does not match algo spec: (%s)", getClass().getSimpleName(), level2Pattern, algo));
        }
    }

    LocalDate getReportedIfRequired(LocalDate d, DayOfWeek from, DayOfWeek to) {
        if (from == null || to == null)
            return d;

        if (d.getDayOfWeek() == from) {
            return d.with(TemporalAdjusters.next(to));
        } else {
            return d;
        }
    }

    @Override
    protected Pattern getLevel1Pattern()
    {
        return level1Pattern;
    }
}
