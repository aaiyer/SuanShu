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
package com.numericalmethod.suanshu.stats.random.univariate.exp;

import com.numericalmethod.suanshu.stats.random.univariate.InverseTransformSampling;
import com.numericalmethod.suanshu.stats.random.univariate.RandomLongGenerator;
import com.numericalmethod.suanshu.stats.random.univariate.uniform.UniformRng;

/**
 * This is a pseudo random number generator that samples from the exponential distribution using the inverse transform sampling method.
 *
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/Exponential_distribution">Wikipedia: ExponentialDistribution distribution</a>
 */
public class InverseTransformSamplingExpRng extends InverseTransformSampling implements RandomExpGenerator {

    /**
     * Construct a random number generator to sample from the exponential distribution using the inverse transform sampling method.
     *
     * @param lambda the rate parameter
     * @param rng    a uniform random number generator
     */
    public InverseTransformSamplingExpRng(double lambda, RandomLongGenerator rng) {
        super(new com.numericalmethod.suanshu.stats.distribution.univariate.ExponentialDistribution(lambda), rng);
    }

    /**
     * Construct a random number generator to sample from the exponential distribution using the inverse transform sampling method.
     *
     * @param lambda the rate parameter
     */
    public InverseTransformSamplingExpRng(double lambda) {
        this(lambda, new UniformRng());
    }

    /**
     * Construct a random number generator to sample from the standard exponential distribution using the inverse transform sampling method.
     */
    public InverseTransformSamplingExpRng() {
        this(1d);
    }
}
