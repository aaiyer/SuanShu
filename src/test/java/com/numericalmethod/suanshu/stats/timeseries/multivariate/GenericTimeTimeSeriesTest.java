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
package com.numericalmethod.suanshu.stats.timeseries.multivariate;

import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class GenericTimeTimeSeriesTest {

    @Test
    public void test_0010() {
        DateTime[] timestamps = new DateTime[5];
        timestamps[0] = new DateTime(2010, 4, 19, 21, 36, 0, 0, DateTimeZone.UTC);
        timestamps[1] = new DateTime(2010, 4, 20, 21, 36, 0, 0, DateTimeZone.UTC);
        timestamps[2] = new DateTime(2010, 4, 21, 21, 36, 0, 0, DateTimeZone.UTC);
        timestamps[3] = new DateTime(2010, 4, 22, 21, 36, 0, 0, DateTimeZone.UTC);
        timestamps[4] = new DateTime(2010, 4, 23, 21, 36, 0, 0, DateTimeZone.UTC);

        Vector[] values = new Vector[5];
        values[0] = new DenseVector(new double[]{1, 2, 3, 4});
        values[1] = new DenseVector(new double[]{11, 12, 13, 14});
        values[2] = new DenseVector(new double[]{21, 22, 23, 24});
        values[3] = new DenseVector(new double[]{31, 32, 33, 34});
        values[4] = new DenseVector(new double[]{41, 42, 43, 44});

        GenericTimeTimeSeries<DateTime> instance = new GenericTimeTimeSeries<DateTime>(timestamps, values);
        assertEquals(5, instance.size());
        assertEquals(4, instance.dimension());

        assertArrayEquals(values[0].toArray(), instance.get(1).toArray(), 0);
        assertArrayEquals(values[1].toArray(), instance.get(2).toArray(), 0);
        assertArrayEquals(values[2].toArray(), instance.get(3).toArray(), 0);
        assertArrayEquals(values[3].toArray(), instance.get(4).toArray(), 0);
        assertArrayEquals(values[4].toArray(), instance.get(5).toArray(), 0);

        assertEquals(timestamps[0], instance.time(1));
        assertEquals(timestamps[1], instance.time(2));
        assertEquals(timestamps[2], instance.time(3));
        assertEquals(timestamps[3], instance.time(4));
        assertEquals(timestamps[4], instance.time(5));
    }

    @Test
    public void test_0020() {
        DateTime[] timestamps = new DateTime[5];
        timestamps[0] = new DateTime(2010, 4, 19, 21, 36, 0, 0, DateTimeZone.UTC);
        timestamps[1] = new DateTime(2010, 4, 20, 21, 36, 0, 0, DateTimeZone.UTC);
        timestamps[2] = new DateTime(2010, 4, 21, 21, 36, 0, 0, DateTimeZone.UTC);
        timestamps[3] = new DateTime(2010, 4, 22, 21, 36, 0, 0, DateTimeZone.UTC);
        timestamps[4] = new DateTime(2010, 4, 23, 21, 36, 0, 0, DateTimeZone.UTC);

        Vector[] values = new Vector[5];
        values[0] = new DenseVector(new double[]{1, 2, 3, 4});
        values[1] = new DenseVector(new double[]{11, 12, 13, 14});
        values[2] = new DenseVector(new double[]{21, 22, 23, 24});
        values[3] = new DenseVector(new double[]{31, 32, 33, 34});
        values[4] = new DenseVector(new double[]{41, 42, 43, 44});

        GenericTimeTimeSeries<DateTime> ts = new GenericTimeTimeSeries<DateTime>(timestamps, values);
        GenericTimeTimeSeries<DateTime> instance = ts.drop(2);

        assertEquals(3, instance.size());
        assertEquals(4, instance.dimension());

        assertArrayEquals(values[2].toArray(), instance.get(1).toArray(), 0);
        assertArrayEquals(values[3].toArray(), instance.get(2).toArray(), 0);
        assertArrayEquals(values[4].toArray(), instance.get(3).toArray(), 0);

        assertEquals(timestamps[2], instance.time(1));
        assertEquals(timestamps[3], instance.time(2));
        assertEquals(timestamps[4], instance.time(3));
    }

    @Test
    public void test_0030() {
        DateTime[] timestamps = new DateTime[5];
        timestamps[0] = new DateTime(2010, 4, 19, 21, 36, 0, 0, DateTimeZone.UTC);
        timestamps[1] = new DateTime(2010, 4, 20, 21, 36, 0, 0, DateTimeZone.UTC);
        timestamps[2] = new DateTime(2010, 4, 21, 21, 36, 0, 0, DateTimeZone.UTC);
        timestamps[3] = new DateTime(2010, 4, 22, 21, 36, 0, 0, DateTimeZone.UTC);
        timestamps[4] = new DateTime(2010, 4, 23, 21, 36, 0, 0, DateTimeZone.UTC);

        Vector[] values = new Vector[5];
        values[0] = new DenseVector(new double[]{1, 2, 3, 4});
        values[1] = new DenseVector(new double[]{11, 12, 13, 14});
        values[2] = new DenseVector(new double[]{21, 22, 23, 24});
        values[3] = new DenseVector(new double[]{31, 32, 33, 34});
        values[4] = new DenseVector(new double[]{41, 42, 43, 44});

        GenericTimeTimeSeries<DateTime> ts = new GenericTimeTimeSeries<DateTime>(timestamps, values);
        GenericTimeTimeSeries<DateTime> instance = ts.diff(1);

        assertEquals(4, instance.size());
        assertEquals(4, instance.dimension());

        double[] diff = new double[]{10, 10, 10, 10};

        assertArrayEquals(diff, instance.get(1).toArray(), 0);
        assertArrayEquals(diff, instance.get(2).toArray(), 0);
        assertArrayEquals(diff, instance.get(3).toArray(), 0);
        assertArrayEquals(diff, instance.get(4).toArray(), 0);

        assertEquals(timestamps[1], instance.time(1));
        assertEquals(timestamps[2], instance.time(2));
        assertEquals(timestamps[3], instance.time(3));
        assertEquals(timestamps[4], instance.time(4));
    }

    @Test
    public void test_0040() {
        DateTime[] timestamps = new DateTime[5];
        timestamps[0] = new DateTime(2010, 4, 19, 21, 36, 0, 0, DateTimeZone.UTC);
        timestamps[1] = new DateTime(2010, 4, 20, 21, 36, 0, 0, DateTimeZone.UTC);
        timestamps[2] = new DateTime(2010, 4, 21, 21, 36, 0, 0, DateTimeZone.UTC);
        timestamps[3] = new DateTime(2010, 4, 22, 21, 36, 0, 0, DateTimeZone.UTC);
        timestamps[4] = new DateTime(2010, 4, 23, 21, 36, 0, 0, DateTimeZone.UTC);

        Vector[] values = new Vector[5];
        values[0] = new DenseVector(new double[]{1});
        values[1] = new DenseVector(new double[]{3});
        values[2] = new DenseVector(new double[]{9});
        values[3] = new DenseVector(new double[]{15});
        values[4] = new DenseVector(new double[]{50});

        GenericTimeTimeSeries<DateTime> ts = new GenericTimeTimeSeries<DateTime>(timestamps, values);
        GenericTimeTimeSeries<DateTime> instance = ts.diff(2);

        assertEquals(3, instance.size());
        assertEquals(1, instance.dimension());

        assertEquals(timestamps[2], instance.time(1));
        assertEquals(timestamps[3], instance.time(2));
        assertEquals(timestamps[4], instance.time(3));
    }

    @Test
    public void test_0050() {
        DateTime[] timestamps = new DateTime[5];
        timestamps[0] = new DateTime(2010, 4, 19, 21, 36, 0, 0, DateTimeZone.UTC);
        timestamps[1] = new DateTime(2010, 4, 20, 21, 36, 0, 0, DateTimeZone.UTC);
        timestamps[2] = new DateTime(2010, 4, 21, 21, 36, 0, 0, DateTimeZone.UTC);
        timestamps[3] = new DateTime(2010, 4, 22, 21, 36, 0, 0, DateTimeZone.UTC);
        timestamps[4] = new DateTime(2010, 4, 23, 21, 36, 0, 0, DateTimeZone.UTC);

        Vector[] values = new Vector[5];
        values[0] = new DenseVector(new double[]{1, 2, 3, 4});
        values[1] = new DenseVector(new double[]{11, 12, 13, 14});
        values[2] = new DenseVector(new double[]{21, 22, 23, 24});
        values[3] = new DenseVector(new double[]{31, 32, 33, 34});
        values[4] = new DenseVector(new double[]{41, 42, 43, 44});

        GenericTimeTimeSeries<DateTime> t1 = new GenericTimeTimeSeries<DateTime>(timestamps, values);
        GenericTimeTimeSeries<DateTime> t2 = new GenericTimeTimeSeries<DateTime>(timestamps, values);

        assertEquals(t2, t1);
    }
}
