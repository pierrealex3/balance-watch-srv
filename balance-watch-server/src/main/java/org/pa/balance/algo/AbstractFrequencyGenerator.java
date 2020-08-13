package org.pa.balance.algo;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AbstractFrequencyGenerator
{
    static Pattern timePattern = Pattern.compile(";time=(\\d+)h(\\d+)m");

    public List<LocalDateTime> generate(String algo, YearMonth ym) throws DateGenValidationException {
        return process(algo, ym);
    }

    protected abstract List<LocalDateTime> process(String algo, YearMonth ym) throws DateGenValidationException;

    protected abstract PatternWrapper getPatternWrapper();

    protected static TimeMatchRes getTime(String timePatternStr) {
        TimeMatchRes res = TimeMatchRes.getDefault();
        if (timePatternStr == null)
            return res;

        Matcher m = timePattern.matcher(timePatternStr);
        if (m.find()) {
            res = new TimeMatchRes(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)));
        }
        return res;
    }

    @Getter
    @AllArgsConstructor
    public static class TimeMatchRes {
        private int hours = 0;
        private int minutes = 0;

        static TimeMatchRes getDefault() {
            return new TimeMatchRes(0,0);
        }
    }


}
