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
package com.numericalmethod.suanshu.stats.timeseries.linear.univariate.stationaryprocess.arma;

import com.numericalmethod.suanshu.stats.timeseries.linear.univariate.arima.ARIMAXModel;
import static com.numericalmethod.suanshu.analysis.function.FunctionOps.dotProduct;

/**
 * This class represents a univariate ARMAX (ARMA model with eXogenous inputs) model.
 *
 * <p>
 * The ARMAX model incorporates exogenous variables and it can be considered as a generalization of the ARMA model.
 * Let Y_t be an ARMAX process, then
 * <blockquote><code>
 * Y_t = μ + Σ φ_i * Y_{t-i} + Σ θ_j * ε_{t-j} + ψ * D_t + ε_t,
 * </code></blockquote>
 * where D_t is an (m * 1) vector which contains all exogenous variables at time t (excl. the intercept term),
 * and its coefficients are represented by an m-dimensional vector ψ.
 *
 * @author Kevin Sun
 *
 * @see <a href="http://en.wikipedia.org/wiki/Autoregressive_moving_average#ARMAX">Wikipedia: Autoregressive moving average model with exogenous inputs model (ARMAX model)</a>
 */
public class ARMAXModel extends ARIMAXModel {

    /**
     * Construct a univariate ARMAX (ARMA model with eXogenous inputs) model.
     *
     * @param mu the intercept (constant) term
     * @param AR the AR coefficients (excluding the initial 1); {@code null} if no AR coefficient
     * @param MA the MA coefficients (excluding the initial 1); {@code null} if no MA coefficient
     * @param psi the coefficients of the deterministic terms (excluding the intercept term)
     * @param sigma the white noise variance
     */
    public ARMAXModel(double mu, double[] AR, double[] MA, double[] psi, double sigma) {
        super(mu, AR, 0, MA, psi, sigma);
    }

    /**
     * Construct a univariate ARMAX model with unit variance.
     *
     * @param mu the intercept (constant) term
     * @param AR the AR coefficients (excluding the initial 1); {@code null} if no AR coefficient
     * @param MA the MA coefficients (excluding the initial 1); {@code null} if no MA coefficient
     * @param psi the coefficients of the deterministic terms (excluding the intercept term)
     */
    public ARMAXModel(double mu, double[] AR, double[] MA, double[] psi) {
        this(mu, AR, MA, psi, 1);
    }

    /**
     * Construct a zero-intercept (mu) univariate ARMAX model.
     *
     * @param AR the AR coefficients (excluding the initial 1); {@code null} if no AR coefficient
     * @param MA the MA coefficients (excluding the initial 1); {@code null} if no MA coefficient
     * @param psi the coefficients of the deterministic terms (excluding the intercept term)
     * @param sigma the white noise variance
     */
    public ARMAXModel(double[] AR, double[] MA, double[] psi, double sigma) {
        this(0, AR, MA, psi, sigma);
    }

    /**
     * Construct a zero-intercept (mu) univariate ARMAX model with unit variance.
     *
     * @param AR the AR coefficients (excluding the initial 1); {@code null} if no AR coefficient
     * @param MA the MA coefficients (excluding the initial 1); {@code null} if no MA coefficient
     * @param psi the coefficients of the deterministic terms (excluding the intercept term)
     */
    public ARMAXModel(double[] AR, double[] MA, double[] psi) {
        this(0, AR, MA, psi);
    }

    /**
     * Copy constructor.
     *
     * @param that a univariate ARMAX model
     */
    public ARMAXModel(ARMAXModel that) {
        super(that);
    }

    /**
     * Compute the zero-intercept (mu) univariate ARMAX conditional mean.
     *
     * @param arLags the AR lags
     * @param maLags the MA lags
     * @param exVar the exogenous variables
     * @return the conditional mean
     */
    public double armaxMeanNoIntercept(double[] arLags, double[] maLags, double[] exVar) {
        double arMean = AR != null ? dotProduct(AR, arLags) : 0;
        double maMean = MA != null ? dotProduct(MA, maLags) : 0;
        double exMean = psi != null ? dotProduct(psi, exVar) : 0;
        double mean = arMean + maMean + exMean;
        return mean;
    }

    /**
     * Compute the univariate ARMAX conditional mean.
     *
     * @param arLags the AR lags
     * @param maLags the MA lags
     * @param exVar the exogenous variables
     * @return the conditional mean
     */
    public double armaxMean(double[] arLags, double[] maLags, double[] exVar) {
        double mu1 = armaxMeanNoIntercept(arLags, maLags, exVar);
        double mu2 = mu + mu1;
        return mu2;
    }
}
