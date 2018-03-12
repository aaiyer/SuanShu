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
import com.numericalmethod.suanshu.matrix.doubles.operation.Inverse;
import com.numericalmethod.suanshu.matrix.doubles.ImmutableMatrix;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.stats.timeseries.multivariate.realtime.MultiVariateTimeSeries;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import com.numericalmethod.suanshu.vector.doubles.ImmutableVector;
import com.numericalmethod.suanshu.vector.doubles.Vector;

/**
 * This class implements the part of the innovation algorithm that computes the prediction coefficients, V and Θ.
 * Subclasses implement the actual prediction algorithms for {@code XtHat}.
 * 
 * @author Chun Yip Yau
 *
 * @see
 * <ul>
 * <li>"P. J. Brockwell and R. A. Davis, "Proposition. 5.2.2. Chapter 5. Multivariate Time Series," in <i>Time Series: Theory and Methods</i>, Springer, 2006."
 * <li>"P. J. Brockwell and R. A. Davis, "Proposition. 11.4.2. Chapter 11.4 Best Linear Predictors of Second Order Random Vectors," in <i>Time Series: Theory and Methods</i>, Springer, 2006."
 * </ul>
 */
public abstract class InnovationAlgorithmImpl {

    /**
     * the coefficients of the linear predictor at each time point
     *
     * <p>
     * Θ in Eq. 11.4.23; note that Theta[0][0] is not used to agree with the indexing in Eq. 11.4.23
     *
     * @see "P. J. Brockwell and R. A. Davis, "Proposition. 11.4.2. Chapter 11.4 Best Linear Predictors of Second Order Random Vectors," in <i>Time Series: Theory and Methods</i>, Springer, 2006."
     */
    private Matrix[][] Theta;//row/column index from 1 to t
    /**
     * the prediction mean squared error covariance matrices
     *
     * <p>
     * These are the covariance matrices for prediction errors at time <i>t</i> for <i>X^<sub>t+1</sub></i>, for all <i>t</i>'s.
     * Each V is m x m.
     */
    private Matrix[] V;//index from 0 to t
    /**
     * the one-step ahead predictors, <i>{X^<sub>t+1</sub>}</i>
     */
    protected MultiVariateTimeSeries XtHat;//length = n; dimension = m (n x m)

    protected InnovationAlgorithmImpl() {
    }

    /**
     * Run the Innovation Algorithm to compute the prediction parameters.
     * This computes the prediction parameters, V and Θ.
     *
     * @param t time series length
     * @param K the covariance structure of the time series
     */
    protected void run(int t, AutoCovarianceFunction K) {
        V = new Matrix[t + 1];//V in Eq. 11.4.23
        /*
         * the coefficients of the linear predictor at each time point
         * Θ in Eq. 11.4.23; note that Theta[0][0] is not used to agree with the indexing in Eq. 11.4.23
         */
        Theta = new Matrix[t + 1][t + 1];

        V[0] = K.get(1, 1);//V0 in Eq. 11.4.23

        for (int n = 1; n <= t; ++n) {
            //recursively find Θ as in Eq. 11.4.23
            for (int k = 0; k <= n - 1; ++k) {
                Theta[n][n - k] = K.get(n + 1, k + 1);

                for (int j = 0; j <= k - 1; ++j) {
                    Matrix TVT = Theta[n][n - j].multiply(V[j]).multiply(Theta[k][k - j].t());
                    Theta[n][n - k] = Theta[n][n - k].minus(TVT);
                }

                Theta[n][n - k] = Theta[n][n - k].multiply(new Inverse(V[k]));
            }

            //recursively find V
            V[n] = K.get(n + 1, n + 1);
            for (int j = 0; j <= n - 1; ++j) {//Vn in Eq. 11.4.23
                Matrix TVT = Theta[n][n - j].multiply(V[j]).multiply(Theta[n][n - j].t());
                V[n] = V[n].minus(TVT);
            }
        }
    }

    /**
     * Get the coefficients of the linear predictor.
     * 
     * @param i {@code i}, ranging from 1 to t
     * @param j {@code j}, ranging from 1 to t
     * @return Θ[i][j]
     */
    public ImmutableMatrix theta(int i, int j) {
        return new ImmutableMatrix(Theta[i][j]);
    }

    /**
     * Get the covariance matrix for prediction errors at time <i>t</i> for <i>X^<sub>t+1</sub></i>.
     *
     * @param t time, ranging from 0 to t
     * @return the covariance matrix for prediction errors at time <i>t</i>
     */
    public ImmutableMatrix covariance(int t) {
        return new ImmutableMatrix(V[t]);
    }

    /**
     * Get the one-step prediction <i>X^<sub>t+1</sub></i>.
     *
     * @param t time, ranging from 0 to t
     * @return the one-step prediction <i>X^<sub>t+1</sub></i>
     */
    public ImmutableVector XtHat(int t) {
        Vector result = t != 0 ? XtHat.get(t) : new DenseVector(XtHat.dimension(), 0);
        return new ImmutableVector(result);
    }

    /**
     * Get all the one-step predictions <i>X^<sub>t+1</sub></i>, t ∈ [0, t]
     *
     * @return all the one-step predictions
     */
    public MultiVariateTimeSeries XtHat() {
        return XtHat;
    }
}
