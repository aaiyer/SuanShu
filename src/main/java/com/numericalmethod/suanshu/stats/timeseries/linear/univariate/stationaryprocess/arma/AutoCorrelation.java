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

import com.numericalmethod.suanshu.stats.timeseries.linear.univariate.AutoCorrelationFunction;
import com.numericalmethod.suanshu.stats.timeseries.linear.univariate.arima.ARIMAModel;

/**
 * Compute the Auto-Correlation Function (ACF) for an AutoRegressive Moving Average (ARMA) model, assuming that
 * EX<sub>t</sub> = 0.
 *
 * <p>
 * This implementation solves the Yule-Walker equation.
 *
 * <p>
 * The R equivalent function are {@code ARMAacf} and {@code TacvfAR} in package {@code FitAR}.
 *
 * @author Haksun Li, Kevin Sun
 *
 * @see "P. J. Brockwell and R. A. Davis, "p. 420. Eq. 11.3.15. The Covariance Matrix Function of a Causal ARMA Process. Chapter 11.3. Multivariate Time Series," in <i>Time Series: Theory and Methods</i>, Springer, 2006."
 */
public class AutoCorrelation extends AutoCorrelationFunction {

    private com.numericalmethod.suanshu.stats.timeseries.linear.multivariate.stationaryprocess.arma.AutoCorrelation ACF;

    /** 
     * Compute the auto-correlation function of an ARMA model.
     * 
     * @param model an ARIMA specification
     * @param wnVariance white noise variance
     * @param nLags the number of lags in the result
     */
    public AutoCorrelation(ARIMAModel model, double wnVariance, int nLags) {
        //cast the parameters to multivariates variables
        com.numericalmethod.suanshu.stats.timeseries.linear.multivariate.arima.ARIMAModel model_m =
                new com.numericalmethod.suanshu.stats.timeseries.linear.multivariate.arima.ARIMAModel(model);

        ACF = new com.numericalmethod.suanshu.stats.timeseries.linear.multivariate.stationaryprocess.arma.AutoCorrelation(
                model_m,
                nLags);
    }

    @Override
    public double evaluate(double x1, double x2) {
        return evaluate(Math.abs(x2 - x1));
    }

    /**
     * Get the i-th auto-correlation.
     *
     * @param i the lag
     * @return the i-th auto-correlation
     */
    public double evaluate(double i) {
        return ACF.evaluate(i).get(1, 1);
    }
}
