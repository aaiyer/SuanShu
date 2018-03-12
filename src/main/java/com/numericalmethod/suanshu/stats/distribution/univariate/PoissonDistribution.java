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

import com.numericalmethod.suanshu.analysis.sequence.Summation;
import static com.numericalmethod.suanshu.misc.SuanShuUtils.assertArgument;
import static java.lang.Math.*;

/**
 * The Poisson distribution (or Poisson law of small numbers) is a discrete probability distribution that
 * expresses the probability of a given number of events occurring in a fixed interval of time and/or space
 * if these events occur with a known average rate and independently of the time since the last event.
 * <p/>
 * The R equivalent functions are {@code dpois, ppois, qpois, rpois}.
 *
 * @author Haksun Li
 * @see
 * <ul>
 * <li><a href="http://en.wikipedia.org/wiki/Poisson_distribution">Wikipedia: Poisson distribution</a>
 * <li><a href="http://www.boost.org/doc/libs/1_36_0/libs/math/doc/sf_and_dist/html/math_toolkit/policy/pol_tutorial/understand_dis_quant.html">Wikipedia: Understanding Quantiles of Discrete Distributions</a>
 * </ul>
 */
public class PoissonDistribution implements ProbabilityDistribution {

    /** α: a positive real number, equal to the expected number of occurrences during the given interval */
    private final double lambda;

    /**
     * Construct a Poisson distribution.
     *
     * @param lambda the rate per interval
     */
    public PoissonDistribution(double lambda) {
        assertArgument(lambda > 0, "lambda must be > 0");
        this.lambda = lambda;
    }

    @Override
    public double mean() {
        return lambda;
    }

    @Override
    public double median() {
        return (int) (lambda + 1. / 3 - 0.02 * lambda);
    }

    @Override
    public double variance() {
        return lambda;
    }

    @Override
    public double skew() {
        return 1. / sqrt(lambda);
    }

    @Override
    public double kurtosis() {
        return 1. / lambda;
    }

    @Override
    public double entropy() {
        if (lambda < 10) {
            return entropy1();
        } else {
            return entropy2();
        }
    }

    double entropy1() {
        double result = lambda * (1 - log(lambda));

        Summation S = new Summation(new Summation.Term() {

            @Override
            public double evaluate(double k) {
                double s = 1;
                for (int i = 1; i <= k; ++i) {
                    s *= lambda / i;
                }

                double l = 0;
                for (int i = 1; i <= k; ++i) {
                    l += log(i);
                }

                return s * l;
            }
        },
                                    1e-8);

        double sum = S.sumToInfinity(2, 1);//k = 0, 1 imples the term is 0; we skip them

        result += exp(-lambda) * sum;
        return result;
    }

    double entropy2() {
        double result = 0.5 * log(2. * PI * exp(1) * lambda);
        result -= 1. / 12. / lambda;
        result -= 1. / 24. / lambda / lambda;
        result -= 19. / 360. / lambda / lambda / lambda;

        return result;
    }

    @Override
    public double cdf(double k) {
        double result = 0;

        double term = 1;
        result += term;

        for (int i = 1; i <= k; ++i) {
            term *= lambda / i;
            result += term;
        }

        result *= exp(-lambda);

        return result;
    }

    /**
     * The complementary cumulative distribution function.
     *
     * @param x <i>x</i>
     * @return <i>1 - F(x) = 1 - Pr(X &le; x)</i>
     */
    public double ccdf(double x) {
        return 1.0 - cdf(x);
    }

    @Override
    public double density(double k) {
        assert k >= 0 : "x must be >= 0";

        double result = exp(-lambda);
        for (int i = 1; i <= (int) k; ++i) {
            result *= lambda / i;
        }

        return result;
    }

    @Override
    public double quantile(double u) {// TODO: use the Cornish–Fisher Expansion followed by a search
        for (int k = 0;; ++k) {
            if (cdf(k) >= u) {
                return k;
            }
        }
    }

    @Override
    public double moment(double t) {
        double result = exp(t) - 1;
        result *= lambda;
        result = exp(result);

        return result;
    }
}
