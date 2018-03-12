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
package com.numericalmethod.suanshu.stats.test.variance;

import com.numericalmethod.suanshu.stats.descriptive.moment.Variance;
import com.numericalmethod.suanshu.stats.distribution.univariate.ProbabilityDistribution;
import com.numericalmethod.suanshu.stats.test.HypothesisTest;
import static com.numericalmethod.suanshu.misc.SuanShuUtils.assertArgument;
import static java.lang.Math.*;

/**
 * The FDistribution-test tests whether two <em>normal</em> populations have the same variance.
 * This test is sensitive to the assumption that the variables are normally distributed.
 *
 * <p>
 * The R equivalent function is {@code var.test}.
 *
 * @author Chun Yip Yau
 *
 * @see <a href="http://en.wikipedia.org/wiki/FDistribution-test_of_equality_of_variances">Wikipedia: FDistribution-test of equality of variances</a>
 */
public class F extends HypothesisTest {

    /**
     * the degree of freedoms
     */
    public final double df1;
    /**
     * the degree of freedoms
     */
    public final double df2;
    /**
     * the estimate of the ratio of two variances
     */
    public final double estimate;
    /**
     * left, one-sided p-value
     */
    public final double pValue1SidedLess;
    /**
     * right, one-sided p-value
     */
    public final double pValue1SidedGreater;
    /**
     * the associated FDistribution distribution
     */
    public final ProbabilityDistribution F;

    @Override
    public String getNullHypothesis() {
        return "the two normal populations have the same variance";
    }

    @Override
    public String getAlternativeHypothesis() {
        return "the two samples have different variances";
    }

    /**
     * Perform the FDistribution test to test for equal variance of two normal populations.
     *
     * @param sample1 sample 1
     * @param sample2 sample 2
     */
    public F(double[] sample1, double[] sample2) {
        this(sample1, sample2, 1.0);
    }

    /**
     * Perform the FDistribution test to test for equal variance of two normal populations.
     * 
     * @param sample1 sample 1
     * @param sample2 sample 2
     * @param ratio the hypothesized ratio of the population variances of samples 1 and 2
     */
    public F(double[] sample1, double[] sample2, double ratio) {
        super(new double[][]{sample1, sample2});

        double s1 = new Variance(sample1).value();
        double s2 = new Variance(sample2).value();

        estimate = s1 / s2;
        testStatistics = estimate / ratio;

        //compute p-value
        df1 = sample1.length - 1;
        df2 = sample2.length - 1;
        F = new com.numericalmethod.suanshu.stats.distribution.univariate.FDistribution(df1, df2);

        /*
         * This is a two-sided test.
         * Large values and small values of test statistics are to be rejected.
         * Taking the minimum of 1 - FDistribution.cdf(statistics), FDistribution.cdf(statistics) gives the side which is more significant.
         * Multiplying by two gives a two sided p value.
         */
        pValue1SidedGreater = oneSidedPvalue(F, testStatistics);
        pValue1SidedLess = 1.0 - pValue1SidedGreater;
        pValue = 2 * min(pValue1SidedGreater, pValue1SidedLess);
    }

    /**
     * Compute the confidence interval.
     *
     * @param confidence the confidence level, e.g., for a 2-sided 95% confidence interval, we use 0.975 because 1 - 0.95 = 2 * (1 - 0.025)
     * @return the left and right interval values
     */
    public double[] confidenceInterval(double confidence) {
        assertArgument(0 < confidence && confidence < 1, "0 < confidence level < 1");

        double[] CI = new double[2];
        CI[0] = estimate / F.quantile(confidence);
        CI[1] = estimate / F.quantile(1 - confidence);

        return CI;
    }

    /**
     * Compute the one sided right confidence interval, [a, ∞)
     *
     * @param confidence the confidence level, e.g., 0.95 for 95% confidence interval
     * @return the left interval value
     */
    public double rightConfidenceInterval(double confidence) {
        assertArgument(0 < confidence && confidence < 1, "0 < confidence level < 1");

        return estimate / F.quantile(confidence);
    }

    /**
     * Compute the one sided left confidence interval, [0, a]
     *
     * @param confidence the confidence level, e.g., 0.95 for 95% confidence interval
     * @return the right interval value
     */
    public double leftConfidenceInterval(double confidence) {
        assertArgument(0 < confidence && confidence < 1, "0 < confidence level < 1");

        return estimate / F.quantile(1 - confidence);
    }
}
