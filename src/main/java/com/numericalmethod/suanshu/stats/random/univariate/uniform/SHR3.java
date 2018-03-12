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
 * SHR3 is a 3-shift-register generator with period 2^32-1.
 * It uses <i>y(n)=y(n-1)(I+L^17)(I+R^13)(I+L^5)</i>,
 * with the <i>y</i>'s viewed as binary vectors,
 * <i>L</i> the 32x32 binary matrix that shifts a vector left 1,
 * and <i>R</i> its transpose.
 * SHR3 seems to pass all tests except those related to the binary rank test,
 * since 32 successive values, as binary vectors, must be linearly independent,
 * while 32 successive truly random 32-bit integers, viewed as binary vectors, will be linearly independent only about 29% of the time.
 *
 * @author Haksun Li
 */
public class SHR3 implements RandomLongGenerator {

    private int jsr = (int) (8682522807148012L + System.nanoTime());//seed
    private SHR0 shr0 = new SHR0();

    @Override
    public void seed(long... seeds) {
        jsr = (int) seeds[0];
        shr0.seed(seeds);
    }

    @Override
    public long nextLong() {
        return nextInt();
    }

    @Override
    public double nextDouble() {
        return 0.5 * (1. + (double) nextLong() * 0.2328306e-9);//add 1 to the original code to make it "unsigned"
    }

    public int nextInt() {
        int jz = jsr;
        jsr = shr0.nextInt();
        return jz + jsr;
    }
}
