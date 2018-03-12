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
 * This class represents a long-run Vector Error Correction Model (VECM).
 *
 * <p>
 * A long-run vector error correction model (VECM(p)) has the following specification:
 *
 * <blockquote><code>
 * ΔY_t = μ + Π * Y_{t-p} + Σ[Γ_i * ΔY_{t-i}] + ψ * D_t + ε_t, (i = 1, 2, ..., p-1),
 * </code></blockquote>
 *
 * where Y_s, μ and ε_s are n-dimensional vectors;
 * the impact matrix Π and the coefficients {Γ_i} of the lagged time series are (n * n) matrices;
 * D_t is an (m * 1) vector which contains all exogenous variables at time t (excl. the intercept term),
 * and its coefficients are represented by a (n * m) matrix ψ.
 *
 * <p>
 * This class also provides a conversion method between a long-run VECM(p) and a VARX(p) model.
 *
 * @author Kevin Sun
 *
 * @see
 * <ul>
 * <li> <a href="http://en.wikipedia.org/wiki/Error_correction_model">Wikipedia: Error correction model</a>
 * <li> <a href="http://en.wikipedia.org/wiki/Johansen_test#longrun">Wikipedia: Johansen test - The longrun VECM</a>
 * <li> S. Johansen, “Likelihood-Based Inference in Cointegrated Vector Autoregressive Models,” Oxford, Oxford University Press, 1995, ch. 3-6, pp. 34-103.
 * <li> S. Johansen, "Estimation and Hypothesis Testing of Cointegration Vectors in Gaussian Vector Autoregressive Models," Econometrica, vol. 59, 1551-1580, 1991.
 * <li> A. Banerjee et al., "Cointegration, Error Correction, and the Econometric Analysis of Non-Stationary Data," Oxford, Oxford University Press, 1993.
 * </ul>
 */
public class VECMLongrun extends VECM {

    /**
     * Construct a long-run VECM(p) model.
     *
     * @param mu the intercept (constant) vector
     * @param pi the impact matrix
     * @param gamma the AR coefficients on the lagged differences; {@code null} if p = 1
     * @param psi the coefficients of the deterministic terms (excluding the intercept term)
     * @param sigma the covariance matrix of white noise
     */
    public VECMLongrun(Vector mu, Matrix pi, Matrix[] gamma, Matrix psi, Matrix sigma) {
        super(mu, pi, gamma, psi, sigma);
    }

    /**
     * Construct a zero-intercept (mu) long-run VECM(p) model.
     *
     * @param pi the impact matrix
     * @param gamma the AR coefficients on the lagged differences; {@code null} if p = 1
     * @param psi the coefficients of the deterministic terms (excluding the intercept term)
     * @param sigma the covariance matrix of white noise
     */
    public VECMLongrun(Matrix pi, Matrix[] gamma, Matrix psi, Matrix sigma) {
        this(new DenseVector(R.rep(0., pi.nRows())),//0 intercept,
                pi, gamma, psi, sigma);
    }

    /**
     * Construct a long-run VECM(p) from a VARX(p).
     *
     * @param varx a VARX model
     */
    public VECMLongrun(VARXModel varx) {
        this(fromVarx(varx));
    }

    private static VECMLongrun fromVarx(VARXModel varx) {
        Matrix[] vecm_gamma = varx.p() > 1 ? new Matrix[varx.p() - 1] : null;
        Matrix sum = new DenseMatrix(varx.dimension(), varx.dimension()).ONE().opposite();
        for (int i = 1; i < varx.p(); ++i) {
            sum = sum.add(varx.AR(i));
            vecm_gamma[i - 1] = sum;
        }

        Matrix vecm_pi = sum.add(varx.AR(varx.p()));

        return new VECMLongrun(varx.mu(), vecm_pi, vecm_gamma, varx.psi(), varx.sigma());
    }

    /**
     * Copy constructor.
     *
     * @param that a long-run VECM model
     */
    public VECMLongrun(VECMLongrun that) {
        super(that);
    }
}
