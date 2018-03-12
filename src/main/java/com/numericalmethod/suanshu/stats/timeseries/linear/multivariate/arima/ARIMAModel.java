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
package com.numericalmethod.suanshu.stats.timeseries.linear.multivariate.arima;

import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.misc.R;
import com.numericalmethod.suanshu.stats.timeseries.linear.multivariate.stationaryprocess.arma.ARMAModel;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;

/**
 * This class represents a multivariate ARIMA model.
 *
 * @author Kevin Sun
 */
public class ARIMAModel extends ARIMAXModel {

    /**
     * Construct a multivariate ARIMA model.
     *
     * @param mu the intercept (constant) vector
     * @param phi the AR coefficients (excluding the initial 1); {@code null} if no AR coefficient
     * @param d the order of integration
     * @param theta the MA coefficients (excluding the initial 1); {@code null} if no MA coefficient
     * @param sigma the covariance matrix of white noise
     */
    public ARIMAModel(Vector mu, Matrix[] phi, int d, Matrix[] theta, Matrix sigma) {
        super(mu, phi, d, theta, null, sigma);
    }

    /**
     * Construct a multivariate ARIMA model with unit variance.
     *
     * @param mu the intercept (constant) vector
     * @param phi the AR coefficients (excluding the initial 1); {@code null} if no AR coefficient
     * @param d the order of integration
     * @param theta the MA coefficients (excluding the initial 1); {@code null} if no MA coefficient
     */
    public ARIMAModel(Vector mu, Matrix[] phi, int d, Matrix[] theta) {
        this(mu, phi, d, theta,
                new DenseMatrix(mu.size(), mu.size()).ONE());//sigma
    }

    /**
     * Construct a zero-intercept (mu) multivariate ARIMA model.
     *
     * @param phi the AR coefficients (excluding the initial 1); {@code null} if no AR coefficient
     * @param d the order of integration
     * @param theta the MA coefficients (excluding the initial 1); {@code null} if no MA coefficient
     * @param sigma the covariance matrix of white noise
     */
    public ARIMAModel(Matrix[] phi, int d, Matrix[] theta, Matrix sigma) {
        this(new DenseVector(R.rep(0., sigma.nRows())),//0 intercept
                phi, d, theta, sigma);
    }

    /**
     * Construct a zero-intercept (mu) multivariate ARIMA model with unit variance.
     *
     * @param phi the AR coefficients (excluding the initial 1); {@code null} if no AR coefficient
     * @param d the order of integration
     * @param theta the MA coefficients (excluding the initial 1); {@code null} if no MA coefficient
     */
    public ARIMAModel(Matrix[] phi, int d, Matrix[] theta) {
        this(phi, d, theta,
                new DenseMatrix(
                phi != null ? phi[0].nRows() : theta[0].nRows(),
                phi != null ? phi[0].nRows() : theta[0].nRows()).ONE());//sigma
    }

    /**
     * Copy constructor.
     *
     * @param that a multivariate ARIMA model
     */
    public ARIMAModel(ARIMAModel that) {
        super(that);
    }

    /**
     * Cast a univariate ARIMA model to a multivariate model.
     *
     * @param model a univariate ARIMA model
     */
    public ARIMAModel(com.numericalmethod.suanshu.stats.timeseries.linear.univariate.arima.ARIMAModel model) {
        super(model);
    }

    /**
     * Get the ARMA specification of this ARIMA model, essentially ignoring the differencing.
     *
     * @return the ARMA specification
     */
    public ARMAModel getArma() {
        return new ARMAModel(mu, phi, theta, sigma);
    }
}
