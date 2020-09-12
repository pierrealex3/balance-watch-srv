package org.pa.balance.algo.impl;

import org.pa.balance.algo.AbstractFrequencyGenerator;
import org.pa.balance.algo.DateGenValidationException;
import org.pa.balance.error.InternalException;
import org.pa.balance.transactiont.entity.SpanEntity;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class DayOfYearAlgo extends AbstractFrequencyGenerator
{
    Pattern level1Pattern = Pattern.compile("^dayOfYear.*$");
    Pattern level2Pattern = Pattern.compile(String.format("^dayOfYear=([1-9]|1[012])-([1-9]|[12][0-9]|3[01])%s$", OPT_TIME_PTN));

    @Override
    protected List<LocalDateTime> process(@NotNull String algo, @NotNull YearMonth ym, @NotNull List<SpanEntity> spanList)
    {
        Matcher m = level2Pattern.matcher(algo);
        String monthOfYear = null;
        String dayOfMonth = null;
        TimeMatchRes time = null;

        if (m.find()) {
            monthOfYear = m.group(1);
            dayOfMonth = m.group(2);
            time = getTime(m.group(3));
        } else {
            throw new InternalException(String.format("Algo: (%s) Pattern: (%s) does not match algo spec: (%s)", getClass().getSimpleName(), level2Pattern, algo));
        }

        try
        {
            if (ym.getMonthValue() != Integer.parseInt(monthOfYear))
                return Collections.emptyList();

            int dayOfMonthI = Integer.parseInt(dayOfMonth);
            LocalDateTime validated = LocalDateTime.of(ym.getYear(), ym.getMonth(), dayOfMonthI, time.getHours(), time.getMinutes());
            return List.of(validated);
        }
        catch (DateTimeException e)
        {
            throw new DateGenValidationException(Arrays.asList(
                    new DateGenValidationException.ValidationMessage(-1, String.format("Specified day of month: (%s) is not applicable to year-month: (%s)", dayOfMonth, ym))));
        }

    }

    @Override
    protected Pattern getLevel1Pattern()
    {
        return level1Pattern;
    }
}
