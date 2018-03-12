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
package com.numericalmethod.suanshu.stats.random.univariate.uniform.linear;

import java.util.Random;

/**
 * This is the uniform random number generator recommended by L'Ecuyer in 1996.
 * The generator combines two MRGs.
 * It has a period of approximately 2<sup>185</sup>.
 * The first MRG has a modulus {@code m = 2^31-1 = 2147483647}, and
 * {@code a[] = {0, 63308, -183326}}.
 * The second has a modulus {@code m = 2145483479}, and
 * {@code a[] = {86098, 0, -539608}}.
 *
 * @author Haksun Li
 * @see "P. L'Ecuyer, "Combined multiple recursive random number generators," Operations Research 44:816-822, 1996."
 */
public class LEcuyer implements LinearCongruentialGenerator {

    private final CompositeLinearCongruentialGenerator rng;
    private final MRG mrg1, mrg2;
    private final long m;//the modulus

    /**
     * Construct a LEcuyer pseudo uniform random generator.
     */
    public LEcuyer() {
        Random jvmRandom = new Random();
        long seed11 = jvmRandom.nextLong();
        long seed12 = jvmRandom.nextLong();
        long seed13 = jvmRandom.nextLong();
        long seed21 = jvmRandom.nextLong();
        long seed22 = jvmRandom.nextLong();
        long seed23 = jvmRandom.nextLong();

        mrg1 = new MRG(2147483647, new long[]{0, 63308, -183326});
        mrg1.seed(new long[]{seed11 % 2147483647, seed12 % 2147483647, seed13 % 2147483647});
        mrg2 = new MRG(2145483479, new long[]{86098, 0, -539608});
        mrg2.seed(new long[]{seed21 % 2147483647, seed22 % 2147483647, seed23 % 2147483647});
        this.rng = new CompositeLinearCongruentialGenerator(new LinearCongruentialGenerator[]{mrg1, mrg2});
        this.m = rng.modulus();
    }

    /**
     * Construct a LEcuyer pseudo uniform random generator and then seed.
     *
     * @param seed11 a seed
     * @param seed12 a seed
     * @param seed13 a seed
     * @param seed21 a seed
     * @param seed22 a seed
     * @param seed23 a seed
     */
    public LEcuyer(long seed11, long seed12, long seed13, long seed21, long seed22, long seed23) {
        this();
        seed(seed11, seed12, seed13, seed21, seed22, seed23);
    }

    /**
     * {@inheritDoc}
     * <p/>
     * If there are not enough, i.e., fewer than 6, seeds, we recycle them.
     * If there are more, we take the first 6 and ignore the rest.
     *
     * @param seeds an array of seeds
     */
    @Override
    public void seed(long... seeds) {
        long[] copy = new long[6];
        for (int i = 0; i < 6; ++i) {
            copy[i] = seeds[i % seeds.length];

        }

        rng.seed(copy);
    }

    @Override
    public int order() {
        return this.rng.order();
    }

    @Override
    public long modulus() {
        return m;
    }

    @Override
    public long nextLong() {
        return rng.nextLong();
    }

    @Override
    public double nextDouble() {
        double x = nextLong();
        x /= m;
        return x;
    }
}
