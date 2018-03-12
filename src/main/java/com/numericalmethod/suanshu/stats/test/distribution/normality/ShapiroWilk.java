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

import com.numericalmethod.suanshu.analysis.function.FunctionOps;
import com.numericalmethod.suanshu.analysis.function.polynomial.Polynomial;
import static com.numericalmethod.suanshu.misc.SuanShuUtils.assertArgument;
import com.numericalmethod.suanshu.stats.descriptive.moment.Mean;
import com.numericalmethod.suanshu.stats.distribution.univariate.NormalDistribution;
import com.numericalmethod.suanshu.stats.distribution.univariate.ProbabilityDistribution;
import com.numericalmethod.suanshu.stats.test.HypothesisTest;
import java.util.Arrays;
import static java.lang.Math.*;

/**
 * The Shapiro–Wilk test tests the null hypothesis that a sample comes from a normally distributed population.
 *
 * <p>
 * The sample size must be between 3 and 5000.
 *
 * <p>
 * The R equivalent function is {@code shapiro.test}.
 *
 * @author Haksun Li
 *
 * @see
 * <ul>
 * <li><a href="http://en.wikipedia.org/wiki/Shapiro%E2%80%93Wilk_test">Wikipedia: Shapiro–Wilk test</a>
 * <li>Patrick Royston. "A Remark on Algorithm AS 181: The W Test for Normality," Applied Statistics, 44, 547–551. 1995.
 * <li>Patrick Royston. "Approximating the Shapiro-Wilk W-test for non-normality," Statistics and Computing, Volume 2, Number 3, 117-119. 1992.
 * <li>Patrick Royston. "An Extension of Shapiro and Wilk's W Test for Normality to Large Samples," Applied Statistics, 31, 115–124. 1982d.
 * <li>Patrick Royston. "Algorithm AS 181: The W Test for Normality," Applied Statistics, 31, 176–180. 1982c.
 * <li>Patrick Royston. "An extension of Shapiro and Wilk's Wtest for normality to large samples," Appl. Statist., 31, 115-124. 1982b.
 * <li>Patrick Royston. "Algorithm AS177. Expected normal order statistics (exact and approximate)," Applied Statistics, 31, 161-165. 1982a.
 * </ul>
 */
public class ShapiroWilk extends HypothesisTest {

    private final ProbabilityDistribution norm = new NormalDistribution();
    private Polynomial a_n = new Polynomial(new double[]{-2.706056, 4.434685, -2.071190, -0.147981, 0.221157, 0});
    private Polynomial a_nm1 = new Polynomial(new double[]{-3.582633, 5.682633, -1.752461, -0.293762, 0.042981, 0});

    @Override
    public String getNullHypothesis() {
        return "the samples come from a normally distributed population";
    }

    @Override
    public String getAlternativeHypothesis() {
        return "the samples do not come from a normally distributed population";
    }

    /**
     * Perform the Shapiro-Wilk test to test for the null hypothesis that a sample comes from a normally distributed population.
     * 
     * @param sample a sample
     */
    public ShapiroWilk(double[] sample) {
        super(sample);

        assertArgument(3 <= N && N <= 5000, "the sample size must be between 3 and 5000");

        double[] x = Arrays.copyOf(sample, sample.length);
        Arrays.sort(x);

        double[] mtilde = new double[N];
        double m = 0;
        for (int i = 0; i < N; ++i) {
            mtilde[i] = norm.quantile((i + 1 - 0.375) / (N + 0.25));//Patrick Royston. 1982a. pp. 161.
            m += mtilde[i] * mtilde[i];
        }

        double root_mm = sqrt(m);
        double[] c = new double[N];
        for (int i = 0; i < N; ++i) {
            c[i] = mtilde[i] / root_mm;//Patrick Royston. 1992. pp. 117.
        }

        final double u = 1 / sqrt(N);//Patrick Royston. 1992. pp. 118.

        //the weights
        double[] a = new double[N];

        if (N == 3) {
            a[0] = 0.707106781;
            a[N - 1] = -a[0];
        } else {
            a[N - 1] = a_n.evaluate(u) + c[N - 1];
            a[0] = -a[N - 1];

            //getNormalization a[]
            int start = 0;
            double phi = 0;
            if (N <= 5) {
                start = 2;
                phi = (m - 2. * mtilde[N - 1] * mtilde[N - 1]);
                phi /= (1. - 2. * a[N - 1] * a[N - 1]);
            } else {
                start = 3;

                a[N - 2] = a_nm1.evaluate(u) + c[N - 2];
                a[1] = -a[N - 2];

                phi = (m - 2. * mtilde[N - 1] * mtilde[N - 1] - 2. * mtilde[N - 2] * mtilde[N - 2]);
                phi /= (1. - 2. * a[N - 1] * a[N - 1] - 2. * a[N - 2] * a[N - 2]);
            }

            double root_phi = sqrt(phi);
            for (int i = start - 1; i < N - start + 1; ++i) {
                a[i] = mtilde[i] / root_phi;
            }
        }

        //Patrick Royston. 1982b. pp. 116.
        double mu = new Mean(x).value();
        double sumDiffs = 0;
        for (int i = 0; i < N; ++i) {
            double diff = x[i] - mu;
            sumDiffs += diff * diff;
        }

        double W = FunctionOps.dotProduct(a, x);
        W *= W;
        W /= sumDiffs;

        testStatistics = W;
        pValue = oneSidedPvalue(new com.numericalmethod.suanshu.stats.test.distribution.normality.ShapiroWilkDistribution(N),
                testStatistics);
    }
}
