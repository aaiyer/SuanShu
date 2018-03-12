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

import com.numericalmethod.suanshu.stats.timeseries.linear.univariate.arima.ARIMAModel;
import static com.numericalmethod.suanshu.analysis.function.FunctionOps.dotProduct;

/**
 * This class represents a univariate ARMA model.
 *
 * @author Haksun Li, Kevin Sun
 *
 * @see <a href="http://en.wikipedia.org/wiki/Autoregressive_moving_average">Wikipedia: Autoregressive moving average model</a>
 */
public class ARMAModel extends ARIMAModel {

    /**
     * Construct a univariate ARMA model.
     *
     * @param mu the intercept (constant) term
     * @param AR the AR coefficients (excluding the initial 1); {@code null} if no AR coefficients
     * @param MA the MA coefficients (excluding the initial 1); {@code null} if no MA coefficients
     * @param sigma the white noise variance
     */
    public ARMAModel(double mu, double[] AR, double[] MA, double sigma) {
        super(mu, AR, 0, MA, sigma);
    }

    /**
     * Construct a univariate ARMA model with unit variance.
     *
     * @param mu the intercept (constant) term
     * @param AR the AR coefficients (excluding the initial 1); {@code null} if no AR coefficient
     * @param MA the MA coefficients (excluding the initial 1); {@code null} if no MA coefficient
     */
    public ARMAModel(double mu, double[] AR, double[] MA) {
        this(mu, AR, MA, 1);
    }

    /**
     * Construct a zero-intercept (mu) univariate ARMA model.
     *
     * @param AR the AR coefficients (excluding the initial 1); {@code null} if no AR coefficient
     * @param MA the MA coefficients (excluding the initial 1); {@code null} if no MA coefficient
     * @param sigma the white noise variance
     */
    public ARMAModel(double[] AR, double[] MA, double sigma) {
        this(0, AR, MA, sigma);
    }

    /**
     * Construct a zero-intercept (mu) univariate ARMA model with unit variance.
     *
     * @param AR the AR coefficients (excluding the initial 1); {@code null} if no AR coefficient
     * @param MA the MA coefficients (excluding the initial 1); {@code null} if no MA coefficient
     */
    public ARMAModel(double[] AR, double[] MA) {
        this(AR, MA, 1);
    }

    /**
     * Copy constructor.
     *
     * @param that a univariate ARMA model
     */
    public ARMAModel(ARMAModel that) {
        super(that);
    }

    /**
     * Compute the zero-intercept (mu) univariate ARMA conditional mean.
     *
     * @param arLags the AR lags
     * @param maLags the MA lags
     * @return the conditional mean
     */
    public double armaMeanNoIntercept(double[] arLags, double[] maLags) {
        double arMean = AR != null ? dotProduct(AR, arLags) : 0;
        double maMean = MA != null ? dotProduct(MA, maLags) : 0;
        double mean = arMean + maMean;
        return mean;
    }

    /**
     * Compute the univariate ARMA conditional mean.
     *
     * @param arLags the AR lags
     * @param maLags the MA lags
     * @return the conditional mean
     */
    public double armaMean(double[] arLags, double[] maLags) {
        double mu1 = armaMeanNoIntercept(arLags, maLags);
        double mu2 = mu + mu1;
        return mu2;
    }
}
