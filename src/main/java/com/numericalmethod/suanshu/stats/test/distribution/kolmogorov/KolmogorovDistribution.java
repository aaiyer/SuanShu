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

import com.numericalmethod.suanshu.Constant;
import com.numericalmethod.suanshu.analysis.sequence.Summation;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.matrix.doubles.operation.Pow;
import com.numericalmethod.suanshu.stats.distribution.univariate.ProbabilityDistribution;
import static java.lang.Math.*;

/**
 * KolmogorovDistribution distribution is the distribution of the KolmogorovDistribution–Smirnov statistic.
 * The statistic is defined as the supremum of the absolute difference between the empirical and reference distributions.
 *
 * To compute the cdf of the KolmogorovDistribution distribution, we implement the algorithm published in
 * <pre>
 * Evaluating KolmogorovDistribution's distribution
 * by
 * George Marsaglia, Wai Wan Tsang & Jingbo Wang (2003)
 * Journal of Statistical Software, 8/18.
 * </pre>
 *
 * <em>This part is not done yet.</em>
 * To compute the moments, we might use
 * <pre>
 * Computing the cumulative distribution function of the KolmogorovDistribution-Smirnov statistic
 * Drew, J.H., Glen, A.G. and Leemis, L.M.
 * Computational Statistics and Data Analysis 34 (2000) 1-15.
 * </pre>
 *
 * @author Haksun Li
 *
 * @see
 * <ul>
 * <li><a href="http://en.wikipedia.org/wiki/Kolmogorov_distribution#Kolmogorov_distribution">Wikipedia: KolmogorovDistribution distribution</a>
 * <li>"George Marsaglia, Wai Wan Tsang & Jingbo Wang. "Evaluating KolmogorovDistribution's distribution," Journal of Statistical Software, 8/18."
 * <li>"Drew, J.H., Glen, A.G. and Leemis, L.M. "Computing the cumulative distribution function of the KolmogorovDistribution-Smirnov statistic," Computational Statistics and Data Analysis 34 (2000) 1-15."
 * </ul>
 */
public class KolmogorovDistribution implements ProbabilityDistribution {

    /**
     * the number of observations
     */
    public final int n;
    /**
     * the big N for which n > bigN we use the asymptotic distribution
     */
    public final int bigN;
    /**
     * {@code true} if we use approximation for the right tail to speed up computation; up to 7 digit of accuracy
     */
    public final boolean rightTailApproximation;

    /**
     * Construct a KolmogorovDistribution distribution for a sample size <i>n</i>.
     *
     * @param n the number of observations
     * @param bigN we use asymptotic distribution for n > bigN
     * @param rightTailApproximation {@code true} if we use the right tail approximation
     */
    public KolmogorovDistribution(int n, int bigN, boolean rightTailApproximation) {
        this.n = n;
        this.bigN = bigN;
        this.rightTailApproximation = rightTailApproximation;
    }

    /**
     * Construct a KolmogorovDistribution distribution for a sample size <i>n</i>.
     * We use the asymptotic distribution for n > 16000.
     * We use an approximation for the right tail.
     *
     * @param n the number of observation
     */
    public KolmogorovDistribution(int n) {
        this(n, 16000, true);
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

    public double cdf(double d) {
        if (rightTailApproximation) {
            double nd2 = n * d * d;
            if (nd2 > 7.24
                    || (nd2 > 3.76 && n > 99)) {//approximation for K(n, d) for the right tail
                return 1 - 2 * exp(-(2.000071 + 0.331 / sqrt(n) + 1.409 / n) * nd2);//K(n, d) will exceed 0.999
            }
        }

        if (n > bigN) {//use the limiting distribution
            double x = sqrt(n) * d;
            return asymptoticCDF(x);
        }

        //See "Evaluating KolmogorovDistribution's distribution" for the meaning of these variables
        int k = (int) (n * d) + 1;
        int m = 2 * k - 1;
        double h = k - n * d;

        DenseMatrix H = new DenseMatrix(m, m);
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= m; j++) {
                if (j >= i + 2) {//column - row >= 2, e.g, H(1, 3), H(1, 4), H(2, 4)
                    H.set(i, j, 0);
                } else {
                    H.set(i, j, 1);
                }
            }
        }

        for (int i = 1; i <= m; i++) {
            H.set(i, 1, 1d - pow(h, i));//first column from the top
            H.set(m, m - i + 1, 1d - pow(h, i));//bottom row from the right
        }
        H.set(m, 1, H.get(m, 1) - pow(h, m) + (2 * h - 1 > 0 ? pow(2 * h - 1, m) : 0));//bottom left entry

        for (int i = 1; i <= m; i++) {//divide by the factorial
            for (int j = 1; j <= m; j++) {
                if (j < i + 2) {//column - row < 2, e.g, H(1, 1), H(1, 2), H(2, 3)
                    double Hij = H.get(i, j);
                    for (int g = 1; g <= i - j + 1; g++) {
                        Hij /= g;
                    }
                    H.set(i, j, Hij);
                }
            }
        }

        Pow Hn = new Pow(H, n, 1e140);
        int scale = Hn.scale();
        double tkk = Hn.B().get(k, k);

        final double tiny = 1d / Hn.base();
        double p = tkk;
        for (int i = 1; i <= n; i++) {
            p *= (double) i / n;
            if (p < tiny) {
                p *= Hn.base();
                --scale;
            }
        }
        p *= pow(Hn.base(), scale);

        return p;
    }

    /**
     * the asymptotic distribution of the KolmogorovDistribution distribution
     *
     * @param x
     * @return {@code F(x)}
     *
     * @see <a href="http://en.wikipedia.org/wiki/KolmogorovDistribution%E2%80%93Smirnov_test#Kolmogorov_distribution">Wikipedia: KolmogorovDistribution distribution</a>
     */
    public static double asymptoticCDF(final double x) {
        final Summation asymptoticDist = new Summation(new Summation.Term() {

            public double evaluate(double i) {
                double v = (2 * i - 1) * Math.PI / x;
                return exp(-v * v / 8);
            }
        }, 1e-15);

        double p = asymptoticDist.sumToInfinity(1);
        p *= Constant.ROOT_2_PI / x;

        return p;
    }

    /**
     * @deprecated Not supported yet.
     */
    @Deprecated
    public double quantile(double u) {
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
}
