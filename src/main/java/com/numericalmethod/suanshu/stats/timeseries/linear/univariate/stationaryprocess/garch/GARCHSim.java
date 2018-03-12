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
package com.numericalmethod.suanshu.stats.timeseries.linear.univariate.stationaryprocess.garch;

import static com.numericalmethod.suanshu.misc.SuanShuUtils.assertArgument;
import com.numericalmethod.suanshu.stats.timeseries.linear.univariate.arima.ARIMASim;
import com.numericalmethod.suanshu.stats.timeseries.univariate.realtime.SimpleTimeSeries;
import com.numericalmethod.suanshu.stats.timeseries.univariate.realtime.TimeSeries;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import static java.lang.Math.sqrt;
import java.util.Arrays;

/**
 * This class simulates the GARCH models.
 *
 * <p>
 * An AutoRegressive Moving Average model (ARMA model) is assumed for the error variance,
 * the model is a Generalized AutoRegressive Conditional Heteroskedasticity (GARCH, Bollerslev(1986)) model.
 *
 * <p>
 * An AutoRegressive Conditional Heteroskedasticity (ARCH) models are used to characterize and model observed time series.
 * They are used whenever there's reason to believe that, at any point in a series, the terms will have a characteristic size, or variance.
 * In particular ARCH models assume the variance of the current error term or innovation to be a function of the actual sizes of the previous time periods' error terms:
 * often the variance is related to the squares of the previous innovations.
 *
 * @author Chun Yip Yau
 *
 * @see
 * <ul>
 * <li><a href="http://en.wikipedia.org/wiki/Autoregressive_conditional_heteroskedasticity#GARCH">Wikipedia: GARCH</a>
 * <li><a href="http://en.wikipedia.org/wiki/Autoregressive_conditional_heteroskedasticity">Wikipedia: Autoregressive conditional heteroskedasticity</a>
 * </ul>
 */
public class GARCHSim extends SimpleTimeSeries {

    /**
     * the conditional variances
     */
    private final double[] sigma2;

    private GARCHSim(Instance instance) {
        super(instance.series);
        sigma2 = instance.sigma2;
    }

    /**
     * Simulate an GARCH model.
     *
     * <p>
     * The innovation length is at least
     * <blockquote><code>
     * n + max(a.length, b.length)
     * </code></blockquote>
     *
     * @param n           the length of the time series to generate
     * @param model       a GARCH model specification
     * @param innovations the innovations
     */
    public GARCHSim(int n, GARCHModel model, double[] innovations) {
        this(sim(n, model, innovations));
    }

    /**
     * Simulate an GARCH model.
     * The random error terms are drawn from the standard Normal distribution.
     *
     * @param n     the length of the time series to generate
     * @param model a GARCH model specification
     */
    public GARCHSim(int n, GARCHModel model) {
        this(n, model, ARIMASim.getWhiteNoise(n + model.maxPQ()));
    }

    /**
     * Get a copy of the conditional variances.
     *
     * @return a copy of the conditional variances
     */
    public TimeSeries sigma2() {
        return new SimpleTimeSeries(sigma2);
    }

    /**
     * a trick so that {@link #sim} can return "two" values
     */
    private static class Instance {

        double[] series;
        double[] sigma2;
    }

    /**
     * an implementation of the GARCH simulation algorithm
     *
     * @param n           the length of the time series to generate
     * @param model       a GARCH model specification
     * @param innovations the innovations
     * @return a GARCH time series
     */
    private static Instance sim(int n, GARCHModel model, double[] innovations) {
        assertArgument(n > model.maxPQ(), "time series length > max(p, q)");

        int length = n + model.maxPQ();
        double[] Z = Arrays.copyOf(innovations, length);

        //simulation
        double[] series = new double[length];
        double[] sigma2 = new DenseVector(length, model.var()).toArray();//an interesting way to initialize a double[]
        for (int t = model.maxPQ(); t < length; ++t) {
            double[] e2 = new double[model.q()];
            for (int i = 1; i <= model.q(); ++i) {
                e2[i - 1] = series[t - i] * series[t - i];
            }

            double[] sigma_lag = new double[model.p()];
            for (int i = 1; i <= model.p(); ++i) {
                sigma_lag[i - 1] = sigma2[t - i];
            }

            sigma2[t] = model.sigma2(e2, sigma_lag);
            series[t] = sqrt(sigma2[t]) * Z[t];
        }

        Instance instance = new Instance();

        instance.series = Arrays.copyOfRange(series, series.length - n, series.length);
        instance.sigma2 = Arrays.copyOfRange(sigma2, sigma2.length - n, sigma2.length);

        return instance;
    }
}
