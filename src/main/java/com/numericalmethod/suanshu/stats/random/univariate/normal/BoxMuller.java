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
import static java.lang.Math.*;

/**
 * The Box–Muller transform (by George Edward Pelham Box and Mervin Edgar Muller 1958)
 * is a pseudo-random number sampling method for generating pairs of independent <em>standard</em> normally distributed
 * (zero expectation, unit variance) random numbers,
 * given a source of uniformly distributed random numbers.
 *
 * @author Haksun Li
 * @see
 * <ul>
 * <li><a href="http://en.wikipedia.org/wiki/Box-Muller_method">Wikipedia: Box–Muller transform</a>
 * <li>"G. E. P. Box, and M. E. Muller, "A note on the generation of random normal deviates," Annals of Mathematical Statistics 29:610-611, 1958."
 * </ul>
 */
public class BoxMuller implements RandomStandardNormalNumberGenerator {

    private double z1 = Double.NaN;
    private double z2 = Double.NaN;//save the second random number
    private boolean next;//is the next sample available
    private final RandomLongGenerator uniform;

    /**
     * Construct a random number generator to sample from the standard Normal distribution.
     *
     * @param uniform a uniform random number generator
     */
    public BoxMuller(RandomLongGenerator uniform) {
        this.uniform = uniform;
        generate();//generate two gaussian samples
    }

    /**
     * Construct a random number generator to sample from the standard Normal distribution.
     */
    public BoxMuller() {
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
        double u1 = uniform.nextDouble();
        double u2 = uniform.nextDouble();
        double r = -2d * log(u1);
        double v = 2d * Math.PI * u2;
        this.z1 = sqrt(r) * cos(v);
        this.z2 = sqrt(r) * sin(v);
    }
}
