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

import com.numericalmethod.suanshu.analysis.function.polynomial.Polynomial;
import com.numericalmethod.suanshu.stats.distribution.univariate.NormalDistribution;
import com.numericalmethod.suanshu.stats.distribution.univariate.ProbabilityDistribution;
import static com.numericalmethod.suanshu.misc.SuanShuUtils.assertArgument;
import com.numericalmethod.suanshu.stats.descriptive.moment.Mean;
import com.numericalmethod.suanshu.stats.descriptive.moment.Variance;
import com.numericalmethod.suanshu.stats.test.HypothesisTest;
import java.util.Arrays;
import static java.lang.Math.*;

/**
 * Lilliefors test tests the null hypothesis that data come from a normally distributed population with an estimated sample mean and variance.
 * The test statistic is the maximal absolute difference between empirical and the hypothetical NormalDistribution distribution function.
 *
 * <p>
 * Since the hypothesized CDF has been moved closer to the data by estimation based on the data,
 * the "null distribution" of the test statistic, i.e. its probability distribution assuming the null hypothesis is true,
 * is stochastically smaller than the Kolmogorov–Smirnov distribution, hence the Lilliefors distribution.
 *
 * <p>
 * We first compute the p-value from the Dallal-Wilkinson (1986) formula, which is claimed to be only reliable when the p-value is smaller than 0.1.
 * When this p-value is > 0.1, we compute again the p-value from the distribution of the modified statistic (Stephen 1974).
 *
 * <p>
 * The R equivalent function is {@code lillie.test} in package {@code nortest}.
 *
 * @author Haksun Li
 *
 * @see
 * <ul>
 * <li><a href="http://en.wikipedia.org/wiki/Lilliefors_test">Wikipedia: Lilliefors test</a>
 * <li>Dallal, G.E. and Wilkinson, L. "An analytic approximation to the distribution of Lilliefors' test for normality," The American Statistician, 40, 294–296. 1986.
 * <li>Stephens, M.A. "EDF statistics for goodness of fit and some comparisons," Journal of the American Statistical Association, 69, 730–737. 1974.
 * </ul>
 */
public class Lilliefors extends HypothesisTest {

    private final ProbabilityDistribution norm = new NormalDistribution();
    private final Polynomial p1 = new Polynomial(new double[]{81.218052, -138.55152, 80.709644, -19.828315, 2.76773});
    private final Polynomial p2 = new Polynomial(new double[]{-32.355711, 94.029866, -97.490286, 40.662806, -4.901232});
    private final Polynomial p3 = new Polynomial(new double[]{2.423045, -12.234627, 23.186922, -19.558097, 6.198765});

    @Override
    public String getNullHypothesis() {
        return "the samples come from a normally distributed population";
    }

    @Override
    public String getAlternativeHypothesis() {
        return "the samples do not come from a normally distributed population";
    }

    /**
     * Perform the Lilliefors test to test for the null hypothesis that data come from a normally distributed population with an estimated sample mean and variance.
     * 
     * @param sample a sample
     */
    public Lilliefors(double[] sample) {
        super(sample);

        assertArgument(N >= 5, "the sample size must be >= 5");

        double[] x = Arrays.copyOf(sample, sample.length);
        Arrays.sort(x);

        double x_mu = new Mean(x).value();
        double x_var = new Variance(x).value();
        double x_sd = sqrt(x_var);

        double Dplus = Double.MIN_VALUE;
        double Dminus = Double.MIN_VALUE;
        double[] p = new double[N];
        for (int i = 0; i < N; ++i) {
            p[i] = norm.cdf((x[i] - x_mu) / x_sd);

            double dp = (double) (i + 1) / N - p[i];
            if (dp > Dplus) {
                Dplus = dp;
            }

            double dm = p[i] - (double) i / N;
            if (dm > Dminus) {
                Dminus = dm;
            }
        }

        double K = max(Dplus, Dminus);//Dmax, the Kolmogorov-Smirnov statistic
        testStatistics = K;

        //the Dallal-Wilkinson formula
        pValue = DallalWilkinson(N, K);

        //Stephen 1974
        if (pValue > 0.1) {
            pValue = Stephen(N, K);
        }
    }

    /**
     * the Dallal-Wilkinson formula
     * 
     * @param N
     * @param K
     * @return
     *
     * @see "Dallal, G.E. and Wilkinson, L. "An analytic approximation to the distribution of Lilliefors' test for normality," The American Statistician, 40, 294–296. 1986."
     */
    private double DallalWilkinson(int N, double K) {
        int n = N;
        double k = K;

        if (N > 100) {
            n = 100;
            k = K * pow(((double) N / 100.), 0.49);
        }

        double result = exp(-7.01256 * k * k * (n + 2.78019)
                + 2.99587 * k * sqrt(n + 2.78019)
                - 0.122119
                + 0.974598 / sqrt(n)
                + 1.67997 / n);

        return result;
    }

    /**
     * Stephen 1974
     *
     * @param N
     * @param K
     * @return
     *
     * @see "Stephens, M.A. "EDF statistics for goodness of fit and some comparisons," Journal of the American Statistical Association, 69, 730–737. 1974."
     */
    private double Stephen(int N, double K) {
        double k = (sqrt(N) - 0.01 + 0.85 / sqrt(N)) * K;

        double result = 0;

        if (k <= 0.302) {
            result = 1;
        } else if (k <= 0.5) {
            result = p1.evaluate(k);
        } else if (k <= 0.9) {
            result = p2.evaluate(k);
        } else if (k <= 1.31) {
            result = p3.evaluate(k);
        }

        return result;
    }
}
