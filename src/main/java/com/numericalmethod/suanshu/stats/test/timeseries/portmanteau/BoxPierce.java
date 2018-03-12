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
package com.numericalmethod.suanshu.stats.test.timeseries.portmanteau;

import com.numericalmethod.suanshu.stats.distribution.univariate.ChiSquareDistribution;
import com.numericalmethod.suanshu.stats.distribution.univariate.ProbabilityDistribution;
import com.numericalmethod.suanshu.stats.test.HypothesisTest;
import com.numericalmethod.suanshu.stats.timeseries.univariate.realtime.SimpleTimeSeries;
import com.numericalmethod.suanshu.stats.timeseries.linear.univariate.sample.AutoCorrelation;
import com.numericalmethod.suanshu.stats.timeseries.linear.univariate.sample.AutoCovariance;

/**
 * The Box–Pierce test (named for George E. P. Box and David A. Pierce) is a portmanteau test for autocorrelated errors.
 * A portmanteau test tests whether any of a group of autocorrelations of a time series are different from zero.
 *
 * <p>
 * The Box–Pierce statistic is computed as the weighted sum of squares of a sequence of autocorrelations.
 *
 * <p>
 * The R equivalent function is {@code Box.test}.
 *
 * @author Haksun Li
 *
 * @see
 * <ul>
 * <li><a href="http://en.wikipedia.org/wiki/Box%E2%80%93Pierce_test">Wikipedia: Box–Pierce test</a>
 * <li><a href="http://en.wikipedia.org/wiki/Portmanteau_test">Wikipedia: Portmanteau test</a>
 * </ul>
 */
public class BoxPierce extends HypothesisTest {

    private final int lag = 0;

    @Override
    public String getNullHypothesis() {
        return String.format("none of the autocorrelation coefficients up to lag %d are different from zero; the data is random", lag);
    }

    @Override
    public String getAlternativeHypothesis() {
        return "at least one value of autocorrelation coefficient is statistically different from zero at the specified significance level; the data is not random";
    }

    /**
     * Compute the Box–Pierce test statistic for examining the null hypothesis of independence in a given time series.
     *
     * @param xt a univariate time series
     * @param lag the statistic will be based on {@code lag} autocorrelation coefficients
     * @param fitdf number of degrees of freedom to be subtracted if x is a series of residuals
     */
    public BoxPierce(double[] xt, int lag, int fitdf) {
        AutoCorrelation cor = new AutoCorrelation(new SimpleTimeSeries(xt), AutoCovariance.Type.TYPE_I);

        double[] obs = new double[lag];
        for (int i = 0; i < lag; ++i) {
            obs[i] = cor.evaluate(i + 1);
        }

        testStatistics = Q(obs, xt.length);

        int df = lag - fitdf;//degree of freedom
        ProbabilityDistribution X2 = new ChiSquareDistribution(df);
        pValue = oneSidedPvalue(X2, testStatistics);
    }

    /**
     * Compute the Q statistics.
     *
     * @return the Q statistics
     */
    double Q(double[] obs, int N) {
        double sum2 = 0;
        for (double d : obs) {
            sum2 += d * d;
        }

        return N * sum2;
    }
}
