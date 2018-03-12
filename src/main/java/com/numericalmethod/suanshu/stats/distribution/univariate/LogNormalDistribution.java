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

import static com.numericalmethod.suanshu.misc.SuanShuUtils.assertArgument;
import static java.lang.Math.*;

/**
 * A log-normal distribution is a probability distribution of a random variable whose logarithm is normally distributed.
 * A variable might be modeled as log-normal if it can be thought of as the multiplicative product of many independent random variables each of which is positive.
 *
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/Binomial_distribution">Wikipedia: Binomial distribution</a>
 */
public class LogNormalDistribution implements ProbabilityDistribution {

    /** the log-mean μ ∈ R */
    private final double logMu;
    /** the log-standard deviation; shape */
    private final double logSigma;
    private final NormalDistribution normal;

    /**
     * Construct a log-normal distribution.
     *
     * @param logMu    the log mean
     * @param logSigma the log standard deviation
     */
    public LogNormalDistribution(double logMu, double logSigma) {
        assertArgument(logSigma > 0, "standard deviation must be > 0");
        this.logMu = logMu;
        this.logSigma = logSigma;
        this.normal = new NormalDistribution(logMu, logSigma);
    }

    @Override
    public double mean() {
        double result = logMu + logSigma * logSigma / 2.;
        result = exp(result);
        return result;
    }

    @Override
    public double median() {
        return exp(logMu);
    }

    @Override
    public double variance() {
        double sigma2 = logSigma * logSigma;
        double result = exp(sigma2) - 1.;
        result *= exp(2. * logMu + sigma2);
        return result;
    }

    @Override
    public double skew() {
        double sigma2 = logSigma * logSigma;
        double esigma2 = exp(sigma2);
        double result = esigma2 + 2.;
        result *= sqrt(esigma2 - 1.);
        return result;
    }

    @Override
    public double kurtosis() {
        double sigma2 = logSigma * logSigma;
        double result = exp(4. * sigma2);
        result += 2. * exp(3. * sigma2);
        result += 3. * exp(2. * sigma2);
        result -= 6.;
        return result;
    }

    @Override
    public double entropy() {
        double result = 0.5;
        result += 0.5 * log(2. * PI * logSigma * logSigma) + logMu;
        return result;
    }

    @Override
    public double cdf(double x) {
        if (x <= 0) {
            return 0;
        }

        return normal.cdf(log(x));
    }

    @Override
    public double quantile(double u) {
        return exp(normal.quantile(u));
    }

    @Override
    public double density(double x) {
        if (x <= 0) {
            return 0;
        }

        double result = normal.density(log(x));
        result /= x;
        return result;
    }

    @Override
    public double moment(double s) {
        double result = s * logMu;
        result += 0.5 * s * s * logSigma * logSigma;
        result = exp(result);
        return result;
    }
}
