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

import com.numericalmethod.suanshu.number.big.BigIntegerUtils;
import com.numericalmethod.suanshu.stats.distribution.univariate.ProbabilityDistribution;
import java.math.BigInteger;
import static java.lang.Math.*;

/**
 * Compute the exact distribution of the Wilcoxon rank sum test statistic.
 *
 * <p>
 * Let x and y be two random, independent samples of size M and N.
 * The Wilcoxon rank sum statistic is the number of all pairs (x[i], y[j]) for which y[j] is not greater than x[i].
 * This statistic takes values between 0 and M * N.
 *
 * <p>
 * The R equivalent functions are {@code dwilcox, pwilcox, qwilcox, rwilcox}.
 *
 * @author Chun Yip Yau
 *
 * @see "Zbynek Sidak, Pranab K. Sen, Jaroslav Hajek. Theory of Rank Tests. Theorems 1, Section 5.3, p.173."
 */
public class WilcoxonRankSumDistribution implements ProbabilityDistribution {

    /**
     * number of observations in group 1
     */
    public final int M;
    /**
     * number of observations in group 2
     */
    public final int N;
    private double[][][] distribution;

    /**
     * Construct a Wilcoxon Rank Sum distribution for sample sizes {@code M} and {@code N}.
     * 
     * @param M number of observations in group 1
     * @param N number of observations in group 2
     */
    public WilcoxonRankSumDistribution(int M, int N) {
        this.M = M;
        this.N = N;
        this.distribution = new double[M + 1][N + 1][];

        //the initial and boundary conditions
        for (int n = 1; n <= N; ++n) {//m = 0
            distribution[0][n] = new double[1];
            distribution[0][n][0] = 1;
        }

        for (int m = 1; m <= M; ++m) {//other m's, n = 0
            int maxK = maxK(m, 0);
            distribution[m][0] = new double[maxK + 1];//use 0 to maxK
            distribution[m][0][maxK] = 1;
        }

        //@see "Zbynek Sidak, Pranab K. Sen, Jaroslav Hajek. Theory of Rank Tests. Eq. 2, the recurrence formula, on p.173."
        for (int m = 1; m <= M; ++m) {
            for (int n = 1; n <= N; ++n) {
                int maxK = maxK(m, n);
                distribution[m][n] = new double[maxK + 1];
                for (int k = m * (m + 1) / 2; k <= maxK; ++k) {
                    double term1 = (k >= distribution[m][n - 1].length) ? 0 : distribution[m][n - 1][k];
                    double term2 = (k - m - n >= 0) ? distribution[m - 1][n][k - m - n] : 0;
                    distribution[m][n][k] = term1 + term2;
                }
            }
        }

        //divide by C((m+n),m)
        for (int m = 1; m <= M; ++m) {
            for (int n = 1; n <= N; ++n) {
                BigInteger denominator = BigIntegerUtils.combination(m + n, m);
                for (int k = 0; k < distribution[m][n].length; ++k) {
                    distribution[m][n][k] /= denominator.doubleValue();
                }
            }
        }
    }

    public double mean() {
        return M * N / 2.0;
    }

    /**
     * @deprecated Not supported yet.
     */
    @Deprecated
    public double median() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public double variance() {
        return M * N * (M + N + 1) / 12.0;
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
        //The test statistic defined in WilcoxonRandSum is relocated. We change the statistic back to the sum of the ranks.
        int testStatistic = (int) x + M * (M + 1) / 2;

        double pValue1SidedLess = 0;
        for (int i = 0; i <= maxK(M, N); ++i) {
            if (i <= testStatistic) {
                pValue1SidedLess += distribution[M][N][i];
            }
        }

        return pValue1SidedLess;
    }

    public double quantile(double u) {
        int x = 0;
        double cdf = 0;

        for (int i = 0; x <= M * N; ++x) {
            int testStatistic = x + M * (M + 1) / 2;

            for (; i <= testStatistic && i <= maxK(M, N); ++i) {
                cdf += distribution[M][N][i];
            }

            if (cdf > u) {
                break;
            }
        }

        return x;
    }

    public double density(double x) {
        return distribution[M][N][(int) x + M * (M + 1) / 2];
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
        //The test statistic defined in WilcoxonRandSum is relocated. We change the statistic back to the sum of the ranks.
        int testStatistic = (int) x + M * (M + 1) / 2;

        double result = 0;
        for (int i = 0; i <= maxK(M, N); ++i) {
            if (i >= testStatistic) {
                result += distribution[M][N][i];
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
     * @see "Zbynek Sidak, Pranab K. Sen, Jaroslav Hajek. Theory of Rank Tests. Eq. 1, p.173."
     */
    private static int maxK(int m, int n) {
        return m * (m + 2 * n + 1) / 2;
    }
}
