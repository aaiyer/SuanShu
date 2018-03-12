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
package com.numericalmethod.suanshu.stats.timeseries.multivariate.realtime;

import java.util.Iterator;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class SimpleMultiVariateTimeSeriesTest {

    @Test
    public void test_0010() {
        SimpleMultiVariateTimeSeries t1 = new SimpleMultiVariateTimeSeries(new double[][]{
                    {1, 1.1},
                    {2, 2.2},
                    {3, 3.3},
                    {4, 4.4}
                });

        Iterator<SimpleMultiVariateTimeSeries.Entry> it = t1.iterator();

        assertArrayEquals(new double[]{1, 1.1}, it.next().getValue().toArray(), 0.0);
        assertArrayEquals(new double[]{2, 2.2}, it.next().getValue().toArray(), 0.0);
        assertArrayEquals(new double[]{3, 3.3}, it.next().getValue().toArray(), 0.0);
        assertArrayEquals(new double[]{4, 4.4}, it.next().getValue().toArray(), 0.0);

        assertEquals(4, t1.size());
    }

    @Test
    public void test_0020() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 1.1},
                    {2, 2.2},
                    {3, 3.3},
                    {4, 4.4}
                });

        SimpleMultiVariateTimeSeries t1 = new SimpleMultiVariateTimeSeries(A1);

        Iterator<SimpleMultiVariateTimeSeries.Entry> it = t1.iterator();

        assertArrayEquals(new double[]{1, 1.1}, it.next().getValue().toArray(), 0.0);
        assertArrayEquals(new double[]{2, 2.2}, it.next().getValue().toArray(), 0.0);
        assertArrayEquals(new double[]{3, 3.3}, it.next().getValue().toArray(), 0.0);
        assertArrayEquals(new double[]{4, 4.4}, it.next().getValue().toArray(), 0.0);

        assertEquals(4, t1.size());

        assertEquals(A1, t1.toMatrix());
    }

    @Test
    public void test_0030() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 1.1},
                    {2, 2.2},
                    {3, 3.3},
                    {4, 4.4}
                });

        SimpleMultiVariateTimeSeries t1 = new SimpleMultiVariateTimeSeries(A1);
        SimpleMultiVariateTimeSeries t2 = new SimpleMultiVariateTimeSeries(A1);
        assertEquals(t1, t2);
    }

    @Test
    public void test_0040() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 1.1},
                    {2, 2.2},
                    {3, 3.3},
                    {4, 4.4}
                });

        SimpleMultiVariateTimeSeries t1 = new SimpleMultiVariateTimeSeries(A1);
        SimpleMultiVariateTimeSeries t2 = new SimpleMultiVariateTimeSeries(new double[][]{
                    {1, 1.1},
                    {2, 2.2},
                    {3, 3.3},
                    {4, 99}
                });
        assertFalse(t1.equals(t2));
    }

    @Test
    public void test_0050() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 1.1},
                    {2, 2.2},
                    {3, 3.3},
                    {4, 4.4},
                    {5, 5.5},
                    {6, 6.6},
                    {7, 7.7},
                    {8, 8.8}});

        SimpleMultiVariateTimeSeries t1 = new SimpleMultiVariateTimeSeries(A1);
        SimpleMultiVariateTimeSeries t2 = t1.drop(3);
        assertEquals(new SimpleMultiVariateTimeSeries(new double[][]{
                    {4, 4.4},
                    {5, 5.5},
                    {6, 6.6},
                    {7, 7.7},
                    {8, 8.8}}),
                t2);
    }

    @Test
    public void test_0060() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 1.1},
                    {2, 2.2},
                    {3, 3.3},
                    {4, 4.4},
                    {5, 5.5},
                    {6, 6.6},
                    {7, 7.7},
                    {8, 8.8}});

        SimpleMultiVariateTimeSeries t1 = new SimpleMultiVariateTimeSeries(A1);
        SimpleMultiVariateTimeSeries t2 = t1.lag(3, 4);
        assertEquals(new SimpleMultiVariateTimeSeries(new double[][]{
                    {2, 2.2},
                    {3, 3.3},
                    {4, 4.4},
                    {5, 5.5}}),
                t2);
    }

    @Test
    public void test_0070() {
        SimpleMultiVariateTimeSeries t1 = new SimpleMultiVariateTimeSeries(
                new DenseVector(1.0, 1.1),
                new DenseVector(2.0, 2.2),
                new DenseVector(3.0, 3.3),
                new DenseVector(4.0, 4.4));

        Iterator<SimpleMultiVariateTimeSeries.Entry> it = t1.iterator();

        assertArrayEquals(new double[]{1, 1.1}, it.next().getValue().toArray(), 0.0);
        assertArrayEquals(new double[]{2, 2.2}, it.next().getValue().toArray(), 0.0);
        assertArrayEquals(new double[]{3, 3.3}, it.next().getValue().toArray(), 0.0);
        assertArrayEquals(new double[]{4, 4.4}, it.next().getValue().toArray(), 0.0);

        assertEquals(4, t1.size());
    }

    @Test
    public void test_0110() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 1.1},
                    {2, 2.2},
                    {3, 3.3},
                    {4, 4.4},
                    {5, 5.5},
                    {6, 6.6},
                    {7, 7.7},
                    {8, 8.8}});

        SimpleMultiVariateTimeSeries t1 = new SimpleMultiVariateTimeSeries(A1);
        assertArrayEquals(new double[]{1, 1.1}, t1.get(1).toArray(), 0.0);
        assertArrayEquals(new double[]{8, 8.8}, t1.get(8).toArray(), 0.0);
    }
}
