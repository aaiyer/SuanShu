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
package com.numericalmethod.suanshu.stats.test;

import com.numericalmethod.suanshu.stats.distribution.univariate.EmpiricalDistribution;
import com.numericalmethod.suanshu.stats.distribution.univariate.ProbabilityDistribution;

/**
 * A statistical hypothesis test is a method of making decisions using experimental data.
 * A result is called statistically significant if it is unlikely to have occurred by chance.
 *
 * <p>
 * Specifically, given a null hypothesis, we compute the p-value of a test statistics.
 * The p-value tells the probability of observing the observations.
 * We often accept the alternative hypothesis, (i.e. rejects a null hypothesis) if
 * the p-value is less than 0.05 or 0.01, corresponding respectively to a 5% or 1% chance of rejecting the null hypothesis when it is true.
 *
 * @author Haksun Li
 *
 * @see
 * <ul>
 * <li><a href="http://en.wikipedia.org/wiki/Statistical_hypothesis_testing">Wikipedia: Statistical hypothesis testing</a>
 * <li><a href="http://en.wikipedia.org/wiki/Test_statistic">Wikipedia: Test statistic</a>
 * <li><a href="http://en.wikipedia.org/wiki/P-value">Wikipedia: P-value</a>
 * <li><a href="http://en.wikipedia.org/wiki/Null_hypothesis">Wikipedia: Null hypothesis</a>
 * <li><a href="http://en.wikipedia.org/wiki/Alternative_hypothesis">Wikipedia: Alternative hypothesis</a>
 * </ul>
 */
public abstract class HypothesisTest {

    /**
     * number of groups of observations
     */
    public final int k;
    /**
     * total number of observations
     */
    public final int N;
    /**
     * the test statistics
     */
    protected volatile double testStatistics;
    /**
     * p-value for the test statistics
     */
    protected volatile double pValue;

    /**
     * Construct an instance of <tt>HypothesisTest</tt> from samples.
     *
     * @param samples an array of samples
     */
    protected HypothesisTest(double[]... samples) {
//        Util.assertOrThrow(samples.length >= 2 ? null
//                : new IllegalArgumentException("there must be at least 2 groups"));

        k = samples.length;

        int tmp = 0;
        for (int i = 0; i < k; ++i) {
            tmp += samples[i] != null ? samples[i].length : 0;
        }
        N = tmp;
    }

    /**
     * Get the test statistics.
     *
     * @see <a href="http://en.wikipedia.org/wiki/Test_statistic">Wikipedia: Test statistic</a>
     *
     * @return the test statistics
     */
    public double statistics() {
        return testStatistics;
    }

    /**
     * Get the rejection region.
     *
     * @return the rejection region
     */
    /* public double rejectionRegion() {
     * return rejectionRegion;
     * } */
    /**
     * Get the p-value.
     *
     * @see <a href="http://en.wikipedia.org/wiki/P-value">Wikipedia: P-value</a>
     *
     * @return the p-value
     */
    public double pValue() {
        return pValue;
    }

    /**
     * Use p-value to check whether the null hypothesis can be rejected for given significance level (size) alpha.
     *
     * @param alpha significance level (size) of test
     * @return {@code true} if the hypothesis is rejected due to
     * <code>p-value < alpha</code>
     */
    public boolean isNullRejected(double alpha) {
        return pValue() < alpha;
    }

    /**
     * Get a description of the null hypothesis.
     *
     * @see <a href="http://en.wikipedia.org/wiki/Null_hypothesis">Wikipedia: Null hypothesis</a>
     *
     * @return the null hypothesis description
     */
    public abstract String getNullHypothesis();

    /**
     * Get a description of the alternative hypothesis.
     *
     * @see <a href="http://en.wikipedia.org/wiki/Alternative_hypothesis">Wikipedia: Alternative hypothesis</a>
     *
     * @return the alternative hypothesis description
     */
    public abstract String getAlternativeHypothesis();

    /**
     * A one-sided P-value is the probability of observing a test statistic <em>at least</em> as extreme as the one observed;
     * hence, the one-sided P-value is simply given by the complementary cumulative distribution function (survival function) for <em>continuous</em> distribution.
     *
     * <p>
     * For discrete distribution, we need to include the probability observing the critical value as well.
     *
     * @param F a univariate distribution
     * @param x the critical value
     * @return the p-value for the critical value
     */
    public static double oneSidedPvalue(ProbabilityDistribution F, double x) {
        if (F instanceof EmpiricalDistribution) {
            return 1.0 - F.cdf(x) + F.density(x);
        }

        return 1.0 - F.cdf(x);
    }
}
