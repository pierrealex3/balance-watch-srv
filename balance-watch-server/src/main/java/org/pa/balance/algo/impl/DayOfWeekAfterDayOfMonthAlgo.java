package org.pa.balance.algo.impl;

import org.pa.balance.algo.AbstractFrequencyGenerator;
import org.pa.balance.algo.DateGenValidationException;
import org.pa.balance.error.InternalException;
import org.pa.balance.transactiont.entity.SpanEntity;
import org.springframework.stereotype.Component;

import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class DayOfWeekAfterDayOfMonthAlgo extends AbstractFrequencyGenerator
{
    Pattern levelAllPattern = Pattern.compile(String.format("^dayOfWeek=(MONDAY|TUESDAY|WEDNESDAY|THURSDAY|FRIDAY|SATURDAY|SUNDAY)\\|after\\|dayOfMonth=(\\d+)%s$", OPT_TIME_PTN));


    @Override
    protected List<LocalDateTime> process(String algo, YearMonth ym, List<SpanEntity> spanList) throws DateGenValidationException
    {
        List<LocalDateTime> genList = new ArrayList<>();

        Matcher m = levelAllPattern.matcher(algo);
        if (!m.find()) {
            throw new InternalException(String.format("Algo: (%s) Pattern: (%s) does not match algo spec: (%s)", getClass().getSimpleName(), levelAllPattern, algo));
        }

        DayOfWeek dow = DayOfWeek.valueOf(m.group(1));
        Integer dom = Integer.parseInt(m.group(2));
        TimeMatchRes time = getTime(m.group(3));

        LocalDate target = ym.atDay(dom);
        do {
            target = target.plusDays(1);
        } while (target.getDayOfWeek() != dow);

        genList.add( LocalDateTime.of(target, LocalTime.of(time.getHours(), time.getMinutes())) );
        return super.filterBasedOnSpan(genList, spanList);
    }

    @Override
    protected Pattern getLevel1Pattern()
    {
        return levelAllPattern;
    }
}
