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
package com.numericalmethod.suanshu.dsp.univariate.operation.system.doubles;

import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import java.util.Arrays;

/**
 * This applies a linear filter to a univariate time series using the moving average estimation.
 * <pre><code>
 * y[i] = f[1]*x[i+o] + … + f[p]*x[i+o-(p-1)]
 * </code></pre>
 * <i>o</i> is the offset, depending on whether only past values or both past and future values centered around lag 0 are used.
 * When a symmetric window is used and the filter length is even, more of the filter is forward in time than backward.
 *
 * <p>
 * The R equivalent function is {@code filter}.
 *
 * @author Haksun Li
 */
public class MovingAverage implements Filter {

    /**
     * the types of moving average filtering available
     */
    public static enum Side {

        /**
         * Use a symmetric window of past and future values, centered around lag 0.
         * When the filter length is even, more of the filter is forward in time than backward.
         */
        SYMMETRIC_WINDOW,
        /**
         * Use only past values.
         */
        PAST
    };

    private final Side side;
    private final double[] filter;

    /**
     * Construct a moving average filter.
     *
     * @param filter the filter coefficients in reverse time order
     * @param side   specify the data window to use
     */
    public MovingAverage(double[] filter, Side side) {
        this.filter = Arrays.copyOf(filter, filter.length);
        this.side = side;
    }

    /**
     * Construct a moving average filter using a symmetric window.
     *
     * @param filter the filter coefficients in reverse time order
     */
    public MovingAverage(double[] filter) {
        this(filter, Side.SYMMETRIC_WINDOW);
    }

    @Override
    public double[] transform(double[] x) {//TODO: anyway to construct a 'transformed' signal dynamically (w/o processing the whole data set in memory)?
        final int nObs = x.length;
        final int q = (filter.length - 1) / 2;//round down b/c when the filter length is even, more of the filter is forward in time than backward

        //the filtered smoother
        double[] mtHat = new DenseVector(nObs, Double.NaN).toArray();

        final int begin = side == Side.PAST ? filter.length - 1 : q;
        final int end = side == Side.PAST ? nObs : nObs - (filter.length / 2);
        final int offset = side == Side.PAST ? filter.length - 1 : q;

        for (int i = begin; i < end; ++i) {
            double sum = 0.;
            for (int j = 0; j < filter.length; ++j) {
                sum += x[i - offset + j] * filter[filter.length - j - 1];
            }

            mtHat[i] = sum;
        }

        return mtHat;
    }
}
