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

import com.numericalmethod.suanshu.stats.random.univariate.RandomNumberGenerator;
import com.numericalmethod.suanshu.stats.random.multivariate.IID;
import com.numericalmethod.suanshu.stats.timeseries.univariate.realtime.SimpleTimeSeries;

/**
 * The additive model of a time series is an additive composite of the trend, seasonality and irregular random components.
 * 
 * <blockquote><code><pre>
 * Y[t] = T[t] + S[t] + e[t]
 * </pre></code></blockquote>
 *
 * @author Haksun Li
 */
public class AdditiveModel extends SimpleTimeSeries {

    /**
     * Construct a univariate time series by adding up the components.
     * 
     * @param trend the trend component
     * @param seasonality the seasonality component
     * @param randoms the irregular random component
     */
    public AdditiveModel(double[] trend, double[] seasonality, double[] randoms) {
        super(generate(trend, seasonality, randoms));
    }

    /**
     * Construct a univariate time series by adding up the components.
     * The irregular random component is generated using a custom random number generator.
     * 
     * @param trend the trend component
     * @param seasonality the seasonality component
     * @param rng a random number generator
     */
    public AdditiveModel(double[] trend, double[] seasonality, RandomNumberGenerator rng) {
        this(trend, seasonality, new IID(rng, trend.length).nextVector());
    }

    /**
     * Generate a univariate time series by adding up the components.
     * 
     * @param trend the trend component
     * @param seasonality the seasonality component
     * @param randoms the irregular random component
     * @return a univariate time series
     */
    private static double[] generate(double[] trend, double[] seasonality, double[] randoms) {
        double[] Xt = new double[trend.length];
        for (int i = 0; i < trend.length; ++i) {
            Xt[i] = (trend != null ? trend[i] : 0)
                    + (seasonality != null ? seasonality[i % seasonality.length] : 0)
                    + (randoms != null ? randoms[i] : 0);
        }

        return Xt;
    }
}
