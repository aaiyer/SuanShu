package com.numericalmethod.suanshu.stats.test.variance;

import com.numericalmethod.suanshu.stats.descriptive.rank.Quantile;
import com.numericalmethod.suanshu.stats.test.HypothesisTest;
import com.numericalmethod.suanshu.stats.test.mean.OneWayANOVA;
import static java.lang.Math.*;

/**
 * The Brown–Forsythe test is a statistical test for the equality of group variances based on performing an ANOVA on a transformation of the response variable.
 * In statistics, when a usual one-way ANOVA is performed, it is assumed that the group variances are statistically equal.
 * If this assumption is not valid, then the resulting F-test is invalid.
 *
 * @author Chun Yip Yau
 *
 * @see <a href="http://en.wikipedia.org/wiki/Brown%E2%80%93Forsythe_test">Wikipedia: Brown-Forsythe Test</a>
 */
public class BrownForsythe extends HypothesisTest {

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
        return "all population variances are equal";
    }

    @Override
    public String getAlternativeHypothesis() {
        return "at least two variances are different";
    }

    /**
     * Perform the Brown-Forsythe test to test for equal variances of the samples.
     *
     * @param samples samples
     */
    public BrownForsythe(double[]... samples) {
        super(samples);

        /*
         * transform the variables into z, the spread, defined by
         * z_{i,j} = |y_{i,j} - median(group j)|,
         * where y_{i,j} is the i observation from group j.
         */
        double[][] transformation = new double[k][];
        for (int i = 0; i < k; ++i) {
            Quantile quantile = new Quantile(samples[i]);
            double median = quantile.value(0.5);
            transformation[i] = new double[samples[i].length];
            for (int j = 0; j < samples[i].length; ++j) {
                transformation[i][j] = abs(samples[i][j] - median);
            }
        }

        OneWayANOVA anova = new OneWayANOVA(transformation);

        df1 = anova.df1;
        df2 = anova.df2;
        testStatistics = anova.statistics();
        pValue = anova.pValue();
    }
}
