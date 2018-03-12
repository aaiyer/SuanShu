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

import com.numericalmethod.suanshu.misc.R;
import static com.numericalmethod.suanshu.misc.SuanShuUtils.assertArgument;
import com.numericalmethod.suanshu.stats.random.univariate.normal.StandardNormalRng;
import com.numericalmethod.suanshu.stats.timeseries.univariate.realtime.SimpleTimeSeries;
import java.util.Arrays;

/**
 * This class simulates the ARIMA models.
 *
 * <p>
 * An AutoRegressive Integrated Moving Average (ARIMA) model is a generalization of an AutoRegressive Moving Average (ARMA) model.
 * They are applied in some cases where data show evidence of non-stationarity,
 * where an initial differencing step (corresponding to the "integrated" part of the model) can be applied to remove the non-stationarity.
 *
 * <p>
 * Given a time series of data Xt, the ARMA model is a tool for understanding and, perhaps, predicting future values in this series.
 * The model consists of two parts, an autoregressive (AR) part and a moving average (MA) part.
 * The model is usually then referred to as the ARMA(p,q) model where p is the order of the autoregressive part and q is the order of the moving average part.
 *
 * <p>
 * The notation AR(p) refers to the autoregressive model of order p. The AR(p) model is defined as
 * the weighted sum of the lagged values, a constant, and a white noise.
 *
 * <p>
 * The notation MA(q) refers to the autoregressive model of order q. The MA(q) model is defined as
 * the weighted sum of the lagged white noises and a drift.
 * That is, a moving average model is conceptually a linear regression of the current value of the series against previous (unobserved) white noise error terms or random shocks.
 * The random shocks are suppose to propagate to future values of the time series.
 *
 * @author Chun Yip Yau
 *
 * @see
 * <ul>
 * <li><a href="http://en.wikipedia.org/wiki/Autoregressive_integrated_moving_average">Wikipedia: Autoregressive integrated moving average</a>
 * <li><a href="http://en.wikipedia.org/wiki/Autoregressive_moving_average">Wikipedia: Autoregressive moving average model</a>
 * <li><a href="http://en.wikipedia.org/wiki/Autoregressive_model">Wikipedia: Autoregressive model</a>
 * <li><a href="http://en.wikipedia.org/wiki/Moving_average_model">Wikipedia: Moving average model</a>
 * <li>"P. J. Brockwell and R. A. Davis, "Chapter 9. Model Building and Forecasting with ARIMA Processes," in <i>Time Series: Theory and Methods</i>, Springer, 2006."</a>
 * </ul>
 */
public class ARIMASim extends SimpleTimeSeries {

    public static double[] getWhiteNoise(int size) {//TODO: other distributions? seed?
        StandardNormalRng rnorm = new StandardNormalRng();

        double[] wn = new double[size];
        for (int i = 0; i < size; ++i) {
            wn[i] = rnorm.nextDouble();
        }
        
        return wn;
    }

    /**
     * Simulate an ARIMA model.
     *
     * <p>
     * The innovation length is at least
     * <blockquote><code>
     * n + max(AR.length, MA.length) + d
     * </code></blockquote>
     *
     * @param n           the length of the time series to generate
     * @param arima       an ARIMA model
     * @param innovations the innovations
     */
    public ARIMASim(int n, ARIMAModel arima, double[] innovations) {
        super(sim(n, arima, innovations));
    }

    /**
     * Simulate an ARIMA model.
     * The random error terms are from the standard Normal distribution.
     *
     * @param n     the length of the time series to generate
     * @param arima an ARIMA model
     */
    public ARIMASim(int n, ARIMAModel arima) {
        this(n, arima, getWhiteNoise(n + arima.maxPQ() + arima.d()));
    }

    /**
     * an implementation of the simulation algorithm
     *
     * @param n           the length of the time series to generate
     * @param arima       an ARIMA model
     * @param innovations the innovations
     * @return an ARIMA time series
     */
    private static double[] sim(int n, ARIMAModel arima, double[] innovations) {
        int maxPQ = arima.maxPQ();
        assertArgument(n > maxPQ, "time series length > max(p, q)");

        int length = n + maxPQ + arima.d();
        double[] Z = Arrays.copyOf(innovations, length);

        //simulation
        double[] series0 = new double[length];

        for (int t = maxPQ; t < length; ++t) {
            double[] xLags = new double[arima.p()];//autoregressive
            for (int i = 1; i <= arima.p(); ++i) {
                xLags[i - 1] = series0[t - i];
            }

            double[] zLags = new double[arima.q()];//moving average
            for (int i = 1; i <= arima.q(); ++i) {
                zLags[i - 1] = Z[t - i];
            }

            //@see "P. J. Brockwell and R. A. Davis, "Eq. 3.1.4. Chapter 3. Stationary ARMA Processes," in <i>Time Series: Theory and Methods</i>, Springer, 2006."
            series0[t] = arima.getArma().armaMean(xLags, zLags) + Z[t];
        }

        double[] series1 = series0;
        for (int i = 1; i <= arima.d(); ++i) {
            series1 = R.cumsum(series1);
        }

        double[] series2 = Arrays.copyOfRange(series1, series1.length - n, series1.length);
        return series2;
    }
}
