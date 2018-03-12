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
package com.numericalmethod.suanshu.stats.timeseries.univariate;

import java.util.Iterator;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class GenericTimeTimeSeriesTest {

    @Test
    public void test_0010() {
        GenericTimeTimeSeries<Integer> t1 = new GenericTimeTimeSeries<Integer>(
                new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10},
                new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});
        GenericTimeTimeSeries<Integer> t2 = new GenericTimeTimeSeries<Integer>(
                new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10},
                new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});
        assertEquals(t1, t2);

        GenericTimeTimeSeries<Integer> t3 = new GenericTimeTimeSeries<Integer>(
                new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9},
                new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9});
        assertFalse(t1.equals(t3));
    }

    @Test
    public void test_0110() {
        GenericTimeTimeSeries<Integer> t1 = new GenericTimeTimeSeries<Integer>(
                new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10},
                new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});

        GenericTimeTimeSeries<Integer> t2 = t1.drop(0);
        assertEquals(t1, t2);
    }

    @Test
    public void test_0120() {
        GenericTimeTimeSeries<Integer> t1 = new GenericTimeTimeSeries<Integer>(
                new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10},
                new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});

        GenericTimeTimeSeries<Integer> t2_expected = new GenericTimeTimeSeries<Integer>(
                new Integer[]{6, 7, 8, 9, 10},
                new double[]{6, 7, 8, 9, 10});

        GenericTimeTimeSeries<Integer> t2 = t1.drop(5);
        assertEquals(t2_expected, t2);
    }

    @Test
    public void test_0310() {
        GenericTimeTimeSeries<Integer> t1 = new GenericTimeTimeSeries<Integer>(
                new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10},
                new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});

        GenericTimeTimeSeries<Integer> t2_expected = new GenericTimeTimeSeries<Integer>(
                new Integer[]{2, 3, 4, 5, 6, 7, 8, 9, 10},
                new double[]{1, 1, 1, 1, 1, 1, 1, 1, 1});

        GenericTimeTimeSeries<Integer> t2 = t1.diff(1);
        assertEquals(t2_expected, t2);
    }

    @Test
    public void test_0320() {
        GenericTimeTimeSeries<Integer> t1 = new GenericTimeTimeSeries<Integer>(
                new Integer[]{1, 2, 3, 4, 5, 6, 7},
                new double[]{1, 2, 4, 5, 6, 8, 9});

        GenericTimeTimeSeries<Integer> t2_expected = new GenericTimeTimeSeries<Integer>(
                new Integer[]{3, 4, 5, 6, 7},
                new double[]{1, -1, 0, 1, -1});

        GenericTimeTimeSeries<Integer> t2 = t1.diff(2);
        assertEquals(t2_expected, t2);
    }

    @Test
    public void test_0330() {
        GenericTimeTimeSeries<Integer> t1 = new GenericTimeTimeSeries<Integer>(
                new Integer[]{1, 2, 3, 4, 5, 6, 7},
                new double[]{1, 2, 4, 5, 6, 8, 9});

        GenericTimeTimeSeries<Integer> t2_expected = new GenericTimeTimeSeries<Integer>(
                new Integer[]{4, 5, 6, 7},
                new double[]{-2, 1, 1, -2});

        GenericTimeTimeSeries<Integer> t2 = t1.diff(3);
        assertEquals(t2_expected, t2);
    }

    @Test
    public void test_0340() {
        GenericTimeTimeSeries<Integer> t1 = new GenericTimeTimeSeries<Integer>(
                new Integer[]{1, 2, 3, 4, 5},
                new double[]{1, 3, 9, 15, 50});

        GenericTimeTimeSeries<Integer> t2 = new GenericTimeTimeSeries<Integer>(
                new Integer[]{3, 4, 5},
                new double[]{4, 0, 29});

        GenericTimeTimeSeries<Integer> t3 = t1.diff(2);
        assertEquals(t2, t3);
    }

    /**
     * recommended way to iterate
     */
    @Test
    public void test_0410() {
        GenericTimeTimeSeries<Integer> t1 = new GenericTimeTimeSeries<Integer>(
                new Integer[]{2, 4, 6, 8, 10, 12, 14, 16, 18, 20},
                new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});

        int i = 1;
        for (Iterator<TimeSeries.Entry<Integer>> it = t1.iterator(); it.hasNext(); ++i) {
            assertEquals(i, it.next().getValue(), 0);
        }
    }
}
