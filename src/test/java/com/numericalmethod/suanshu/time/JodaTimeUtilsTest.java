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

import java.text.SimpleDateFormat;
import java.text.ParseException;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeZone;
import org.joda.time.Period;
import static com.numericalmethod.suanshu.time.JodaTimeUtils.*;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class JodaTimeUtilsTest {

    @Test
    public void test_comparison_0010() {
        assertTrue(BEGINNING_OF_TIME.compareTo(ENDING_OF_TIME) < 0);
        assertTrue(ENDING_OF_TIME.compareTo(BEGINNING_OF_TIME) > 0);
    }

    @Test
    public void test_comparison_0020() {
        DateTime t1 = new DateTime(2011, 1, 27, 0, 0, 0, 0);
        DateTime t2 = new DateTime(2011, 1, 27, 1, 0, 0, 0);
        assertTrue(t1.compareTo(t2) < 0);
    }

    @Test
    public void test_timezone_conversion_0010() {
        DateTime t1 = new DateTime(2011, 1, 27, 0, 0, 0, 0, DateTimeZone.forID("Europe/London"));
        DateTime t2 = t1.toDateTime(DateTimeZone.forID("Asia/Hong_Kong"));
        assertTrue(t1.compareTo(t2) == 0);

        DateTime t3 = t1.toDateTime(DateTimeZone.forID("Europe/Paris"));
        assertTrue(t1.compareTo(t3) == 0);
    }

    @Test
    public void test_parse_0010() throws ParseException {
        DateTime t = getDateTime("2010-06-04 00:00:00:000 EST");
        assertEquals(new DateTime(2010, 6, 4, 0, 0, 0, 0, EST), t);
    }

    @Test
    public void test_parse_0020() throws ParseException {
        DateTime t = getDateTime("2010-06-04 01:02:30:000 GMT");
        assertEquals(new DateTime(2010, 6, 4, 1, 2, 30, 0, GMT), t);
    }

    @Test
    public void test_parse_0030() throws ParseException {
        DateTime t = getDateTime("2010-06-04 11:22:33:999 GMT");
        assertEquals(new DateTime(2010, 6, 4, 11, 22, 33, 999, GMT), t);
    }

    @Test
    public void test_parse_0040() throws ParseException {
        DateTime t = getDateTime("1987-01-09 16:00:00:000 Asia/Hong_Kong");
        assertEquals(new DateTime(1987, 1, 9, 16, 0, 0, 0, DateTimeZone.forID("Asia/Hong_Kong")), t);
    }

    @Test
    public void test_parse_0050() throws ParseException {
        DateTime t = getDateTime("2008-06-01 17:01:07:000 EST");
        assertEquals(new DateTime(2008, 6, 1, 17, 1, 7, 0, JodaTimeUtils.EST), t);
    }

    @Test
    public void test_parse_0060() throws ParseException {
        DateTime t = getDateTime("2008-06-01 17:01:07:000", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSSS"), JodaTimeUtils.EST);
        assertEquals(new DateTime(2008, 6, 1, 17, 1, 7, 0, JodaTimeUtils.EST), t);
    }

    @Test
    public void test_nextWeekDay_0010() {
        DateTime today = new DateTime(2011, 4, 10, 0, 0, 0, 0, DateTimeZone.UTC); // Sunday
        assertEquals(DateTimeConstants.SUNDAY, today.getDayOfWeek());

        DateTime nextWeekDay = JodaTimeUtils.nextWeekDay(today);
        assertEquals(new DateTime(2011, 4, 11, 0, 0, 0, 0, DateTimeZone.UTC), nextWeekDay);
        assertEquals(DateTimeConstants.MONDAY, nextWeekDay.getDayOfWeek());

        today = today.plusDays(1); // Monday
        nextWeekDay = JodaTimeUtils.nextWeekDay(today);
        assertEquals(new DateTime(2011, 4, 12, 0, 0, 0, 0, DateTimeZone.UTC), nextWeekDay);
        assertEquals(DateTimeConstants.TUESDAY, nextWeekDay.getDayOfWeek());

        today = today.plusDays(1); // Tuesday
        nextWeekDay = JodaTimeUtils.nextWeekDay(today);
        assertEquals(new DateTime(2011, 4, 13, 0, 0, 0, 0, DateTimeZone.UTC), nextWeekDay);
        assertEquals(DateTimeConstants.WEDNESDAY, nextWeekDay.getDayOfWeek());

        today = today.plusDays(1); // Wednesday
        nextWeekDay = JodaTimeUtils.nextWeekDay(today);
        assertEquals(new DateTime(2011, 4, 14, 0, 0, 0, 0, DateTimeZone.UTC), nextWeekDay);
        assertEquals(DateTimeConstants.THURSDAY, nextWeekDay.getDayOfWeek());

        today = today.plusDays(1); // Thursday
        nextWeekDay = JodaTimeUtils.nextWeekDay(today);
        assertEquals(new DateTime(2011, 4, 15, 0, 0, 0, 0, DateTimeZone.UTC), nextWeekDay);
        assertEquals(DateTimeConstants.FRIDAY, nextWeekDay.getDayOfWeek());

        today = today.plusDays(1); // Friday
        nextWeekDay = JodaTimeUtils.nextWeekDay(today);
        assertEquals(new DateTime(2011, 4, 18, 0, 0, 0, 0, DateTimeZone.UTC), nextWeekDay);
        assertEquals(DateTimeConstants.MONDAY, nextWeekDay.getDayOfWeek());

        today = today.plusDays(1); // Saturday
        nextWeekDay = JodaTimeUtils.nextWeekDay(today);
        assertEquals(new DateTime(2011, 4, 18, 0, 0, 0, 0, DateTimeZone.UTC), nextWeekDay);
        assertEquals(DateTimeConstants.MONDAY, nextWeekDay.getDayOfWeek());

        today = today.plusDays(1); // Sunday
        nextWeekDay = JodaTimeUtils.nextWeekDay(today);
        assertEquals(new DateTime(2011, 4, 18, 0, 0, 0, 0, DateTimeZone.UTC), nextWeekDay);
        assertEquals(DateTimeConstants.MONDAY, nextWeekDay.getDayOfWeek());

    }

    @Test
    public void test_previousWeekDay_0010() {
        DateTime today = new DateTime(2011, 4, 10, 0, 0, 0, 0, DateTimeZone.UTC); // Sunday
        assertEquals(DateTimeConstants.SUNDAY, today.getDayOfWeek());

        DateTime previousWeekDay = JodaTimeUtils.previousWeekDay(today);
        assertEquals(new DateTime(2011, 4, 8, 0, 0, 0, 0, DateTimeZone.UTC), previousWeekDay);
        assertEquals(DateTimeConstants.FRIDAY, previousWeekDay.getDayOfWeek());

        today = today.plusDays(1); // Monday
        previousWeekDay = JodaTimeUtils.previousWeekDay(today);
        assertEquals(new DateTime(2011, 4, 8, 0, 0, 0, 0, DateTimeZone.UTC), previousWeekDay);
        assertEquals(DateTimeConstants.FRIDAY, previousWeekDay.getDayOfWeek());

        today = today.plusDays(1); // Tuesday
        previousWeekDay = JodaTimeUtils.previousWeekDay(today);
        assertEquals(new DateTime(2011, 4, 11, 0, 0, 0, 0, DateTimeZone.UTC), previousWeekDay);
        assertEquals(DateTimeConstants.MONDAY, previousWeekDay.getDayOfWeek());

        today = today.plusDays(1); // Wednesday
        previousWeekDay = JodaTimeUtils.previousWeekDay(today);
        assertEquals(new DateTime(2011, 4, 12, 0, 0, 0, 0, DateTimeZone.UTC), previousWeekDay);
        assertEquals(DateTimeConstants.TUESDAY, previousWeekDay.getDayOfWeek());

        today = today.plusDays(1); // Thursday
        previousWeekDay = JodaTimeUtils.previousWeekDay(today);
        assertEquals(new DateTime(2011, 4, 13, 0, 0, 0, 0, DateTimeZone.UTC), previousWeekDay);
        assertEquals(DateTimeConstants.WEDNESDAY, previousWeekDay.getDayOfWeek());

        today = today.plusDays(1); // Friday
        previousWeekDay = JodaTimeUtils.previousWeekDay(today);
        assertEquals(new DateTime(2011, 4, 14, 0, 0, 0, 0, DateTimeZone.UTC), previousWeekDay);
        assertEquals(DateTimeConstants.THURSDAY, previousWeekDay.getDayOfWeek());

        today = today.plusDays(1); // Saturday
        previousWeekDay = JodaTimeUtils.previousWeekDay(today);
        assertEquals(new DateTime(2011, 4, 15, 0, 0, 0, 0, DateTimeZone.UTC), previousWeekDay);
        assertEquals(DateTimeConstants.FRIDAY, previousWeekDay.getDayOfWeek());

        today = today.plusDays(1); // Sunday
        previousWeekDay = JodaTimeUtils.previousWeekDay(today);
        assertEquals(new DateTime(2011, 4, 15, 0, 0, 0, 0, DateTimeZone.UTC), previousWeekDay);
        assertEquals(DateTimeConstants.FRIDAY, previousWeekDay.getDayOfWeek());
    }

    @Test
    public void test_plusWeekday_0010() {
        DateTime t1 = new DateTime(2011, 5, 24, 22, 42, 23, 789); // Tue
        Period period = Period.days(20).plusHours(2).plusMinutes(34); // more than a week

        DateTime expected = new DateTime(2011, 6, 22, 1, 16, 23, 789);
        DateTime result = JodaTimeUtils.plusWeekdayPeriod(t1, period);

        assertEquals(expected, result);
    }

    @Test
    public void test_plusWeekday_0020() {
        DateTime t1 = new DateTime(2011, 5, 24, 22, 42, 23, 789); // Tue
        Period period = Period.days(2); // within the week

        DateTime expected = new DateTime(2011, 5, 26, 22, 42, 23, 789);
        DateTime result = JodaTimeUtils.plusWeekdayPeriod(t1, period);

        assertEquals(expected, result);
    }

    @Test
    public void test_plusWeekday_0030() {
        DateTime t1 = new DateTime(2011, 5, 24, 22, 42, 23, 789); // Tue
        Period period = Period.days(4); // on Sat after addition

        DateTime expected = new DateTime(2011, 5, 30, 22, 42, 23, 789);
        DateTime result = JodaTimeUtils.plusWeekdayPeriod(t1, period);

        assertEquals(expected, result);
    }

    @Test
    public void test_plusWeekday_0040() {
        DateTime t1 = new DateTime(2011, 5, 24, 22, 42, 23, 789); // Tue
        Period period = Period.days(5); // on Sun after addition

        DateTime expected = new DateTime(2011, 5, 31, 22, 42, 23, 789);
        DateTime result = JodaTimeUtils.plusWeekdayPeriod(t1, period);

        assertEquals(expected, result);
    }

    @Test
    public void test_plusWeekday_0050() {
        DateTime t1 = new DateTime(2011, 5, 22, 22, 42, 23, 789); // Sun
        Period period = Period.days(1).plusHours(3);

        DateTime expected = new DateTime(2011, 5, 24, 3, 0, 0, 0);
        DateTime result = JodaTimeUtils.plusWeekdayPeriod(t1, period);

        assertEquals(expected, result);
    }

    @Test
    public void test_minusWeekday_0010() {
        DateTime t1 = new DateTime(2011, 5, 24, 22, 42, 23, 789); // Tue
        Period period = Period.days(20).plusHours(15); // more than a week

        DateTime expected = new DateTime(2011, 4, 26, 7, 42, 23, 789);
        DateTime result = JodaTimeUtils.minusWeekdayPeriod(t1, period);

        assertEquals(expected, result);
    }

    @Test
    public void test_minusWeekday_0020() {
        DateTime t1 = new DateTime(2011, 5, 24, 22, 42, 23, 789); // Tue
        Period period = Period.days(1); // within the week

        DateTime expected = new DateTime(2011, 5, 23, 22, 42, 23, 789);
        DateTime result = JodaTimeUtils.minusWeekdayPeriod(t1, period);

        assertEquals(expected, result);
    }

    @Test
    public void test_minusWeekday_0030() {
        DateTime t1 = new DateTime(2011, 5, 24, 22, 42, 23, 789); // Tue
        Period period = Period.days(2); // on Sun after subtraction

        DateTime expected = new DateTime(2011, 5, 20, 22, 42, 23, 789);
        DateTime result = JodaTimeUtils.minusWeekdayPeriod(t1, period);

        assertEquals(expected, result);
    }

    @Test
    public void test_minusWeekday_0040() {
        DateTime t1 = new DateTime(2011, 5, 24, 22, 42, 23, 789); // Tue
        Period period = Period.days(3); // on Sat after subtraction

        DateTime expected = new DateTime(2011, 5, 19, 22, 42, 23, 789);
        DateTime result = JodaTimeUtils.minusWeekdayPeriod(t1, period);

        assertEquals(expected, result);
    }

    @Test
    public void test_minusWeekday_0050() {
        DateTime t1 = new DateTime(2011, 5, 21, 22, 42, 23, 789); // Sat
        Period period = Period.days(3).plusHours(3);

        DateTime expected = new DateTime(2011, 5, 17, 21, 0, 0, 0);
        DateTime result = JodaTimeUtils.minusWeekdayPeriod(t1, period);

        assertEquals(expected, result);
    }

    @Test
    public void test_min_0010() {
        DateTime t1 = new DateTime(3);
        DateTime t2 = new DateTime(5);

        assertEquals(t1, JodaTimeUtils.min(t1, t2));
    }

    @Test
    public void test_max_0010() {
        DateTime t1 = new DateTime(3);
        DateTime t2 = new DateTime(5);

        assertEquals(t2, JodaTimeUtils.max(t1, t2));
    }

    @Test
    public void test_getNumberOfPeriodsBetween_0010() {
        DateTime t1 = new DateTime(1990, 1, 1, 0, 0, 0, 0);
        DateTime t2 = new DateTime(1990, 1, 1, 0, 0, 0, 0);

        assertEquals(0, JodaTimeUtils.getNumberOfPeriodsBetween(t1, t2, Period.years(1)));//TODO: should this be 1 or 0?
    }

    @Test
    public void test_getNumberOfPeriodsBetween_0020() {
        DateTime t1 = new DateTime(1990, 1, 1, 0, 0, 0, 0);
        DateTime t2 = new DateTime(1990, 1, 1, 0, 0, 0, 1);

        assertEquals(1, JodaTimeUtils.getNumberOfPeriodsBetween(t1, t2, Period.years(1)));
    }

    @Test
    public void test_getNumberOfPeriodsBetween_0030() {
        DateTime t1 = new DateTime(1990, 1, 1, 0, 0, 0, 0);
        DateTime t2 = new DateTime(1991, 1, 1, 0, 0, 0, 0);

        assertEquals(1, JodaTimeUtils.getNumberOfPeriodsBetween(t1, t2, Period.years(1)));
    }

    @Test
    public void test_getNumberOfPeriodsBetween_0040() {
        DateTime t1 = new DateTime(1990, 1, 1, 0, 0, 0, 0);
        DateTime t2 = new DateTime(1991, 1, 1, 0, 0, 0, 1);

        assertEquals(2, JodaTimeUtils.getNumberOfPeriodsBetween(t1, t2, Period.years(1)));
    }

    @Test
    public void test_getNumberOfPeriodsBetween_0050() {
        DateTime t1 = new DateTime(1990, 1, 1, 0, 0, 0, 0);
        DateTime t2 = new DateTime(1992, 1, 1, 0, 0, 0, 0);

        assertEquals(2, JodaTimeUtils.getNumberOfPeriodsBetween(t1, t2, Period.years(1)));
    }

    @Test
    public void test_getNumberOfPeriodsBetween_0060() {
        DateTime t1 = new DateTime(1990, 1, 1, 0, 0, 0, 0);
        DateTime t2 = new DateTime(1992, 1, 1, 0, 0, 0, 1);

        assertEquals(3, JodaTimeUtils.getNumberOfPeriodsBetween(t1, t2, Period.years(1)));
    }

    @Test
    public void test_getNumberOfPeriodsBetween_0070() {
        DateTime t1 = new DateTime(1990, 1, 1, 0, 0, 0, 0);
        DateTime t2 = new DateTime(1990, 1, 2, 0, 0, 0, 0);

        assertEquals(1, JodaTimeUtils.getNumberOfPeriodsBetween(t1, t2, Period.days(1)));
    }

    @Test
    public void test_getNumberOfPeriodsBetween_0080() {
        DateTime t1 = new DateTime(1990, 1, 1, 0, 0, 0, 0);
        DateTime t2 = new DateTime(1990, 1, 2, 0, 0, 0, 1);

        assertEquals(2, JodaTimeUtils.getNumberOfPeriodsBetween(t1, t2, Period.days(1)));
    }
}
