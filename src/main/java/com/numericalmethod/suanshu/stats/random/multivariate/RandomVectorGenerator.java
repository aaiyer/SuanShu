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
package com.numericalmethod.suanshu.stats.random.multivariate;

/**
 * A (pseudo) multivariate random number generator samples a random vector from a multivariate distribution.
 * The elements in a vector can be correlated.
 *
 * @author Haksun Li
 * @see
 * <ul>
 * <li><a href="http://en.wikipedia.org/wiki/Random_vector">Wikipedia: Multivariate random variable</a>
 * <li><a href="http://en.wikipedia.org/wiki/Random_number_generation">Wikipedia: Random number generation</a>
 * <li><a href="http://en.wikipedia.org/wiki/Pseudorandom_number_generator">Wikipedia: Pseudorandom number generator</a>
 * </ul>
 */
public interface RandomVectorGenerator {

    /**
     * Seed the random vector generator to produce repeatable sequences.
     *
     * @param seeds the seeds
     */
    public void seed(long... seeds);

    /**
     * Get the next random vector.
     *
     * @return the next random vector
     */
    public double[] nextVector();
}
