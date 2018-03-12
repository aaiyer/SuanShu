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
package com.numericalmethod.suanshu.stats.timeseries.univariate.realtime;

import java.util.Iterator;
import com.numericalmethod.suanshu.misc.R;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class SimpleTimeSeriesTest {

    @Test
    public void test_0010() {
        SimpleTimeSeries t1 = new SimpleTimeSeries(new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});
        SimpleTimeSeries t2 = new SimpleTimeSeries(new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});
        assertEquals(t1, t2);

        SimpleTimeSeries t3 = new SimpleTimeSeries(new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9});
        assertFalse(t1.equals(t3));
    }

    @Test
    public void test_0020() {
        SimpleTimeSeries t1 = new SimpleTimeSeries(new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});

        SimpleTimeSeries t2_expected = new SimpleTimeSeries(new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9});
        SimpleTimeSeries t2 = t1.lag(1);
        assertEquals(t2_expected, t2);
        SimpleTimeSeries t3 = t1.lag(1, 9);
        assertEquals(t2_expected, t3);

        SimpleTimeSeries t4_expected = new SimpleTimeSeries(new double[]{5, 6, 7, 8, 9});
        SimpleTimeSeries t4 = t1.lag(1, 5);
        assertEquals(t4_expected, t4);
    }

    @Test
    public void test_0030() {
        SimpleTimeSeries t1 = new SimpleTimeSeries(new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});

        SimpleTimeSeries t2 = t1.lag(0);
        assertEquals(t1, t2);
    }

    @Test
    public void test_0040() {
        SimpleTimeSeries t1 = new SimpleTimeSeries(new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});

        SimpleTimeSeries t2 = t1.lag(0, 0);
        assertEquals(0, t2.size());
    }

    @Test(expected = RuntimeException.class)
    public void test_0100() {
        SimpleTimeSeries t1 = new SimpleTimeSeries(new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});

        SimpleTimeSeries t2 = t1.lag(0, 11);
    }

    @Test
    public void test_0210() {
        SimpleTimeSeries t1 = new SimpleTimeSeries(new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});

        SimpleTimeSeries t2 = t1.drop(0);
        assertEquals(t1, t2);
    }

    @Test
    public void test_0220() {
        SimpleTimeSeries t1 = new SimpleTimeSeries(new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});

        SimpleTimeSeries t2_expected = new SimpleTimeSeries(new double[]{6, 7, 8, 9, 10});
        SimpleTimeSeries t2 = t1.drop(5);
        assertEquals(t2_expected, t2);
    }

    @Test
    public void test_0300() {
        SimpleTimeSeries t1 = new SimpleTimeSeries(new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});

        SimpleTimeSeries t2_expected = new SimpleTimeSeries(new double[]{1, 1, 1, 1, 1, 1, 1, 1, 1});
        SimpleTimeSeries t2 = t1.diff(1);
        assertEquals(t2_expected, t2);
    }

    @Test
    public void test_0310() {
        SimpleTimeSeries t1 = new SimpleTimeSeries(new double[]{1, 2, 4, 5, 6, 8, 9});

        SimpleTimeSeries t2_expected = new SimpleTimeSeries(new double[]{1, -1, 0, 1, -1});
        SimpleTimeSeries t2 = t1.diff(2);
        assertEquals(t2_expected, t2);
    }

    @Test
    public void test_0410() {
        SimpleTimeSeries t1 = new SimpleTimeSeries(
                new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});

        Iterator<TimeSeries.Entry> it = t1.iterator();
        for (int i = 1; it.hasNext(); ++i) {
            assertEquals(i, it.next().getValue(), 0);
        }
    }

    @Test
    public void test_0420() {
        SimpleTimeSeries t1 = new SimpleTimeSeries(
                new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});

        Iterator<TimeSeries.Entry> it = t1.iterator();
        for (int i = 1; it.hasNext(); ++i) {
            TimeSeries.Entry entry = it.next();
            assertEquals(i, entry.getTime().intValue());
            assertEquals(i, entry.getValue(), 0);
        }
    }

    @Test
    public void test_0430() {
        SimpleTimeSeries t1 = new SimpleTimeSeries(
                new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});

        Iterator<TimeSeries.Entry> it = t1.iterator();
        for (int i = 1; it.hasNext(); ++i) {
            com.numericalmethod.suanshu.stats.timeseries.univariate.realtime.TimeSeries.Entry entry = it.next();
            assertEquals(i, entry.getTime(), 0);
            assertEquals(i, entry.getValue(), 0);
        }
    }

    @Test
    public void test_0440() {
        SimpleTimeSeries t1 = new SimpleTimeSeries(
                new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});

        Iterator<TimeSeries.Entry> it = t1.iterator();
        for (int i = 1; it.hasNext(); ++i) {
            com.numericalmethod.suanshu.stats.timeseries.univariate.TimeSeries.Entry<Integer> entry = it.next();
            assertEquals(i, entry.getTime(), 0);
            assertEquals(i, entry.getValue(), 0);
        }
    }

    @Test
    public void test_0450() {
        SimpleTimeSeries t1 = new SimpleTimeSeries(
                new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});

        Iterator<TimeSeries.Entry> it = t1.iterator();
        for (int i = 1; it.hasNext(); ++i) {
            com.numericalmethod.suanshu.stats.timeseries.univariate.realtime.TimeSeries.Entry entry = it.next();
            assertEquals(i, entry.getTime(), 0);
            assertEquals(i, entry.getValue(), 0);
        }
    }

    /**
     * 2 different iterators
     */
    @Test
    public void test_0470() {
        SimpleTimeSeries t1 = new SimpleTimeSeries(
                new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});

        Iterator<TimeSeries.Entry> it = t1.iterator();
        for (int i = 1; it.hasNext(); ++i) {
            assertEquals(i, it.next().getValue(), 0);
        }

        it = t1.iterator();
        for (int i = 1; it.hasNext(); ++i) {
            assertEquals(i, it.next().getValue(), 0);
        }
    }

    @Test
    public void test_0510() {
        SimpleTimeSeries t1 = new SimpleTimeSeries(new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});
        String t1Str = t1.toString();
        assertEquals("length = 10\n[1,] 1.000000, [2,] 2.000000, [3,] 3.000000, [4,] 4.000000, [5,] 5.000000, [6,] 6.000000, [7,] 7.000000, [8,] 8.000000, [9,] 9.000000, [10,] 10.000000, ", t1Str);
    }

    @Test
    public void test_0520() {
        SimpleTimeSeries t1 = new SimpleTimeSeries(R.seq(1, 25, 1.0));
        String t1Str = t1.toString();
        assertEquals("length = 25\n[1,] 1.000000, [2,] 2.000000, [3,] 3.000000, [4,] 4.000000, [5,] 5.000000, [6,] 6.000000, [7,] 7.000000, [8,] 8.000000, [9,] 9.000000, [10,] 10.000000, [11,] 11.000000, [12,] 12.000000, [13,] 13.000000, [14,] 14.000000, [15,] 15.000000, [16,] 16.000000, [17,] 17.000000, [18,] 18.000000, [19,] 19.000000, [20,] 20.000000, \n[21,] 21.000000, [22,] 22.000000, [23,] 23.000000, [24,] 24.000000, [25,] 25.000000, ", t1Str);
    }

    @Test
    public void test_0610() {
        SimpleTimeSeries t1 = new SimpleTimeSeries(new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});
        assertEquals(1, t1.get(1), 0.0);
        assertEquals(10, t1.get(10), 0.0);
    }
}
