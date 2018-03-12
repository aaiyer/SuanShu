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
 * Mersenne Twister is one of the best pseudo random number generators available.
 * It was invented by Makoto Matsumoto and Takuji Nishimura in 1997.
 * This implementation is the MT19937 version that produces uniformly 32-bit integers.
 * It is by-and-large a Java translation of the 2002-01-26 version of
 * the C by Makoto Matsumoto and Takuji Nishimura.
 * <p/>
 * Here is <em>their</em> copyright notice.
 * <pre>
 *    A C-program for MT19937, with initialization improved 2002/1/26.
 *  Coded by Takuji Nishimura and Makoto Matsumoto.
 *
 *  Before using, initialize the state by using init_genrand(seed)
 *  or init_by_array(init_key, key_length).
 *
 *  Copyright (C) 1997 - 2002, Makoto Matsumoto and Takuji Nishimura,
 *  All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions
 *  are met:
 *
 *    1. Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *
 *    2. Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *
 *    3. The names of its contributors may not be used to endorse or promote
 *       products derived from this software without specific prior written
 *       permission.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 *  "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 *  LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 *  A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 *  CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 *  EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 *  PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 *  PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 *  LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 *  NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *  SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 *
 *  Any feedback is very welcome.
 *  http://www.math.sci.hiroshima-u.ac.jp/~m-mat/MT/emt.html
 *  email: m-mat @ math.sci.hiroshima-u.ac.jp (remove space)
 * </pre>
 *
 * @author Haksun Li
 * @see
 * <ul>
 * <li><a href="http://www.math.sci.hiroshima-u.ac.jp/~m-mat/MT/MT2002/CODES/mt19937ar.c">The original C code</a>
 * <li><a href="http://en.wikipedia.org/wiki/Mersenne_twister">Wikipedia: Mersenne twister</a>
 * </ul>
 */
public class MersenneTwister implements RandomLongGenerator {

    //Magic constants for Mersenne Twister
    private static final int LENGTH = 624;
    private static final int OFFSET = 397;
    private static final int MATRIX_A = 0x9908b0df;
    private static final int[] MAG01 = {0x0, MATRIX_A};
    /** most significant w-r bits */
    private static final int UPPER_MASK = 0x80000000;
    /** least significant r bits */
    private static final int LOWER_MASK = 0x7fffffff;
    /** the array for the state vector */
    private int[] mt = new int[LENGTH];
    /** current index in mt[]; mti==LENGTH+1 means mt is not initialized */
    private int mti = LENGTH + 1;

    /**
     * Construct a random number generator to sample uniformly from [0, 1].
     */
    public MersenneTwister() {
        seed(8682522807148012L + System.nanoTime());
    }

    /**
     * Construct a random number generator to sample uniformly from [0, 1].
     *
     * @param seeds the seeds
     */
    public MersenneTwister(long... seeds) {
        seed(seeds);
    }

    @Override
    public void seed(long... seeds) {
        if (seeds.length == 1) {
            seedByValue(seeds[0]);
        } else {
            seedByArray(seeds);
        }
    }

    @Override
    public long nextLong() {
        int rand = next(32);
        long randL = unsignedLong(rand);
        return randL;
    }

    @Override
    public double nextDouble() {
        return nextLong() / 4294967295.0;//divided by 2^32-1
    }

    private void seedByValue(long seed) {
        mt[0] = (int) seed;

        long mtlong = seed;
        for (int i = 1; i < LENGTH; ++i) {
            //See Knuth TAOCP Vol2. 3rd Ed. P.106 for multiplier
            mtlong = (1812433253L * (mtlong ^ (mtlong >> 30)) + i);
            mtlong &= 0xffffffffL;//TODO: is this necessary?
            mt[i] = (int) mtlong;
        }

        mti = LENGTH + 1;
    }

    private void seedByArray(long... seeds) {
        seedByValue(19650218L);

        int i = 1, j = 0, k = Math.max(LENGTH, seeds.length);
        for (; k > 0; --k) {
            long lmt = (unsignedLong(mt[i]) ^ ((unsignedLong(mt[i - 1])
                                                ^ (unsignedLong(mt[i - 1]) >> 30)) * 1664525l)) + seeds[j] + j;//non-linear
            mt[i] = (int) (lmt & 0xffffffffL);
            i++;
            j++;
            if (i >= LENGTH) {
                mt[0] = mt[LENGTH - 1];
                i = 1;
            }
            if (j >= seeds.length) {
                j = 0;
            }
        }

        for (k = LENGTH - 1; k > 0; --k) {
            long mtlong = (unsignedLong(mt[i]) ^ ((unsignedLong(mt[i - 1])
                                                   ^ (unsignedLong(mt[i - 1]) >> 30)) * 1566083941l)) - i;//non-linear
            mt[i] = (int) (mtlong & 0xffffffffL);
            i++;
            if (i >= LENGTH) {
                mt[0] = mt[LENGTH - 1];
                i = 1;
            }
        }

        mt[0] = 0x80000000;//MSB is 1; assuring non-zero initial array
        mti = LENGTH + 1;
    }

    private int next(int bits) {
        int y;

        if (mti >= LENGTH) {//generate all words at one time
            int k;

            for (k = 0; k < LENGTH - OFFSET; ++k) {
                y = (mt[k] & UPPER_MASK) | (mt[k + 1] & LOWER_MASK);
                mt[k] = mt[k + OFFSET] ^ (y >>> 1) ^ MAG01[y & 0x1];
            }

            for (k = (LENGTH - OFFSET); k < LENGTH - 1; ++k) {
                y = (mt[k] & UPPER_MASK) | (mt[k + 1] & LOWER_MASK);
                mt[k] = mt[k + (OFFSET - LENGTH)] ^ (y >>> 1) ^ MAG01[y & 0x1];
            }

            y = (mt[LENGTH - 1] & UPPER_MASK) | (mt[0] & LOWER_MASK);
            mt[LENGTH - 1] = mt[OFFSET - 1] ^ (y >>> 1) ^ MAG01[y & 0x1];

            mti = 0;
        }

        y = mt[mti++];

        //tempering
        y ^= (y >>> 11);
        y ^= (y << 7) & 0x9d2c5680L;
        y ^= (y << 15) & 0xefc60000L;
        y ^= (y >>> 18);

        return y >>> (32 - bits);
    }

    private long unsignedLong(int v) {//TODO: move this to some Util?
        return (v & 0x7fffffffL) | ((v < 0) ? 0x80000000L : 0x0L);
    }
}
