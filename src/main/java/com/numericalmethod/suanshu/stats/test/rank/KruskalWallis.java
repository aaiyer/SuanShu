package com.numericalmethod.suanshu.stats.test.rank;

import com.numericalmethod.suanshu.number.Counter;
import com.numericalmethod.suanshu.number.DoubleUtils;
import static com.numericalmethod.suanshu.number.DoubleUtils.concat;
import com.numericalmethod.suanshu.stats.descriptive.rank.Rank;
import com.numericalmethod.suanshu.stats.distribution.univariate.ChiSquareDistribution;
import com.numericalmethod.suanshu.stats.distribution.univariate.ProbabilityDistribution;
import com.numericalmethod.suanshu.stats.test.HypothesisTest;
import static java.lang.Math.pow;
import java.util.Set;

/**
 * The Kruskal–Wallis test is a non-parametric method for testing equality of population medians among groups.
 * It is identical to a one-way analysis of variance with the data replaced by their ranks.
 *
 * <p>
 * Since it is a non-parametric method, the Kruskal–Wallis test does not assume a normal population,
 * unlike the analogous one-way analysis of variance.
 * However, the test does assume an identically-shaped and scaled distribution for each group, except for any difference in medians.
 *
 * <p>
 * TODO: correction for ties
 *
 * <p>
 * The R equivalent function is {@code kruskal.test}.
 *
 * @author Chun Yip Yau
 *
 * @see <a href="http://en.wikipedia.org/wiki/Kruskal-Wallis">Wikipedia: Kruskal–Wallis one-way analysis of variance</a>
 */
public class KruskalWallis extends HypothesisTest {

    @Override
    public String getNullHypothesis() {
        return "all population medians are equal";
    }

    @Override
    public String getAlternativeHypothesis() {
        return "at least two medians are different";
    }

    /**
     * Construct a Kruskal-Wallis test for the equality of median of groups.
     * 
     * @param samples samples
     */
    public KruskalWallis(double[]... samples) {
        super(samples);

        double[] all = concat(samples);
        double[] ranks = (new Rank(all)).ranks();

        //compute the test statistics
        double meanRank = 0;
        double sum = 0;
        for (int i = 0, offset = 0; i < k; offset += samples[i].length, ++i) {
            meanRank = 0;
            for (int j = 0; j < samples[i].length; ++j) {
                meanRank += ranks[offset + j];
            }
            meanRank /= samples[i].length;

            sum += samples[i].length * pow(meanRank, 2);
        }
        testStatistics = sum * 12 / (N * (N + 1)) - 3 * (N + 1);

        //correction for ties
        double term = 0;
        Counter counter = new Counter();
        counter.add(ranks);
        Set<Double> groups = counter.keySet();
        for (Double d : groups) {
            int t = counter.count(d.doubleValue());
            term += t * t * t - t;
        }

        double adjustment = 1 - term / (N * N * N - N);
        testStatistics /= adjustment;

        //compute p-value
        pValue = Double.NaN;
        if (DoubleUtils.isZero(testStatistics, 0)) {
            pValue = 1;
        } else if (DoubleUtils.isNumber(testStatistics)) {
            ProbabilityDistribution X2 = new ChiSquareDistribution(k - 1);
            pValue = oneSidedPvalue(X2, testStatistics);
        }
    }
}
