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
import com.numericalmethod.suanshu.stats.timeseries.linear.multivariate.arima.ARIMAModel;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;

/**
 * This class represents a multivariate ARMA model.
 *
 * @author Kevin Sun
 *
 * @see "P. J. Brockwell and R. A. Davis, <i>Time Series: Theory and Methods,</i> Springer, 2006."
 */
public class ARMAModel extends ARIMAModel {

    /**
     * Construct a multivariate ARMA model.
     *
     * @param mu the intercept (constant) vector
     * @param phi the AR coefficients (excluding the initial 1); {@code null} if no AR coefficient
     * @param theta the MA coefficients (excluding the initial 1); {@code null} if no MA coefficient
     * @param sigma the covariance matrix of white noise
     */
    public ARMAModel(Vector mu, Matrix[] phi, Matrix[] theta, Matrix sigma) {
        super(mu, phi, 0, theta, sigma);
    }

    /**
     * Construct a multivariate ARMA model with unit variance.
     *
     * @param mu the intercept (constant) vector
     * @param phi the AR coefficients (excluding the initial 1); {@code null} if no AR coefficient
     * @param theta the MA coefficients (excluding the initial 1); {@code null} if no MA coefficient
     */
    public ARMAModel(Vector mu, Matrix[] phi, Matrix[] theta) {
        this(mu, phi, theta,
                new DenseMatrix(mu.size(), mu.size()).ONE());//sigma
    }

    /**
     * Construct a zero-intercept (mu) multivariate ARMA model.
     *
     * @param phi the AR coefficients (excluding the initial 1); {@code null} if no AR coefficient
     * @param theta the MA coefficients (excluding the initial 1); {@code null} if no MA coefficient
     * @param sigma the covariance matrix of white noise
     */
    public ARMAModel(Matrix[] phi, Matrix[] theta, Matrix sigma) {
        this(new DenseVector(R.rep(0., sigma.nRows())),//0 intercept
                phi, theta, sigma);
    }

    /**
     * Construct a zero-intercept (mu) multivariate ARMA model with unit variance.
     *
     * @param phi the AR coefficients (excluding the initial 1); {@code null} if no AR coefficient
     * @param theta the MA coefficients (excluding the initial 1); {@code null} if no MA coefficient
     */
    public ARMAModel(Matrix[] phi, Matrix[] theta) {
        this(phi, theta,
                new DenseMatrix(
                phi != null ? phi[0].nRows() : theta[0].nRows(),
                phi != null ? phi[0].nRows() : theta[0].nRows()).ONE());//sigma
    }

    /**
     * Copy constructor.
     *
     * @param that a multivariate ARMA model
     */
    public ARMAModel(ARMAModel that) {
        super(that);
    }

    /**
     * Cast a univariate ARMA model to a multivariate model.
     *
     * @param model a univariate ARMA model
     */
    public ARMAModel(com.numericalmethod.suanshu.stats.timeseries.linear.univariate.stationaryprocess.arma.ARMAModel model) {
        super(model);
    }

    /**
     * Compute the zero-intercept (mu) multivariate ARMA conditional mean.
     *
     * @param arLags the AR lags
     * @param maLags the MA lags
     * @return the conditional mean
     */
    public Matrix armaMeanNoIntercept(Matrix arLags, Matrix maLags) {
        Matrix arMean = new DenseMatrix(R.rep(0., dimension()), dimension(), 1);
        Matrix maMean = new DenseMatrix(R.rep(0., dimension()), dimension(), 1);

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

        Matrix mean = arMean.add(maMean);

        return mean;
    }

    /**
     * Compute the multivariate ARMA conditional mean.
     *
     * @param arLags the AR lags
     * @param maLags the MA lags
     * @return the conditional mean
     */
    public Matrix armaMean(Matrix arLags, Matrix maLags) {
        Matrix mu1 = armaMeanNoIntercept(arLags, maLags);
        Matrix mu2 = new DenseMatrix(this.mu()).add(mu1);
        return mu2;
    }
}
