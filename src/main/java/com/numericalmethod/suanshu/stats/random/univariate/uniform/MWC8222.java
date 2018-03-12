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

/**
 * Marsaglia's MWC256 (also known as MWC8222) is a multiply-with-carry generator.
 * It has a period of 2^8222 and fares well in tests of randomness.
 * It is also extremely fast, between 2 and 3 times faster than the Mersenne Twister.
 *
 * @author Haksun Li
 */
public class MWC8222 implements RandomLongGenerator {

    private static final double M_RAN_INVM32 = 2.32830643653869628906e-010;// 1.0 / 2^32
    private static final int MWC_R = 256;
    private static final long MWC_A = 809430660;
    private static final int MWC_AI = 809430660;
    private static final int MWC_C = 362436;
    private int s_uiStateMWC = MWC_R - 1;
    private int s_uiCarryMWC = MWC_C;
    private long[] s_auiStateMWC = new long[MWC_R];
    private long uiMin = 0;// TODO: made parameter

    /**
     * Construct a random number generator to sample uniformly from [0, 1].
     */
    public MWC8222() {
        seed(8682522807148012L + System.nanoTime());
    }

    @Override
    public void seed(long... seeds) {
        long uiSeed = seeds[0];

        int i;
        /* may be 0 */
        long s = uiSeed;

        /* see Knuth p.106, Table 1(16) and Numerical Recipes p.284 (ranqd1) */
        for (i = 0; i < MWC_R;) {
            s = 1664525 * s + 1013904223;
            if (s <= uiMin) {
                continue;
            }
            s_auiStateMWC[i] = s;
            ++i;
        }
    }

    @Override
    public long nextLong() {
        long t;
        s_uiStateMWC = (s_uiStateMWC + 1) & (MWC_R - 1);
        t = MWC_A * s_auiStateMWC[s_uiStateMWC] + s_uiCarryMWC;
        s_uiCarryMWC = (int) (t >> 32);
        s_auiStateMWC[s_uiStateMWC] = (int) t;
        return (int) t;
    }

    @Override
    public double nextDouble() {
        long t;
        s_uiStateMWC = (s_uiStateMWC + 1) & (MWC_R - 1);
        t = MWC_A * s_auiStateMWC[s_uiStateMWC] + s_uiCarryMWC;
        s_uiCarryMWC = (int) (t >> 32);
        s_auiStateMWC[s_uiStateMWC] = (int) t;
        return RANDBL_32new(t);
    }

    private static double RANDBL_32new(long iRan1) {
        return (((int) (iRan1)) * M_RAN_INVM32 + (0.5 + M_RAN_INVM32 / 2));
    }
}
