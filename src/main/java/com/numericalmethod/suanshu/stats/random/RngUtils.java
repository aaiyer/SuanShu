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
package com.numericalmethod.suanshu.stats.random;

import com.numericalmethod.suanshu.stats.random.multivariate.RandomVectorGenerator;
import com.numericalmethod.suanshu.stats.random.univariate.RandomLongGenerator;
import com.numericalmethod.suanshu.stats.random.univariate.RandomNumberGenerator;

/**
 * This class provides static methods that wraps random number generators to
 * produce synchronized generators.
 * For example,
 * <blockquote><code>
 * RandomNumberGenerator rng = RngUtils.synchronizedRNG(new Gaussian());
 * </code></blockquote>
 * or
 * <blockquote><code>
 * RandomLongGenerator rlg = RngUtils.synchronizedRLG(new MersenneTwister());
 * </code></blockquote>
 *
 * @author Ken Yiu
 */
public class RngUtils {

    private RngUtils() {
        // no constructor for utility
    }

    /**
     * Return a synchronized (thread-safe) {@link RandomNumberGenerator}
     * backed by a specified generator.
     *
     * @param rng the generator to be "wrapped" in a synchronized generator
     * @return a synchronized generator
     */
    public static RandomNumberGenerator synchronizedRNG(final RandomNumberGenerator rng) {
        return new RandomNumberGenerator() {

            @Override
            public synchronized void seed(long... seeds) {
                rng.seed(seeds);
            }

            @Override
            public synchronized double nextDouble() {
                return rng.nextDouble();
            }
        };
    }

    /**
     * Return a synchronized (thread-safe) {@link RandomLongGenerator}
     * backed by a specified generator.
     *
     * @param rng the generator to be "wrapped" in a synchronized generator
     * @return a synchronized generator
     */
    public static RandomLongGenerator synchronizedRLG(final RandomLongGenerator rng) {
        return new RandomLongGenerator() {

            @Override
            public synchronized void seed(long... seeds) {
                rng.seed(seeds);
            }

            @Override
            public synchronized double nextDouble() {
                return rng.nextDouble();
            }

            @Override
            public synchronized long nextLong() {
                return rng.nextLong();
            }
        };
    }

    /**
     * Return a synchronized (thread-safe) {@link RandomVectorGenerator}
     * backed by a specified generator.
     *
     * @param rng the generator to be "wrapped" in a synchronized generator
     * @return a synchronized generator
     */
    public static RandomVectorGenerator synchronizedRVG(final RandomVectorGenerator rng) {
        return new RandomVectorGenerator() {

            @Override
            public synchronized void seed(long... seeds) {
                rng.seed(seeds);
            }

            @Override
            public synchronized double[] nextVector() {
                return rng.nextVector();
            }
        };
    }
}
