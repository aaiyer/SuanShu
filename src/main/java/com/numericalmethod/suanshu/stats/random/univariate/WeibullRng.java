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

import com.numericalmethod.suanshu.stats.random.univariate.uniform.UniformRng;

/**
 * This random number generator samples from the Weibull distribution using the inverse transform sampling method.
 *
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/Weibull_distribution">Wikipedia: WeibullDistribution distribution</a>
 */
public class WeibullRng extends InverseTransformSampling {

    /**
     * Construct a random number generator to sample from the Weibull distribution.
     *
     * @param lambda the scale parameter
     * @param k      the shape parameter
     * @param rng    a uniform random number generator
     */
    public WeibullRng(double lambda, double k, RandomLongGenerator rng) {
        super(new com.numericalmethod.suanshu.stats.distribution.univariate.WeibullDistribution(lambda, k), rng);
    }

    /**
     * Construct a random number generator to sample from the Weibull distribution.
     */
    public WeibullRng() {
        this(1d, 1d, new UniformRng());
    }
}
