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
package com.numericalmethod.suanshu.stats.test.distribution.normality;

import com.numericalmethod.suanshu.stats.descriptive.moment.Kurtosis;
import com.numericalmethod.suanshu.stats.descriptive.moment.Skewness;
import com.numericalmethod.suanshu.stats.distribution.univariate.ChiSquareDistribution;
import com.numericalmethod.suanshu.stats.distribution.univariate.ProbabilityDistribution;
import com.numericalmethod.suanshu.stats.test.HypothesisTest;

/**
 * The Jarque–Bera test is a goodness-of-fit measure of departure from normality, based on the sample kurtosis and skewness.
 *
 * <p>
 * The statistic JB has an asymptotic chi-square distribution with two degrees of freedom and
 * can be used to test the null hypothesis that the data are from a normal distribution.
 * The null hypothesis is a joint hypothesis of the skewness being zero and the excess kurtosis being 0,
 * since samples from a normal distribution have an expected skewness of 0 and an expected excess kurtosis of 0 (which is the same as a kurtosis of 3).
 * As the definition of JB shows, any deviation from this increases the JB statistic.
 *
 * <p>
 * The R equivalent function is {@code rjb.test} in package {@code lawstat}, or  {@code jarque.bera.test} in package {@code tseries} for N > 2000.
 *
 * @author Haksun Li
 *
 * @see <a href="http://en.wikipedia.org/wiki/Jarque%E2%80%93Bera_test">Wikipedia: Jarque–Bera test</a>
 */
public class JarqueBera extends HypothesisTest {

    public final int nSim = 50000;

    @Override
    public String getNullHypothesis() {
        return "both the skewness and the excess kurtosis are 0";
    }

    @Override
    public String getAlternativeHypothesis() {
        return "either the skewness or the excess kurtosis is non-zero";
    }

    /**
     * Perform the Jarque-Bera test to test for the departure from normality.
     *
     * @param sample a sample
     * @param isExact indicate whether the exact Jarque-Bera distribution is to be used
     */
    public JarqueBera(double[] sample, boolean isExact) {
        super(sample);

        Skewness skew = new Skewness(sample);
        Kurtosis kurtosis = new Kurtosis(sample);

        double s = skew.sample();
        double k = kurtosis.sample();

        testStatistics = (s * s + k * k / 4.) * N / 6.;

        ProbabilityDistribution dist = new ChiSquareDistribution(2);
        if (isExact && N <= 2000) {
            dist = new com.numericalmethod.suanshu.stats.test.distribution.normality.JarqueBeraDistribution(N, nSim);
        }

        pValue = oneSidedPvalue(dist, testStatistics);
    }

    /**
     * Perform the Jarque-Bera test to test for the departure from normality,
     * using the asymptotic chi-square distribution.
     *
     * @param sample a sample
     */
    public JarqueBera(double[] sample) {
        this(sample, false);
    }
}
