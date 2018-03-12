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

import com.numericalmethod.suanshu.stats.random.univariate.uniform.UniformRng;

/**
 * This random number generator samples from the binomial distribution.
 * <p/>
 * The R equivalent class are {@code dbinom, pbinom, qbinom, rbinom}.
 *
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/Binomial_distribution">Wikipedia: Binomial distribution</a>
 */
public class BinomialRng implements RandomNumberGenerator {

    /** the number of trials, a natural number */
    private final int n;
    /** the success probability in each trial, [0, 1] */
    private final double p;
    private final RandomLongGenerator uniform = new UniformRng();

    /**
     * Construct a random number generator to sample from the binomial distribution.
     *
     * @param n the number of trials, a natural number
     * @param p the success probability in each trial, [0, 1]
     */
    public BinomialRng(int n, double p) {
        this.n = n;
        this.p = p;
    }

    @Override
    public void seed(long... seeds) {
        uniform.seed(seeds);
    }

    @Override
    public double nextDouble() {
        int count = 0;

        for (int i = 0; i < n; ++i) {
            double x = uniform.nextDouble();
            if (x < p) {
                ++count;
            }
        }

        return count;
    }
}
