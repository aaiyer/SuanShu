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
import com.numericalmethod.suanshu.stats.timeseries.multivariate.realtime.MultiVariateTimeSeries;
import com.numericalmethod.suanshu.stats.timeseries.multivariate.realtime.SimpleMultiVariateTimeSeries;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import com.numericalmethod.suanshu.vector.doubles.Vector;

/**
 * The innovation algorithm is an efficient way of
 * obtaining a one step least square linear predictor for a linear time series {X<sub>t</sub>} with known covariance structure.
 *
 * <p>
 * This implementation works for multivariate time series with known auto-covariance structure
 * and these properties (not limited to ARMA processes):
 * 
 * <ul>
 * <li>{X<sub>t</sub>} can be non-stationary.
 * <li>E{X<sub>t</sub>} = 0 for all <i>t</i>.
 * </ul>
 * 
 * @author Chun Yip Yau
 *
 * @see
 * <ul>
 * <li>"P. J. Brockwell and R. A. Davis, "Proposition. 5.2.2. Chapter 5. Multivariate Time Series," in <i>Time Series: Theory and Methods</i>, Springer, 2006."
 * <li>"P. J. Brockwell and R. A. Davis, "Proposition. 11.4.2. Chapter 11.4 Best Linear Predictors of Second Order Random Vectors," in <i>Time Series: Theory and Methods</i>, Springer, 2006."
 * </ul>
 */
public class InnovationAlgorithm extends InnovationAlgorithmImpl {

    /**
     * Construct an instance of <tt>InnovationAlgorithm</tt> for a multivariate time series with known auto-covariance structure.
     * 
     * @param Xt an m-dimensional time series, length <i>t</i>
     * @param K auto-covariance function K(i, j) = E(Xi * Xj'), a m x m matrix
     */
    public InnovationAlgorithm(MultiVariateTimeSeries Xt, AutoCovarianceFunction K) {
        int m = Xt.dimension();
        int t = Xt.size();

        run(t, K);//compute V and Theta

        Vector[] vt = new Vector[t + 1];//xHat_1 to xHat_(n+1); we count from 0
        vt[0] = new DenseVector(m, 0.0);//xHat_1 = 0 (n = 0)
        for (int n = 1; n <= t; ++n) {
            vt[n] = new DenseVector(m, 0.0);
            for (int j = 1; j <= n; ++j) {
                Vector TdX = theta(n, j).multiply(Xt.get(n + 1 - j).minus(vt[n - j]));
                vt[n] = vt[n].add(TdX);
            }
        }

        XtHat = new SimpleMultiVariateTimeSeries(vt);
    }
}
