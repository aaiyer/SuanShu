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

import static com.numericalmethod.suanshu.number.DoubleUtils.concat;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import java.util.Arrays;

/**
 * This implements a moving average filter with these properties:
 * 1) both past and future observations are used in smoothing;
 * 2) the head is prepended with the first element in the inputs (x_t = x_1 for t &lt; 1);
 * 3) the tail is appended with the last element in the inputs (x_t = x_n for t &gt; n).
 * When the filter length is even, more of the filter is forward in time than backward.
 * Note that both past as well as future values are used in generating the outputs.
 * Be cautious to use this in a forecasting model.
 *
 * @author Haksun Li
 */
public class MovingAverageByExtension implements Filter {

    private final double[] filter;

    /**
     * Construct a moving average filter with prepending and appending.
     *
     * @param filter the filter coefficients in reverse time order
     */
    public MovingAverageByExtension(double[] filter) {
        this.filter = Arrays.copyOf(filter, filter.length);
    }

    @Override
    public double[] transform(double[] x) {
        final int nObs = x.length;
        final int q = filter.length / 2;

        double[] augmentedX = concat(
                new DenseVector(q, x[0]).toArray(),//prepend the 1st element q times
                x,
                new DenseVector(q, x[nObs - 1]).toArray());//append the last element q times

        //the filtered smoother
        double[] mtHat = new double[nObs];
        for (int i = 0; i < nObs; ++i) {
            double sum = 0.;
            for (int j = 0; j < filter.length; ++j) {
                sum += augmentedX[i + 2 * q - j] * filter[filter.length - j - 1];//When the filter length is even, more of the filter is forward in time than backward.
            }

            mtHat[i] = sum;
        }

        return mtHat;
    }
}
