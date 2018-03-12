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

import com.numericalmethod.suanshu.misc.SuanShuUtils;

/**
 * A Multiple Recursive Generator (MRG) is a linear congruential generator which takes this form:
 * <blockquote><pre><i>
 * x<sub>i</sub> = (a<sub>1</sub> * x<sub>i-1</sub> + a<sub>2</sub> * x<sub>i-2</sub> + ... + a<sub>k</sub> * x<sub>i-k</sub>) mod m
 * u<sub>i</sub> = x<sub>i</sub> / m
 * </i></pre></blockquote>
 *
 * @author Haksun Li
 * @see "Paul Glasserman, "p. 44," Monte Carlo Methods in Financial Engineering, 2004."
 */
public class MRG implements LinearCongruentialGenerator {

    private Lehmer[] rng;
    private boolean[] sign;
    private long[] x = null;//past values of the rngs
    private final long m;//the modulus

    /**
     * Construct a Multiple Recursive Generator.
     *
     * @param m the modulus
     * @param a multipliers for the Lehmer rng (a {@code long})
     */
    public MRG(long m, long... a) {
        this.m = m;
        this.rng = new Lehmer[a.length];
        this.sign = new boolean[a.length];

        long[] seeds = new long[a.length];
        for (int i = 0; i < a.length; ++i) {
            if (a[i] != 0) {
                rng[i] = new Lehmer(Math.abs(a[i]), m, 0);
                sign[i] = a[i] > 0 ? true : false;
            }

            seeds[i] = 8682522807148012L + System.nanoTime();
        }

        seed(seeds);
    }

    @Override
    public void seed(long... x) {
        SuanShuUtils.assertArgument(x.length == rng.length, "unmatched number of seeds");

        this.x = new long[x.length];
        for (int i = 0; i < rng.length; ++i) {
            this.x[i] = x[i];
            if (rng[i] != null) {
                rng[i].seed(x[i]);
            }
        }
    }

    @Override
    public int order() {
        return rng.length;
    }

    @Override
    public long modulus() {
        return m;
    }

    @Override
    public long nextLong() {//TODO: optimize this to make this run faster
        SuanShuUtils.assertOrThrow(x != null ? null : new RuntimeException("the generator has not been seeded"));
//        assert x != null : "the generated has not been seeded";
//        SuanShuUtils.assertTrue(x != null, new RuntimeException("the generated has not been seeded"));

        long result = 0;
        for (int i = rng.length - 1; i >= 0; --i) {
            if (rng[i] == null) {
                continue;//for a == 0, nothing to add
            }

            /*
             * x<sub>i</sub> =
             * (a<sub>1</sub> * x<sub>i-1</sub> + a<sub>2</sub> * x<sub>i-2</sub> + ... + a<sub>k</sub> * x<sub>i-k</sub>) mod m
             */
            result = sign[i] ? result + rng[i].nextLong() : result - rng[i].nextLong();
            while (result < 0) {
                result += m;//make it positive if it falls below 0, keeping the same modulo value
            }
            if (result > m) {
                result %= m;//avoid overflow
            }
        }

        //play musical chair for x<sub>i</sub>'s
        for (int i = rng.length - 1; i >= 0; --i) {//TODO: this can be optimized
            x[i] = i == 0 ? result : x[i - 1];
            if (rng[i] != null) {
                rng[i].seed(x[i]);
            }
        }

        return result;
    }

    @Override
    public double nextDouble() {
        return ((double) nextLong()) / ((double) m);
    }
}
