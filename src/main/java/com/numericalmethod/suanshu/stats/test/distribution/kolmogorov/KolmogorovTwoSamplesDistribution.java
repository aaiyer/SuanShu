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
package com.numericalmethod.suanshu.stats.test.distribution.kolmogorov;

import static com.numericalmethod.suanshu.number.DoubleUtils.concat;
import com.numericalmethod.suanshu.stats.distribution.univariate.ProbabilityDistribution;
import static java.lang.Math.*;
import java.util.Arrays;

/**
 * Compute the p-values for the generalized (conditionally distribution-free) Smirnov homogeneity test.
 *
 * <p>
 * That is,
 * <code>
 * P(D<sub>m,n</sub> >= c | H0) = 1 - P(D<sub>m,n</sub> < c | H0) = 1 - cdf(c)
 * </code>, where
 *
 * <code>
 * D<sub>m,n</sub> max |S<sub>m</sub>(x) - S<sub>n</sub>(x)|
 * </code>
 *
 * @author Haksun Li
 *
 * @see
 * <ul>
 * <li>"Algorithm AS 288: Exact Smirnov Two-Sample Tests for Arbitrary Distributions. Andrei M. Nikiforov, 1994. Royal Statistical Society."
 * <li>"Section 6.3. Nonparametric Statistical Inference. 4th edition. Jean Dickinson Gibbons, Subhabrata Chakraborti. CRC."
 * </ul>
 */
public class KolmogorovTwoSamplesDistribution implements ProbabilityDistribution {

    /**
     * the types of KolmogorovDistribution two-sample test available
     */
    public static enum Side {

        /**
         * two-sample; two-sided
         */
        EQUAL,
        /**
         * two-sample; one-sided
         */
        GREATER,
        /**
         * two-sample; one-sided
         */
        LESS
    };
    /**
     * the type of KolmogorovDistribution two-sample distribution, i.e., equal, greater, less
     */
    public final Side side;
    /**
     * the big N for which {@code n > bigN} we use the asymptotic distribution
     */
    public final int bigN;
    /**
     * the number of observations of the first sample
     */
    public final int n1;
    /**
     * the number of observations of the second sample
     */
    public final int n2;
    /**
     * the total number of observations of the two samples
     */
    public final int n;
    /**
     * the concat of the two samples in ascending order
     * <em>not copied</em>
     */
    private final double[] samples;

    /**
     * Construct a two-sample KolmogorovDistribution distribution.
     *
     * @param n1 size of sample 1
     * @param n2 size of sample 2
     * @param samples the concatenate of the two samples in <em>ascending</em> order
     * @param bigN when <code>n > bigN</code>, we use the asymptotic distribution
     */
    public KolmogorovTwoSamplesDistribution(int n1, int n2, double[] samples, Side side, int bigN) {
        this.n1 = n1;
        this.n2 = n2;
        this.n = n1 + n2;
        this.side = side;
        this.samples = samples;
        this.bigN = bigN;
    }

    /**
     * Construct a two-sample KolmogorovDistribution distribution,
     * assuming that there is no tie in the samples.
     *
     * @param n1 size of sample 1
     * @param n2 size of sample 2
     * @param bigN when <code>n > bigN</code>, we use the asymptotic distribution
     */
    public KolmogorovTwoSamplesDistribution(int n1, int n2, Side side, int bigN) {
        this(n1, n2, null, side, bigN);
    }

    /**
     * Construct a two-sample KolmogorovDistribution distribution.
     * 
     * @param n1 size of sample 1
     * @param n2 size of sample 2
     * @param side the type of KolmogorovDistribution two-sample test
     * @param samples the concatenate of the two samples in <em>ascending</em> order
     */
    public KolmogorovTwoSamplesDistribution(int n1, int n2, Side side, double[] samples) {
        this(n1, n2, samples, side, 200);
    }

    /**
     * Construct a two-sample KolmogorovDistribution distribution.
     *
     * @param sample1 sample 1
     * @param sample2 sample 2
     * @param side the type of KolmogorovDistribution two-sample test
     */
    public KolmogorovTwoSamplesDistribution(double[] sample1, double[] sample2, Side side) {
        this(sample1.length, sample2.length, sortSamples(sample1, sample2), side, 200);
    }

    /**
     * @deprecated Not supported yet.
     */
    @Deprecated
    public double mean() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * @deprecated Not supported yet.
     */
    @Deprecated
    public double median() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * @deprecated Not supported yet.
     */
    @Deprecated
    public double variance() {
        throw new UnsupportedOperationException("Not supported yet.");
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
        if (n >= bigN) {
            return asymptoticCDF(x);
        } else {
            return Nikiforov(x);
        }
    }

    /**
     *
     * @param c critical value
     * @return <code>P(D<sub>m,n</sub> < c | H0)</code>
     *
     * @see "Jean Dickinson Gibbons, Subhabrata Chakraborti. "Section 6.3," Nonparametric Statistical Inference, 4th ed., CRC."
     */
    private double Nikiforov(double c) {
        if (c <= 0) {
            return 0;
        }

        c = floor(c * n1 * n2 - 1e-10) / (n1 * n2);//the R way of doing thing; align c to the nearest 'grid point' (scaled by n1*n2)

        /*
         * A[., j]: the number of eligible paths at grid point (i, j).
         * After the two-loop iterations, we will reach A[n1, n2].
         * p[j] = A[., j] scaled by C(n, n1).
         */
        double[] p = new double[n2 + 1];
        for (int i = 0; i <= n1; ++i) {
            for (int j = 0; j <= n2; ++j) {
                /*
                 * To avoid overflow, we multiply p[] by w as we move up along n2.
                 * Accumulating the w's amount to dividing A[n1, n2] by C(n, n1).
                 * 1 / C(n, n1) = 1/(n1+1) * 2/(n1+2) * ... * n2/(n1+n2)
                 */
                double w = (double) (j) / (n1 + j);
                if (i == 0 && j == 0) {//the origin
                    p[j] = 1;
                } else if (i + j < n && (samples == null ? true : samples[i + j - 1] != samples[i + j]) && distance(i, j) > c) {//this path lies outside the boundary lines; > if the R-way; >= otherwise
                    p[j] = 0;
                } else if (i == 0) {//A[0, j] = A[0, j-1] = p[j]
                    p[j] = p[j - 1] * w;
                } else if (j == 0) {//A[i, 0] = A[i-1, 0] = p[0]
                    //do nothing - we simply use the value from the last i-iteration
                } else {
                    /*
                     * the recursion relationship
                     * p[j]: A[i-1, j] from last iteration
                     * p[j-1]: A[i, j-1] from this iteration
                     */
                    p[j] = p[j] + p[j - 1] * w;
                }
            }
        }

        //the number of paths which lie entirely within the boundary lines
        //scaled by the number of possible paths
        return p[n2] > 1 ? 1 : p[n2];//A[n1, n2]
    }

    /**
     * 
     * @param x x-coordinate in the grid
     * @param y y-coordinate in the grid
     * @return distance to the boundary/boundaries
     *
     * @see "Andrei M. Nikiforov. "Algorithm AS 288: Exact Smirnov Two-Sample Tests for Arbitrary Distributions," Royal Statistical Society, p. 266."
     */
    private double distance(int x, int y) {
        double result = 0;

        switch (side) {
            case GREATER:
                result = (double) x / n1 - (double) y / n2;//(d = i/Nx - j/Ny) for >
                break;
            case LESS:
                result = (double) y / n2 - (double) x / n1;//-d for <
                break;
            case EQUAL:
                result = abs((double) x / n1 - (double) y / n2);//abs(d) for <>
                break;
            default:
                throw new RuntimeException("not reachable");
        }

        return result;
    }

    private double asymptoticCDF(double x) {
        double result = 0;
        double m = sqrt(n1 * n2 / (double) (n1 + n2));

        switch (side) {
            case GREATER:
            case LESS://same as that of KolmogorovDistribution one-sided distribution from Smirnov
                result = KolmogorovOneSidedDistribution.asymptoticCDF(m, x);
                break;
            case EQUAL://same as that of KolmogorovDistribution distribution
                double c = x * m;
                result = KolmogorovDistribution.asymptoticCDF(c);
                break;
            default:
                throw new RuntimeException("not reachable");
        }

        return result;
    }

    /**
     * @deprecated Not supported yet.
     */
    @Deprecated
    public double quantile(double q) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * @deprecated Not supported yet.
     */
    @Deprecated
    public double density(double x) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * @deprecated Not supported yet.
     */
    @Deprecated
    public double moment(double x) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private static double[] sortSamples(double[] sample1, double[] sample2) {
        double[] merge = concat(sample1, sample2);
        Arrays.sort(merge);
        return merge;
    }
}
