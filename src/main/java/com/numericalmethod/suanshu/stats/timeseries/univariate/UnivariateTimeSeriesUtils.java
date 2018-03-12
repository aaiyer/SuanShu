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

import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;

/**
 * These are the utility functions to manipulate {@linkplain com.numericalmethod.suanshu.stats.timeseries.univariate.TimeSeries a univariate time series}.
 *
 * @author Haksun Li
 */
public class UnivariateTimeSeriesUtils {

    private UnivariateTimeSeriesUtils() {
        //no ctor for utility class
    }

    /**
     * Cast a time series into a vector, discarding the timestamps.
     *
     * @param ts a time series
     * @return a vector of values
     */
    public static Vector toVector(TimeSeries<?, ?> ts) {
        double[] result = ts.toArray();
        return new DenseVector(result);
    }

    /**
     * Cast a time series into a column matrix, discarding the timestamps.
     *
     * @param ts a time series
     * @return a column matrix of values
     */
    public static Matrix toMatrix(TimeSeries<?, ?> ts) {
        double[] result = ts.toArray();
        return new DenseMatrix(result, result.length, 1);
    }
}
