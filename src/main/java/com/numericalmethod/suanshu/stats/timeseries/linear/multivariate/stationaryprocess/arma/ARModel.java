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
 * This class represents a VAR model.
 *
 * @author Kevin Sun
 *
 * @see <a href="http://en.wikipedia.org/wiki/Vector_Autoregression">Wikipedia: Vector autoregressive model</a>
 */
public class ARModel extends ARMAModel {

    /**
     * Construct a VAR model.
     *
     * @param mu the intercept (constant) vector
     * @param phi the AR coefficients (excluding the initial 1)
     * @param sigma the covariance matrix of white noise
     */
    public ARModel(Vector mu, Matrix[] phi, Matrix sigma) {
        super(mu, phi, null, sigma);
    }

    /**
     * Construct a VAR model with unit variance.
     *
     * @param mu the intercept (constant) vector
     * @param phi the AR coefficients (excluding the initial 1)
     */
    public ARModel(Vector mu, Matrix[] phi) {
        this(mu, phi,
                new DenseMatrix(phi[0].nRows(), phi[0].nRows()).ONE());//sigma
    }

    /**
     * Construct a zero-intercept (mu) VAR model.
     *
     * @param phi the AR coefficients (excluding the initial 1)
     * @param sigma the covariance matrix of white noise
     */
    public ARModel(Matrix[] phi, Matrix sigma) {
        this(new DenseVector(R.rep(0., phi[0].nRows())),//0 intercept,
                phi, sigma);
    }

    /**
     * Construct a zero-intercept (mu) VAR model with unit variance.
     *
     * @param phi the AR coefficients (excluding the initial 1)
     */
    public ARModel(Matrix[] phi) {
        this(new DenseVector(R.rep(0., phi[0].nRows())),//0 intercept,,
                phi);
    }

    /**
     * Copy constructor.
     *
     * @param that a VAR model
     */
    public ARModel(ARModel that) {
        super(that);
    }

    /**
     * Cast a univariate AR model to a multivariate model.
     *
     * @param model a univariate AR model
     */
    public ARModel(com.numericalmethod.suanshu.stats.timeseries.linear.univariate.stationaryprocess.arma.ARModel model) {
        super(model);
    }
}
