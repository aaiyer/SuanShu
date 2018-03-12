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

import com.numericalmethod.suanshu.matrix.doubles.ImmutableMatrix;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.misc.R;
import static com.numericalmethod.suanshu.misc.SuanShuUtils.assertArgument;
import com.numericalmethod.suanshu.stats.timeseries.linear.multivariate.stationaryprocess.arma.ARMAXModel;
import com.numericalmethod.suanshu.vector.doubles.ImmutableVector;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import java.util.Arrays;

/**
 * This class represents a multivariate ARIMAX (ARIMA model with eXogenous inputs) model.
 *
 * <p>
 * The multivariate ARIMAX model incorporates exogenous variables and it can be considered as a generalization of the multivariate ARIMA model.
 * Letting <i>L</i> be lag operator, the <i>d</i>-th difference of a multivariate ARIMAX(p, d, q) process Y_t is
 * <blockquote><code>
 * X_t = (1 - L) ^ d * Y_t,
 * </code></blockquote>
 * where
 * X_t is an ARMAX(p, q) process, for which
 * <blockquote><code>
 * X_t = μ + Σ φ_i * X_{t-i} + Σ θ_j * ε_{t-j} + ψ * D_t + ε_t.
 * </code></blockquote>
 *
 * In the equation above, X_s, μ and ε_s are n-dimensional vectors; (n * n) matrices {φ_i} and {θ_j} are the AR and MA
 * coefficients, respectively; D_t is an (m * 1) vector which contains all exogenous variables at time t (excl. the intercept term),
 * and its coefficients are represented by a (n * m) matrix ψ.
 *
 * @author Kevin Sun
 */
public class ARIMAXModel {

    /**
     * the intercept (constant) vector
     */
    protected final ImmutableVector mu;//n * 1 vector
    /**
     * the AR coefficients
     */
    protected final ImmutableMatrix[] phi;//(n * n) square matrices
    /**
     * the order of integration
     */
    private final int d;
    /**
     * the MA coefficients
     */
    protected final ImmutableMatrix[] theta;//(n * n) square matrices
    /**
     * the coefficients of the deterministic terms (excluding the intercept term)
     */
    protected final ImmutableMatrix psi;//(n * m)
    /**
     * the covariance matrix of white noise
     */
    protected final ImmutableMatrix sigma;//(n * n) square matrices

    /**
     * Construct a multivariate ARIMAX (ARIMA model with eXogenous inputs) model.
     *
     * @param mu the intercept (constant) vector
     * @param phi the AR coefficients (excluding the initial 1); {@code null} if no AR coefficient
     * @param d the order of integration
     * @param theta the MA coefficients (excluding the initial 1); {@code null} if no MA coefficient
     * @param psi the coefficients of the deterministic terms (excluding the intercept term)
     * @param sigma the covariance matrix of white noise
     */
    public ARIMAXModel(Vector mu, Matrix[] phi, int d, Matrix[] theta, Matrix psi, Matrix sigma) {
        assertArgument(d >= 0, "d >= 0");
        this.d = d;

        this.mu = new ImmutableVector(mu);
        final int dim = mu.size();

        assertArgument((phi != null) || (theta != null),
                "at least one of phi and theta cannot be null");

        if (phi != null) {
            this.phi = new ImmutableMatrix[phi.length];
            for (int i = 0; i < phi.length; ++i) {
                assertArgument((phi[i].nRows() == dim) && (phi[i].nCols() == dim),
                        "each AR coefficient should be a square matrix of the correct dimension");
                this.phi[i] = new ImmutableMatrix(phi[i]);
            }
        } else {
            this.phi = null;
        }

        if (theta != null) {
            this.theta = new ImmutableMatrix[theta.length];
            for (int j = 0; j < theta.length; ++j) {
                assertArgument((theta[j].nRows() == dim) && (theta[j].nCols() == dim),
                        "each MA coefficient should be a square matrix of the correct dimension");
                this.theta[j] = new ImmutableMatrix(theta[j]);
            }
        } else {
            this.theta = null;
        }

        if (psi != null) {
            assertArgument((psi.nRows() == dim),
                    "number of rows of psi should be the same as the dimension of the multivariate ARIMA model");
            this.psi = new ImmutableMatrix(psi);
        } else {
            this.psi = null;
        }

        assertArgument((sigma.nRows() == dim) && (sigma.nCols() == dim),
                "sigma should be a square matrix of the correct dimension");
        this.sigma = new ImmutableMatrix(sigma);
    }

    /**
     * Construct a multivariate ARIMAX model with unit variance.
     *
     * @param mu the intercept (constant) vector
     * @param phi the AR coefficients (excluding the initial 1); {@code null} if no AR coefficient
     * @param d the order of integration
     * @param theta the MA coefficients (excluding the initial 1); {@code null} if no MA coefficient
     * @param psi the coefficients of the deterministic terms (excluding the intercept term)
     */
    public ARIMAXModel(Vector mu, Matrix[] phi, int d, Matrix[] theta, Matrix psi) {
        this(mu, phi, d, theta, psi,
                new DenseMatrix(mu.size(), mu.size()).ONE());//sigma
    }

    /**
     * Construct a zero-intercept (mu) multivariate ARIMAX model.
     *
     * @param phi the AR coefficients (excluding the initial 1); {@code null} if no AR coefficient
     * @param d the order of integration
     * @param theta the MA coefficients (excluding the initial 1); {@code null} if no MA coefficient
     * @param psi the coefficients of the deterministic terms (excluding the intercept term)
     * @param sigma the covariance matrix of white noise
     */
    public ARIMAXModel(Matrix[] phi, int d, Matrix[] theta, Matrix psi, Matrix sigma) {
        this(new DenseVector(R.rep(0., sigma.nRows())),//0 intercept
                phi, d, theta, psi, sigma);
    }

    /**
     * Construct a zero-intercept (mu) multivariate ARIMAX model with unit variance.
     *
     * @param phi the AR coefficients (excluding the initial 1); {@code null} if no AR coefficient
     * @param d the order of integration
     * @param theta the MA coefficients (excluding the initial 1); {@code null} if no MA coefficient
     * @param psi the coefficients of the deterministic terms (excluding the intercept term)
     */
    public ARIMAXModel(Matrix[] phi, int d, Matrix[] theta, Matrix psi) {
        this(phi, d, theta, psi,
                new DenseMatrix(
                phi != null ? phi[0].nRows() : theta[0].nRows(),
                phi != null ? phi[0].nRows() : theta[0].nRows()).ONE());//sigma
    }

    /**
     * Copy constructor.
     *
     * @param that a multivariate ARIMAX model
     */
    public ARIMAXModel(ARIMAXModel that) {
        this.mu = new ImmutableVector(new DenseVector(that.mu));
        this.phi = that.phi != null ? Arrays.copyOf(that.phi, that.phi.length) : null;
        this.theta = that.theta != null ? Arrays.copyOf(that.theta, that.theta.length) : null;
        this.psi = that.psi != null ? new ImmutableMatrix(new DenseMatrix(that.psi)) : null;
        this.d = that.d;
        this.sigma = new ImmutableMatrix(new DenseMatrix(that.sigma));
    }

    /**
     * Cast a univariate ARIMAX model to a multivariate model.
     *
     * @param model a univariate ARIMAX model
     */
    public ARIMAXModel(com.numericalmethod.suanshu.stats.timeseries.linear.univariate.arima.ARIMAXModel model) {
        this.mu = new ImmutableVector(new DenseVector(R.rep(model.mu(), 1)));
        this.d = model.d();
        this.sigma = new ImmutableMatrix(new DenseMatrix(new double[]{model.sigma()}, 1, 1));

        final int p = model.p();
        final int q = model.q();

        this.phi = p != 0 ? new ImmutableMatrix[p] : null;
        for (int i = 1; i <= p; ++i) {
            this.phi[i - 1] = new ImmutableMatrix(new DenseMatrix(new double[]{model.AR(i)}, 1, 1));
        }

        this.theta = q != 0 ? new ImmutableMatrix[q] : null;
        for (int i = 1; i <= q; ++i) {
            this.theta[i - 1] = new ImmutableMatrix(new DenseMatrix(new double[]{model.MA(i)}, 1, 1));
        }

        this.psi = model.psi() != null ? new ImmutableMatrix(new DenseMatrix(model.psi(), 1, model.psi().length)) : null;
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
     * Get the i-th AR coefficient; AR(0) = 1.
     *
     * @param i an index
     * @return the i-th AR coefficient
     */
    public ImmutableMatrix AR(int i) {
        return i == 0 ? new ImmutableMatrix(new DenseMatrix(dimension(), dimension()).ONE()) : phi[i - 1];
    }

    /**
     * Get the AR coefficients, excluding the initial 1.
     *
     * @return the AR coefficients; could be {@code null}
     */
    public ImmutableMatrix[] AR() {
        return phi != null ? phi : null;
    }

    /**
     * Get the i-th MA coefficient; AR(0) = 1.
     *
     * @param i an index
     * @return the i-th MA coefficient
     */
    public ImmutableMatrix MA(int i) {
        return i == 0 ? new ImmutableMatrix(new DenseMatrix(dimension(), dimension()).ONE()) : theta[i - 1];
    }

    /**
     * Get the MA coefficients, excluding the initial 1.
     *
     * @return the MA coefficients; could be {@code null}
     */
    public ImmutableMatrix[] MA() {
        return theta != null ? theta : null;
    }

    /**
     * Get the coefficients of the deterministic terms.
     *
     * @return the coefficients of the deterministic terms; could be {@code null}
     */
    public ImmutableMatrix psi() {
        return psi != null ? psi : null;
    }

    /**
     * Get the order of integration.
     *
     * @return the order of integration
     */
    public int d() {
        return d;
    }

    /**
     * Get the dimension of multivariate time series.
     *
     * @return the dimension of multivariate time series
     */
    public int dimension() {
        return phi != null ? phi[0].nCols() : theta[0].nCols();
    }

    /**
     * Get the number of AR terms.
     *
     * @return the number of AR terms
     */
    public int p() {
        return phi != null ? phi.length : 0;
    }

    /**
     * Get the number of MA terms.
     *
     * @return the number of MA terms
     */
    public int q() {
        return theta != null ? theta.length : 0;
    }

    /**
     * Get the maximum of AR length or MA length.
     *
     * @return max(AR terms, MA terms)
     */
    public int maxPQ() {
        return Math.max(p(), q());
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
     * Get the ARMAX specification of this ARIMAX model, essentially ignoring the differencing.
     *
     * @return the ARMAX specification
     */
    public ARMAXModel getArmax() {
        return new ARMAXModel(mu, phi, theta, psi, sigma);
    }
}
