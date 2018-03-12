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

import com.numericalmethod.suanshu.matrix.doubles.ImmutableMatrix;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import static com.numericalmethod.suanshu.misc.SuanShuUtils.assertArgument;
import com.numericalmethod.suanshu.vector.doubles.ImmutableVector;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import java.util.Arrays;

/**
 * This class represents a Vector Error Correction Model (VECM).
 *
 * <p>
 * A vector error correction model (VECM(p)) has one the following specifications:
 *
 * <p>
 * (Transitory):
 * <blockquote><code>
 * ΔY_t = μ + Π * Y_{t-1} + Σ[Γ_i * Y_{t-i}] + ψ * D_t + ε_t, (i = 1, 2, ..., p-1),
 * </code></blockquote>
 *
 * or
 *
 * <p>
 * (Long-run):
 * <blockquote><code>
 * ΔY_t = μ + Π * Y_{t-p} + Σ[Γ_i * Y_{t-i}] + ψ * D_t + ε_t, (i = 1, 2, ..., p-1),
 * </code></blockquote>
 *
 * where Y_s, μ and ε_s are n-dimensional vectors;
 * 
 * the impact matrix Π and the coefficients {Γ_i} of the lagged time series are (n * n) matrices;
 * D_t is an (m * 1) vector which contains all exogenous variables at time t (excl. the intercept term),
 * and its coefficients are represented by a (n * m) matrix ψ.
 *
 * @author Kevin Sun
 *
 * @see
 * <ul>
 * <li> <a href="http://en.wikipedia.org/wiki/Error_correction_model">Wikipedia: Error correction model</a>
 * <li> <a href="http://en.wikipedia.org/wiki/Johansen_test">Wikipedia: Johansen test</a>
 * <li> S. Johansen, “Likelihood-Based Inference in Cointegrated Vector Autoregressive Models,” Oxford, Oxford University Press, 1995, ch. 3-6, pp. 34-103.
 * <li> S. Johansen, "Estimation and Hypothesis Testing of Cointegration Vectors in Gaussian Vector Autoregressive Models," Econometrica, vol. 59, 1551-1580, 1991.
 * <li> A. Banerjee et al., "Cointegration, Error Correction, and the Econometric Analysis of Non-Stationary Data," Oxford, Oxford University Press, 1993.
 * </ul>
 */
public class VECM {

    /**
     * the intercept (constant) vector
     */
    private final ImmutableVector mu;
    /**
     * the impact matrix
     */
    private final ImmutableMatrix pi;//an (n * n) square matrix
    /**
     * the AR coefficients on the lagged differences
     */
    private final ImmutableMatrix[] gamma;//(n * n) square matrices
    /**
     * the coefficients of the deterministic terms (excluding the intercept term)
     */
    private final ImmutableMatrix psi;//(n * m)
    /**
     * the covariance matrix of white noise
     */
    private final ImmutableMatrix sigma;

    /**
     * Construct a VECM(p) model.
     *
     * @param mu the intercept (constant) vector
     * @param pi the impact matrix
     * @param gamma the AR coefficients on the lagged differences; {@code null} if p = 1
     * @param psi the coefficients of the deterministic terms (excluding the intercept term)
     * @param sigma the covariance matrix of white noise
     */
    public VECM(Vector mu, Matrix pi, Matrix[] gamma, Matrix psi, Matrix sigma) {
        this.mu = new ImmutableVector(mu);

        final int dim = mu.size();

        assertArgument((pi.nRows() == dim) && (pi.nCols() == dim),
                "pi should be a square matrix of the correct dimension");
        this.pi = new ImmutableMatrix(pi);

        this.gamma = gamma != null ? new ImmutableMatrix[gamma.length] : null;
        if (gamma != null) {
            for (int i = 0; i < gamma.length; ++i) {
                assertArgument((gamma[i].nRows() == dim) && (gamma[i].nCols() == dim),
                        "each gamma should be a square matrix of the correct dimension");
                this.gamma[i] = new ImmutableMatrix(gamma[i]);
            }
        }

        this.psi = psi != null ? new ImmutableMatrix(psi) : null;
        if (psi != null) {
            assertArgument((psi.nRows() == dim),
                    "number of rows of psi should be the same as the dimension of the multivariate ARIMA model");
        }

        assertArgument((sigma.nRows() == dim) && (sigma.nCols() == dim),
                "sigma should be a square matrix of the correct dimension");
        this.sigma = new ImmutableMatrix(sigma);
    }

    /**
     * Copy constructor.
     *
     * @param that a VECM model
     */
    public VECM(VECM that) {
        this.mu = new ImmutableVector(new DenseVector(that.mu));
        this.pi = new ImmutableMatrix(new DenseMatrix(that.pi));
        this.gamma = Arrays.copyOf(that.gamma, that.gamma.length);
        this.psi = that.psi != null ? new ImmutableMatrix(new DenseMatrix(that.psi)) : null;
        this.sigma = new ImmutableMatrix(new DenseMatrix(that.sigma));
    }

    /**
     * Get the intercept vector.
     *
     * @return the intercept (constant) vector
     */
    public ImmutableVector mu() {
        return mu;
    }

    /**
     * Get the impact matrix.
     *
     * @return the impact matrix
     */
    public ImmutableMatrix pi() {
        return pi;
    }

    /**
     * Get the AR coefficient on the i-th lagged differences.
     *
     * @param i an index, count from 1
     * @return the AR coefficient on the i-th lagged differences
     */
    public ImmutableMatrix gamma(int i) {
        return i <= 0 ? null : gamma[i - 1];
    }

    //This method leaks out gamma to be modifiable!
    //TODO: ImmutableArray<T>
//    /**
//     * Get the AR coefficients on the lagged differences; {@code null} if p = 1
//     *
//     * @return the AR coefficients on the lagged differences; {@code null} if p = 1; could be {@code null}
//     */
//    public ImmutableMatrix[] gamma() {
//        return gamma != null ? gamma : null;
//    }
    /**
     * Get the coefficients of the deterministic terms.
     *
     * @return the coefficients of the deterministic terms; could be {@code null}
     */
    public ImmutableMatrix psi() {
        return psi != null ? psi : null;
    }

    /**
     * Get the covariance matrix of white noise.
     *
     * @return the covariance matrix of white noise
     */
    public ImmutableMatrix sigma() {
        return sigma;
    }

    /**
     * Get the dimension of multivariate time series.
     *
     * @return the dimension of multivariate time series
     */
    public int dimension() {
        return mu.size();
    }

    /**
     * Get the order of the VECM model.
     *
     * @return the the order of the VECM model
     */
    public int p() {
        return gamma != null ? (gamma.length + 1) : 1;
    }
}
