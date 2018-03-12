/*
 * Copyright (c) Numerical Method Inc.
 * http://www.numericalmethod.com/
 * 
 * THIS SOFTWARE IS LICENSED, NOT SOLD.
 * 
 * YOU MAY USE THIS SOFTWARE ONLY AS DESCRIBED IN THE LICENSE.
 * IF YOU ARE NOT AWARE OF AND/OR DO NOT AGREE TO THE TERMS OF THE LICENSE,
 * DO NOT USE THIS SOFTWARE.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITH NO WARRANTY WHATSOEVER,
 * EITHER EXPRESS OR IMPLIED, INCLUDING, WITHOUT LIMITATION,
 * ANY WARRANTIES OF ACCURACY, ACCESSIBILITY, COMPLETENESS,
 * FITNESS FOR A PARTICULAR PURPOSE, MERCHANTABILITY, NON-INFRINGEMENT, 
 * TITLE AND USEFULNESS.
 * 
 * IN NO EVENT AND UNDER NO LEGAL THEORY,
 * WHETHER IN ACTION, CONTRACT, NEGLIGENCE, TORT, OR OTHERWISE,
 * SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR
 * ANY CLAIMS, DAMAGES OR OTHER LIABILITIES,
 * ARISING AS A RESULT OF USING OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.numericalmethod.suanshu.time;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeZone;
import org.joda.time.Period;

/**
 * These are the utility functions to manipulate {@code JodaTime}.
 *
 * @author Haksun Li
 */
public class JodaTimeUtils {

    /**
     * GMT
     */
    public static final DateTimeZone GMT = DateTimeZone.forID("GMT");
    /**
     * EST
     */
    public static final DateTimeZone EST = DateTimeZone.forID("EST");
    /**
     * This is the reference time "origin": 1970 January 1, midnight, UTC.
     */
    public static final DateTime ORIGIN = new DateTime(1970, 1, 1, 0, 0, 0, 0, DateTimeZone.UTC);
    /**
     * This represents a time before all (representable) times.
     */
    public static final DateTime BEGINNING_OF_TIME = new DateTime(0, 1, 1, 0, 0, 0, 0, DateTimeZone.UTC);
    /**
     * This represents a time before all (representable) times, in <tt>long</tt> representation.
     */
    public static final long BEGINNING_OF_TIME_LONG = BEGINNING_OF_TIME.getMillis();
    /**
     * This represents a time after all (representable) times.
     */
    public static final DateTime ENDING_OF_TIME = new DateTime(9999, 12, 31, 0, 0, 0, 0, DateTimeZone.UTC);
    /**
     * This represents a time after all (representable) times, in <tt>long</tt> representation.
     */
    public static final long ENDING_OF_TIME_LONG = ENDING_OF_TIME.getMillis();

    private JodaTimeUtils() {
        // no constructor for utility class
    }

    /**
     * Construct a {@link DateTime} instance with year, month, day, and time zone. It is set to mid-night.
     *
     * @param year  year
     * @param month month of the year
     * @param day   day of the month
     * @param tz    time zone
     * @return a <tt>DateTime</tt> object
     */
    public static DateTime getDate(int year, int month, int day, DateTimeZone tz) {
        return new DateTime(year, month, day, 0, 0, 0, 0, tz);
    }

    /**
     * Construct a {@link DateTime} instance from a string which ends in TimeZone specification.
     *
     * @param str the date-time string
     * @return a {@link DateTime} representation
     * @throws ParseException if there is an error parsing the string
     */
    public static DateTime getDateTime(String str) throws ParseException {
        String tzStr = str.substring(str.lastIndexOf(' ') + 1, str.length());
        DateTimeZone tz = DateTimeZone.forID(tzStr);
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSSS");
        String timeStr = str.substring(0, str.lastIndexOf(' '));

        return getDateTime(timeStr, format, tz);
    }

    /**
     * Construct a {@link DateTime} instance from a string with no TimeZone specified.
     *
     * @param str    the date-time string
     * @param format the formatter
     * @param tz     the time zone; {@code null} if default time zone
     * @return a {@link DateTime} representation
     * @throws ParseException if there is an error parsing the string
     */
    public static DateTime getDateTime(String str, DateFormat format, DateTimeZone tz) throws ParseException {
        if (tz != null) {//default time zone
            format.setTimeZone(tz.toTimeZone());//format for a particular time zone
        }

        long nMillis = format.parse(str).getTime();
        return new DateTime(nMillis, tz);
    }

    /**
     * Get the next weekday, i.e., skipping Saturdays and Sundays.
     *
     * @param time a {@link DateTime}
     * @return next weekday
     */
    public static DateTime nextWeekDay(DateTime time) {
        DateTime copyWithTimeReset = new DateTime(
                time.getYear(), time.getMonthOfYear(), time.getDayOfMonth(),
                time.getHourOfDay(), time.getMinuteOfHour(), time.getSecondOfMinute(), time.getMillisOfSecond(),
                time.getZone());
        switch (time.getDayOfWeek()) {
            case DateTimeConstants.FRIDAY:
                return copyWithTimeReset.plusDays(3);
            case DateTimeConstants.SATURDAY:
                return copyWithTimeReset.plusDays(2);
            default:
                return copyWithTimeReset.plusDays(1);
        }
    }

    /**
     * Get the previous weekday, i.e., skipping Saturdays and Sundays.
     *
     * @param time a {@link DateTime}
     * @return the previous weekday
     */
    public static DateTime previousWeekDay(DateTime time) {
        DateTime copyWithTimeReset = new DateTime(
                time.getYear(), time.getMonthOfYear(), time.getDayOfMonth(),
                time.getHourOfDay(), time.getMinuteOfHour(), time.getSecondOfMinute(), time.getMillisOfSecond(),
                time.getZone());
        switch (time.getDayOfWeek()) {
            case DateTimeConstants.SUNDAY:
                return copyWithTimeReset.minusDays(2);
            case DateTimeConstants.MONDAY:
                return copyWithTimeReset.minusDays(3);
            default:
                return copyWithTimeReset.minusDays(1);
        }
    }

    /**
     * Add a weekday-period (i.e., skipping weekends) to a {@link DateTime}.
     * For example, adding a weekday-period of 2 days and 4 hours to a time
     * instant representing Thursday 5pm results in a time instant at next
     * Monday 9pm.
     *
     * @param time   the original time instant
     * @param period the period to be added
     * @return the {@link DateTime} after the period added
     */
    public static DateTime plusWeekdayPeriod(DateTime time, Period period) {
        DateTime adjustedTime = time;
        if (isWeekend(time)) {
            adjustedTime = nextWeekDay(time).withTime(0, 0, 0, 0);
        }

        int nWeeks = period.getDays() / 5;
        int nDays = period.getDays() % 5;
        Period remainder = period.minusDays(period.getDays());

        DateTime result = adjustedTime.plusWeeks(nWeeks);
        result = result.plusDays(nDays);
        if (nDays + adjustedTime.getDayOfWeek() > 5) {
            result = result.plusDays(2);
        }

        result = result.plus(remainder);
        if (result.getDayOfWeek() == DateTimeConstants.SATURDAY) {
            result = result.plusDays(2);
        }

        return result;
    }

    /**
     * Subtract a weekday-period (i.e., skipping weekends) from a {@link DateTime}.
     * For example, subtracting a weekday-period of 2 days and 4 hours from a
     * time instant representing Monday 9pm results in a time instant at
     * previous Thursday 5pm.
     *
     * @param time   the original time instant
     * @param period the period to be added
     * @return the {@link DateTime} after the period subtracted
     */
    public static DateTime minusWeekdayPeriod(DateTime time, Period period) {
        DateTime adjustedTime = time;
        if (isWeekend(time)) {
            adjustedTime = previousWeekDay(time).plusDays(1).withTime(0, 0, 0, 0);
        }

        int nWeeks = period.getDays() / 5;
        int nDays = period.getDays() % 5;
        Period remainder = period.minusDays(period.getDays());

        DateTime result = adjustedTime.minusWeeks(nWeeks);
        result = result.minusDays(nDays);
        if (nDays > adjustedTime.getDayOfWeek()) {
            result = result.minusDays(2);
        }

        result = result.minus(remainder);
        if (result.getDayOfWeek() == DateTimeConstants.SUNDAY) {
            result = result.minusDays(2);
        }

        return result;
    }

    /**
     * Check if the given time is a weekend.
     *
     * @param time a time
     * @return {@code true} if the given time is a weekend
     */
    public static boolean isWeekend(DateTime time) {
        return (time.getDayOfWeek() == DateTimeConstants.SATURDAY
                || time.getDayOfWeek() == DateTimeConstants.SUNDAY);
    }

    /**
     * Return the earlier of two {@link DateTime} instances.
     *
     * @param t1 time 1
     * @param t2 time 2
     * @return the earlier of {@code t1} and {@code t2}
     */
    public static DateTime min(DateTime t1, DateTime t2) {
        return t1.compareTo(t2) <= 0 ? t1 : t2;
    }

    /**
     * Return the later of two {@link DateTime} instances.
     *
     * @param t1 time 1
     * @param t2 time 2
     * @return the later of {@code t1} and {@code t2}
     */
    public static DateTime max(DateTime t1, DateTime t2) {
        return t1.compareTo(t2) >= 0 ? t1 : t2;
    }

    /**
     * Return the number of periods between two times, rounding up.
     *
     * @param t1     time 1
     * @param t2     time 2
     * @param period a duration
     * @return the number of periods between t1 and t2
     */
    public static int getNumberOfPeriodsBetween(DateTime t1, DateTime t2, Period period) {
        int n = 0;
        for (DateTime t = new DateTime(t1); t.isBefore(t2); t = t.plus(period)) {
            ++n;
        }
        return n;
    }

    /**
     * Make a list of periodic time instants.
     *
     * @param startTime start time
     * @param period    the period
     * @param nPeriods  the number of instants, excluding start time
     * @return a list of periodic time instants
     */
    public static List<DateTime> makePeriodicInstants(DateTime startTime, Period period, int nPeriods) {
        List<DateTime> instants = new ArrayList<DateTime>(nPeriods + 1);

        instants.add(startTime); // 1st time point
        DateTime time = startTime;
        for (int i = 0; i < nPeriods; ++i) {
            time = time.plus(period);
            instants.add(time);
        }

        return instants;
    }

    /**
     * Get the last day of the month which contain a time.
     *
     * @param t a time
     * @return the last day of the month {@code t} is in
     */
    public static DateTime getLastDayOfMonth(DateTime t) {
        return t.dayOfMonth().withMaximumValue();
    }

    /**
     * Get the last millisecond the day which contain a time.
     *
     * @param t a time
     * @return the last millisecond of the day {@code t} is in
     */
    public static DateTime getLastMillisecondOfDay(DateTime t) {
        return t.withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59).withMillisOfSecond(999);
    }
}
