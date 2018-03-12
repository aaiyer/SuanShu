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

import com.numericalmethod.suanshu.analysis.function.special.gaussian.CumulativeNormalInverse;
import com.numericalmethod.suanshu.analysis.function.special.gaussian.CumulativeNormalMarsaglia;
import com.numericalmethod.suanshu.analysis.function.special.gaussian.Gaussian;
import com.numericalmethod.suanshu.analysis.function.special.gaussian.StandardCumulativeNormal;
import static java.lang.Math.exp;
import static java.lang.Math.log;

/**
 * The Normal distribution has its density a Gaussian function.
 * The Normal distribution is probably the most important single distribution.
 * By the central limit theorem, under certain conditions,
 * the sum of a number of random variables with finite means and variances approaches a Normal distribution as the number of variables increases.
 * Laplace proved that the Normal distribution occurs as a limiting distribution of arithmetic means of independent,
 * identically distributed random variables with finite second moment.
 * <p/>
 * The R equivalent functions are {@code dnorm, pnorm, qnorm, rnorm}.
 *
 * @author Haksun Li
 * @see
 * <ul>
 * <li><a href="http://en.wikipedia.org/wiki/Normal_distribution">Wikipedia: NormalDistribution distribution</a>
 * <li>Gaussian
 * </ul>
 */
public class NormalDistribution implements ProbabilityDistribution {

    /** the mean */
    private final double mu;
    /** the standard deviation */
    private final double sigma;
    private static Gaussian f = new Gaussian();
    private static StandardCumulativeNormal N = new CumulativeNormalMarsaglia();
    private static CumulativeNormalInverse Ninv = new CumulativeNormalInverse();

    /**
     * Construct an instance of the standard Normal distribution with mean 0 and standard deviation 1.
     */
    public NormalDistribution() {
        this(0, 1);
    }

    /**
     * Construct a Normal distribution with mean {@code mu} and standard deviation {@code sigma}.
     *
     * @param mu    the mean
     * @param sigma the standard deviation
     */
    public NormalDistribution(double mu, double sigma) {
        this.mu = mu;
        this.sigma = sigma;
    }

    @Override
    public double mean() {
        return mu;
    }

    @Override
    public double median() {
        return mu;
    }

    @Override
    public double variance() {
        return sigma * sigma;
    }

    @Override
    public double skew() {
        return 0;
    }

    @Override
    public double kurtosis() {
        return 0;
    }

    @Override
    public double entropy() {
        return log(2 * Math.PI * sigma * sigma * Math.E);
    }

    @Override
    public double cdf(double x) {
        double t = (x - mu) / sigma;
        double y = N.evaluate(t);
        return y;
    }

    @Override
    public double quantile(double u) {
        double y = Ninv.evaluate(u);
        y = mu + sigma * y;
        return y;
    }

    @Override
    public double density(double x) {
        double t = (x - mu) / sigma;
        double y = f.evaluate(t);
        y /= sigma;
        return y;
    }

    @Override
    public double moment(double t) {
        double y = mu * t;
        y += 0.5 * sigma * sigma * t * t;
        y = exp(y);
        return y;
    }
}
