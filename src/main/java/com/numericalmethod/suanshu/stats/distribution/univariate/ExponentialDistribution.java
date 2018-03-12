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

import static java.lang.Math.exp;
import static java.lang.Math.log;

/**
 * The exponential distribution describes the times between events in a Poisson process,
 * a process in which events occur continuously and independently at a constant average rate.
 * <p/>
 * The R equivalent functions are {@code dexp, pexp, qexp, rexp}.
 *
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/Exponential_distribution">Wikipedia: ExponentialDistribution distribution</a>
 */
public class ExponentialDistribution implements ProbabilityDistribution {

    /** the rate parameter */
    private final double lambda;

    /**
     * Construct an instance of the standard exponential distribution, where the rate/lambda is 1.
     */
    public ExponentialDistribution() {
        this(1d);
    }

    /**
     * Construct an exponential distribution.
     *
     * @param lambda the rate
     */
    public ExponentialDistribution(double lambda) {
        if (lambda <= 0) {
            throw new IllegalArgumentException("lambda > 0");
        }
        this.lambda = lambda;
    }

    @Override
    public double mean() {
        return 1d / lambda;
    }

    @Override
    public double median() {
        return log(2d) / lambda;
    }

    @Override
    public double variance() {
        return 1d / lambda / lambda;
    }

    @Override
    public double skew() {
        return 2d;
    }

    @Override
    public double kurtosis() {
        return 6d;
    }

    @Override
    public double entropy() {
        return 1 - log(lambda);
    }

    @Override
    public double cdf(double x) {
        double y = x < 0 ? 0 : 1d - exp(-lambda * x);
        return y;
    }

    @Override
    public double quantile(double u) {
        double y = -log(1 - u) / lambda;
        return y;
    }

    @Override
    public double density(double x) {
        double y = x < 0 ? 0 : lambda * exp(-lambda * x);
        return y;
    }

    @Override
    public double moment(double t) {
        double y = 1d - t / lambda;
        y = 1d / y;
        return y;
    }
}
