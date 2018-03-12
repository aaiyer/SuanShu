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

import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.misc.R;
import com.numericalmethod.suanshu.stats.timeseries.linear.multivariate.arima.ARIMAXModel;
import com.numericalmethod.suanshu.vector.doubles.Vector;

/**
 * This class represents a multivariate ARMAX (ARMA model with eXogenous inputs) model.
 * <p>
 * The multivariate ARMAX model incorporates exogenous variables and it can be considered as a generalization of the multivariate ARMA model.
 * Let Y_t be a multivariate ARMAX process, then
 * <blockquote><code>
 * Y_t = μ + Σ φ_i * Y_{t-i} + Σ θ_j * ε_{t-j} + ψ * D_t + ε_t.
 * </code></blockquote>
 *
 * In the equation above, Y_s, μ and ε_s are n-dimensional vectors;
 * (n * n) matrices {φ_i} and {θ_j} are the AR and MA coefficients, respectively;
 * D_t is an (m * 1) vector which contains all exogenous variables at time t (excl. the intercept term),
 * and its coefficients are represented by a (n * m) matrix ψ.
 *
 * @author Kevin Sun
 * 
 * @see <a href="http://en.wikipedia.org/wiki/Autoregressive_moving_average#Generalizations">Wikipedia: Autoregressive moving average model - Generalizations</a>
 */
public class ARMAXModel extends ARIMAXModel {

    /**
     * Construct a multivariate ARMAX (ARMA model with eXogenous inputs) model.
     *
     * @param mu the intercept (constant) vector
     * @param phi the AR coefficients (excluding the initial 1); {@code null} if no AR coefficient
     * @param theta the MA coefficients (excluding the initial 1); {@code null} if no MA coefficient
     * @param psi the coefficients of the deterministic terms (excluding the intercept term)
     * @param sigma the covariance matrix of white noise
     */
    public ARMAXModel(Vector mu, Matrix[] phi, Matrix[] theta, Matrix psi, Matrix sigma) {
        super(mu, phi, 0, theta, psi, sigma);
    }

    /**
     * Construct a multivariate ARMAX model with unit variance.
     *
     * @param mu the intercept (constant) vector
     * @param phi the AR coefficients (excluding the initial 1); {@code null} if no AR coefficient
     * @param theta the MA coefficients (excluding the initial 1); {@code null} if no MA coefficient
     * @param psi the coefficients of the deterministic terms (excluding the intercept term)
     */
    public ARMAXModel(Vector mu, Matrix[] phi, Matrix[] theta, Matrix psi) {
        super(mu, phi, 0, theta, psi);
    }

    /**
     * Construct a zero-intercept (mu) multivariate ARMAX model.
     *
     * @param phi the AR coefficients (excluding the initial 1); {@code null} if no AR coefficient
     * @param theta the MA coefficients (excluding the initial 1); {@code null} if no MA coefficient
     * @param psi the coefficients of the deterministic terms (excluding the intercept term)
     * @param sigma the covariance matrix of white noise
     */
    public ARMAXModel(Matrix[] phi, Matrix[] theta, Matrix psi, Matrix sigma) {
        super(phi, 0, theta, psi, sigma);
    }

    /**
     * Construct a zero-intercept (mu) multivariate ARMAX model with unit variance.
     *
     * @param phi the AR coefficients (excluding the initial 1); {@code null} if no AR coefficient
     * @param theta the MA coefficients (excluding the initial 1); {@code null} if no MA coefficient
     * @param psi the coefficients of the deterministic terms (excluding the intercept term)
     */
    public ARMAXModel(Matrix[] phi, Matrix[] theta, Matrix psi) {
        super(phi, 0, theta, psi);
    }

    /**
     * Copy constructor.
     *
     * @param that a multivariate ARMAX model
     */
    public ARMAXModel(ARMAXModel that) {
        super(that);
    }

    /**
     * Cast a univariate ARMAX model to a multivariate model.
     *
     * @param model a univariate ARIMA model
     */
    public ARMAXModel(com.numericalmethod.suanshu.stats.timeseries.linear.univariate.stationaryprocess.arma.ARMAXModel model) {
        super(model);
    }

    /**
     * Compute the zero-intercept (mu) multivariate ARMAX conditional mean.
     *
     * @param arLags the AR lags
     * @param maLags the MA lags
     * @param exVar the exogenous variables
     * @return the conditional mean
     */
    public Matrix armaxMeanNoIntercept(Matrix arLags, Matrix maLags, Vector exVar) {
        Matrix arMean = new DenseMatrix(R.rep(0., dimension()), dimension(), 1);
        Matrix maMean = new DenseMatrix(R.rep(0., dimension()), dimension(), 1);
        Matrix exMean = new DenseMatrix(psi().multiply(exVar));

        if (p() > 0) {
            for (int i = 1; i <= p(); ++i) {
                arMean = arMean.add(AR(i).multiply(new DenseMatrix(arLags.getColumn(i).toArray(), dimension(), 1)));
            }
        }

        if (q() > 0) {
            for (int j = 1; j <= q(); ++j) {
                maMean = maMean.add(MA(j).multiply(new DenseMatrix(maLags.getColumn(j).toArray(), dimension(), 1)));
            }
        }

        Matrix mean = arMean.add(maMean).add(exMean);

        return mean;
    }

    /**
     * Compute the multivariate ARMAX conditional mean.
     *
     * @param arLags the AR lags
     * @param maLags the MA lags
     * @param exVar the exogenous variables
     * @return the conditional mean
     */
    public Matrix armaxMean(Matrix arLags, Matrix maLags, Vector exVar) {
        Matrix mu1 = armaxMeanNoIntercept(arLags, maLags, exVar);
        Matrix mu2 = new DenseMatrix(this.mu()).add(mu1);
        return mu2;
    }
}

