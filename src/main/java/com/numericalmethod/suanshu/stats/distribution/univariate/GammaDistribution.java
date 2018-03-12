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

import com.numericalmethod.suanshu.analysis.function.special.gamma.GammaRegularizedP;
import com.numericalmethod.suanshu.analysis.function.special.gamma.GammaRegularizedPInverse;
import com.numericalmethod.suanshu.analysis.function.special.gamma.LogGamma;
import com.numericalmethod.suanshu.misc.SuanShuUtils;
import static com.numericalmethod.suanshu.number.DoubleUtils.isZero;
import static java.lang.Math.*;

/**
 * This gamma distribution, when <i>k</i> is an integer, is the distribution of
 * the sum of <i>k</i> independent exponentially distributed random variables,
 * each of which has a mean of <i>θ</i> (which is equivalent to a rate parameter of <i>θ<sup>−1</sup></i>).
 * <p/>
 * The R equivalent functions are {@code dgamma, pgamma, qgamma, rgamma}.
 *
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/Gamma_distribution">Wikipedia: Gamma distribution distribution</a>
 */
public class GammaDistribution implements ProbabilityDistribution {

    /** the shape parameter */
    private final double k;
    /** the scale parameter */
    private final double theta;
    private static final LogGamma lgamma = new LogGamma();
    private static final GammaRegularizedP pgamma = new GammaRegularizedP();
    private static final GammaRegularizedPInverse qgamma = new GammaRegularizedPInverse();

    /**
     * Construct a Gamma distribution.
     *
     * @param k     the shape parameter
     * @param theta the scale parameter
     */
    public GammaDistribution(double k, double theta) {
        SuanShuUtils.assertArgument(k > 0, "k must be > 0");
        SuanShuUtils.assertArgument(theta > 0, "theta must be > 0");

        this.k = k;
        this.theta = theta;
    }

    @Override
    public double mean() {
        return k * theta;
    }

    @Override
    public double median() {
        throw new UnsupportedOperationException("There is no simple closed form.");
    }

    @Override
    public double variance() {
        return k * theta * theta;
    }

    @Override
    public double skew() {
        return 2 / sqrt(k);
    }

    @Override
    public double kurtosis() {
        return 6 / k;
    }

    /**
     * @deprecated Not supported yet.
     */
    @Deprecated
    public double entropy() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public double cdf(double x) {
        assert x >= 0 : "x must be non-negative";
        return pgamma.evaluate(k, x / theta);
    }

    @Override
    public double quantile(double u) {
        return theta * qgamma.evaluate(k, u);
    }

    @Override
    public double density(double x) {
        if (x < 0) {
            return 0;
        }

        if (isZero(x, 0) && k < 1) {
            return Double.POSITIVE_INFINITY;
        }

        if (isZero(x, 0) && k == 1) {
            return exp(-lgamma.evaluate(k) - k * log(theta));//f = 1 / Γ(k) / theta^k
        }

        double f = (k - 1) * log(x) - x / theta - k * log(theta) - lgamma.evaluate(k);
        f = exp(f);

        return f;
    }

    @Override
    public double moment(double t) {
        SuanShuUtils.assertArgument(t < 1. / theta, "only for t < 1/theta");

        return pow(1d - theta * t, -k);
    }
}
