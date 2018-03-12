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
 * This class represents a transitory Vector Error Correction Model (VECM).
 *
 * <p>
 * A transitory vector error correction model (VECM(p)) has the following specification:
 *
 * <blockquote><code>
 * ΔY_t = μ + Π * Y_{t-1} + Σ[Γ_i * ΔY_{t-i}] + ψ * D_t + ε_t, (i = 1, 2, ..., p-1),
 * </code></blockquote>
 *
 * where Y_s, μ and ε_s are n-dimensional vectors;
 * 
 * the impact matrix Π and the coefficients {Γ_i} of the lagged time series are (n * n) matrices;
 * D_t is an (m * 1) vector which contains all exogenous variables at time t (excl. the intercept term),
 * and its coefficients are represented by a (n * m) matrix ψ.
 *
 * <p>
 * This class also provides a conversion method between a transitory VECM(p) and a VARX(p) model.
 *
 * @author Kevin Sun
 *
 * @see
 * <ul>
 * <li> <a href="http://en.wikipedia.org/wiki/Error_correction_model">Wikipedia: Error correction model</a>
 * <li> <a href="http://en.wikipedia.org/wiki/Johansen_test#transitory">Wikipedia: Johansen test - The transitory VECM</a>
 * <li> S. Johansen, "Likelihood-Based Inference in Cointegrated Vector Autoregressive Models," Oxford, Oxford University Press, 1995, ch. 3-6, pp. 34-103.
 * <li> S. Johansen, "Estimation and Hypothesis Testing of Cointegration Vectors in Gaussian Vector Autoregressive Models," Econometrica, vol. 59, 1551-1580, 1991.
 * <li> A. Banerjee et al., "Cointegration, Error Correction, and the Econometric Analysis of Non-Stationary Data," Oxford, Oxford University Press, 1993.
 * </ul>
 */
public class VECMTransitory extends VECM {

    /**
     * Construct a transitory VECM(p) model.
     *
     * @param mu the intercept (constant) vector
     * @param pi the impact matrix
     * @param gamma the AR coefficients on the lagged differences; {@code null} if p = 1
     * @param psi the coefficients of the deterministic terms (excluding the intercept term)
     * @param sigma the covariance matrix of white noise
     */
    public VECMTransitory(Vector mu, Matrix pi, Matrix[] gamma, Matrix psi, Matrix sigma) {
        super(mu, pi, gamma, psi, sigma);
    }

    /**
     * Construct a zero-intercept (mu) transitory VECM(p) model.
     *
     * @param pi the impact matrix
     * @param gamma the AR coefficients on the lagged differences; {@code null} if p = 1
     * @param psi the coefficients of the deterministic terms (excluding the intercept term)
     * @param sigma the covariance matrix of white noise
     */
    public VECMTransitory(Matrix pi, Matrix[] gamma, Matrix psi, Matrix sigma) {
        this(new DenseVector(R.rep(0., pi.nRows())),//0 intercept,
                pi, gamma, psi, sigma);
    }

    public VECMTransitory(VARXModel varx) {
        super(fromVarx(varx));
    }

    /**
     * Construct a transitory VECM(p) from a VARX(p).
     *
     * @return a transitory VECM(p) model
     */
    public static VECMTransitory fromVarx(VARXModel varx) {
        Matrix[] vecm_gamma = varx.p() > 1 ? new Matrix[varx.p() - 1] : null;
        Matrix sum = new DenseMatrix(varx.dimension(), varx.dimension()).ZERO();
        for (int i = varx.p() - 1; i >= 1; --i) {
            sum = sum.minus(varx.AR(i + 1));
            vecm_gamma[i - 1] = new DenseMatrix(sum);
        }

        Matrix vecm_pi = sum.minus(varx.AR(1)).opposite().minus(sum.ONE());

        VECMTransitory vecm = new VECMTransitory(varx.mu(), vecm_pi, vecm_gamma, varx.psi(), varx.sigma());
        return vecm;
    }

    /**
     * Copy constructor.
     *
     * @param that a transitory VECM model
     */
    public VECMTransitory(VECMTransitory that) {
        super(that);
    }
}
