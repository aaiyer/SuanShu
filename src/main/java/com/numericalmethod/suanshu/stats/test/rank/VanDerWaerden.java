package com.numericalmethod.suanshu.stats.test.rank;

import com.numericalmethod.suanshu.stats.descriptive.rank.Rank;
import com.numericalmethod.suanshu.stats.distribution.univariate.ChiSquareDistribution;
import com.numericalmethod.suanshu.stats.distribution.univariate.NormalDistribution;
import com.numericalmethod.suanshu.stats.distribution.univariate.ProbabilityDistribution;
import com.numericalmethod.suanshu.stats.test.HypothesisTest;
import static com.numericalmethod.suanshu.number.DoubleUtils.concat;

/**
 * The Van der Waerden test tests for the equality of all population distribution functions.
 *
 * <p>
 * The Van Der Waerden test converts the ranks from a standard Kruskal-Wallis one-way analysis of variance to quantiles of the standard normal distribution.
 * These are called normal scores and the test is computed from these normal scores.
 *
 * @author Chun Yip Yau
 *
 * @see <a href="http://en.wikipedia.org/wiki/Van_der_Waerden_test">Wikipedia: Van der Waerden test</a>
 */
public class VanDerWaerden extends HypothesisTest {

    private final static ProbabilityDistribution normal = new NormalDistribution(0, 1);

    @Override
    public String getNullHypothesis() {
        return "all population distribution functions are identical";
    }

    @Override
    public String getAlternativeHypothesis() {
//        return "at least one of the populations tends to yield larger observations than at least one of the other populations";
        return "at least two samples do not come from the same distribution";
    }

    /**
     * Perform the Van Der Waerden test to test for the equality of all population distribution functions.
     *
     * @param samples samples
     */
    public VanDerWaerden(double[]... samples) {//TODO: the multiple comparisons procedure
        super(samples);

        double[] all = concat(samples);
        double[] ranks = (new Rank(all)).ranks();

        //compute the test statistics
        testStatistics = 0;
        double s2 = 0;//variance of Aij's
        for (int i = 0, offset = 0; i < k; offset += samples[i].length, ++i) {
            double Ai = 0;
            for (int j = 0; j < samples[i].length; ++j) {
                double Aij = normal.quantile(ranks[offset + j] / (N + 1));//the normal score
                Ai = Ai + Aij;
                s2 += Aij * Aij;
            }
            Ai /= samples[i].length;
            testStatistics += samples[i].length * Ai * Ai;
        }
        s2 /= N - 1;
        testStatistics /= s2;

        //compute the p-value
        ProbabilityDistribution X2 = new ChiSquareDistribution(k - 1);
        pValue = oneSidedPvalue(X2, testStatistics);
    }
}
