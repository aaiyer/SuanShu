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
package com.numericalmethod.suanshu.stats.timeseries.linear.univariate.stationaryprocess;

import static com.numericalmethod.suanshu.misc.SuanShuUtils.assertArgument;
import com.numericalmethod.suanshu.dsp.univariate.operation.system.doubles.Filter;
import com.numericalmethod.suanshu.dsp.univariate.operation.system.doubles.MovingAverage;
import com.numericalmethod.suanshu.stats.descriptive.moment.Mean;
import com.numericalmethod.suanshu.stats.timeseries.univariate.realtime.SimpleTimeSeries;
import com.numericalmethod.suanshu.stats.timeseries.univariate.realtime.TimeSeries;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;

/**
 * This class decomposes a time series into the trend, seasonal and the stationary random components
 * using the Moving Average Estimation with symmetric window.
 *
 * <p>
 * That is,
 * <blockquote><code><pre>
 * X<sub>t</sub> = m<sub>t</sub> + s<sub>t</sub> + Y<sub>t</sub>
 * </pre></code></blockquote>
 *
 * We have
 * <ul>
 * EY<sub>t</sub> = 0
 * s<sub>t+d</sub> = s<sub>t</sub>
 * Σ(s<sub>j</sub>) = 0
 * </ul>
 *
 * <p>
 * The R equivalent function is {@code decompose}.
 *
 * @author Chun Yip Yau
 *
 * @see "P. J. Brockwell and R. A. Davis, "p. 23. Chapter 1.4. Elimination of both Trend and Seasonality," in <i>Time Series: Theory and Methods</i>, 2nd ed. Springer, 2006."
 */
public class MADecomposition {

    /**
     * the estimated trend of the time series
     */
    public final TimeSeries trend;
    /**
     * the estimated seasonal effect of the time series
     */
    public final TimeSeries seasonal;
    /**
     * the stationary random component of the time series after the trend and seasonal components are removed
     */
    public final TimeSeries random;

    /**
     * Decompose a time series into the trend, seasonal and the stationary random components using the Moving Average Estimation.
     * 
     * @param Xt a time series
     * @param MAFilter the moving average filter to smooth the time series
     * @param period the period of the time series; if aperiodic, use 1
     */
    public MADecomposition(TimeSeries Xt, double[] MAFilter, int period) {
        assertArgument(MAFilter.length > 0, "MAFilter must be supplied");

        assertArgument(period >= 1, "period must be +ve");

        final double[] Xarr = Xt.toArray();
        final Vector xt = new DenseVector(Xarr);
        final int nObs = xt.size();

        //compute the moving average smoother
        Filter filter = new MovingAverage(MAFilter);
        double[] mt = filter.transform(Xarr);

        Vector dt = xt;//when aperiodic; st = 0; dt = xt - st = xt
        Vector sk = null;
        if (period > 1) {
            //estimate the seasonal component
            Mean[] w = new Mean[period];
            for (int i = 0; i < nObs; ++i) {//TODO: in Java, how do you initialize an array of Objects with default constructors?
                w[i % period] = new Mean();
            }

            double[] err = new double[nObs];
            for (int i = 0; i < nObs; ++i) {
                err[i] = xt.get(i + 1) - mt[i];//deviations from the mean

                if (!Double.isNaN(err[i])) {
                    w[i % period].addData(err[i]);
                }
            }

            double w_mean = 0;
            for (int i = 0; i < period; ++i) {
                w_mean += w[i].value();
            }
            w_mean /= w.length;

            sk = new DenseVector(nObs);
            for (int i = 1; i <= nObs; ++i) {
                int k = (i - 1) % period;
                sk.set(i, w[k].value() - w_mean);
            }

            //the deseasonalized data
            dt = xt.minus(sk);//dt = xt - st

            mt = filter.transform(dt.toArray());
        }

        //the stationary random component
        Vector Yt = dt.minus(new DenseVector(mt));//Yt = xt - mt - st = dt - mt

        //package the results
        trend = new SimpleTimeSeries(mt);
        seasonal = sk != null ? new SimpleTimeSeries(sk.toArray()) : new SimpleTimeSeries(new DenseVector(nObs, 0).toArray());
        random = new SimpleTimeSeries(Yt.toArray());
    }

    /**
     * Decompose a periodic time series into the seasonal and stationary random components using no MA filter.
     *
     * @param Xt a time series
     * @param period the period of the time series; if aperiodic, use 0
     */
    public MADecomposition(TimeSeries Xt, int period) {
        this(Xt, period, period);
    }

    /**
     * Decompose a time series into the trend, seasonal and the stationary random components using the default filter.
     *
     * @param Xt a time series
     * @param MAOrder the length of the MA filter (automatically increased by 1 for even {@code MAOrder})
     * @param period the period of the time series; if aperiodic, use 0
     *
     * @see "P. J. Brockwell and R. A. Davis, "Eq. 1.4.16. Chapter 1.4. Elimination of both Trend and Seasonality," in <i>Time Series: Theory and Methods</i>, 2nd ed. Springer, 2006."
     */
    public MADecomposition(TimeSeries Xt, int MAOrder, int period) {
        this(Xt, defaultFilter(MAOrder), period);
    }

    private static double[] defaultFilter(int MAOrder) {
        assertArgument(MAOrder > 0, "MAOrder must be > 0");

        double[] MAFilter;

        if (MAOrder % 2 == 0) {//even order; Eq. 1.4.16.
            MAFilter = new DenseVector(MAOrder + 1, 1.0 / MAOrder).toArray();
            MAFilter[0] = 0.5 / MAOrder;
            MAFilter[MAFilter.length - 1] = 0.5 / MAOrder;
        } else {//odd order; Eq. 1.4.6.
            MAFilter = new DenseVector(MAOrder, 1.0 / MAOrder).toArray();
        }

        return MAFilter;
    }
}
