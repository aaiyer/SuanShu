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

import com.numericalmethod.suanshu.stats.timeseries.linear.univariate.stationaryprocess.arma.ARMAModel;

/**
 * This class represents an ARIMA model.
 *
 * @author Haksun Li, Kevin Sun
 *
 * @see <a href="http://en.wikipedia.org/wiki/Autoregressive_integrated_moving_average">Wikipedia: Autoregressive integrated moving average</a>
 */
public class ARIMAModel extends ARIMAXModel {

    /**
     * Construct a univariate ARIMA model.
     *
     * @param mu the intercept (constant) term
     * @param AR the AR coefficients (excluding the initial 1); {@code null} if no AR coefficients
     * @param d the order of integration
     * @param MA the MA coefficients (excluding the initial 1); {@code null} if no MA coefficients
     * @param sigma the white noise variance
     */
    public ARIMAModel(double mu, double[] AR, int d, double[] MA, double sigma) {
        super(mu, AR, d, MA, null, sigma);//TODO: replace the 'super' with 'this'
    }

    /**
     * Construct a univariate ARIMA model with unit variance.
     *
     * @param mu the intercept (constant) term
     * @param AR the AR coefficients (excluding the initial 1); {@code null} if no AR coefficient
     * @param d the order of integration
     * @param MA the MA coefficients (excluding the initial 1); {@code null} if no MA coefficient
     */
    public ARIMAModel(double mu, double[] AR, int d, double[] MA) {
        this(mu, AR, d, MA, 1);
    }

    /**
     * Construct a zero-intercept (mu) univariate ARIMA model.
     *
     * @param AR the AR coefficients (excluding the initial 1); {@code null} if no AR coefficient
     * @param d the order of integration
     * @param MA the MA coefficients (excluding the initial 1); {@code null} if no MA coefficient
     * @param sigma the white noise variance
     */
    public ARIMAModel(double[] AR, int d, double[] MA, double sigma) {
        this(0, AR, d, MA, sigma);
    }

    /**
     * Construct a zero-intercept (mu) univariate ARIMA model with unit variance.
     *
     * @param AR the AR coefficients (excluding the initial 1); {@code null} if no AR coefficient
     * @param d the order of integration
     * @param MA the MA coefficients (excluding the initial 1); {@code null} if no MA coefficient
     */
    public ARIMAModel(double[] AR, int d, double[] MA) {
        this(AR, d, MA, 1);
    }

    /**
     * Copy constructor.
     *
     * @param that a univariate ARIMA model
     */
    public ARIMAModel(ARIMAModel that) {
        super(that);
    }

    /**
     * Get the ARMA specification of this ARIMA model, essentially ignoring the differencing.
     *
     * @return the ARMA specification
     */
    public ARMAModel getArma() {
        return new ARMAModel(mu, AR, MA, sigma);
    }
}
