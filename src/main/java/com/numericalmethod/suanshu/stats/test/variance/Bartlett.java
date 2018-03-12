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
import com.numericalmethod.suanshu.stats.distribution.univariate.ChiSquareDistribution;
import com.numericalmethod.suanshu.stats.distribution.univariate.ProbabilityDistribution;
import com.numericalmethod.suanshu.stats.test.HypothesisTest;
import static java.lang.Math.*;

/**
 * Bartlett's test is used to test if {@code k} samples are from populations with equal variances, hence homoscedasticity.
 * Some statistical tests, for example the analysis of variance, assume that variances are equal across groups or samples.
 * The Bartlett test can be used to verify that assumption.
 *
 * <p>
 * Bartlett's test is sensitive to departures from normality.
 * That is, if the samples come from non-normal distributions, then Bartlett's test may simply be testing for non-normality.
 * The {@link Levene} and {@link BrownForsythe} tests are alternatives to the Bartlett test that are less sensitive to departures from normality.
 *
 * <p>
 * The R equivalent function is {@code bartlett.test}.
 *
 * @author Chun Yip Yau
 *
 * @see
 * <ul>
 * <li><a href="http://en.wikipedia.org/wiki/Bartlett%27s_test">Wikipedia: Bartlett's test</a>
 * <li><a href="http://www.itl.nist.gov/div898/handbook/eda/section3/eda357.htm">Engineer Statistics handbook</a>
 * </ul>
 */
public class Bartlett extends HypothesisTest {

    /**
     * the degree of freedom
     */
    public final double df;

    @Override
    public String getNullHypothesis() {
        return "all population variances are equal";
    }

    @Override
    public String getAlternativeHypothesis() {
        return "at least two variances are different";
    }

    /**
     * Perform the Bartlett test to test if the samples are from populations with equal variances.
     * 
     * @param samples samples
     */
    public Bartlett(double[]... samples) {
        super(samples);

        double pooledVar = 0;
        double[] vars = new double[k];
        for (int i = 0; i < k; ++i) {
            vars[i] = new Variance(samples[i]).value();
            pooledVar += (samples[i].length - 1) * vars[i];
        }
        pooledVar = pooledVar / (N - k);

        double term1 = 0;
        double term2 = 0;
        for (int i = 0; i < k; ++i) {
            term1 += (samples[i].length - 1) * log(vars[i]);
            term2 += 1.0 / (samples[i].length - 1);
        }
        testStatistics = ((N - k) * log(pooledVar) - term1) / (1.0 + (term2 - 1.0 / (N - k)) / (3 * (k - 1)));

        df = k - 1;
        ProbabilityDistribution X2 = new ChiSquareDistribution(df);
        pValue = oneSidedPvalue(X2, testStatistics);
    }
}
