package org.pa.balance.algo.impl;

import org.pa.balance.algo.AbstractFrequencyGenerator;
import org.pa.balance.algo.DateGenValidationException;
import org.pa.balance.algo.PatternWrapper;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.*;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class DayOfMonthAlgo extends AbstractFrequencyGenerator
{
    Pattern level1Pattern = Pattern.compile("^dayOfMonth.*$");
    Pattern level2Pattern = Pattern.compile("^dayOfMonth=(\\d+)$");

    @Override
    protected List<LocalDateTime> process(@NotNull String algo, @NotNull YearMonth ym, @Null LocalTime time) throws DateGenValidationException
    {
        LocalTime timepp = Optional.ofNullable(time).orElseGet( () -> LocalTime.of(0,0));
        Matcher m = level2Pattern.matcher(algo);
        String dayOfMonth = null;

        if (m.find() && m.groupCount() == 1) {
            dayOfMonth = m.group(1);
        } else {
            throw new DateGenValidationException(Arrays.asList(
                    new DateGenValidationException.ValidationMessage(-1, String.format("Algo: (%s) Pattern: (%s) does not match algo spec: (%s)", getClass().getSimpleName(), level2Pattern, algo))));
        }

        try
        {
            int dayOfMonthI = Integer.parseInt(dayOfMonth);
            LocalDateTime validated = LocalDateTime.of(ym.getYear(), ym.getMonth(), dayOfMonthI, timepp.getHour(), timepp.getMinute());
            return List.of(validated);
        }
        catch (DateTimeException e)
        {
            throw new DateGenValidationException(Arrays.asList(
                    new DateGenValidationException.ValidationMessage(-1, String.format("Specified day of month: (%s) is not applicable to year-month: (%s)", dayOfMonth, ym))));
        }

    }

    @Override
    protected PatternWrapper getPatternWrapper()
    {
        return new PatternWrapper(level1Pattern, level2Pattern);
    }
}
