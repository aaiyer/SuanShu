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
package com.numericalmethod.suanshu.stats.test.rank.wilcoxon;

import com.numericalmethod.suanshu.stats.distribution.univariate.ProbabilityDistribution;
import static java.lang.Math.*;

/**
 * Compute exactly the distribution of the Wilcoxon signed rank test statistic.
 *
 * <p>
 * Let x be a sample of size N from a continuous distribution symmetric about the origin.
 * The Wilcoxon signed rank statistic is the sum of the ranks of the absolute values x[i] for which x[i] is positive.
 * This statistic takes values between 0 and N(N+1)/2.
 *
 * <p>
 * The R equivalent functions are {@code dsignrank, pdsignrank, qdsignrank, rdsignrank}.
 *
 * @author Chun Yip Yau
 *
 * @see "Zbynek Sidak, Pranab K. Sen, Jaroslav Hajek. Theory of Rank Tests. Theorems 2, Section 5.3, p.173."
 */
public class WilcoxonSignedRankDistribution implements ProbabilityDistribution {

    /**
     * number of observations in group 2
     */
    public final int N;
    private double[][] distribution;

    /**
     * Construct a Wilcoxon Signed Rank distribution for a sample size {@code N}.
     *
     * @param N number of observations
     */
    public WilcoxonSignedRankDistribution(int N) {
        this.N = N;
        this.distribution = new double[N + 1][];

        //the initial and boundary conditions
        distribution[0] = new double[1];
        distribution[0][0] = 1;

        //@see "Zbynek Sidak, Pranab K. Sen, Jaroslav Hajek. Theory of Rank Tests. Eq. 4, the recurrence formula, on p.173."
        for (int n = 1; n <= N; ++n) {
            int maxN = maxN(n);
            distribution[n] = new double[maxN + 1];
            for (int k = 0; k <= maxN; ++k) {
                double term1 = k < distribution[n - 1].length ? distribution[n - 1][k] : 0;
                double term2 = k - n >= 0 ? distribution[n - 1][k - n] : 0;
                distribution[n][k] = term1 + term2;
            }
        }

        //divide by 2^n
        for (int n = 1; n <= N; ++n) {
            for (int k = 0; k < distribution[n].length; ++k) {
                for (int i = 1; i <= n; ++i) {
                    distribution[n][k] /= 2;
                }
            }
        }
    }

    public double mean() {
        return N * (N + 1) / 4.0;
    }

    /**
     * @deprecated Not supported yet.
     */
    @Deprecated
    public double median() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public double variance() {
        return N * (N + 1) * (2 * N + 1) / 24.0;
    }

    /**
     * @deprecated Not supported yet.
     */
    @Deprecated
    public double skew() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * @deprecated Not supported yet.
     */
    @Deprecated
    public double kurtosis() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * @deprecated Not supported yet.
     */
    @Deprecated
    public double entropy() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public double cdf(double x) {
        double pValue1SidedLess = 0;
        for (int i = 0; i <= maxN(N); ++i) {
            if (i <= x) {
                pValue1SidedLess += distribution[N][i];
            }
        }

        return pValue1SidedLess;
    }

    public double quantile(double u) {
        int x = 0;
        double cdf = 0;

        for (int i = 0; x <= maxN(N); ++x) {
            for (; i <= x && i <= maxN(N); ++i) {
                cdf += distribution[N][i];
            }

            if (cdf >= u) {
                break;
            }
        }

        return x;
    }

    public double density(double x) {
        return distribution[N][(int) x];
    }

    /**
     * @deprecated Not supported yet.
     */
    @Deprecated
    public double moment(double x) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Compute the one-sided p-value for the statistics greater than a critical value.
     *
     * @param x a critical value
     * @return the one-sided p-value
     */
    public double pValue1SidedGreater(double x) {
        double result = 0;
        for (int i = 0; i <= maxN(N); ++i) {
            if (i >= x) {
                result += distribution[N][i];
            }
        }

        return result;
    }

    /**
     * Compute the two-sided p-value for a critical value.
     *
     * @param x a critical value
     * @return the two-sided p-value
     */
    public double pValue(double x) {
        double less = cdf(x);
        double greater = pValue1SidedGreater(x);

        double result = 2 * min(greater, less);
        return result;
    }

    /**
     * @see "Zbynek Sidak, Pranab K. Sen, Jaroslav Hajek. Theory of Rank Tests. Eq. 3, p.173."
     */
    private static int maxN(int n) {
        return n * (n + 1) / 2;
    }
}
