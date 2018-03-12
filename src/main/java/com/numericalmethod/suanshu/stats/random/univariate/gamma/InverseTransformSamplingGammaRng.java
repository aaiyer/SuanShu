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
package com.numericalmethod.suanshu.stats.random.univariate.gamma;

import com.numericalmethod.suanshu.stats.random.univariate.InverseTransformSampling;
import com.numericalmethod.suanshu.stats.random.univariate.RandomLongGenerator;
import com.numericalmethod.suanshu.stats.random.univariate.uniform.UniformRng;

/**
 * This is a pseudo random number generator that samples from the gamma distribution using the inverse transform sampling method.
 * This method is very slow and much better methods exist.
 *
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/Gamma_distribution">Wikipedia: Gamma distribution distribution</a>
 * @deprecated There exist much more efficient algorithms.
 */
@Deprecated
public class InverseTransformSamplingGammaRng extends InverseTransformSampling implements RandomGammaGenerator {

    /**
     * Construct a random number generator to sample from the gamma distribution using the inverse transform sampling method.
     *
     * @param k     the shape parameter
     * @param theta the scale parameter
     * @param rng   a uniform random number generator
     */
    public InverseTransformSamplingGammaRng(double k, double theta, RandomLongGenerator rng) {
        super(new com.numericalmethod.suanshu.stats.distribution.univariate.GammaDistribution(k, theta), rng);
    }

    /**
     * Construct a random number generator to sample from the standard gamma distribution using the inverse transform sampling method.
     */
    public InverseTransformSamplingGammaRng() {
        this(1d, 1d, new UniformRng());
    }
}
