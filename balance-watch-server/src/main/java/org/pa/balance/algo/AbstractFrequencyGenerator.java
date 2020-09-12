package org.pa.balance.algo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.pa.balance.transactiont.entity.SpanEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AbstractFrequencyGenerator
{
    protected static final String FULL_DOW_PTN = "(MONDAY|TUESDAY|WEDNESDAY|THURSDAY|FRIDAY|SATURDAY|SUNDAY)";
    protected static final String OPT_TIME_PTN = "(;time=\\d+h\\d+m)?";

    static Pattern timePattern = Pattern.compile(";time=(\\d+)h(\\d+)m");

    public List<LocalDateTime> generate(String algo, YearMonth ym, List<SpanEntity> spanList) throws DateGenValidationException {
        return process(algo, ym, spanList);
    }

    protected abstract List<LocalDateTime> process(String algo, YearMonth ym, List<SpanEntity> spanList) throws DateGenValidationException;

    protected abstract Pattern getLevel1Pattern();

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

    /**
     * Returns a list of dates potentially reduced of dates that are not in scope, based on the date spans for the related TransactionTemplate.
     * Note: this algo does not check for spans overlap.  Those must be validated at TT add & update.
     * @param genList
     * @param spanList
     * @return
     */
    protected List<LocalDateTime> filterBasedOnSpan(List<LocalDateTime> genList, List<SpanEntity> spanList) {
        Set<LocalDateTime> res = new HashSet<>();

        genList.stream().forEach(dt -> {
            LocalDate d = dt.toLocalDate();
            spanList.forEach(sp -> {
                if ((d.isAfter(sp.getStartDate()) || d.isEqual(sp.getStartDate())) && (d.isBefore(sp.getEndDate()) || d
                        .isEqual(sp.getEndDate())))
                {
                    res.add(dt);
                }
            });
        });

        return List.copyOf(res);
    }

    /**
     * Returns the date serving as anchor to the start of a specific month.
     * This date is based on the spans for the related TransactionTemplate, and will default to the first of the month.
     * @param ym
     * @param spanList
     * @return
     */
    protected LocalDate getValidStartDateOrDefault(YearMonth ym, List<SpanEntity> spanList) {
        LocalDate defStart = ym.atDay(1);    // default, start of month
        SpanEntity def = new SpanEntity();
        def.setStartDate(defStart);

        return spanList.stream().reduce(def, (a, b) -> {
            if (b.getStartDate().isBefore(a.getStartDate()) && isEndDateInTargetMonthOrAfter(b.getEndDate(), ym) ) {
                return b;
            } else if ( b.getStartDate().isAfter(a.getStartDate() ) && isStartDateInTargetMonthOrBefore(b.getStartDate(), ym) && isEndDateInTargetMonthOrAfter(b.getEndDate(), ym) ) {
                return b;
            } else
                return a;

        }).getStartDate();
    }

    boolean isEndDateInTargetMonthOrAfter(LocalDate endDate, YearMonth ym) {
        return endDate.isAfter(ym.minusMonths(1).atEndOfMonth());
    }

    boolean isStartDateInTargetMonthOrBefore(LocalDate startDate, YearMonth ym) {
        return startDate.isBefore(ym.plusMonths(1).atDay(1));
    }


}
