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
package com.numericalmethod.suanshu.stats.random.univariate.poisson;

import com.numericalmethod.suanshu.stats.random.univariate.RandomLongGenerator;
import com.numericalmethod.suanshu.stats.random.univariate.RandomNumberGenerator;
import com.numericalmethod.suanshu.stats.random.univariate.uniform.UniformRng;
import static java.lang.Math.exp;

/**
 * This is a random number generator that generates random deviates according to the Poisson distribution.
 *
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/Poisson_distribution#Generating_Poisson-distributed_random_variables">Wikipedia: Generating Poisson-distributed random variables</a>
 */
public class Knuth1969 implements RandomNumberGenerator {

    private final RandomLongGenerator uniform;
//    private final double lambda;
    private final double L;

    /**
     * Construct a random number generator to sample from the Poisson distribution.
     *
     * @param lambda  the shape parameter
     * @param uniform a uniform random number generator
     */
    public Knuth1969(double lambda, RandomLongGenerator uniform) {
        this.uniform = uniform;
//        this.lambda = lambda;
        this.L = exp(-lambda);
    }

    /**
     * Construct a random number generator to sample from the Poisson distribution.
     *
     * @param lambda the shape parameter
     */
    public Knuth1969(double lambda) {
        this(lambda, new UniformRng());
    }

    @Override
    public void seed(long... seeds) {
        uniform.seed(seeds);
    }

    @Override
    public double nextDouble() {
        int k = 0;
        double p = 1;

        do {
            ++k;
            double u = uniform.nextDouble();
            p *= u;

        } while (p > L);


        return k - 1;
    }
}
