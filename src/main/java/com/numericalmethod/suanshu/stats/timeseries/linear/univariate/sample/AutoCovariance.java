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
package com.numericalmethod.suanshu.stats.timeseries.linear.univariate.sample;

import com.numericalmethod.suanshu.misc.SuanShuUtils;
import com.numericalmethod.suanshu.stats.descriptive.moment.Mean;
import com.numericalmethod.suanshu.stats.timeseries.linear.univariate.AutoCovarianceFunction;
import com.numericalmethod.suanshu.stats.timeseries.univariate.realtime.TimeSeries;

/**
 * This computes the sample Auto-Covariance Function (ACVF) for a univariate data set.
 *
 * @author Haksun Li
 *
 * @see "William W.S. Wei, "Section 2.5.2. Sample Auto-covariance function" in <i>Time Series Analysis : Univariate and Multivariate Methods (2nd Edition)</i>, Addison Wesley; 2 edition (July 17, 2005)"
 */
public class AutoCovariance extends AutoCovarianceFunction {

    public enum Type {

        /**
         * default: the denominator is the time series length
         */
        TYPE_I,
        /**
         * the denominator is the time series length minus the lag
         */
        TYPE_II
    };
    private final TimeSeries xt;
    private final Type type;
    /**
     * mean of the time series
     */
    private final double mu;

    public AutoCovariance(TimeSeries xt, Type type) {
        this.xt = xt;
        this.type = type;

        Mean mean = new Mean(xt.toArray());
        this.mu = mean.value();
    }

    public AutoCovariance(TimeSeries xt) {
        this(xt, Type.TYPE_I);
    }

    /**
     * Compute the auto-covariance for lag {@code k}.
     *
     * @param k lag
     * @return γ(k)
     */
    public double evaluate(int k) {//eq. 2.5.8
        SuanShuUtils.assertArgument(k < xt.size(), "lag must be < lenght of the time series");

        final int n = xt.size();

        double sum = 0;
        for (int t = 1; t <= n - k; ++t) {
            sum += (xt.get(t) - mu) * (xt.get(t + k) - mu);
        }

        double gamma = sum;
        if (type == Type.TYPE_II) {
            gamma /= n - k;
        } else {
            gamma /= n;
        }

        return gamma;
    }

    @Override
    public double evaluate(double x1, double x2) {
        return evaluate(Math.round((float) Math.abs(x1 - x2)));
    }
}
