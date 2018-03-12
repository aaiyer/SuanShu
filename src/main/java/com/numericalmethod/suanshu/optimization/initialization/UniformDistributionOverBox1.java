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
package com.numericalmethod.suanshu.optimization.initialization;

import com.numericalmethod.suanshu.interval.RealInterval;
import com.numericalmethod.suanshu.stats.random.multivariate.UniformDistributionOverBox;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;

/**
 * This algorithm, by sampling uniformly in each dimension,
 * generates a set of initials uniformly distributed over a box region,
 * with some degree of irregularity or randomness.
 *
 * @author Haksun Li
 */
public class UniformDistributionOverBox1 implements BuildInitials {

    private final int N;
    private final UniformDistributionOverBox rng;

    /**
     * Construct a generator to uniformly sample points over a feasible region.
     *
     * @param N      the number of initials to generate
     * @param bounds the feasible box region
     */
    public UniformDistributionOverBox1(int N, RealInterval... bounds) {
        this.N = N;
        this.rng = new UniformDistributionOverBox(bounds);
    }

    /**
     * Seed the random number generator to produce repeatable sequences.
     *
     * @param seeds the seeds
     */
    public void seed(long... seeds) {
        rng.seed(seeds);
    }

    @Override
    public Vector[] getInitials(Vector... notused) {//initials not used
        Vector[] initials = new Vector[N];

        for (int i = 0; i < N; ++i) {
            initials[i] = new DenseVector(rng.nextVector());
        }

        return initials;
    }

    /**
     * Generate a set of initial points for optimization.
     *
     * @return a full set of initial points
     */
    public Vector[] getInitials() {
        return getInitials(new Vector[]{});
    }
}
