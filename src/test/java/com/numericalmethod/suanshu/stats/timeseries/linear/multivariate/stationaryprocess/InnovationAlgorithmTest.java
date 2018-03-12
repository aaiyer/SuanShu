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
package com.numericalmethod.suanshu.stats.timeseries.linear.multivariate.stationaryprocess;

import com.numericalmethod.suanshu.stats.timeseries.linear.multivariate.AutoCovarianceFunction;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.stats.timeseries.multivariate.realtime.SimpleMultiVariateTimeSeries;
import com.numericalmethod.suanshu.stats.timeseries.multivariate.realtime.MultiVariateTimeSeries;
import static com.numericalmethod.suanshu.matrix.doubles.operation.MatrixUtils.*;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class InnovationAlgorithmTest {

    /**
     * P. J. Brockwell and R. A. Davis, "Example 5.2.1. Chapter 5. Multivariate Time Series," in <i>Time Series: Theory and Methods</i>, Springer, 2006.
     */
    @Test
    public void test_0010() {
        final double theta = -0.9;
        final double sigma = 1;

        MultiVariateTimeSeries Xt = new SimpleMultiVariateTimeSeries(new double[][]{
                    {-2.58},
                    {1.62},
                    {-0.96},
                    {2.62},
                    {-1.36}
                });

        AutoCovarianceFunction K = new AutoCovarianceFunction() {

            @Override
            public Matrix evaluate(double x1, double x2) {
                int i = (int) x1;
                int j = (int) x2;

                double k = 0;

                if (i == j) {
                    k = sigma * sigma;
                    k *= 1 + theta * theta;
                }

                if (Math.abs(j - i) == 1) {
                    k = theta;
                    k *= sigma * sigma;
                }

                DenseMatrix result = new DenseMatrix(new double[][]{{k}});
                return result;
            }
        };

        InnovationAlgorithm instance = new InnovationAlgorithm(Xt, K);

        MultiVariateTimeSeries XtHat = new SimpleMultiVariateTimeSeries(new double[][]{
                    {0},
                    {1.28},
                    {-0.22},
                    {0.55},
                    {-1.63},
                    {-0.22}
                });

        assertArrayEquals(
                to1DArray(XtHat.toMatrix()),
                to1DArray(instance.XtHat().toMatrix()),
                1e-2);
    }
}
