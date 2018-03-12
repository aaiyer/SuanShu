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
import com.numericalmethod.suanshu.analysis.function.special.beta.BetaRegularizedInverse;
import com.numericalmethod.suanshu.analysis.function.special.beta.LogBeta;
import com.numericalmethod.suanshu.analysis.function.special.gamma.Digamma;
import static com.numericalmethod.suanshu.misc.SuanShuUtils.assertArgument;
import static com.numericalmethod.suanshu.number.DoubleUtils.isZero;
import static java.lang.Math.*;

/**
 * The beta distribution is the posterior distribution of the parameter <i>p</i> of a binomial distribution
 * after observing <i>α − 1</i> independent events with probability <i>p</i> and
 * <i>β − 1</i> with probability <i>1 - p</i>,
 * if the prior distribution of <i>p</i> is uniform.
 * <p/>
 * The R equivalent functions are {@code dbeta, pbeta, qbeta, rbeta}.
 *
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/Beta_distribution">Wikipedia: BetaDistribution distribution</a>
 */
public class BetaDistribution implements ProbabilityDistribution {

    /** α: the shape parameter */
    private final double alpha;
    /** β: the shape parameter */
    private final double beta;
    private final BetaRegularized Ix;
    private final BetaRegularizedInverse IxInv;
    private static final LogBeta lbeta = new LogBeta();
    private static final Digamma digamma = new Digamma();

    /**
     * Construct a Beta distribution.
     *
     * @param alpha the degree of freedom
     * @param beta  the degree of freedom
     */
    public BetaDistribution(double alpha, double beta) {
        assertArgument(alpha > 0, "alpha must be > 0");// TODO: check for infinity?
        assertArgument(beta > 0, "beta must be > 0");// TODO: check for infinity?

        this.alpha = alpha;
        this.beta = beta;
        Ix = new BetaRegularized(alpha, beta);
        IxInv = new BetaRegularizedInverse(alpha, beta);
    }

    @Override
    public double mean() {
        return alpha / (alpha + beta);
    }

    @Override
    public double median() {
        throw new UnsupportedOperationException("no closed form.");
    }

    @Override
    public double variance() {
        double anb = alpha + beta;
        double result = alpha * beta;
        result /= anb * anb * (anb + 1);
        return result;
    }

    @Override
    public double skew() {
        double anb = alpha + beta;
        double result = 2 * (beta - alpha) * sqrt(anb + 1);
        result /= (anb + 2) * sqrt(alpha * beta);
        return result;
    }

    @Override
    public double kurtosis() {
        double result = 6;
        result *= alpha * alpha * alpha - alpha * alpha * (2 * beta - 1) + beta * beta * (beta + 1) - 2 * alpha * beta * (beta + 2);
        result /= alpha * beta * (alpha + beta + 2) * (alpha + beta + 3);
        return result;
    }

    @Override
    public double entropy() {
        double result = lbeta.evaluate(alpha, beta);
        result -= (alpha - 1) * digamma.evaluate(alpha);
        result -= (beta - 1) * digamma.evaluate(beta);
        result += (alpha + beta - 2) * digamma.evaluate(alpha + beta);

        return result;
    }

    @Override
    public double cdf(double x) {
        assert x >= 0 : "x must be >= 0";
        assert x <= 1 : "x must be <= 1";

        return Ix.evaluate(x);
    }

    public double ccdf(double x) {
        return 1.0 - cdf(x);
    }

    @Override
    public double density(double x) {
        assert x >= 0 : "x must be >= 0";
        assert x <= 1 : "x must be <= 1";

        //special cases
        if (isZero(x, 0)) {
            if (alpha < 1) {
                return Double.POSITIVE_INFINITY;
            } else {
                return 0;
            }
        }

        double result = (alpha - 1) * log(x) + (beta - 1) * log(1 - x) - lbeta.evaluate(alpha, beta);
        result = exp(result);

        return result;
    }

    @Override
    public double quantile(double u) {
        double x = IxInv.evaluate(u);
        return x;
    }

    /**
     * @deprecated Not supported yet.
     */
    @Deprecated
    public double moment(double x) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
