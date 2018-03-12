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
 * SHR0 is a simple uniform random number generator.
 *
 * @author Haksun Li
 * @see <a href="http://arxiv.org/PS_cache/math/pdf/0603/0603058v1.pdf">Boaz Nadler. "Design Flaws in the Implementation of the Ziggurat and Monty Python methods (and some remarks on Matlab randn)," The Journal of Business. arXiv:math/0603058v1. 2 March 2006.</a>
 */
public class SHR0 implements RandomLongGenerator {

    private int jzr = (int) (123456789 + System.nanoTime());//seed

    @Override
    public void seed(long... seeds) {
        jzr = (int) seeds[0];
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
        jzr ^= (jzr << 13);
        jzr ^= (jzr >>> 17);
        jzr ^= (jzr << 5);
        return jzr;
    }
}
