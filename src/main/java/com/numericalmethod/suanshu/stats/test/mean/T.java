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
package com.numericalmethod.suanshu.stats.test.mean;

import com.numericalmethod.suanshu.stats.descriptive.moment.Mean;
import com.numericalmethod.suanshu.stats.descriptive.moment.Variance;
import com.numericalmethod.suanshu.stats.distribution.univariate.ProbabilityDistribution;
import com.numericalmethod.suanshu.stats.test.HypothesisTest;
import static com.numericalmethod.suanshu.misc.SuanShuUtils.assertArgument;
import static java.lang.Math.*;

/**
 * Student's TDistribution-test tests for the equality of means, for the one-sample case, against a hypothetical mean, and for two-sample case, of two populations.
 *
 * <p>
 * Welch's t test is an adaptation of Student's t-test intended for use with two samples having possibly <em>unequal</em> variances.
 *
 * <p>
 * The R equivalent function is {@code t.test}.
 *
 * @author Chun Yip Yau
 *
 * @see
 * <ul>
 * <li>Richard D. De Veaux, Paul F. Velleman, David E. Bock. "Stats: Data and Models", Chapter 24.
 * <li><a href="http://en.wikipedia.org/wiki/Student's_t-test">Student's t-test</a>
 * <li><a href="http://en.wikipedia.org/wiki/Welch%27s_t_test">Welch's t test</a>
 * <li><a href="http://en.wikipedia.org/wiki/Welch-Satterthwaite_equation">Welch–Satterthwaite equation</a>
 * </ul>
 */
public class T extends HypothesisTest {

    /**
     * degree of freedom
     */
    public final double df;
    /**
     * mean for sample 1
     */
    public final double mean1;
    /**
     * variance for sample 1
     */
    public final double var1;
    /**
     * mean for sample 2
     */
    public final double mean2;
    /**
     * variance for sample 2
     */
    public final double var2;
    /**
     * left, one-sided p-value
     */
    public final double pValue1SidedLess;
    /**
     * right, one-sided p-value
     */
    public final double pValue1SidedGreater;
    /**
     * the associated TDistribution distribution
     */
    public final ProbabilityDistribution T;
    private int length1, length2;

    @Override
    public String getNullHypothesis() {
        return "the means are equal";
    }

    @Override
    public String getAlternativeHypothesis() {
        return "the means are different";
    }

    /**
     * Construct a one-sample location test of whether the mean of a normally distributed population has a value specified in a null hypothesis.
     *
     * @param sample sample
     * @param mu     the hypothetical mean in the null hypothesis
     */
    public T(double[] sample, double mu) {
        this(sample, new double[]{0}, true, mu);
    }

    /**
     * Construct Welch's t test, an adaptation of Student's t-test, for the use with two samples having possibly <em>unequal</em> variances.
     *
     * @param sample1 sample 1
     * @param sample2 sample 2
     */
    public T(double[] sample1, double[] sample2) {
        this(sample1, sample2, false, 0);
    }

    /**
     * Construct Welch's t test, an adaptation of Student's t-test, for the use with two samples having possibly <em>unequal</em> variances.
     *
     * @param sample1 sample 1
     * @param sample2 sample 2
     * @param mu      the hypothetical mean-difference in the null hypothesis
     */
    public T(double[] sample1, double[] sample2, double mu) {
        this(sample1, sample2, false, mu);
    }

    /**
     * Construct a two sample location test of the null hypothesis that the means of two normally distributed populations are equal.
     *
     * @param sample1    sample 1
     * @param sample2    sample 2
     * @param isEqualVar {@code true} if we assume the variances of the two samples are equal; false otherwise
     * @param mu         the hypothetical mean-difference in the null hypothesis. Default value is 0.
     */
    public T(double[] sample1, double[] sample2, boolean isEqualVar, double mu) {//TODO:the true distribution of the test statistic actually depends (slightly) on the two unknown variances: see Behrens–Fisher problem
        length1 = sample1.length;
        length2 = sample2.length;

        mean1 = new Mean(sample1).value();
        var1 = new Variance(sample1).value();
        mean2 = new Mean(sample2).value();
        var2 = new Variance(sample2).value();

        if (isEqualVar) {//the two samples are assumed to have equal variance
            double s12 = ((length1 - 1) * var1 + (length2 - 1) * var2) / (length1 + length2 - 2);
            s12 = sqrt(s12);//pooled variance
            testStatistics = (mean1 - mean2 - mu) / s12 / sqrt(1.0 / length1 + (length2 == 1 ? 0 : 1.0 / length2));//account for the one-sample case
            this.df = length1 + length2 - 2;
        } else {//the equal variance assumption is dropped
            testStatistics = (mean1 - mean2 - mu) / sqrt(var1 / length1 + var2 / length2);
            double df = var1 / length1 + var2 / length2;
            df *= df;//square
            df /= pow(var1 / length1, 2) / (length1 - 1) + pow(var2 / length2, 2) / (length2 - 1);//the Welch-Satterthwaite equation

            this.df = df;
        }

        //compute the p-values
        T = new com.numericalmethod.suanshu.stats.distribution.univariate.TDistribution(this.df);
        pValue1SidedGreater = oneSidedPvalue(T, testStatistics);
        pValue1SidedLess = T.cdf(testStatistics);
        pValue = 2 * min(pValue1SidedGreater, pValue1SidedLess);//the two-sided p-value
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
        CI[0] = (mean1 - mean2) - T.quantile(0.975) * sqrt(var1 / length1 + var2 / length2);//TODO: shall we ignore 'mean2' when length2 <= 1, as in R?
        CI[1] = (mean1 - mean2) + T.quantile(0.975) * sqrt(var1 / length1 + var2 / length2);

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

        return (mean1 - mean2) - T.quantile(0.95) * sqrt(var1 / length1 + var2 / length2);
    }

    /**
     * Compute the one sided left confidence interval, [0, a]
     *
     * @param confidence the confidence level, e.g., 0.95 for 95% confidence interval
     * @return the right interval value
     */
    public double leftConfidenceInterval(double confidence) {
        assertArgument(0 < confidence && confidence < 1, "0 < confidence level < 1");

        return (mean1 - mean2) + T.quantile(0.95) * sqrt(var1 / length1 + var2 / length2);
    }
}
