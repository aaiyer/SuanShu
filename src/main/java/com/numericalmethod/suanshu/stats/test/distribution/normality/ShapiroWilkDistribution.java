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
import static java.lang.Math.*;

/**
 * Shapiro-Wilk distribution is the distribution of the Shapiro-Wilk statistics,
 * which tests the null hypothesis that a sample comes from a normally distributed population.
 *
 * <p>
 * This is an implementation of ALGORITHM AS R94.
 * Although our implementation allows for sample size > 5000, its validity is not rigorously established.
 *
 * @author Haksun Li
 *
 * @see
 * <ul>
 * <li><a href="http://en.wikipedia.org/wiki/Shapiro-Wilk">Wikipedia: Shapiro–Wilk test</a>
 * <li>Patrick Royston. "A Remark on Algorithm AS 181: The W Test for Normality," Applied Statistics, 44, 547–551. 1995.
 * <li>Patrick Royston. "Approximating the Shapiro-Wilk W-test for non-normality," Statistics and Computing, Volume 2, Number 3, 117-119.
 * <li>Patrick Royston. "Algorithm AS 181: The W Test for Normality," Applied Statistics, 31, 176–180. 1982.
 * </ul>
 */
public class ShapiroWilkDistribution implements ProbabilityDistribution {//TODO: the exact case

    /**
     * the number of observations
     */
    public final int n;
    //@see Patrick Royston. "Approximating the Shapiro-Wilk W-test for non-normality," Statistics and Computing, Volume 2, Number 3, 117-119.
    private Polynomial mu1 = new Polynomial(new double[]{-0.0006714, 0.025054, -0.39978, 0.5440});
    private Polynomial sigma1 = new Polynomial(new double[]{-0.0020322, 0.062767, -0.77857, 1.3822});
    private Polynomial gamma = new Polynomial(new double[]{0.459, -2.273});
    private Polynomial mu2 = new Polynomial(new double[]{0.00389150, -0.083751, -0.31082, -1.5861});
    private Polynomial sigma2 = new Polynomial(new double[]{0.00303020, -0.082676, -0.48030});
    private ProbabilityDistribution norm = new NormalDistribution();

    /**
     * Construct a Shapiro-Wilk distribution.
     * 
     * @param n the number of observations
     */
    public ShapiroWilkDistribution(int n) {
        this.n = n;
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

    public double cdf(double W) {
        if (n == 3) {
            double pValue = 1.909859 * (asin(sqrt(W)) - 1.047198);
            return 1. - pValue;
        }

        //the defaults for n == 3
        double mu = 0;
        double sigma = 1;
        double w = 0;

        if (4 <= n && n <= 11) {
            mu = mu1.evaluate(n);
            sigma = exp(sigma1.evaluate(n));
            double gamma = this.gamma.evaluate(n);
            w = -log(gamma - log(1 - W));
        } else if (n > 11) {
            double x = log(n);
            mu = mu2.evaluate(x);
            sigma = exp(sigma2.evaluate(x));
            w = log(1 - W);
        }

        double normalizedW = (w - mu) / sigma;
        return norm.cdf(normalizedW);
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
