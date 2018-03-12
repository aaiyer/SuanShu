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
package com.numericalmethod.suanshu.stats.random.univariate.normal;

import com.numericalmethod.suanshu.stats.random.univariate.RandomLongGenerator;
import com.numericalmethod.suanshu.stats.random.univariate.uniform.UniformRng;
import static java.lang.Math.log;
import static java.lang.Math.sqrt;

/**
 * The polar method (attributed to George Marsaglia, 1964) is a pseudo-random number sampling method for generating a pair of independent <em>standard</em> normal random variables.
 * This is an acceptance-rejection method so there is no upper-bound on the number of uniforms it may use to generate a sample.
 * Hence, this method is not applicable to quasi-Monte Carlo simulation.
 *
 * @author Haksun Li
 * @see
 * <ul>
 * <li><a href="http://en.wikipedia.org/wiki/Marsaglia_polar_method">Wikipedia: Marsaglia polar method</a>
 * <li>"G. Marsaglia, and T. A. Bray, "A convenient method for generating normal variates," SIAM Review 6:260-264, 1964."
 * </ul>
 */
public class MarsagliaBray1964 implements RandomStandardNormalNumberGenerator {

    private double z1 = Double.NaN;
    private double z2 = Double.NaN;//save the second random number
    private boolean next;//is the next sample available
    private final RandomLongGenerator uniform;

    /**
     * Construct a random number generator to sample from the standard Normal distribution.
     *
     * @param uniform a uniform random number generator
     */
    public MarsagliaBray1964(RandomLongGenerator uniform) {
        this.uniform = uniform;
        generate();//generate two gaussian samples
    }

    /**
     * Construct a random number generator to sample from the standard Normal distribution.
     */
    public MarsagliaBray1964() {
        this(new UniformRng());
    }

    @Override
    public void seed(long... seeds) {
        uniform.seed(seeds);
    }

    @Override
    public double nextDouble() {
        if (!next) {
            generate();
            return z1;
        } else {
            next = false;
            return z2;
        }
    }

    private void generate() {
        double x = Double.MAX_VALUE;
        double u1, u2;
        do {
            u1 = uniform.nextDouble();
            u2 = uniform.nextDouble();
            u1 = 2 * u1 - 1d;
            u2 = 2 * u2 - 1d;
            x = u1 * u1 + u2 * u2;
        } while (x > 1);

        double y = sqrt(-2d * log(x) / x);
        this.z1 = u1 * y;
        this.z2 = u2 * y;
    }
}
