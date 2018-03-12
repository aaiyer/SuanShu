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
package com.numericalmethod.suanshu.stats.regression.linear.glm.distribution;

import com.numericalmethod.suanshu.vector.doubles.Vector;

/**
 * This interface represents a probability distribution from the exponential family.
 *
 * <blockquote><i>
 * f<sub>Yi</sub>(y<sub>i</sub>; θ<sub>i</sub>) = exp[(y * θ<sub>i</sub> - b(θ<sub>i</sub>)) / a(φ) + c(y)]
 * </i></blockquote>
 *
 * where the parameter <i>θ<sub>i</sub></i> is called the canonical parameter,
 * <i>b(θ<sub>i</sub>)</i> the cumulant function, and <i>φ</i> the dispersion parameter.
 *
 * @author Ken Yiu
 *
 * @see "P. J. MacCullagh and J. A. Nelder, <i>Generalized Linear Models,<i> 2nd ed. Chapter 2. Eq. 2.4. pp.28"
 */
public interface ExponentialDistribution {

    /**
     * The variance function of the distribution in terms of the mean <i>μ</i>.
     *
     * @see "P. J. MacCullagh and J. A. Nelder, <i>Generalized Linear Models,<i> 2nd ed. Chapter 2. Table 2.1. pp.30"
     *
     * @param mu the distribution mean, <i>μ</i>
     * @return the value of variance function at {@code μ}
     */
    public double variance(double mu);

    /**
     * The canonical parameter of the distribution in terms of the mean <i>μ</i>.
     *
     * @see "P. J. MacCullagh and J. A. Nelder, <i>Generalized Linear Models,<i> 2nd ed. Chapter 2. Table 2.1. pp.30."
     * 
     * @param mu the distribution mean, <i>μ</i>
     * @return the value of canonical parameter <i>θ</i> at {@code μ}
     */
    public double theta(double mu);

    /**
     * The cumulant function of the exponential distribution.
     *
     * @see "P. J. MacCullagh and J. A. Nelder, <i>Generalized Linear Models,<i> 2nd ed. Chapter 2. Table 2.1. pp.30."
     *
     * @param theta the input argument of the cumulant function
     * @return the value of the cumulant function at (@code θ}
     */
    public double cumulant(double theta);

    /**
     * Different distribution models have different ways to compute dispersion, φ.
     *
     * <p>
     * Note that in R's output, this is called "over-dispersion".
     *
     * @see "P. J. MacCullagh and J. A. Nelder, <i>Generalized Linear Models,<i> 2nd ed. Section 2.2.2. Table 2.1."
     *
     * @param y
     * @param mu <i>μ</i>
     * @param nFactors
     * @return the dispersion
     */
    public double dispersion(Vector y, Vector mu, int nFactors);

    /**
     * Overdispersion is the presence of greater variability (statistical dispersion)
     * in a data set than would be expected based on the nominal variance of a given simple statistical model.
     *
     * σ^2 = X^2/(n-p), 4.23
     * X^2 = sum{(y-μ)^2}/V(μ), p.34
     * = sum{(y-μ)^2}/b''(θ), p.29
     *
     * X^2 estimates a(φ) = φ, the dispersion parameter (assuming w = 1).
     *
     * <p>
     * For, Gamma, Gaussian, InverseGaussian, over-dispersion is the same as dispersion.
     *
     * @see "P. J. MacCullagh and J. A. Nelder, <i>Generalized Linear Models,<i> 2nd ed. Section 4.5. Equation 4.23."
     *
     * @param y
     * @param mu <i>μ</i>
     * @param nFactors
     * @return the dispersion
     */
    public double overdispersion(Vector y, Vector mu, int nFactors);

    /**
     * Deviance <i>D(y;μ^)</i> measures the goodness-of-fit of a model, which is defined as
     * the difference between the maximum log likelihood achievable and that achieved by the model.
     * <blockquote><i>
     * D(y;μ^) = 2 * [l(y;y) - l(μ^;y)]
     * </i></blockquote>
     * where <i>l</i> is the log-likelihood.
     *
     * <p>
     * For an exponential family distribution, this is equivalent to
     * <blockquote><i>
     * 2 * [(y * θ(y) - b(θ(y))) - (y * θ(μ^) - b(θ(μ^)]
     * </i></blockquote>
     * where <i>b()</i> is the cumulant function of the distribution.
     *
     * @param y the observed value
     * @param mu the <em>estimated</em> mean, μ^
     * @return the deviance
     *
     * @see
     * <ul>
     * <li>P. J. MacCullagh and J. A. Nelder, "Measuring the goodness-of-fit," <i>Generalized Linear Models,<i> 2nd ed. Section 2.3. pp.34.
     * <li><a href="http://en.wikipedia.org/wiki/Deviance_%28statistics%29">Wikipedia: Deviance<a>
     * </ul>
     */
    public double deviance(double y, double mu);

    /**
     * AIC = 2 * #param - 2 * log-likelihood
     *
     * @param y
     * @param mu <i>μ</i>
     * @param weight
     * @param preLogLike sum of <i>y * θ<sub>i</sub> - b(θ<sub>i</sub>)</i>
     * @param deviance
     * @param nFactors
     * @return the AIC
     */
    public double AIC(Vector y, Vector mu, Vector weight, double preLogLike, double deviance, int nFactors);
}
