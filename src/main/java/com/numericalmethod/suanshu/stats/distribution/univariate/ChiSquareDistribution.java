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
package com.numericalmethod.suanshu.stats.distribution.univariate;

import com.numericalmethod.suanshu.analysis.function.special.gamma.Digamma;
import com.numericalmethod.suanshu.analysis.function.special.gamma.GammaRegularizedP;
import com.numericalmethod.suanshu.analysis.function.special.gamma.GammaRegularizedPInverse;
import com.numericalmethod.suanshu.analysis.function.special.gamma.LogGamma;
import com.numericalmethod.suanshu.misc.SuanShuUtils;
import static com.numericalmethod.suanshu.number.DoubleUtils.isZero;
import static java.lang.Math.*;

/**
 * The Chi-square distribution is the distribution of
 * the sum of the squares of a set of statistically independent standard Gaussian random variables.
 * <p/>
 * The R equivalent functions are {@code dchisq, pchisq, qchisq, rchisq}.
 *
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/Chi_square">Wikipedia: Chi-square distribution</a>
 */
public class ChiSquareDistribution implements ProbabilityDistribution {

    /** the degree of freedom */
    private final double k;
    private static final LogGamma lgamma = new LogGamma();
    private static final GammaRegularizedP pgamma = new GammaRegularizedP();
    private static final GammaRegularizedPInverse qgamma = new GammaRegularizedPInverse();
    private static final Digamma digamma = new Digamma();

    /**
     * Construct a Chi-Square distribution.
     *
     * @param k the degree of freedom
     */
    public ChiSquareDistribution(double k) {
        SuanShuUtils.assertArgument(k > 0, "k must be > 0");
        this.k = k;
    }

    @Override
    public double mean() {
        return k;
    }

    @Override
    public double median() {
        return k - 2 / 3 + 4 / 27 / k - 8 / 729 / k / k;
    }

    @Override
    public double variance() {
        return 2 * k;
    }

    @Override
    public double skew() {
        return sqrt(8 / k);
    }

    @Override
    public double kurtosis() {
        return 12 / k;
    }

    @Override
    public double entropy() {
        return k / 2 + log(2) + lgamma.evaluate(k / 2) + (1 - k / 2) * digamma.evaluate(k / 2);
    }

    @Override
    public double cdf(double x) {
        assert x >= 0 : "x must be non-negative";
        return pgamma.evaluate(k / 2, x / 2);
    }

    @Override
    public double quantile(double u) {
        return 2 * qgamma.evaluate(k / 2, u);
    }

    @Override
    public double density(double x) {
        if (x < 0) {
            return 0;
        }

        if (isZero(x, 0) && k < 2) {
            return Double.POSITIVE_INFINITY;
        }

        if (isZero(x, 0) && k == 2) {
            return 0.5;
        }

        double f = pow(0.5, k / 2);
        double power = (k / 2 - 1) * log(x) - x / 2 - lgamma.evaluate(k / 2);
        f *= exp(power);

        return f;
    }

    @Override
    public double moment(double t) {
        SuanShuUtils.assertArgument(t < 0.5, "only for t < 0.5");
        return pow(1d - 2 * t, -k / 2);
    }
}
