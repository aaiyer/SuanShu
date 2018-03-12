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
 * This class represents a VARX (VAR model with eXogenous inputs) model.
 *
 * <p>
 * The VARX (Vector AutoRegressive model with eXogeneous inputs) process Y_t has the following specification:
 *
 * <blockquote><code>
 * Y_t = μ + Σ φ_i * Y_{t-i} + ψ * D_t + ε_t,
 * </code></blockquote>
 *
 * where Y_s, μ and ε_s are n-dimensional vectors;
 * (n * n) matrices {φ_i} (i = 1, 2, ..., p) denote the AR coefficients;
 * D_t is an (m * 1) vector which contains all exogenous variables at time t (excl. the intercept term),
 * and its coefficients are represented by a (n * m) matrix ψ.
 *
 * <p>
 * This class also provides conversion methods between a VARX(p) model and a VECM(p) (long-run or transitory).
 *
 * @author Kevin Sun
 *
 * @see
 * <ul>
 * <li> S. Johansen, “Likelihood-Based Inference in Cointegrated Vector Autoregressive Models,” Oxford, Oxford University Press, 1995, ch. 2-4, pp. 11-88.
 * <li> S. Johansen and K. Juselius, "Maximum Likelihood Estimation and Inference on Cointegration - with Applications to the Demand for Money," Oxford Bull. of Econ. and Stat, vol. 52, 169–210, 1990.
 * </ul>
 */
public class VARXModel extends ARMAXModel {

    /**
     * Construct a VARX model.
     *
     * @param mu the intercept (constant) vector
     * @param phi the AR coefficients (excluding the initial 1)
     * @param psi the coefficients of the deterministic terms (excluding the intercept term)
     * @param sigma the covariance matrix of white noise
     */
    public VARXModel(Vector mu, Matrix[] phi, Matrix psi, Matrix sigma) {
        super(mu, phi, null, psi, sigma);
    }

    /**
     * Construct a VARX model with unit variance.
     *
     * @param mu the intercept (constant) vector
     * @param phi the AR coefficients (excluding the initial 1)
     * @param psi the coefficients of the deterministic terms (excluding the intercept term)
     */
    public VARXModel(Vector mu, Matrix[] phi, Matrix psi) {
        this(mu, phi, psi,
                new DenseMatrix(mu.size(), mu.size()).ONE());//sigma
    }

    /**
     * Construct a zero-mean VARX model.
     *
     * @param phi the AR coefficients (excluding the initial 1)
     * @param psi the coefficients of the deterministic terms (excluding the intercept term)
     * @param sigma the covariance matrix of white noise
     */
    public VARXModel(Matrix[] phi, Matrix psi, Matrix sigma) {
        this(new DenseVector(R.rep(0., phi[0].nRows())),//0 intercept
                phi, psi, sigma);
    }

    /**
     * Construct a zero-mean VARX model with unit variance.
     *
     * @param phi the AR coefficients (excluding the initial 1)
     * @param psi the coefficients of the deterministic terms (excluding the intercept term)
     */
    public VARXModel(Matrix[] phi, Matrix psi) {
        this(new DenseVector(R.rep(0., phi[0].nRows())),//0 intercept
                phi, psi);
    }

    /**
     * Construct a VARX(p) from a transitory VECM(p).
     * 
     * @param vecm a transitory VECM(p)
     */
    public VARXModel(VECMTransitory vecm) {
        this(fromVecmTransitory(vecm));
    }

    /**
     * Convert a to VARX(p) from a transitory VECM(p).
     *
     * @param vecm a transitory VECM(p)
     */
    private static VARXModel fromVecmTransitory(VECMTransitory vecm) {
        Matrix[] varx_phi = new Matrix[vecm.p()];

        varx_phi[0] = vecm.pi().ONE().add(vecm.pi()).add(vecm.gamma(1));

        for (int i = 1; i < vecm.p() - 1; ++i) {
            varx_phi[i] = vecm.gamma(i + 1).minus(vecm.gamma(i));
        }

        varx_phi[vecm.p() - 1] = vecm.gamma(vecm.p() - 1).opposite();

        VARXModel varx = new VARXModel(vecm.mu(), varx_phi, vecm.psi(), vecm.sigma());
        return varx;
    }

    /**
     * Construct a VARX(p) from a long-run VECM(p).
     *
     * @param vecm a long-run VECM(p)
     */
    public VARXModel(VECMLongrun vecm) {
        this(fromVecmLongrun(vecm));
    }

    /**
     * Convert a VARX(p) from a long-run VECM(p).
     *
     * @param vecm a long-run VECM(p)
     */
    private static VARXModel fromVecmLongrun(VECMLongrun vecm) {
        Matrix[] varx_phi = new Matrix[vecm.p()];

        varx_phi[0] = vecm.gamma(1);
        varx_phi[0] = varx_phi[0].add(varx_phi[0].ONE());

        for (int i = 1; i < vecm.p() - 1; ++i) {
            varx_phi[i] = vecm.gamma(i + 1).minus(vecm.gamma(i));
        }

        varx_phi[vecm.p() - 1] = vecm.pi().minus(vecm.gamma(vecm.p() - 1));

        VARXModel varx = new VARXModel(vecm.mu(), varx_phi, vecm.psi(), vecm.sigma());
        return varx;
    }

    /**
     * Copy constructor.
     *
     * @param that a VARX model
     */
    public VARXModel(VARXModel that) {
        super(that);
    }
}
