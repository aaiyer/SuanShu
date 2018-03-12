/*
 * Copyright (c)
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
package com.numericalmethod.suanshu.interval;

import org.joda.time.DateTimeZone;
import com.numericalmethod.suanshu.time.TimeIntervals;
import org.joda.time.DateTime;
import com.numericalmethod.suanshu.time.JodaTimeUtils;
import com.numericalmethod.suanshu.time.TimeInterval;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class IntervalsTest {

    @Test
    public void test_0010() {
        DateTime t1 = JodaTimeUtils.getDate(2010, 6, 6, JodaTimeUtils.EST);
        DateTime t2 = JodaTimeUtils.getDate(2010, 6, 10, JodaTimeUtils.EST);
        Intervals<DateTime> instance1 = new Intervals<DateTime>(t1, t2);

        TimeInterval interval1 = new TimeInterval(t1, t2);
        Intervals<DateTime> instance2 = new Intervals<DateTime>(interval1);

        assertEquals(1, instance1.size());
        assertEquals(1, instance2.size());
        assertTrue(instance1.equals(instance2));

        Intervals<DateTime> instance3 = new Intervals<DateTime>(instance1);
        assertEquals(1, instance3.size());
        assertTrue(instance3.equals(instance1));
        assertTrue(instance3.equals(instance2));

        assertEquals("[2010-06-06T00:00:00.000-05:00, 2010-06-10T00:00:00.000-05:00]", interval1.toString());
    }

    @Test
    public void test_0020() {
        DateTime t1 = JodaTimeUtils.getDate(2010, 6, 6, JodaTimeUtils.EST);
        DateTime t2 = JodaTimeUtils.getDate(2010, 6, 10, JodaTimeUtils.EST);
        TimeInterval interval1 = new TimeInterval(t1, t2);

        DateTime t3 = JodaTimeUtils.getDate(2010, 7, 6, JodaTimeUtils.EST);
        DateTime t4 = JodaTimeUtils.getDate(2010, 7, 10, JodaTimeUtils.EST);
        TimeInterval interval2 = new TimeInterval(t3, t4);
        @SuppressWarnings("unchecked")
        Intervals<DateTime> instance1 = new Intervals<DateTime>(interval1, interval2);
        Intervals<DateTime> instance2 = new Intervals<DateTime>(
                new DateTime(t1),
                new DateTime(t2));
        instance2.add(interval2);

        assertEquals(instance1, instance2);
        assertEquals("[2010-06-06T00:00:00.000-05:00, 2010-06-10T00:00:00.000-05:00], [2010-07-06T00:00:00.000-05:00, 2010-07-10T00:00:00.000-05:00]", instance1.toString());
    }

    /**
     * test of I1 includes I2
     */
    @Test
    public void test_0030() {
        TimeIntervals instance1 = new TimeIntervals(
                JodaTimeUtils.getDate(2010, 6, 6, JodaTimeUtils.GMT),
                JodaTimeUtils.getDate(2010, 6, 30, JodaTimeUtils.GMT));
        instance1.add(new TimeInterval(
                JodaTimeUtils.getDate(2010, 6, 10, JodaTimeUtils.GMT),
                JodaTimeUtils.getDate(2010, 6, 15, JodaTimeUtils.GMT)));

        TimeIntervals expected = new TimeIntervals(
                JodaTimeUtils.getDate(2010, 6, 6, JodaTimeUtils.GMT),
                JodaTimeUtils.getDate(2010, 6, 30, JodaTimeUtils.GMT));
        assertEquals(expected, instance1);
    }

    /**
     * test of I1 includes I2
     */
    @Test
    public void test_0040() {
        TimeIntervals instance1 = new TimeIntervals(
                JodaTimeUtils.getDate(2010, 6, 6, JodaTimeUtils.EST),
                JodaTimeUtils.getDate(2010, 6, 30, JodaTimeUtils.EST));
        instance1.add(new TimeInterval(
                JodaTimeUtils.getDate(2010, 6, 6, JodaTimeUtils.EST),
                JodaTimeUtils.getDate(2010, 6, 15, JodaTimeUtils.EST)));

        TimeIntervals expected = new TimeIntervals(
                JodaTimeUtils.getDate(2010, 6, 6, JodaTimeUtils.EST),
                JodaTimeUtils.getDate(2010, 6, 30, JodaTimeUtils.EST));
        assertEquals(expected, instance1);
    }

    /**
     * test of I1 includes I2
     */
    @Test
    public void test_0050() {
        TimeIntervals instance1 = new TimeIntervals(
                JodaTimeUtils.getDate(2010, 6, 6, JodaTimeUtils.EST),
                JodaTimeUtils.getDate(2010, 6, 30, JodaTimeUtils.EST));
        instance1.add(new TimeInterval(
                JodaTimeUtils.getDate(2010, 6, 6, JodaTimeUtils.EST),
                JodaTimeUtils.getDate(2010, 6, 30, JodaTimeUtils.EST)));

        TimeIntervals expected = new TimeIntervals(
                JodaTimeUtils.getDate(2010, 6, 6, JodaTimeUtils.EST),
                JodaTimeUtils.getDate(2010, 6, 30, JodaTimeUtils.EST));
        assertEquals(expected, instance1);
    }

    /**
     * test of I1 includes I2
     */
    @Test
    public void test_0060() {
        TimeIntervals instance1 = new TimeIntervals(
                JodaTimeUtils.getDate(2010, 6, 6, JodaTimeUtils.GMT),
                JodaTimeUtils.getDate(2010, 6, 30, JodaTimeUtils.GMT));
        instance1.add(new TimeInterval(
                JodaTimeUtils.getDate(2010, 6, 15, JodaTimeUtils.GMT),
                JodaTimeUtils.getDate(2010, 6, 30, JodaTimeUtils.GMT)));

        TimeIntervals expected = new TimeIntervals(
                JodaTimeUtils.getDate(2010, 6, 6, JodaTimeUtils.GMT),
                JodaTimeUtils.getDate(2010, 6, 30, JodaTimeUtils.GMT));
        assertEquals(expected, instance1);
    }

    /**
     * test of I2 includes I1
     */
    @Test
    public void test_0070() {
        TimeIntervals instance1 = new TimeIntervals(
                JodaTimeUtils.getDate(2010, 6, 10, JodaTimeUtils.GMT),
                JodaTimeUtils.getDate(2010, 6, 15, JodaTimeUtils.GMT));
        instance1.add(new TimeInterval(
                JodaTimeUtils.getDate(2010, 6, 6, JodaTimeUtils.GMT),
                JodaTimeUtils.getDate(2010, 6, 30, JodaTimeUtils.GMT)));

        TimeIntervals expected = new TimeIntervals(
                JodaTimeUtils.getDate(2010, 6, 6, JodaTimeUtils.GMT),
                JodaTimeUtils.getDate(2010, 6, 30, JodaTimeUtils.GMT));
        assertEquals(expected, instance1);
    }

    /**
     * test of I2 includes I1
     */
    @Test
    public void test_0080() {
        TimeIntervals instance1 = new TimeIntervals(
                JodaTimeUtils.getDate(2010, 6, 15, JodaTimeUtils.GMT),
                JodaTimeUtils.getDate(2010, 6, 30, JodaTimeUtils.GMT));
        instance1.add(new TimeInterval(
                JodaTimeUtils.getDate(2010, 6, 6, JodaTimeUtils.GMT),
                JodaTimeUtils.getDate(2010, 6, 30, JodaTimeUtils.GMT)));

        TimeIntervals expected = new TimeIntervals(
                JodaTimeUtils.getDate(2010, 6, 6, JodaTimeUtils.GMT),
                JodaTimeUtils.getDate(2010, 6, 30, JodaTimeUtils.GMT));
        assertEquals(expected, instance1);
    }

    /**
     * test of I2 includes I1
     */
    @Test
    public void test_0090() {
        TimeIntervals instance1 = new TimeIntervals(
                JodaTimeUtils.getDate(2010, 6, 6, JodaTimeUtils.EST),
                JodaTimeUtils.getDate(2010, 6, 15, JodaTimeUtils.EST));
        instance1.add(new TimeInterval(
                JodaTimeUtils.getDate(2010, 6, 6, JodaTimeUtils.EST),
                JodaTimeUtils.getDate(2010, 6, 30, JodaTimeUtils.EST)));

        TimeIntervals expected = new TimeIntervals(
                JodaTimeUtils.getDate(2010, 6, 6, JodaTimeUtils.EST),
                JodaTimeUtils.getDate(2010, 6, 30, JodaTimeUtils.EST));
        assertEquals(expected, instance1);
    }

    /**
     * test of I1 includes I2
     */
    @Test
    public void test_0100() {
        TimeIntervals instance1 = new TimeIntervals(
                JodaTimeUtils.getDate(2010, 6, 6, JodaTimeUtils.EST),
                JodaTimeUtils.getDate(2010, 6, 30, JodaTimeUtils.EST));
        instance1.add(new TimeInterval(
                JodaTimeUtils.getDate(2010, 6, 1, JodaTimeUtils.EST),
                JodaTimeUtils.getDate(2010, 6, 15, JodaTimeUtils.EST)));

        TimeIntervals expected = new TimeIntervals(
                JodaTimeUtils.getDate(2010, 6, 1, JodaTimeUtils.EST),
                JodaTimeUtils.getDate(2010, 6, 30, JodaTimeUtils.EST));
        assertEquals(expected, instance1);
    }

    /**
     * test of I1 includes I2
     */
    @Test
    public void test_0110() {
        TimeIntervals instance1 = new TimeIntervals(
                JodaTimeUtils.getDate(2010, 6, 1, JodaTimeUtils.EST),
                JodaTimeUtils.getDate(2010, 6, 30, JodaTimeUtils.EST));
        instance1.add(new TimeInterval(
                JodaTimeUtils.getDate(2010, 6, 6, JodaTimeUtils.EST),
                JodaTimeUtils.getDate(2010, 7, 30, JodaTimeUtils.EST)));

        TimeIntervals expected = new TimeIntervals(
                JodaTimeUtils.getDate(2010, 6, 1, JodaTimeUtils.EST),
                JodaTimeUtils.getDate(2010, 7, 30, JodaTimeUtils.EST));
        assertEquals(expected, instance1);
    }

    /**
     * test of I2 includes I1
     */
    @Test
    public void test_0120() {
        TimeIntervals instance1 = new TimeIntervals(
                JodaTimeUtils.getDate(2010, 6, 6, DateTimeZone.forID("Asia/Hong_Kong")),
                JodaTimeUtils.getDate(2010, 7, 30, DateTimeZone.forID("Asia/Hong_Kong")));
        instance1.add(new TimeInterval(
                JodaTimeUtils.getDate(2010, 6, 1, DateTimeZone.forID("Asia/Hong_Kong")),
                JodaTimeUtils.getDate(2010, 6, 30, DateTimeZone.forID("Asia/Hong_Kong"))));

        TimeIntervals expected = new TimeIntervals(
                JodaTimeUtils.getDate(2010, 6, 1, DateTimeZone.forID("Asia/Hong_Kong")),
                JodaTimeUtils.getDate(2010, 7, 30, DateTimeZone.forID("Asia/Hong_Kong")));
        assertEquals(expected, instance1);
    }

    /**
     * test of I1 meets I2
     */
    @Test
    public void test_0130() {
        TimeIntervals instance1 = new TimeIntervals(
                JodaTimeUtils.getDate(2010, 6, 1, DateTimeZone.forID("Asia/Hong_Kong")),
                JodaTimeUtils.getDate(2010, 6, 30, DateTimeZone.forID("Asia/Hong_Kong")));
        instance1.add(new TimeInterval(
                JodaTimeUtils.getDate(2010, 6, 30, DateTimeZone.forID("Asia/Hong_Kong")),
                JodaTimeUtils.getDate(2010, 7, 30, DateTimeZone.forID("Asia/Hong_Kong"))));

        TimeIntervals expected = new TimeIntervals(
                JodaTimeUtils.getDate(2010, 6, 1, DateTimeZone.forID("Asia/Hong_Kong")),
                JodaTimeUtils.getDate(2010, 7, 30, DateTimeZone.forID("Asia/Hong_Kong")));
        assertEquals(expected, instance1);
    }

    /**
     * test of I2 meets I1
     */
    @Test
    public void test_0140() {
        TimeIntervals instance1 = new TimeIntervals(
                JodaTimeUtils.getDate(2010, 6, 30, JodaTimeUtils.GMT),
                JodaTimeUtils.getDate(2010, 7, 30, JodaTimeUtils.GMT));
        instance1.add(new TimeInterval(
                JodaTimeUtils.getDate(2010, 6, 1, JodaTimeUtils.GMT),
                JodaTimeUtils.getDate(2010, 6, 30, JodaTimeUtils.GMT)));

        TimeIntervals expected = new TimeIntervals(
                JodaTimeUtils.getDate(2010, 6, 1, JodaTimeUtils.GMT),
                JodaTimeUtils.getDate(2010, 7, 30, JodaTimeUtils.GMT));
        assertEquals(expected, instance1);
    }

    /**
     * test of I1 is before I2
     */
    @Test
    public void test_0200() {
        TimeInterval[] intervals = new TimeInterval[2];
        intervals[0] = new TimeInterval(
                JodaTimeUtils.getDate(2010, 5, 1, JodaTimeUtils.EST),
                JodaTimeUtils.getDate(2010, 5, 30, JodaTimeUtils.EST));
        intervals[1] = new TimeInterval(
                JodaTimeUtils.getDate(2010, 6, 1, JodaTimeUtils.EST),
                JodaTimeUtils.getDate(2010, 6, 30, JodaTimeUtils.EST));

        TimeIntervals instance1 = new TimeIntervals(intervals[0]);
        instance1.add(intervals[1]);

        assertEquals(2, instance1.size());
        for (int i = 0; i < instance1.size(); ++i) {
            assertEquals(intervals[i], instance1.get(i + 1));
        }
    }

    /**
     * test of I2 is before I1
     */
    @Test
    public void test_0210() {
        TimeInterval[] intervals = new TimeInterval[2];
        intervals[0] = new TimeInterval(
                JodaTimeUtils.getDate(2010, 5, 1, JodaTimeUtils.EST),
                JodaTimeUtils.getDate(2010, 5, 30, JodaTimeUtils.EST));
        intervals[1] = new TimeInterval(
                JodaTimeUtils.getDate(2010, 6, 1, JodaTimeUtils.EST),
                JodaTimeUtils.getDate(2010, 6, 30, JodaTimeUtils.EST));

        TimeIntervals instance1 = new TimeIntervals(intervals[1]);
        instance1.add(intervals[0]);

        assertEquals(2, instance1.size());
        for (int i = 0; i < instance1.size(); ++i) {
            assertEquals(intervals[i], instance1.get(i + 1));
        }
    }

    /**
     * test of I1 is before I2
     */
    @Test
    public void test_0220() {
        TimeInterval[] intervals = new TimeInterval[2];
        intervals[0] = new TimeInterval(
                JodaTimeUtils.getDate(2010, 5, 1, JodaTimeUtils.EST),
                JodaTimeUtils.getDate(2010, 5, 30, JodaTimeUtils.EST));
        intervals[1] = new TimeInterval(
                JodaTimeUtils.getDate(2010, 6, 1, JodaTimeUtils.EST),
                JodaTimeUtils.getDate(2010, 6, 30, JodaTimeUtils.EST));

        TimeIntervals instance1 = new TimeIntervals(intervals[0]);
        instance1.add(intervals[1]);

        assertEquals(2, instance1.size());
        for (int i = 0; i < instance1.size(); ++i) {
            assertEquals(intervals[i], instance1.get(i + 1));
        }
    }

    /**
     * test of I2 is before I1
     */
    @Test
    public void test_0230() {
        TimeInterval[] intervals = new TimeInterval[2];
        intervals[0] = new TimeInterval(
                JodaTimeUtils.getDate(2010, 5, 1, JodaTimeUtils.EST),
                JodaTimeUtils.getDate(2010, 5, 30, JodaTimeUtils.EST));
        intervals[1] = new TimeInterval(
                JodaTimeUtils.getDate(2010, 6, 1, JodaTimeUtils.EST),
                JodaTimeUtils.getDate(2010, 6, 30, JodaTimeUtils.EST));

        TimeIntervals instance1 = new TimeIntervals(intervals[1]);
        instance1.add(intervals[0]);

        assertEquals(2, instance1.size());
        for (int i = 0; i < instance1.size(); ++i) {
            assertEquals(intervals[i], instance1.get(i + 1));
        }
    }

    /**
     * test of multiple intervals
     */
    @Test
    public void test_0300() {
        TimeInterval[] intervals = new TimeInterval[3];
        intervals[0] = new TimeInterval(
                JodaTimeUtils.getDate(2010, 6, 15, JodaTimeUtils.EST),
                JodaTimeUtils.getDate(2010, 7, 15, JodaTimeUtils.EST));
        intervals[1] = new TimeInterval(
                JodaTimeUtils.getDate(2010, 5, 1, JodaTimeUtils.EST),
                JodaTimeUtils.getDate(2010, 5, 30, JodaTimeUtils.EST));
        intervals[2] = new TimeInterval(
                JodaTimeUtils.getDate(2010, 6, 1, JodaTimeUtils.EST),
                JodaTimeUtils.getDate(2010, 6, 30, JodaTimeUtils.EST));

        TimeIntervals instance1 = new TimeIntervals(intervals[0]);
        instance1.add(intervals[1]);
        instance1.add(intervals[2]);
        @SuppressWarnings("unchecked")
        TimeIntervals expected = new TimeIntervals(
                new TimeInterval(
                JodaTimeUtils.getDate(2010, 5, 1, JodaTimeUtils.EST),
                JodaTimeUtils.getDate(2010, 5, 30, JodaTimeUtils.EST)),
                new TimeInterval(
                JodaTimeUtils.getDate(2010, 6, 1, JodaTimeUtils.EST),
                JodaTimeUtils.getDate(2010, 7, 15, JodaTimeUtils.EST)));

        assertEquals(expected, instance1);
    }

    /**
     * test of multiple intervals
     */
    @Test
    public void test_0310() {
        TimeInterval[] intervals = new TimeInterval[9];

        intervals[0] = new TimeInterval(JodaTimeUtils.getDate(2010, 9, 25, JodaTimeUtils.GMT), JodaTimeUtils.getDate(2010, 9, 30, JodaTimeUtils.GMT));
        intervals[1] = new TimeInterval(JodaTimeUtils.getDate(2010, 6, 15, JodaTimeUtils.GMT), JodaTimeUtils.getDate(2010, 7, 15, JodaTimeUtils.GMT));
        intervals[2] = new TimeInterval(JodaTimeUtils.getDate(2010, 5, 1, JodaTimeUtils.GMT), JodaTimeUtils.getDate(2010, 5, 30, JodaTimeUtils.GMT));
        intervals[3] = new TimeInterval(JodaTimeUtils.getDate(2010, 6, 1, JodaTimeUtils.GMT), JodaTimeUtils.getDate(2010, 6, 30, JodaTimeUtils.GMT));
        intervals[4] = new TimeInterval(JodaTimeUtils.getDate(2010, 8, 1, JodaTimeUtils.GMT), JodaTimeUtils.getDate(2010, 8, 15, JodaTimeUtils.GMT));
        intervals[5] = new TimeInterval(JodaTimeUtils.getDate(2010, 8, 10, JodaTimeUtils.GMT), JodaTimeUtils.getDate(2010, 8, 20, JodaTimeUtils.GMT));
        intervals[6] = new TimeInterval(JodaTimeUtils.getDate(2010, 8, 30, JodaTimeUtils.GMT), JodaTimeUtils.getDate(2010, 9, 20, JodaTimeUtils.GMT));
        intervals[7] = new TimeInterval(JodaTimeUtils.getDate(2010, 10, 10, JodaTimeUtils.GMT), JodaTimeUtils.getDate(2010, 11, 20, JodaTimeUtils.GMT));
        intervals[8] = new TimeInterval(JodaTimeUtils.getDate(2010, 10, 20, JodaTimeUtils.GMT), JodaTimeUtils.getDate(2010, 11, 10, JodaTimeUtils.GMT));

        TimeIntervals instance1 = new TimeIntervals();
        for (int i = 0; i < intervals.length; ++i) {
            instance1.add(intervals[i]);
        }
        @SuppressWarnings("unchecked")
        TimeIntervals expected = new TimeIntervals(
                new TimeInterval(JodaTimeUtils.getDate(2010, 5, 1, JodaTimeUtils.GMT), JodaTimeUtils.getDate(2010, 5, 30, JodaTimeUtils.GMT)),
                new TimeInterval(JodaTimeUtils.getDate(2010, 6, 1, JodaTimeUtils.GMT), JodaTimeUtils.getDate(2010, 7, 15, JodaTimeUtils.GMT)),
                new TimeInterval(JodaTimeUtils.getDate(2010, 8, 1, JodaTimeUtils.GMT), JodaTimeUtils.getDate(2010, 8, 20, JodaTimeUtils.GMT)),
                new TimeInterval(JodaTimeUtils.getDate(2010, 8, 30, JodaTimeUtils.GMT), JodaTimeUtils.getDate(2010, 9, 20, JodaTimeUtils.GMT)),
                new TimeInterval(JodaTimeUtils.getDate(2010, 9, 25, JodaTimeUtils.GMT), JodaTimeUtils.getDate(2010, 9, 30, JodaTimeUtils.GMT)),
                new TimeInterval(JodaTimeUtils.getDate(2010, 10, 10, JodaTimeUtils.GMT), JodaTimeUtils.getDate(2010, 11, 20, JodaTimeUtils.GMT)));

        assertEquals(expected, instance1);
    }

    /**
     * test of multiple intervals
     */
    @Test
    public void test_0320() {
        TimeInterval[] intervals = new TimeInterval[10];

        intervals[0] = new TimeInterval(JodaTimeUtils.getDate(2010, 9, 25, JodaTimeUtils.GMT), JodaTimeUtils.getDate(2010, 9, 30, JodaTimeUtils.GMT));
        intervals[1] = new TimeInterval(JodaTimeUtils.getDate(2010, 6, 15, JodaTimeUtils.GMT), JodaTimeUtils.getDate(2010, 7, 15, JodaTimeUtils.GMT));
        intervals[2] = new TimeInterval(JodaTimeUtils.getDate(2010, 5, 1, JodaTimeUtils.GMT), JodaTimeUtils.getDate(2010, 5, 30, JodaTimeUtils.GMT));
        intervals[3] = new TimeInterval(JodaTimeUtils.getDate(2010, 6, 1, JodaTimeUtils.GMT), JodaTimeUtils.getDate(2010, 6, 30, JodaTimeUtils.GMT));
        intervals[4] = new TimeInterval(JodaTimeUtils.getDate(2010, 8, 1, JodaTimeUtils.GMT), JodaTimeUtils.getDate(2010, 8, 15, JodaTimeUtils.GMT));
        intervals[5] = new TimeInterval(JodaTimeUtils.getDate(2010, 8, 10, JodaTimeUtils.GMT), JodaTimeUtils.getDate(2010, 8, 20, JodaTimeUtils.GMT));
        intervals[6] = new TimeInterval(JodaTimeUtils.getDate(2010, 8, 30, JodaTimeUtils.GMT), JodaTimeUtils.getDate(2010, 9, 20, JodaTimeUtils.GMT));
        intervals[7] = new TimeInterval(JodaTimeUtils.getDate(2010, 10, 10, JodaTimeUtils.GMT), JodaTimeUtils.getDate(2010, 11, 20, JodaTimeUtils.GMT));
        intervals[8] = new TimeInterval(JodaTimeUtils.getDate(2010, 10, 20, JodaTimeUtils.GMT), JodaTimeUtils.getDate(2010, 11, 10, JodaTimeUtils.GMT));
        intervals[9] = new TimeInterval(JodaTimeUtils.getDate(2010, 4, 1, JodaTimeUtils.GMT), JodaTimeUtils.getDate(2010, 5, 15, JodaTimeUtils.GMT));

        TimeIntervals instance1 = new TimeIntervals();
        for (int i = 0; i < intervals.length; ++i) {
            instance1.add(intervals[i]);
        }
        @SuppressWarnings("unchecked")
        TimeIntervals expected = new TimeIntervals(
                new TimeInterval(JodaTimeUtils.getDate(2010, 4, 1, JodaTimeUtils.GMT), JodaTimeUtils.getDate(2010, 5, 30, JodaTimeUtils.GMT)),
                new TimeInterval(JodaTimeUtils.getDate(2010, 6, 1, JodaTimeUtils.GMT), JodaTimeUtils.getDate(2010, 7, 15, JodaTimeUtils.GMT)),
                new TimeInterval(JodaTimeUtils.getDate(2010, 8, 1, JodaTimeUtils.GMT), JodaTimeUtils.getDate(2010, 8, 20, JodaTimeUtils.GMT)),
                new TimeInterval(JodaTimeUtils.getDate(2010, 8, 30, JodaTimeUtils.GMT), JodaTimeUtils.getDate(2010, 9, 20, JodaTimeUtils.GMT)),
                new TimeInterval(JodaTimeUtils.getDate(2010, 9, 25, JodaTimeUtils.GMT), JodaTimeUtils.getDate(2010, 9, 30, JodaTimeUtils.GMT)),
                new TimeInterval(JodaTimeUtils.getDate(2010, 10, 10, JodaTimeUtils.GMT), JodaTimeUtils.getDate(2010, 11, 20, JodaTimeUtils.GMT)));

        assertEquals(expected, instance1);
    }

    /**
     * test of multiple intervals
     */
    @Test
    public void test_0330() {
        TimeInterval[] intervals = new TimeInterval[11];

        intervals[0] = new TimeInterval(JodaTimeUtils.getDate(2010, 9, 25, JodaTimeUtils.GMT), JodaTimeUtils.getDate(2010, 9, 30, JodaTimeUtils.GMT));
        intervals[1] = new TimeInterval(JodaTimeUtils.getDate(2010, 6, 15, JodaTimeUtils.GMT), JodaTimeUtils.getDate(2010, 7, 15, JodaTimeUtils.GMT));
        intervals[2] = new TimeInterval(JodaTimeUtils.getDate(2010, 5, 1, JodaTimeUtils.GMT), JodaTimeUtils.getDate(2010, 5, 30, JodaTimeUtils.GMT));
        intervals[3] = new TimeInterval(JodaTimeUtils.getDate(2010, 6, 1, JodaTimeUtils.GMT), JodaTimeUtils.getDate(2010, 6, 30, JodaTimeUtils.GMT));
        intervals[4] = new TimeInterval(JodaTimeUtils.getDate(2010, 8, 1, JodaTimeUtils.GMT), JodaTimeUtils.getDate(2010, 8, 15, JodaTimeUtils.GMT));
        intervals[5] = new TimeInterval(JodaTimeUtils.getDate(2010, 8, 10, JodaTimeUtils.GMT), JodaTimeUtils.getDate(2010, 8, 20, JodaTimeUtils.GMT));
        intervals[6] = new TimeInterval(JodaTimeUtils.getDate(2010, 8, 30, JodaTimeUtils.GMT), JodaTimeUtils.getDate(2010, 9, 20, JodaTimeUtils.GMT));
        intervals[7] = new TimeInterval(JodaTimeUtils.getDate(2010, 10, 10, JodaTimeUtils.GMT), JodaTimeUtils.getDate(2010, 11, 20, JodaTimeUtils.GMT));
        intervals[8] = new TimeInterval(JodaTimeUtils.getDate(2010, 10, 20, JodaTimeUtils.GMT), JodaTimeUtils.getDate(2010, 11, 10, JodaTimeUtils.GMT));
        intervals[9] = new TimeInterval(JodaTimeUtils.getDate(2010, 4, 1, JodaTimeUtils.GMT), JodaTimeUtils.getDate(2010, 5, 15, JodaTimeUtils.GMT));
        intervals[10] = new TimeInterval(JodaTimeUtils.getDate(2010, 4, 1, JodaTimeUtils.GMT), JodaTimeUtils.getDate(2010, 11, 20, JodaTimeUtils.GMT));

        TimeIntervals instance1 = new TimeIntervals();
        for (int i = 0; i < intervals.length; ++i) {
            instance1.add(intervals[i]);
        }

        TimeIntervals expected = new TimeIntervals(
                new TimeInterval(JodaTimeUtils.getDate(2010, 4, 1, JodaTimeUtils.GMT), JodaTimeUtils.getDate(2010, 11, 20, JodaTimeUtils.GMT)));

        assertEquals(expected, instance1);
    }
}
