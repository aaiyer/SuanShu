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

/**
 * A univariate probability distribution completely characterizes a random variable by stipulating
 * the probability of each value of a random variable (when the variable is discrete),
 * or the probability of the value falling within a particular interval (when the variable is continuous).
 * <blockquote><i>
 * F(x) = Pr(X &le x)
 * </i></blockquote>
 *
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/Probability_distribution">Wikipedia: Probability distribution</a>
 */
public interface ProbabilityDistribution {

    /**
     * Get the mean of this distribution.
     *
     * @return the mean
     * @see <a href="http://en.wikipedia.org/wiki/Expected_value">Wikipedia: Expected value</a>
     */
    public double mean();

    /**
     * Get the median of this distribution.
     *
     * @return the median
     * @see <a href="http://en.wikipedia.org/wiki/Median">Wikipedia: Median</a>
     */
    public double median();

    /**
     * Get the variance of this distribution.
     *
     * @return the variance
     * @see <a href="http://en.wikipedia.org/wiki/Variance">Wikipedia: Variance</a>
     */
    public double variance();

    /**
     * Get the skewness of this distribution.
     *
     * @return the skewness
     * @see <a href="http://en.wikipedia.org/wiki/Skewness">Wikipedia: Skewness</a>
     */
    public double skew();

    /**
     * Get the excess kurtosis of this distribution.
     *
     * @return the excess kurtosis
     * @see <a href="http://en.wikipedia.org/wiki/Kurtosis">Wikipedia: Kurtosis</a>
     */
    public double kurtosis();

    /**
     * Get the entropy of this distribution.
     *
     * @return the entropy
     * @see <a href="http://en.wikipedia.org/wiki/Information_entropy">Wikipedia: Entropy (information theory)</a>
     */
    public double entropy();

    /**
     * Get the cumulative probability <i>F(x) = Pr(X &le; x)</i>.
     *
     * @param x <i>x</i>
     * @return <i>F(x) = Pr(X &le; x)</i>
     * @see <a href="http://en.wikipedia.org/wiki/Cumulative_distribution_function">Wikipedia: Cumulative distribution function</a>
     */
    public double cdf(double x);

    /**
     * Get the quantile, the inverse of the cumulative distribution function.
     * It is the value below which random draws from the distribution would fall <i>u×100</i> percent of the time.
     * <blockquote><pre><i>
     * F<sup>-1</sup>(u) = x, such that
     * Pr(X &le; x) = u
     * </i></pre></blockquote>
     * <em>This may not always exist.</em>
     *
     * @param u {@code u}, a quantile
     * @return <i>F<sup>-1</sup>(u)</i>
     * @see <a href="http://en.wikipedia.org/wiki/Quantile_function">Wikipedia: Quantile function</a>
     */
    public double quantile(double u);

    /**
     * The density function, which, if exists, is the derivative of <i>F</i>.
     * It describes the density of probability at each point in the sample space.
     * <blockquote><i>
     * f(x) = dF(X) / dx
     * </i></blockquote>
     * <em>This may not always exist.</em>
     * <p/>
     * For the discrete cases, this is the probability mass function.
     * It gives the probability that a discrete random variable is exactly equal to some value.
     *
     * @param x <i>x</i>
     * @return <i>f(x)</i>
     * @see
     * <ul>
     * <li><a href="http://en.wikipedia.org/wiki/Probability_density_function">Wikipedia: Probability density function</a>
     * <li><a href="http://en.wikipedia.org/wiki/Probability_mass_function">Wikipedia: Probability mass function</a>
     * </ul>
     */
    public double density(double x);

//    /**
//     * The survival functions describes the probability that the system will survive beyond a specified time.
//     * <blockquote><i>
//     * S(t) = 1 - F(t)
//     * </i></blockquote>
//     * Another name for this is complementary cumulative distribution function.
//     *
//     * @param t time
//     * @return <i>S(t)</i>
//     * @see <a href="http://en.wikipedia.org/wiki/Survival_function">Wikipedia: Survival function</a>
//     */
//    public double survival(double t);
    /**
     * The moment generating function is the expected value of <i>e<sup>tX</sup></i>. That is,
     * <blockquote><i>
     * E(e<sup>tX</sup>)
     * </i></blockquote>
     * <em>This may not always exist.</em>
     *
     * @param x <i>x</i>
     * @return <i>E(exp(tX))</i>
     * @see <a href="http://en.wikipedia.org/wiki/Moment_generating_function">Wikipedia: Moment-generating function</a>
     */
    public double moment(double x);
//    /**
//     * The characteristic function, which is the expected value of
//     * <blockquote><code>
//     * e<sup>itX</sup>
//     * </code></blockquote>
//     *
//     * @param x <i>x</i>
//     * @return {@code E(exp(itX))}
//     *
//     * @see <a href="http://en.wikipedia.org/wiki/Characteristic_function_%28probability_theory%29">Wikipedia: Characteristic function (probability theory)</a>
//     */
//    public double Chi(double x);
}
