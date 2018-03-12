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

import com.numericalmethod.suanshu.analysis.function.special.beta.BetaRegularized;
import com.numericalmethod.suanshu.number.big.BigIntegerUtils;
import static java.lang.Math.*;
import java.math.BigInteger;

/**
 * The binomial distribution is the discrete probability distribution of the number of successes in a sequence of <i>n</i> independent yes/no experiments,
 * each of which yields success with probability <i>p</i>.
 * The binomial distribution is frequently used to model the number of successes in a sample of size <i>n</i> drawn with replacement from a population of size <i>N</i>.
 * For <i>N</i> much larger than <i>n</i>, the binomial distribution is a good approximation of hypergeometric distribution.
 *
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/Binomial_distribution">Wikipedia: Binomial distribution</a>
 */
public class BinomialDistribution implements ProbabilityDistribution {

    /** the number of trials, a natural number */
    private final int n;
    /** the success probability in each trial, [0, 1] */
    private final double p;
    private double z;// for the R source code

    /**
     * Construct a Binomial distribution.
     *
     * @param n the number of trials, a natural number
     * @param p the success probability in each trial, <i>[0, 1]</i>
     */
    public BinomialDistribution(int n, double p) {
        this.n = n;
        this.p = p;
    }

    @Override
    public double mean() {
        return (double) n * p;
    }

    /**
     * {@inheritDoc}
     * <p/>
     * The result is rounded rounded down to an integer.
     *
     * @return median
     */
    @Override
    public double median() {
        return floor((double) n * p);
    }

    @Override
    public double variance() {
        return (double) n * p * (1. - p);
    }

    @Override
    public double skew() {
        double skew = 1. - 2. * p;
        skew /= sqrt((double) n * p * (1. - p));
        return skew;
    }

    @Override
    public double kurtosis() {
        double kurtosis = 1. - 6. * p * (1. - p);
        kurtosis /= (double) n * p * (1. - p);
        return kurtosis;
    }

    /**
     * {@inheritDoc}
     *
     * @param x the number of success trials, an integer; rounded down to the closest integer if x is not an integer
     * @return cdf(x)
     */
    @Override
    public double cdf(double x) {
        int k = (int) x;

        if (k > n) {
            return 1;
        } else if (k <= 0) {
            return 0;
        }

        return new BetaRegularized(n - k, 1 + k).evaluate(1 - p);
    }

    /**
     * {@inheritDoc}
     * <p/>
     * Uses the Cornish-Fisher Expansion to include a skewness
     * correction to a normal approximation. This gives an
     * initial value which never seems to be off by more than
     * 1 or 2. A search is then conducted of values close to
     * this initial start point.
     *
     * @param u a quantile
     * @return {@code qbinom(u)}
     * @see <a href='http://svn.r-project.org/R/trunk/src/nmath/qbinom.c'>qbinom</a>
     */
    @Override
    public double quantile(double u) {// TODO: rename to 'u'
        double q, mu, sigma, gamma, y;

        if (this.p == 0. || n == 0) {
            return 0.;
        }

        q = 1 - this.p;
        if (q == 0.) {
            return n; /* covers the full range of the distribution */
        }
        mu = n * this.p;
        sigma = sqrt(n * this.p * q);
        gamma = (q - this.p) / sigma;

        /* temporary hack --- FIXME --- */
        final double DBL_EPSILON = 1e-9;
        if (u + 1.01 * DBL_EPSILON >= 1.) {
            return n;
        }

        /* y := approx.value (Cornish-Fisher expansion) : */
        NormalDistribution norm = new NormalDistribution(0, 1);
        z = norm.quantile(u);
        y = floor(mu + sigma * (z + gamma * (z * z - 1) / 6) + 0.5);

        if (y > n) /* way off */ {
            y = n;
        }

        z = cdf(y);

        /* fuzz to ensure left continuity: */
        double uu = u;
        uu *= 1 - 64 * DBL_EPSILON;

        if (n < 1e5) {
            return do_search(y, uu, 1);
        }
        /* Otherwise be a bit cleverer in the search */
        {
            double incr = floor(n * 0.001), oldincr;
            do {
                oldincr = incr;
                y = do_search(y, uu, incr);
                incr = max(1, floor(incr / 100));
            } while (oldincr > 1 && incr > n * 1e-15);
            return y;
        }
    }

    private double do_search(double y, double p, double incr) {
        if (z >= p) {
            /* search to the left */
            for (;;) {
                double newz;
                if (y == 0
                    || (newz = cdf(y - incr)) < p) {
                    return y;
                }
                y = max(0, y - incr);
                z = newz;
            }
        } else {		/* search to the right */
            for (;;) {
                y = min(y + incr, n);
                if (y == n
                    || (z = cdf(y)) >= p) {
                    return y;
                }
            }
        }
    }

    /**
     * This is the probability mass function.
     *
     * @param x the number of success trials, an integer; rounded down to the closest integer if x is not an integer
     * @return {@code pmf(x)}
     */
    @Override
    public double density(double x) {
        int k = (int) x;

        if (k <= 0 || k > n) {
            return 0;
        }

        if (k != x) {
            return 0;// non-integer x
        }

        BigInteger nCk = BigIntegerUtils.combination(n, k);
        double result = nCk.doubleValue();
        result *= pow(p, k);
        result *= pow(1. - p, n - k);

        return result;
    }

    @Override
    public double entropy() {
        double entropy = 2. * PI * exp(1) * n * p * (1. - p);
        entropy = log(entropy) / log(2);
        entropy *= 0.5;
        return entropy;
    }

    @Override
    public double moment(double t) {
        double moment = 1. - p;
        moment += p * exp(t);
        moment = pow(moment, n);
        return moment;
    }
}
