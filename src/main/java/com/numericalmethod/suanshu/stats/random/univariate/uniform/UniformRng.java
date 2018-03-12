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
package com.numericalmethod.suanshu.stats.random.univariate.uniform;

import com.numericalmethod.suanshu.stats.random.univariate.RandomLongGenerator;
import com.numericalmethod.suanshu.stats.random.univariate.uniform.linear.LEcuyer;
import com.numericalmethod.suanshu.stats.random.univariate.uniform.linear.Lehmer;

/**
 * A pseudo uniform random number generator samples numbers from the unit interval, <i>[0, 1]</i>,
 * in such a way that there are equal probabilities of them falling in any same length sub-interval.
 * Sampling from the unit interval is the most basic building block of most pseudo random number generation algorithms.
 *
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/Uniform_distribution_%28continuous%29">Wikipedia: UniformRng distribution (continuous)</a>
 */
public class UniformRng implements RandomLongGenerator {

    /**
     * the pseudo uniform random number generators available
     */
    public static enum Method {

        /** Mersenne Twister (recommended) */
        MERSENNE_TWISTER,
        /** Lehmer */
        LEHMER,
        /** Lecuyer */
        LECUYER
    }

    private final Method method;
    private RandomLongGenerator uniform;

    /**
     * Construct a pseudo uniform random number generator.
     *
     * @param method the uniform random number generation algorithm to use
     */
    public UniformRng(Method method) {
        this.method = method;
        switch (this.method) {
            case LEHMER:
                uniform = new Lehmer();
                break;
            case LECUYER:
                uniform = new LEcuyer();
                break;
            case MERSENNE_TWISTER:
            default:
                uniform = new MersenneTwister();
                break;
        }
    }

    /**
     * Construct a pseudo uniform random number generator.
     */
    public UniformRng() {
        this(Method.MERSENNE_TWISTER);
    }

    @Override
    public void seed(long... seeds) {
        uniform.seed(seeds);
    }

    @Override
    public double nextDouble() {
        return uniform.nextDouble();
    }

    @Override
    public long nextLong() {
        return uniform.nextLong();
    }
}
