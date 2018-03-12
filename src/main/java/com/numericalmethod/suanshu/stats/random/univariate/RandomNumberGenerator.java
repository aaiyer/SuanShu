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

/**
 * A (pseudo) random number generator is an algorithm designed to generate a sequence of numbers that lack any pattern.
 * However, it is very important to know that the sequence is not random at all and
 * that it is completely determined by a relatively small set of initial values.
 * Knowing the generation algorithm and the states can predict the next value, as the values are generated in a deterministic way.
 * <p/>
 * By default, an implementation of {@code RandomNumberGenerator} is
 * <em>not thread-safe</em>, and thus should not be shared among multiple threads.
 * If a {@code RandomNumberGenerator} instance is used in a multi-threaded
 * program, for example, use
 * <blockquote><code>
 * RandomNumberGenerator rng = RandomNumberGenerators.synchronizedRNG(new Gaussian());
 * </code></blockquote>
 *
 * @author Haksun Li
 * @see
 * <ul>
 * <li><a href="http://en.wikipedia.org/wiki/Random_number_generation">Wikipedia: Random number generation</a>
 * <li><a href="http://en.wikipedia.org/wiki/Pseudorandom_number_generator">Wikipedia: Pseudorandom number generator</a>
 * </ul>
 */
public interface RandomNumberGenerator {

    /**
     * Seed the random number generator to produce repeatable sequences.
     *
     * @param seeds the seeds
     */
    public void seed(long... seeds);

    /**
     * Get the next random {@code double}.
     *
     * @return the next random number
     */
    public double nextDouble();
}
