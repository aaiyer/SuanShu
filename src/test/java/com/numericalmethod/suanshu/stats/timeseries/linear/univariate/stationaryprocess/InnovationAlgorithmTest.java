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
package com.numericalmethod.suanshu.stats.timeseries.linear.univariate.stationaryprocess;

import com.numericalmethod.suanshu.stats.timeseries.linear.univariate.AutoCovarianceFunction;
import com.numericalmethod.suanshu.stats.timeseries.univariate.realtime.SimpleTimeSeries;
import com.numericalmethod.suanshu.stats.timeseries.univariate.realtime.TimeSeries;
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
        final double sigma2 = 1;

        SimpleTimeSeries Xt = new SimpleTimeSeries(new double[]{-2.58, 1.62, -0.96, 2.62, -1.36});

        AutoCovarianceFunction K = new AutoCovarianceFunction() {

            @Override
            public double evaluate(double x1, double x2) {
                int i = (int) x1;
                int j = (int) x2;

                if (i == j) {
                    return sigma2 * (1 + theta * theta);
                }

                if (i == j + 1) {
                    return theta * sigma2;
                }

                return 0;
            }
        };

        InnovationAlgorithm instance = new InnovationAlgorithm(Xt, K);
        TimeSeries ts = instance.XtHat();
        assertArrayEquals(new double[]{0, 1.28, -0.22, 0.55, -1.63, -0.22}, ts.toArray(), 1e-2);//XtHat

        double[] var = new double[6];
        for (int i = 0; i <= 5; ++i) {
            var[i] = instance.var(i);
        }
        assertArrayEquals(new double[]{1.81, 1.362, 1.215, 1.144, 1.102, 1.075}, var, 1e-3);//V[]
    }
}
