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
package com.numericalmethod.suanshu.stats.timeseries.linear.univariate.arima;

import static com.numericalmethod.suanshu.misc.SuanShuUtils.assertArgument;
import com.numericalmethod.suanshu.stats.timeseries.linear.univariate.stationaryprocess.arma.ARMAXModel;
import java.util.Arrays;

/**
 * This class represents a univariate ARIMAX (ARIMA model with eXogenous inputs) model.
 * <p>
 * The ARIMAX model incorporates exogenous variables and it can be considered as a generalization of the ARIMA model.
 * Letting <i>L</i> be lag operator, the <i>d</i>-th difference of an ARIMAX(p, d, q) process Y_t is
 * <blockquote><code>
 * X_t = (1 - L) ^ d * Y_t,
 * </code></blockquote>
 * where
 * X_t is an ARMAX(p, q) process, for which
 * <blockquote><code>
 * X_t = μ + Σ φ_i * X_{t-i} + Σ θ_j * ε_{t-j} + ψ' * D_t + ε_t,
 * </code></blockquote>
 * \[
 * X_t = \mu + \Sigma \phi_i X_{t-i} + \Sigma \theta_j \epsilon_{t-j} + \psi' D_t + \epsilon_t,
 * \]
 * where D_t is an (m * 1) vector which contains all exogenous variables at time t (excl. the intercept term),
 * and its coefficients are represented by an m-dimensional vector ψ.
 *
 * @author Kevin Sun
 */
public class ARIMAXModel {

    /**
     * the intercept (constant) term
     */
    protected final double mu;
    /**
     * the AR coefficients
     */
    protected final double[] AR;//φ, dimension = p
    /**
     * the MA coefficients
     */
    protected final double[] MA;//θ, dimension = q
    /**
     * the coefficients of the deterministic terms (excluding the intercept term)
     */
    protected final double[] psi;//ψ, dimension = m
    /**
     * the order of integration
     */
    private final int d;
    /**
     * the white noise variance
     */
    protected final double sigma;

    /**
     * Construct a univariate ARIMAX (ARIMA model with eXogenous inputs) model.
     *
     * @param mu the intercept (constant) term
     * @param AR the AR coefficients (excluding the initial 1); {@code null} if no AR coefficient
     * @param d the order of integration
     * @param MA the MA coefficients (excluding the initial 1); {@code null} if no MA coefficient
     * @param psi the coefficients of the deterministic terms (excluding the intercept term)
     * @param sigma the white noise variance
     */
    public ARIMAXModel(double mu, double[] AR, int d, double[] MA, double[] psi, double sigma) {
        assertArgument(AR != null || MA != null, "at least one of AR and MA terms cannot be null");
        assertArgument(d >= 0, "d >= 0");
        assertArgument(sigma >= 0, "sigma >= 0");

        this.mu = mu;
        this.AR = AR != null ? Arrays.copyOf(AR, AR.length) : null;
        this.MA = MA != null ? Arrays.copyOf(MA, MA.length) : null;
        this.psi = psi != null ? Arrays.copyOf(psi, psi.length) : null;
        this.d = d;
        this.sigma = sigma;
    }

    /**
     * Construct a univariate ARIMAX model with unit variance.
     *
     * @param mu the intercept (constant) term
     * @param AR the AR coefficients (excluding the initial 1); {@code null} if no AR coefficient
     * @param d the order of integration
     * @param MA the MA coefficients (excluding the initial 1); {@code null} if no MA coefficient
     * @param psi the coefficients of the deterministic terms (excluding the intercept term)
     */
    public ARIMAXModel(double mu, double[] AR, int d, double[] MA, double[] psi) {
        this(mu, AR, d, MA, psi, 1);
    }

    /**
     * Construct a zero-intercept (mu) univariate ARIMAX model.
     *
     * @param AR the AR coefficients (excluding the initial 1); {@code null} if no AR coefficient
     * @param d the order of integration
     * @param MA the MA coefficients (excluding the initial 1); {@code null} if no MA coefficient
     * @param psi the coefficients of the deterministic terms (excluding the intercept term)
     * @param sigma the white noise variance
     */
    public ARIMAXModel(double[] AR, int d, double[] MA, double[] psi, double sigma) {
        this(0, AR, d, MA, psi, sigma);
    }

    /**
     * Construct a zero-intercept (mu) univariate ARIMAX model with unit variance.
     *
     * @param AR the AR coefficients (excluding the initial 1); {@code null} if no AR coefficient
     * @param d the order of integration
     * @param MA the MA coefficients (excluding the initial 1); {@code null} if no MA coefficient
     * @param psi the coefficients of the deterministic terms (excluding the intercept term)
     */
    public ARIMAXModel(double[] AR, int d, double[] MA, double[] psi) {
        this(0, AR, d, MA, psi, 1);
    }

    /**
     * Copy constructor.
     *
     * @param that a univariate ARIMAX model
     */
    public ARIMAXModel(ARIMAXModel that) {
        this.mu = that.mu;
        this.AR = that.AR != null ? Arrays.copyOf(that.AR, that.AR.length) : null;
        this.MA = that.MA != null ? Arrays.copyOf(that.MA, that.MA.length) : null;
        this.psi = that.psi != null ? Arrays.copyOf(that.psi, that.psi.length) : null;
        this.d = that.d;
        this.sigma = that.sigma;
    }

    /**
     * Get the intercept term.
     *
     * @return the intercept (constant) term
     */
    public double mu() {
        return mu;
    }

    /**
     * Get the i-th AR coefficient; AR(0) = 1.
     *
     * @param i an index
     * @return the i-th AR coefficient
     */
    public double AR(int i) {
        return i == 0 ? 1 : AR[i - 1];
    }

    /**
     * Get the AR coefficients, excluding the initial 1.
     *
     * @return the AR coefficients; could be {@code null}
     */
    public double[] AR() {
        return AR != null ? Arrays.copyOf(AR, AR.length) : null;
    }

    /**
     * Get the i-th MA coefficient; MA(0) = 1.
     *
     * @param i an index
     * @return the i-th MA coefficient
     */
    public double MA(int i) {
        return i == 0 ? 1 : MA[i - 1];
    }

    /**
     * Get the MA coefficients, excluding the initial 1.
     *
     * @return the MA coefficients; could be {@code null}
     */
    public double[] MA() {
        return MA != null ? Arrays.copyOf(MA, MA.length) : null;
    }

    /**
     * Get the coefficients of the deterministic terms.
     *
     * @return the coefficients of the deterministic terms; could be {@code null}
     */
    public double[] psi() {
        return psi != null ? Arrays.copyOf(psi, psi.length) : null;
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
     * Get the number of AR terms.
     *
     * @return the number of AR terms
     */
    public int p() {
        return AR != null ? AR.length : 0;
    }

    /**
     * Get the number of MA terms.
     *
     * @return the number of MA terms
     */
    public int q() {
        return MA != null ? MA.length : 0;
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
     * Get the white noise variance.
     *
     * @return the white noise variance
     */
    public double sigma() {
        return sigma;
    }

    /**
     * Get the ARMAX specification of this ARIMAX model, essentially ignoring the differencing.
     *
     * @return the ARMAX specification
     */
    public ARMAXModel getArmax() {
        return new ARMAXModel(mu, AR, MA, psi, sigma);
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("Xt = ");
        str.append(mu);

        str.append(" + (");
        if (AR != null) {
            for (int i = 0; i < AR.length; ++i) {
                str.append(AR[i]);
                str.append(String.format("*X_{t-%d}", i + 1));
            }
        }
        str.append(") ");

        str.append(" + (");
        if (MA != null) {
            for (int i = 0; i < MA.length; ++i) {
                str.append(MA[i]);
                str.append(String.format("*e_{t-%d}", i + 1));
            }
        }
        str.append(") ");

        str.append(" + (");
        if (psi != null) {
            for (int i = 0; i < psi.length; ++i) {
                str.append(psi[i]);
                str.append(String.format("*D_{t-%d}", i));
            }
        }
        str.append(") ");

        str.append(" + e_t");

        str.append(String.format("; var(e_t) = %f, d = %d", sigma, d));

        return str.toString();
    }
}
