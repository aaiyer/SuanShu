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

import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import static com.numericalmethod.suanshu.stats.timeseries.univariate.UnivariateTimeSeriesUtils.*;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class UnivariateTimeSeriesUtilsTest {

    @Test
    public void test_0010() {
        double[] data = new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

        TimeSeries<Integer, TimeSeries.Entry<Integer>> t1 = new GenericTimeTimeSeries<Integer>(
                new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10},
                data);

        double[] arr = t1.toArray();
        assertArrayEquals(data, arr, 0);
    }

    @Test
    public void test_0020() {
        double[] data = new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

        TimeSeries<Integer, TimeSeries.Entry<Integer>> t1 = new GenericTimeTimeSeries<Integer>(
                new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10},
                data);

        Vector v1 = toVector(t1);

        assertEquals(new DenseVector(data), v1);
    }

    @Test
    public void test_0030() {
        double[] data = new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

        TimeSeries<Integer, TimeSeries.Entry<Integer>> t1 = new GenericTimeTimeSeries<Integer>(
                new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10},
                data);

        Matrix v1 = toMatrix(t1);

        assertEquals(new DenseMatrix(data, data.length, 1), v1);
    }
}
