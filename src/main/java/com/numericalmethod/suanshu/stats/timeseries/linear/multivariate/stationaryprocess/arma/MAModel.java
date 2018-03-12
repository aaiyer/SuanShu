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
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;

/**
 * This class represents a multivariate MA model.
 *
 * @author Kevin Sun
 */
public class MAModel extends ARMAModel {

    /**
     * Construct a multivariate MA model.
     *
     * @param mu the intercept (constant) vector
     * @param theta the MA coefficients (excluding the initial 1); {@code null} if no MA coefficients
     * @param sigma the covariance matrix of white noise
     */
    public MAModel(Vector mu, Matrix[] theta, Matrix sigma) {
        super(mu, null, theta, sigma);
    }

    /**
     * Construct a multivariate MA model with unit variance.
     *
     * @param mu the intercept (constant) vector
     * @param theta the MA coefficients (excluding the initial 1); {@code null} if no MA coefficients
     */
    public MAModel(Vector mu, Matrix[] theta) {
        this(mu, theta,
                new DenseMatrix(theta[0].nRows(), theta[0].nRows()).ONE());//sigma
    }

    /**
     * Construct a zero-mean multivariate MA model.
     *
     * @param theta the MA coefficients (excluding the initial 1); {@code null} if no MA coefficients
     * @param sigma the covariance matrix of white noise
     */
    public MAModel(Matrix[] theta, Matrix sigma) {
        this(new DenseVector(R.rep(0., theta[0].nRows())),//0 intercept,
                theta, sigma);
    }

    /**
     * Construct a zero-mean multivariate MA model with unit variance.
     *
     * @param theta the MA coefficients (excluding the initial 1); {@code null} if no MA coefficients
     */
    public MAModel(Matrix[] theta) {
        this(new DenseVector(R.rep(0., theta[0].nRows())),//0 intercept,,
                theta);
    }

    /**
     * Copy constructor.
     *
     * @param that a multivariate MA model
     */
    public MAModel(MAModel that) {
        super(that);
    }

    /**
     * Cast a univariate MA model to a multivariate model.
     *
     * @param model a univariate MA model
     */
    public MAModel(com.numericalmethod.suanshu.stats.timeseries.linear.univariate.stationaryprocess.arma.MAModel model) {
        super(model);
    }
}
