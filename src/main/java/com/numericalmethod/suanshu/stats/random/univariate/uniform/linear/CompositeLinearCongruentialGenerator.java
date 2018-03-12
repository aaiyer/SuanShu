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
import java.util.Arrays;

/**
 * A composite generator combines a number of simple {@link LinearCongruentialGenerator}, such as {@link Lehmer},
 * to form one longer period generator by first summing values and then taking modulus.
 * The resultant generator in general has a superior uniformity and a longer period,
 * without overflowing the arithmetics.
 *
 * @author Haksun Li
 */
public class CompositeLinearCongruentialGenerator implements LinearCongruentialGenerator {

    private LinearCongruentialGenerator[] rng;
    private final long m;

    /**
     * Construct a linear congruential generator from some simpler and shorter modulus generators.
     *
     * @param rng simpler and shorter modulus linear congruential generators
     */
    public CompositeLinearCongruentialGenerator(LinearCongruentialGenerator[] rng) {
        SuanShuUtils.assertArgument(rng.length > 0, "no random generator was found");

        this.rng = rng;

        long maxM = 0;
        for (int i = 0; i < rng.length; ++i) {
            if (rng[i].modulus() > maxM) {
                maxM = rng[i].modulus();
            }
        }

        m = maxM;
    }

    @Override
    public void seed(long... seeds) {
        for (int i = 0, from = 0; i < rng.length; ++i) {
            long[] seeds4Rng = Arrays.copyOfRange(seeds, from, from + rng[i].order());
            rng[i].seed(seeds4Rng);
            from += rng[i].order();
        }
    }

    @Override
    public int order() {
        int result = 0;
        for (LinearCongruentialGenerator lcg : rng) {
            result += lcg.order();
        }

        return result;
    }

    @Override
    public long modulus() {
        return m;
    }

    @Override
    public long nextLong() {
        long x = 0;
        for (int i = 0; i < rng.length; ++i) {
            long rand = rng[i].nextLong();
            if (i % 2 == 1) {
                x -= rand;
            } else {
                x += rand;
            }

            while (x < 0) {
                //make it positive if it falls below 0, keeping the same modulo value
                x += m;
            }
            if (x > m) {
                //avoid overflow
                x %= m;
            }
        }

        return x;
    }

    @Override
    public double nextDouble() {
        long x = nextLong();

        double value;
        if (x > 0) {
            value = (double) x / (double) (m - 1);//TODO: why not divide by m, e.g., for normalizing between 0 and 1?
        } else if (x == 0) {
            value = (double) (m - 1) / (double) m;
        } else {
            throw new RuntimeException("-ve. x");
        }

        return value;
    }
}
