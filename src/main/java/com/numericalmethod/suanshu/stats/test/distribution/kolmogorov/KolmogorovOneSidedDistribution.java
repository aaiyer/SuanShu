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

import static com.numericalmethod.suanshu.misc.SuanShuUtils.assertArgument;
import com.numericalmethod.suanshu.stats.distribution.univariate.ProbabilityDistribution;
import static com.numericalmethod.suanshu.number.big.BigIntegerUtils.combination;
import static java.lang.Math.*;

/**
 * Compute Pn(ε) = Pr{F(x) < min{Fn(x) + ε, 1}, for all x},
 * i.e., the probability that F(x) is dominated by the upper confidence contour.
 *
 * @author Haksun Li
 *
 * @see
 * <ul>
 * <li>"One-sided confidence contours for probability distribution functions. Z. W. Birnbaum and Fred H. Tingey (1951). The Annals of Mathematical Statistics,Vol. 22, No. 4 (Dec., 1951), p. 592-596."
 * <li>"Sur les 6carts de la courbe de distribution empirique. N. Smirnov. Rec. Math. (Mat.Sbornik), N. S. Vol. 6 (48) (1939), p. 3-26."
 * </ul>
 */
public class KolmogorovOneSidedDistribution implements ProbabilityDistribution {

    /**
     * the number of observations
     */
    public final int n;
    /**
     * the big N for which {@code n > bigN} we use the asymptotic distribution
     */
    public final int bigN;

    /**
     * Construct a one-sided Kolmogorov distribution.
     * 
     * @param n the number of observation
     * @param bigN the big N for which {@code n > bigN}, we use the asymptotic distribution
     */
    public KolmogorovOneSidedDistribution(int n, int bigN) {
        this.n = n;
        this.bigN = bigN;
    }

    /**
     * Construct a one-sided Kolmogorov distribution.
     * We use the asymptotic distribution for n > 50.
     *
     * @param n the number of observation
     */
    public KolmogorovOneSidedDistribution(int n) {
        this(n, 50);
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
            return asymptoticCDF(sqrt(n), x);
        } else {
            return BirnbaumTingey(x);
        }
    }

    /**
     * @param x <i>x</i>
     * @return Pr(x)
     *
     * @see "Z. W. Birnbaum and Fred H. Tingey. "One-sided confidence contours for probability distribution functions," The Annals of Mathematical Statistics,Vol. 22, No. 4 (Dec., 1951), p. 592-596."
     */
    private double BirnbaumTingey(double x) {
        double result = 0;
        if (x <= 0) {
            result = 0;
        } else if (x >= 1) {
            result = 1;
        } else {
            int nTerms = (int) floor(n * (1 - x));
            for (int j = 0; j <= nTerms; ++j) {
                result += exp(log(combination(n, j).doubleValue()) + (n - j) * log(1 - x - (double) j / n) + (j - 1) * log(x + (double) j / n));
            }
            result = 1 - x * result;
        }

        return result;
    }

    /**
     * the asymptotic distribution of the one-sided Kolmogorov distribution
     *
     * @param m scaling factor; usually a function of the size of the sample(s)
     * @param x <i>x</i>
     * @return Pr(x)
     *
     * @see "N. Smirnov. "Sur les 6carts de la courbe de distribution empirique," Rec. Math. (Mat.Sbornik), N. S. Vol. 6 (48) (1939), p. 3-26."
     */
    public static double asymptoticCDF(double m, double x) {
        return 1 - exp(-2 * m * m * x * x);
    }

    public double quantile(double q) {
        assertArgument(n >= 50, "supported only for n < 50");

        double alpha = 1 - q;
        double result = log(1 / alpha) / 2 / n;
        result = sqrt(result);
        return result;
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
