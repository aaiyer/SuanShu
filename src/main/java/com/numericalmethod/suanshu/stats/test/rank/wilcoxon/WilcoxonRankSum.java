package com.numericalmethod.suanshu.stats.test.rank.wilcoxon;

import com.numericalmethod.suanshu.stats.descriptive.rank.Rank;
import com.numericalmethod.suanshu.stats.distribution.univariate.NormalDistribution;
import com.numericalmethod.suanshu.stats.distribution.univariate.ProbabilityDistribution;
import com.numericalmethod.suanshu.stats.test.HypothesisTest;
import static com.numericalmethod.suanshu.number.DoubleUtils.concat;
import java.util.Arrays;
import static java.lang.Math.*;

/**
 * The Wilcoxon rank sum test tests for the equality of means of two population, or whether the means differs by an offset.
 * That is, it tests the null hypothesis that the distribution <i>x - y</i> is symmetric about {@code mu}.
 *
 * <p>
 * This is also called the Mann–Whitney–Wilcoxon, Mann-Whitney-U, or Wilcoxon–Mann–Whitney test.
 *
 * <p>
 * The R equivalent function is {@code wilcox.test}.
 *
 * @author Chun Yip Yau
 *
 * @see <a href="http://en.wikipedia.org/wiki/Mann%E2%80%93Whitney_U">Wikipedia: Mann–Whitney U</a>
 */
public class WilcoxonRankSum extends HypothesisTest {

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
        return "the means are equal (by offset mu)";
    }

    @Override
    public String getAlternativeHypothesis() {
        return "the means are different (not by mu)";
    }

    /**
     * Perform the Wilcoxon Rank Sum test to test for the equality of means of two population, or whether the means differs by an offset.
     *
     * @param sample1     sample 1
     * @param sample2     sample 2
     * @param mu          the hypothetical location that the <i>sample1 - sample2</i> is symmetric about
     * @param isExact     {@code true} if to use the exact distribution; otherwise, normal approximation is used
     * @param isCorrected {@code true} if to use the continuity correction for the normal distribution; otherwise, standard normal approximation is used
     */
    public WilcoxonRankSum(double[] sample1, double[] sample2, double mu, boolean isExact, boolean isCorrected) {
        super(new double[][]{sample1, sample2});

        final int N1 = sample1.length;
        final int N2 = sample2.length;

        double[] shifted = Arrays.copyOf(sample2, sample2.length);
        for (int i = 0; i < shifted.length; ++i) {
            shifted[i] += mu;
        }
        double[] merged = concat(sample1, shifted);

        double[] ranks = new Rank(merged).ranks();

        int tieAdjustment = 0;       // Find the tie adjustment: sum(V^3-V), where V contains the number of ties for each value
        int[] V = new int[N1 + N2];
        int counter = 1;
        double[] copyMerged = merged;
        Arrays.sort(copyMerged);
        for (int i = 1; i < N1 + N2; ++i) {
            V[counter - 1] = (copyMerged[i - 1] == copyMerged[i]) ? V[counter - 1] + 1 : V[counter - 1];
            counter = (copyMerged[i - 1] != copyMerged[i]) ? counter + 1 : counter;
        }
        for (int j = 0; j < counter; ++j) {
            V[j] = V[j] + 1;
            tieAdjustment = tieAdjustment + V[j] * V[j] * V[j] - V[j]; // Why can't use pow(V,3)???
        }

        double R1 = 0;
        for (int i = 0; i < N1; ++i) {
            R1 = R1 + ranks[i];
        }
        testStatistics = R1 - N1 * (N1 + 1) / 2;

        //compute the p-values
        if (isExact) {
            com.numericalmethod.suanshu.stats.test.rank.wilcoxon.WilcoxonRankSumDistribution dist =
                    new com.numericalmethod.suanshu.stats.test.rank.wilcoxon.WilcoxonRankSumDistribution(N1, N2);//TODO: use the ProbabilityDistribution format
            pValue1SidedGreater = dist.pValue1SidedGreater(testStatistics);
            pValue1SidedLess = dist.cdf(testStatistics);
            pValue = dist.pValue(testStatistics);
        } else {
            double mean = N1 * N2 / 2;
            double sigma = sqrt(N1 * N2 * (N + 1.0 - (double) tieAdjustment / ((N1 + N2) * (N1 + N2 - 1))) / 12);
            ProbabilityDistribution normal = new NormalDistribution(0, 1);

            if (isCorrected) {
                double correction = testStatistics > mean ? 0.5 : -0.5;
                double standarizedTestStatistics = (testStatistics - mean - correction) / sigma;
                pValue1SidedGreater = oneSidedPvalue(normal, (testStatistics - mean - 0.5) / sigma);
                pValue1SidedLess = normal.cdf((testStatistics - mean + 0.5) / sigma);
                double pValue2Sided = normal.cdf(standarizedTestStatistics);
                pValue = 2 * min(pValue2Sided, 1 - pValue2Sided);
            } else {
                double standarizedTestStatistics = (testStatistics - mean) / sigma;
                pValue1SidedGreater = oneSidedPvalue(normal, standarizedTestStatistics);
                pValue1SidedLess = 1 - pValue1SidedGreater;
                pValue = 2 * min(pValue1SidedLess, pValue1SidedGreater);
            }
        }
    }

    /**
     * Perform the Wilcoxon Rank Sum test to test for the equality of means of two population, or whether the means differs by an offset.
     *
     * @param sample1 sample 1
     * @param sample2 sample 2
     * @param mu      the hypothetical location that the <i>sample1 - sample2</i> is symmetric about
     * @param isExact {@code true} if to use the exact distribution; otherwise, normal approximation is used
     */
    public WilcoxonRankSum(double[] sample1, double[] sample2, double mu, boolean isExact) {
        this(sample1, sample2, mu, isExact, true);
    }

    /**
     * Perform the Wilcoxon Rank Sum test to test for the equality of means of two population, or whether the means differs by an offset.
     * <p/>
     * The exact distribution is used for sample size < 50.
     *
     *
     * @param sample1 sample 1
     * @param sample2 sample 2
     * @param mu      the hypotheical location that the <i>sample1 - sample2</i> is symmetric about
     */
    public WilcoxonRankSum(double[] sample1, double[] sample2, double mu) {
        this(sample1, sample2, mu, sample1.length + sample2.length < 50, false);
    }

    /**
     * Perform the Wilcoxon Rank Sum test to test for the equality of means of two population.
     *
     * @param sample1 sample 1
     * @param sample2 sample 2
     */
    public WilcoxonRankSum(double[] sample1, double[] sample2) {
        this(sample1, sample2, 0);
    }
}
