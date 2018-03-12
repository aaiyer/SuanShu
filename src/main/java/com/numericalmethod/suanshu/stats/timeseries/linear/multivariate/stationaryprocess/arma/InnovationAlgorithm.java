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
package com.numericalmethod.suanshu.stats.timeseries.linear.multivariate.stationaryprocess.arma;

import com.numericalmethod.suanshu.stats.timeseries.multivariate.realtime.MultiVariateTimeSeries;
import com.numericalmethod.suanshu.stats.timeseries.multivariate.realtime.SimpleMultiVariateTimeSeries;
import com.numericalmethod.suanshu.stats.timeseries.linear.multivariate.stationaryprocess.InnovationAlgorithmImpl;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.stats.timeseries.linear.multivariate.AutoCovarianceFunction;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;

/**
 * This is an implementation, adapted for an ARMA process, of the innovation algorithm,
 * which is an efficient way of obtaining a one step least square linear predictor.
 *
 * @author Chun Yip Yau
 *
 * @see
 * <ul>
 * <li>"P. J. Brockwell and R. A. Davis, "Chapter 5.3. Recursive Prediction of an ARMA(p,q) Process," in <i>Time Series: Theory and Methods</i>, Springer, 2006."
 * <li>"P. J. Brockwell and R. A. Davis, "Eqs. 11.4.26, 11.4.27, 11.4.28, Chapter 11.4 Recursive Prediction of an ARMA(p,q) Process, Best Linear Predictors of Second Order Random Vectors," in <i>Time Series: Theory and Methods</i>, Springer, 2006."
 * </ul>
 */
public class InnovationAlgorithm extends InnovationAlgorithmImpl {

    /**
     * Construct an instance of <tt>InnovationAlgorithm</tt> for a multivariate ARMA time series.
     * 
     * @param Xt an m-dimensional time series, length <i>t</i>
     * @param model the ARMA model
     */
    public InnovationAlgorithm(MultiVariateTimeSeries Xt, ARMAModel model) {
        final int t = Xt.size();
        final int m = Xt.dimension();

        final int p = model.p();
        final int q = model.q();
        final int maxPQ = model.maxPQ();

        AutoCovariance Gamma = new AutoCovariance(
                model,
                2 * maxPQ);//the auto-covariance function of this ARMA time series

        AutoCovarianceFunction K = getK(Gamma, model);

        run(t, K);//compute V and Theta

        /*
         * compute XtHat as in Eq. 11.4.28
         *
         * The advantage of this XtHat computation over {@link InnovationAlgorithm} is that
         * for n >= m it takes only a summation of at most p past observations
         * and at most q past innovations to predict X_(n+1).
         *
         * Direct computation, as in {@link InnovationAlgorithm}, takes a summation of all the n preceeding innovations.
         *
         * @see "P. J. Brockwell and R. A. Davis, "pp. 176, Remark 2, Chapter 5.3. Recursive Prediction of an ARMA(p,q) Process," in <i>Time Series: Theory and Methods</i>, Springer, 2006."
         */
        Vector[] vt = new Vector[t + 1];
        vt[0] = new DenseVector(m, 0.0);

        for (int n = 1; n <= t; ++n) {
            vt[n] = new DenseVector(m, 0.0);

            if (n < maxPQ) {
                for (int j = 1; j <= n; ++j) {
                    Vector TdX = theta(n, j).multiply(Xt.get(n + 1 - j).minus(vt[n - j]));
                    vt[n] = vt[n].add(TdX);
                }
            } else {//n > maxPQ
                for (int j = 1; j <= p; ++j) {
                    Vector TX = model.AR(j).multiply(Xt.get(n + 1 - j));
                    vt[n] = vt[n].add(TX);
                }
                for (int j = 1; j <= q; ++j) {
                    Vector TdX = theta(n, j).multiply(Xt.get(n + 1 - j).minus(vt[n - j]));
                    vt[n] = vt[n].add(TdX);
                }
            }
        }

        XtHat = new SimpleMultiVariateTimeSeries(vt);
    }

    /**
     * Matrix K(i, j) = E(Wi * Wj') in Eq. 11.4.27 on p.427 of Brockwell and Davis.
     *
     * @param Gamma Γ, the auto-covariance function
     * @param model the ARMA model
     * @return the covariance function K(i, j) = E(Wi * Wj')
     */
    private AutoCovarianceFunction getK(final AutoCovariance Gamma, final ARMAModel model) {
        AutoCovarianceFunction K = new AutoCovarianceFunction() {

            @Override
            public Matrix evaluate(double x1, double x2) {//assume x1, x2 >= 1
                final int p = model.p();
                final int q = model.q();
                final int maxPQ = model.maxPQ();

                final int m = model.sigma().nCols();//dimensionality

                final int i = (int) x1;
                final int j = (int) x2;

                Matrix K = new DenseMatrix(m, m).ZERO();

                if (j < i) {
                    return evaluate(x2, x1).t();
                }

                if (i <= j && j <= maxPQ) {
                    K = Gamma.evaluate(i - j);
                }

                if (i <= maxPQ && maxPQ < j && j <= 2 * maxPQ) {
                    K = Gamma.evaluate(i - j);
                    for (int r = 1; r <= p; ++r) {
                        K = K.minus((model.AR(r).multiply(Gamma.evaluate(i + r - j))).t());
                    }
                }

                if (maxPQ < i && i <= j && j <= i + q) {//TODO: maxPQ < i?
                    K = new DenseMatrix(model.sigma());//r = 0, i == j

                    if (i != j) {
                        K = K.multiply(model.MA(j - i).t());//MA[0] = I
                    }

                    for (int r = 1; r <= q - j + i; ++r) {//when r + j - i - 1 > q, Θ = 0
                        Matrix TST = model.MA(r).multiply(model.sigma()).multiply(model.MA(r + j - i).t());
                        K = K.add(TST);
                    }
                }

                if (maxPQ < i && i + q < j) {//TODO: maxPQ < i?
                    K = new DenseMatrix(m, m).ZERO();
                }

                //if (j < i) {
                //    K = K.t();
                //}

                return K;
//                return new DenseMatrix(m, m).ZERO();
//                throw new RuntimeException("unreachable: invalid (i, j) for K");
            }
        };

        return K;
    }
}
