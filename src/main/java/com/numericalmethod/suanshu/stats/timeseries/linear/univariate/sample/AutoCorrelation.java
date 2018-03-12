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

import com.numericalmethod.suanshu.stats.timeseries.linear.univariate.AutoCorrelationFunction;
import com.numericalmethod.suanshu.stats.timeseries.univariate.realtime.TimeSeries;

/**
 * This computes the sample Auto-Correlation Function (ACF) for a univariate data set.
 *
 * @author Haksun Li
 *
 * @see "William W.S. Wei, "Section 2.5.3. Sample Auto-correlation function" in <i>Time Series Analysis : Univariate and Multivariate Methods (2nd Edition)</i>, Addison Wesley; 2 edition (July 17, 2005)"
 */
public class AutoCorrelation extends AutoCorrelationFunction {

    private final AutoCovariance acvf;
    private final double acvf0;

    public AutoCorrelation(TimeSeries xt, AutoCovariance.Type type) {
        this.acvf = new AutoCovariance(xt, type);
        this.acvf0 = acvf.evaluate(0);
    }

    public AutoCorrelation(TimeSeries xt) {
        this(xt, AutoCovariance.Type.TYPE_I);
    }

    /**
     * Compute the auto-correlation for lag {@code k}.
     *
     * @param k lag
     * @return ρ(k)
     */
    public double evaluate(int k) {//eq. 2.5.18
        return acvf.evaluate(k) / acvf0;
    }

    @Override
    public double evaluate(double x1, double x2) {
        return evaluate(Math.round((float) Math.abs(x1 - x2)));
    }
}
