package com.numericalmethod.suanshu.stats.test.rank;

import com.numericalmethod.suanshu.stats.test.rank.wilcoxon.WilcoxonRankSum;
import com.numericalmethod.suanshu.misc.R;
import com.numericalmethod.suanshu.stats.test.HypothesisTest;
import java.util.Arrays;

/**
 * Siegel–Tukey tests for differences in scale (variability) between two groups.
 *
 * <p>
 * The test is used to determine if one of two groups of data tends to have more widely dispersed values than the other.
 * In other words, the test determines whether one of the two groups tends to move, sometimes to the right, sometimes to the left, but away from the center (of the ordinal scale).
 *
 * @author Chun Yip Yau
 *
 * @see <a href="http://en.wikipedia.org/wiki/Siegel%E2%80%93Tukey_test">Wikipedia: iegel–Tukey test</a>
 */
public class SiegelTukey extends HypothesisTest {

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
        return "the two samples have the same variability and median";
    }

    @Override
    public String getAlternativeHypothesis() {
        return "the two samples have different variabilities";
    }

    /**
     * Perform the Siegel-Tukey test to test for differences in scale (variability) between two groups.
     * 
     * @param sample1 sample 1
     * @param sample2 sample 2
     * @param mu the hypothetical mean difference
     * @param isExact indicate whether the exact Wilcoxon Rank Sum distribution is used
     */
    public SiegelTukey(double[] sample1, double[] sample2, double mu, boolean isExact) {
        super(new double[][]{sample1, sample2});

        final int N1 = sample1.length;
        final int N2 = sample2.length;

        double[] merged = Arrays.copyOf(sample1, N1 + N2);
        for (int i = 0; i < N2; ++i) {
            merged[N1 + i] = sample2[i];//the order must remain unchanged
        }

        int[] orders = R.order(merged);//sorted = merged[orders - 1]

        double[] ranks = new double[N];
        for (int i = 0; i < N; ++i) {
            int j = Math.round(i / 2.0) % 2 == 0 ? i / 2 : N - 1 - i / 2;//the index to assign rank to
            ranks[orders[j] - 1] = i + 1;//rank counts from 1
        }

        //adjust for ties using mid-rank
        int begin = 0, end = 0;
        double sum = ranks[orders[0] - 1];
        for (int i = 1; i <= N; ++i) {
            if ((i == N) || (merged[orders[i] - 1] != merged[orders[i - 1] - 1])) {
                if (end > begin) {//mid-rank rule
                    double midrank = sum / (end - begin + 1);
                    for (int j = begin; j <= end; ++j) {
                        ranks[orders[j] - 1] = midrank;
                    }
                }

                begin = i;
                end = i;
                sum = i < N ? ranks[orders[i] - 1] : 0;
            } else {//ties
                end = i;
                sum += ranks[orders[i] - 1];
            }
        }

        double[] r1 = Arrays.copyOfRange(ranks, 0, N1);
        double[] r2 = Arrays.copyOfRange(ranks, N1, ranks.length);

        WilcoxonRankSum test = new WilcoxonRankSum(r1, r2, mu, isExact);
        testStatistics = test.statistics();
        pValue = test.pValue();
        pValue1SidedGreater = test.pValue1SidedGreater;
        pValue1SidedLess = test.pValue1SidedLess;
    }

    /**
     * Perform the Siegel-Tukey test to test for differences in scale (variability) between two groups.
     * 
     * @param sample1 sample 1
     * @param sample2 sample 2
     * @param mu the hypothetical mean difference
     */
    public SiegelTukey(double[] sample1, double[] sample2, double mu) {
        this(sample1, sample2, mu, sample1.length + sample2.length < 50);
    }

    /**
     * Perform the Siegel-Tukey test to test for differences in scale (variability) between two groups.
     * 
     * @param sample1 sample 1
     * @param sample2 sample 2
     */
    public SiegelTukey(double[] sample1, double[] sample2) {
        this(sample1, sample2, 0);
    }
}
