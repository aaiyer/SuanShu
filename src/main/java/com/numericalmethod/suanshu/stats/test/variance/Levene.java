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

import com.numericalmethod.suanshu.stats.descriptive.rank.Quantile.QuantileType;
import com.numericalmethod.suanshu.analysis.function.rn2r1.univariate.UnivariateRealFunction;
import com.numericalmethod.suanshu.misc.R;
import com.numericalmethod.suanshu.misc.R.which;
import com.numericalmethod.suanshu.stats.descriptive.moment.Mean;
import com.numericalmethod.suanshu.stats.descriptive.moment.Variance;
import com.numericalmethod.suanshu.stats.descriptive.rank.Quantile;
import com.numericalmethod.suanshu.stats.distribution.univariate.FDistribution;
import com.numericalmethod.suanshu.stats.distribution.univariate.ProbabilityDistribution;
import com.numericalmethod.suanshu.stats.test.HypothesisTest;
import static com.numericalmethod.suanshu.number.DoubleUtils.*;
import static com.numericalmethod.suanshu.number.doublearray.DoubleArrayMath.*;
import static java.lang.Math.*;

/**
 * The Levene test tests for the equality of variance of groups. Levene's test does not require normality of the underlying data.
 *
 * <p>
 * The R equivalent function is {@code levene.test}.
 *
 * @author Chun Yip Yau
 *
 * @see
 * <ul>
 * <li><a href="http://en.wikipedia.org/wiki/Levene's_test">Wikipedia: Levene's test</a>
 * <li><a href="http://www.itl.nist.gov/div898/handbook/eda/section3/eda35a.htm">Levene Test for Equality of Variances</a>
 * </ul>
 */
public class Levene extends HypothesisTest {

    /**
     * the implementations available when computing the absolute deviations
     */
    public static enum Type {

        /**
         * compute the absolute deviations from the group medians
         */
        MEDIAN,
        /**
         * compute the absolute deviations from the group means
         */
        MEANS,
        /**
         * compute the absolute deviations from the group trimmed means
         */
        TRIMMED_MEANS;
    }

    @Override
    public String getNullHypothesis() {
        return "all population variances are equal";
    }

    @Override
    public String getAlternativeHypothesis() {
        return "at least two variances are different";
    }
    /**
     * the degree of freedoms
     */
    public final double df1;
    /**
     * the degree of freedoms
     */
    public final double df2;

    /**
     * Perform the Levene test to test for homeogeneity of variance across groups.
     *
     * <p>
     * The absolute deviations are computed from the medians.
     * 
     * @param samples samples
     */
    public Levene(double... samples) {
        this(Type.MEDIAN, samples);
    }

    /**
     * Perform the Levene test to test for homeogeneity of variance across groups.
     *
     * @param type the implementation chosen
     * @param samples samples
     */
    public Levene(Type type, double[]... samples) {
        super(samples);

        //compute the absolute deviations from the center
        final double[] centers = type == Type.MEANS ? means(samples) : type == Type.TRIMMED_MEANS ? trimmedMeans(samples) : medians(samples);//default = Type.MEDIAN
        double[][] z = new double[k][];
        for (int i = 0; i < k; ++i) {
            final int j = i;
            z[i] = foreach(samples[i],
                    new UnivariateRealFunction() {

                        @Override
                        public double evaluate(double x) {
                            return abs(x - centers[j]);//the absolute deviations from the center
                        }
                    });
        }

        //compute the test statistics
        double zMean = sum(concat(z)) / N;

        double term1 = 0;
        double term2 = 0;
        for (int i = 0; i < k; ++i) {
            double ziMean = new Mean(z[i]).value();
            term1 += samples[i].length * pow(ziMean - zMean, 2);

            double ziVar = new Variance(z[i]).value();
            term2 += (samples[i].length - 1) * ziVar;
        }
        testStatistics = term1 / term2 * (N - k) / (k - 1);

        //compute p-value
        df1 = k - 1;
        df2 = N - k;
        ProbabilityDistribution F = new FDistribution(df1, df2);
        pValue = oneSidedPvalue(F, testStatistics);
    }

    /**
     * Compute the medians for each sample.
     * 
     * @param samples samples
     * @return the sample medians
     */
    private double[] medians(double[]... samples) {
        double[] centers = new double[samples.length];

        for (int i = 0; i < k; ++i) {
            Quantile q = new Quantile(samples[i]);
            centers[i] = q.value(0.5);
        }

        return centers;
    }

    /**
     * Compute the means for each sample.
     *
     * @param samples samples
     * @return the sample means
     */
    private double[] means(double[]... samples) {
        double[] centers = new double[samples.length];

        for (int i = 0; i < k; ++i) {
            centers[i] = new Mean(samples[i]).value();
        }

        return centers;
    }

    /**
     * Compute the trimmed means for each sample, with the extreme sample values removed.
     *
     * @param samples samples
     * @return the trimmed sample means
     */
    private double[] trimmedMeans(double[]... samples) {
        double[] centers = new double[samples.length];

        for (int i = 0; i < k; ++i) {
            final double lower = new Quantile(samples[i], QuantileType.APPROXIMATELY_MEDIAN_UNBIASED).value(0.025);//lower bound
            final double upper = new Quantile(samples[i], QuantileType.APPROXIMATELY_MEDIAN_UNBIASED).value(0.975);//upper bound

            double[] trimmed = R.select(samples[i],
                    new which() {

                        public boolean isTrue(double x, int index) {
                            return x >= lower && x <= upper;
                        }
                    });

            centers[i] = new Mean(trimmed).value();
        }

        return centers;
    }
}
