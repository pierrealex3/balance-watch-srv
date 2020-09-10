package org.pa.balance.algo.impl;

import org.pa.balance.algo.AbstractFrequencyGenerator;
import org.pa.balance.algo.DateGenValidationException;
import org.pa.balance.algo.PatternWrapper;
import org.pa.balance.client.model.Span;
import org.pa.balance.transactiont.entity.SpanEntity;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class WeeklyAlgo extends AbstractFrequencyGenerator
{

    Pattern level1Pattern = Pattern.compile("^dayOfWeek=.*\\|everyX?Week$");

    Pattern level2aPattern = Pattern.compile("^dayOfWeek=(MONDAY|TUESDAY|WEDNESDAY|THURSDAY|FRIDAY|SATURDAY|SUNDAY)\\|everyWeek(;time=\\d+h\\d+m)?$");

    Pattern level2bPattern = Pattern.compile("^dayOfWeek=(MONDAY|TUESDAY|WEDNESDAY|THURSDAY|FRIDAY|SATURDAY|SUNDAY)\\|everyXWeeks=([234])(;time=\\d+h\\d+m)?$");

    @Override
    protected List<LocalDateTime> process(@NotNull String algo, @NotNull YearMonth ym, @NotNull List<SpanEntity> spanList) throws DateGenValidationException
    {
        List<LocalDateTime> genList = new ArrayList<>();
        LocalDate startPoint = getValidStartDateOrDefault(ym, spanList);

        Matcher m = level2aPattern.matcher(algo);
        boolean level2aMatched = m.find();
        boolean level2bMatched = false;
        if (!level2aMatched) {
            m = level2bPattern.matcher(algo);
            level2bMatched = m.find();
        }

        // extract raw params
        String dow = m.group(1);
        int weekLeap = Integer.parseInt( level2bMatched ? m.group(2) : "1" );
        String timePattern = level2bMatched ? m.group(3) : m.group(2);
        TimeMatchRes time = getTime(timePattern);

        // validate input fmt
        LocalTime timex = LocalTime.of(time.getHours(), time.getMinutes());

        boolean firstDowFound = false;
        while(startPoint.getMonth().equals(ym.getMonth())) {
            if (!firstDowFound) {
                if (startPoint.getDayOfWeek() == DayOfWeek.valueOf(dow))
                {
                    firstDowFound = true;
                    genList.add(LocalDateTime.of(startPoint, timex));

                } else {
                    startPoint = startPoint.plusDays(1);
                }
            } else {
                startPoint = startPoint.plusWeeks(weekLeap);
                if (startPoint.getMonth().equals(ym.getMonth())) {
                    genList.add(LocalDateTime.of(startPoint, timex));
                }
            }
        }

        return genList;
    }

    @Override
    protected PatternWrapper getPatternWrapper()    // TODO should be changed to getLevel1Pattern()
    {
        return new PatternWrapper(level1Pattern, level1Pattern);
    }
}
