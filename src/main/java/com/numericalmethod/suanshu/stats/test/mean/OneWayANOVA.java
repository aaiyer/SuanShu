package com.numericalmethod.suanshu.stats.test.mean;

import com.numericalmethod.suanshu.stats.descriptive.moment.Mean;
import com.numericalmethod.suanshu.stats.descriptive.moment.Variance;
import com.numericalmethod.suanshu.stats.distribution.univariate.FDistribution;
import com.numericalmethod.suanshu.stats.distribution.univariate.ProbabilityDistribution;
import com.numericalmethod.suanshu.stats.test.HypothesisTest;
import static java.lang.Math.*;

/**
 * The One-Way ANOVA test tests for the equality of the means of several groups.
 *
 * <p>
 * ANOVAs are helpful because they possess an advantage over a two-sample t-test.
 * Doing multiple two-sample t-tests would result in an increased chance of committing a type I error.
 * For this reason, ANOVAs are useful in comparing three or more means.
 *
 * <p>
 * The R equivalent function is {@code aov}.
 *
 * @author Chun Yip Yau
 *
 * @see
 * <ul>
 * <li><a href="http://en.wikipedia.org/wiki/Analysis_of_variance">Wikipedia: ANOVA</a>
 * <li><a href="http://en.wikipedia.org/wiki/Analysis_of_variance#The_F-test">Wikipedia: The FDistribution-test</a>
 * <li><a href="http://en.wikipedia.org/wiki/FDistribution-test">Wikipedia: FDistribution-test</a>
 * </ul>
 */
public class OneWayANOVA extends HypothesisTest {//TODO: sum sq., meam sq.

    /**
     * degree of freedoms
     */
    public final int df1;
    /**
     * degree of freedoms
     */
    public final int df2;

    @Override
    public String getNullHypothesis() {
        return "all k population means are equal";
    }

    @Override
    public String getAlternativeHypothesis() {
        return "at least two means are different";
    }

    /**
     * Perform the one-way ANOVA test to test for the equality of the means of several groups.
     *
     * @param samples the samples
     */
    public OneWayANOVA(double[]... samples) {
        super(samples);

        double totalSum = 0;
        double[] means = new double[k];
        for (int i = 0; i < k; ++i) {
            means[i] = new Mean(samples[i]).value();
            totalSum += means[i] * samples[i].length;
        }

        double overallMean = totalSum / N;

        //compute the test statistics
        double varianceBetweenGroups = 0;
        double varianceWithinGroups = 0;

        for (int i = 0; i < k; ++i) {
            varianceBetweenGroups += samples[i].length * pow(means[i] - overallMean, 2);
            varianceWithinGroups += (samples[i].length - 1) * (new Variance(samples[i]).value());
        }

        varianceBetweenGroups /= (k - 1);
        varianceWithinGroups /= (N - k);

        testStatistics = varianceBetweenGroups / varianceWithinGroups;

        df1 = k - 1;
        df2 = N - k;

        //compute the p-value
        ProbabilityDistribution F = new FDistribution(df1, df2);
        pValue = oneSidedPvalue(F, testStatistics);
    }
}
