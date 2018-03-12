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
package com.numericalmethod.suanshu.stats.random.univariate;

import com.numericalmethod.suanshu.stats.distribution.univariate.ProbabilityDistribution;
import com.numericalmethod.suanshu.stats.random.univariate.uniform.UniformRng;

/**
 * Inverse transform sampling (also known as inversion sampling, the inverse probability integral transform, the inverse transformation method, Smirnov transform, golden rule, etc.)
 * is a basic method for pseudo-random number sampling,
 * i.e. for generating sample numbers at random from any probability distribution given its cumulative distribution function.
 * This basic idea is this:
 * to generate a random variable <i>X</i> with a cumulative distribution function <i>F(x)</i> for all <i>x</i>,
 * we first sample <i>u</i> from the uniform distribution.
 * Then, <i>x = F<sup>-1</sup>(u) = Q(u)</i>.
 * This method requires that <i>F(x)</i> has a continuous density function,
 * hence, strictly increasing and its inverse well defined.
 *
 * @author Haksun Li
 * @see
 * <ul>
 * <li>"Paul Glasserman. "p. 44," Monte Carlo Methods in Financial Engineering, 2004."
 * <li><a href="http://en.wikipedia.org/wiki/Inverse_transform_method">Wikipedia: Inverse transform sampling</a>
 * </ul>
 */
public class InverseTransformSampling implements RandomNumberGenerator {

    /** the distribution to sample random numbers from */
    private final ProbabilityDistribution distribution;
    /** a uniform random number generator */
    private final RandomLongGenerator uniform;

    /**
     * Construct a random number generator to sample from a distribution.
     *
     * @param distribution the distribution to sample from
     * @param uniform      a <em>uniform</em> random number generator that samples between 0 and 1
     */
    public InverseTransformSampling(ProbabilityDistribution distribution, RandomLongGenerator uniform) {
        this.distribution = distribution;
        this.uniform = uniform;
    }

    /**
     * Construct a random number generator to sample from a distribution.
     *
     * @param distribution the distribution to sample from
     */
    public InverseTransformSampling(ProbabilityDistribution distribution) {
        this(distribution, new UniformRng());
    }

    @Override
    public void seed(long... seeds) {
        uniform.seed(seeds);
    }

    @Override
    public double nextDouble() {
        double u = uniform.nextDouble();
        return distribution.quantile(u);
    }
}
