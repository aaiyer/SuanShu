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
package com.numericalmethod.suanshu.stats.test.rank.wilcoxon;

import com.numericalmethod.suanshu.number.doublearray.DoubleArrayMath;
import static com.numericalmethod.suanshu.misc.SuanShuUtils.assertArgument;
import com.numericalmethod.suanshu.number.Counter;
import com.numericalmethod.suanshu.stats.descriptive.rank.Rank;
import com.numericalmethod.suanshu.stats.distribution.univariate.NormalDistribution;
import com.numericalmethod.suanshu.stats.distribution.univariate.ProbabilityDistribution;
import com.numericalmethod.suanshu.stats.test.HypothesisTest;
import static java.lang.Math.*;

/**
 * The Wilcoxon signed rank test tests, for the one-sample case, the median of the distribution against a hypothetical median, and
 * for the two-sample case, the equality of median of groups.
 *
 * <p>
 * Unlike the Student's t-test, the Wilcoxon signed rank test does not assume any distribution of the population.
 *
 * <p>
 * The R equivalent function is {@code wilcox.test}.
 *
 * @author Chun Yip Yau
 *
 * @see
 * <ul>
 * <li>"Gibbons and Chakraborti, Nonparameric statistical inference (2003), Chapter 5.7, p.197."
 * <li><a href="http://en.wikipedia.org/wiki/Wilcoxon_signed-rank_test">Wikipedia: Wilcoxon signed-rank test</a>
 * </ul>
 */
public class WilcoxonSignedRank extends HypothesisTest {//TODO: exact distribution, 1 sided, handle ties

    /**
     * left, one-sided p-value
     */
    public final double pValue1SidedLess;
    /**
     * right, one-sided p-value
     */
    public final double pValue1SidedGreater;

    @Override
    public String getNullHypothesis() {
        return "the median are equal (by offset mu)";
    }

    @Override
    public String getAlternativeHypothesis() {
        return "the medians are different (not by mu)";
    }

    /**
     * Perform the Wilcoxon Signed Rank test to test for the equality of medians.
     *
     * @param sample1 sample 1
     * @param sample2 sample 2
     * @param mu the hypothetical median that the distribution is symmetric about
     * @param isExact {@code true} if to use the exact distribution; otherwise, normal approximation is used
     */
    public WilcoxonSignedRank(double[] sample1, double[] sample2, double mu, boolean isExact) {
        super(new double[][]{sample1, sample2});

        assertArgument(sample2 == null || sample2.length == sample1.length,
                "the two samples must have equal number of observations");

        assertArgument(sample1.length > 1,
                "each sample must have more than 1 observation");

        final int nZ = sample1.length;

        double[] Z = new double[nZ];//Z = X - Y
        for (int i = 0; i < nZ; ++i) {
            Z[i] = sample1[i] - (sample2 != null ? sample2[i] : 0) - mu;
        }

        double[] absZ = DoubleArrayMath.abs(Z);

        double[] ranks = new Rank(absZ).ranks();

        //compute the test statistics
        double W = 0;
        for (int i = 0; i < nZ; ++i) {
            W += ranks[i] * (Z[i] > 0 ? 1 : 0);//X[i] > Y[i]
        }
        testStatistics = W;

        //compute the p-values
        if (isExact) {
            com.numericalmethod.suanshu.stats.test.rank.wilcoxon.WilcoxonSignedRankDistribution dist =
                    new com.numericalmethod.suanshu.stats.test.rank.wilcoxon.WilcoxonSignedRankDistribution(nZ);
            pValue1SidedLess = dist.cdf(testStatistics);
            pValue1SidedGreater = dist.pValue1SidedGreater(testStatistics);
            pValue = dist.pValue(testStatistics);
        } else {
            /*
             * Compute the tie adjustment: sum(v^3-v), where v contains the number of ties for each value.
             * @see p.28 of "Nonparameteric statistical methods", Hollander and Wolfe, Wiley, 1973.
             */
            Counter counter = new Counter();
            counter.add(Z);

            int tieAdjustment = 0;
            for (Double d : counter.keySet()) {
                int v = counter.count(d);
                tieAdjustment += v * (v - 1) * (v + 1);
            }

            double mean = nZ * (nZ + 1.0) / 4;
            double sigma = sqrt((nZ * (nZ + 1) * (2 * nZ + 1.0) - 0.5 * tieAdjustment) / 24);
            double standarizedTestStatistics = (testStatistics - mean) / sigma;

            ProbabilityDistribution normal = new NormalDistribution(0, 1);
            pValue1SidedGreater = oneSidedPvalue(normal, standarizedTestStatistics);
            pValue1SidedLess = 1.0 - pValue1SidedGreater;
            pValue = 2 * min(pValue1SidedGreater, pValue1SidedLess);
        }
    }

    /**
     * Perform the Wilcoxon Signed Rank test to test for the equality of medians.
     *
     * @param sample1 sample 1
     * @param sample2 sample 2
     */
    public WilcoxonSignedRank(double[] sample1, double[] sample2) {
        this(sample1, sample2, 0, sample1.length + sample2.length < 50);
    }

    /**
     * Perform the Wilcoxon Signed Rank test to test for the equality of medians.
     *
     * @param sample a sample
     * @param mu the hypothetical median that the distribution is symmetric about
     */
    public WilcoxonSignedRank(double[] sample, int mu) {
        this(sample, null, mu, sample.length < 50);
    }

    /**
     * Perform the Wilcoxon Signed Rank test to test for the equality of medians.
     *
     * @param sample a sample
     */
    public WilcoxonSignedRank(double[] sample) {
        this(sample, 0);
    }
}
