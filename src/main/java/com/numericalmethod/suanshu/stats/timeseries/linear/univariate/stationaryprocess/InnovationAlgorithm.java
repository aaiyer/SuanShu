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
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.stats.timeseries.multivariate.realtime.MultiVariateTimeSeries;
import com.numericalmethod.suanshu.stats.timeseries.multivariate.realtime.SimpleMultiVariateTimeSeries;
import com.numericalmethod.suanshu.stats.timeseries.univariate.realtime.SimpleTimeSeries;
import com.numericalmethod.suanshu.stats.timeseries.univariate.realtime.TimeSeries;
import com.numericalmethod.suanshu.vector.doubles.Vector;

/**
 * The innovation algorithm is an efficient way of
 * obtaining a one step least square linear predictor for a linear time series {X<sub>t</sub>} with known auto-covariance.
 *
 * <ul>
 * <li>{X<sub>t</sub>} can be non-stationary.
 * <li>E{X<sub>t</sub>} = 0 for all <i>t</i>.
 * </ul>
 *
 * @author Chun Yip Yau
 *
 * @see "P. J. Brockwell and R. A. Davis, "Proposition. 5.2.2. Chapter 5. Multivariate Time Series," in <i>Time Series: Theory and Methods</i>, Springer, 2006."
 */
public class InnovationAlgorithm {

    private com.numericalmethod.suanshu.stats.timeseries.linear.multivariate.stationaryprocess.InnovationAlgorithm impl;

    /**
     * Construct an instance of <tt>InnovationAlgorithm</tt> for a univariate time series with known auto-covariance structure.
     *
     * @param Xt an univariate time series, length <i>t</i>
     * @param K the auto-covariance function
     */
    public InnovationAlgorithm(TimeSeries Xt, final AutoCovarianceFunction K) {
        com.numericalmethod.suanshu.stats.timeseries.linear.multivariate.AutoCovarianceFunction multiK =
                new com.numericalmethod.suanshu.stats.timeseries.linear.multivariate.AutoCovarianceFunction() {

                    @Override
                    public Matrix evaluate(double x1, double x2) {
                        return new DenseMatrix(new double[][]{{K.get((int) x1, (int) x2)}});
                    }
                };

        impl = new com.numericalmethod.suanshu.stats.timeseries.linear.multivariate.stationaryprocess.InnovationAlgorithm(new SimpleMultiVariateTimeSeries(Xt),
                multiK);
    }

    /**
     * Get the one-step prediction <i>X^<sub>t+1</sub></i>.
     *
     * @param t time, ranging from 0 to t
     * @return the one-step prediction <i>X^<sub>t+1</sub></i>
     */
    public double XtHat(int t) {
        Vector v = impl.XtHat(t);
        return v.get(1);
    }

    /**
     * Get all the one-step predictions <i>X^<sub>t+1</sub></i>, t ∈ [0, t]
     *
     * @return all the one-step predictions
     */
    public TimeSeries XtHat() {
        MultiVariateTimeSeries m = impl.XtHat();

        double[] data = new double[m.size()];
        for (int i = 1; i <= m.size(); ++i) {
            data[i - 1] = m.get(i).get(1);
        }

        return new SimpleTimeSeries(data);
    }

    /**
     * Get the mean squared error for prediction errors at time <i>t</i> for <i>X^<sub>t+1</sub></i>, i.e.,
     * <i>E(X_(t+1) - X^_(t+1))</i>
     *
     * @param t time, ranging from 0 to t
     * @return the mean squared error (variance)
     */
    public double var(int t) {
        return impl.covariance(t).get(1, 1);
    }
}
