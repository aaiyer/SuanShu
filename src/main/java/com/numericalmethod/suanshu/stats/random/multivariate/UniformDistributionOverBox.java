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
package com.numericalmethod.suanshu.stats.random.multivariate;

import com.numericalmethod.suanshu.interval.RealInterval;
import com.numericalmethod.suanshu.stats.random.univariate.RandomLongGenerator;
import com.numericalmethod.suanshu.stats.random.univariate.uniform.UniformRng;
import java.util.Arrays;

/**
 * This random vector generator uniformly samples points over a box region.
 *
 * @author Haksun Li
 */
public class UniformDistributionOverBox implements RandomVectorGenerator {

    private final RealInterval[] bounds;
    private final RandomLongGenerator rng = new UniformRng();

    /**
     * Construct a random vector generator to uniformly sample points over a box region.
     *
     * @param bounds the feasible box region
     */
    public UniformDistributionOverBox(RealInterval... bounds) {
        this.bounds = Arrays.copyOf(bounds, bounds.length);
    }

    @Override
    public void seed(long... seeds) {
        rng.seed(seeds);
    }

    @Override
    public double[] nextVector() {
        double[] sample = new double[bounds.length];
        for (int j = 0; j < bounds.length; ++j) {
            sample[j] = rand(bounds[j].lower(), bounds[j].upper());
        }

        return sample;
    }

    private double rand(double lower, double upper) {
        return lower + (upper - lower) * rng.nextDouble();
    }
}
