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

import com.numericalmethod.suanshu.algorithm.Combination;
import com.numericalmethod.suanshu.interval.RealInterval;
import com.numericalmethod.suanshu.misc.R;
import com.numericalmethod.suanshu.misc.SuanShuUtils;
import com.numericalmethod.suanshu.number.DoubleUtils;
import com.numericalmethod.suanshu.stats.random.univariate.RandomLongGenerator;
import com.numericalmethod.suanshu.stats.random.univariate.uniform.UniformRng;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import java.util.List;

/**
 * This algorithm, by perturbing each grid point by a small random scale,
 * generates a set of initials uniformly distributed over a box region,
 * with some degree of irregularity or randomness.
 *
 * @author Haksun Li
 */
public class UniformDistributionOverBox2 implements BuildInitials {

    private final double scale;
    private final RandomLongGenerator rng;
    private final Double[][] grid;
    private final int N;

    /**
     * Construct a generator to uniformly sample points over a feasible region.
     *
     * @param scale           the small percentage of disturbance, e.g., 10%
     * @param bounds          the feasible box region
     * @param discretizations the number of discretization levels in each dimension (bound)
     * @param rng             a uniform random number generator
     */
    public UniformDistributionOverBox2(double scale, RealInterval[] bounds, int[] discretizations, RandomLongGenerator rng) {
        SuanShuUtils.assertArgument(bounds.length == discretizations.length, "number of bounds = number of discretizations");

        this.scale = scale;
        this.rng = rng;

        int N = 1;
        this.grid = new Double[bounds.length][];
        for (int i = 0; i < bounds.length; ++i) {
            this.grid[i] = new Double[discretizations[i] - 1];
            N *= this.grid[i].length;

            double step = (bounds[i].upper() - bounds[i].lower());
            step /= discretizations[i];
            for (int j = 0; j < discretizations[i] - 1; ++j) {
                this.grid[i][j] = bounds[i].lower() + (j + 1) * step;
            }
        }

        this.N = N;
    }

    /**
     * Construct a generator to uniformly sample points over a feasible region.
     *
     * @param scale           the small percentage of disturbance, e.g., 10%
     * @param bounds          the feasible box region
     * @param discretizations the number of discretization in each dimension (bound)
     */
    public UniformDistributionOverBox2(double scale, RealInterval[] bounds, int[] discretizations) {
        this(scale, bounds, discretizations, new UniformRng());
    }

    /**
     * Construct a generator to uniformly sample points over a feasible region.
     *
     * @param scale          the small percentage of disturbance, e.g., 10%
     * @param bounds         the feasible box region
     * @param discretization the number of discretization in all dimensions (bounds)
     * @param rng            a uniform random number generator
     */
    public UniformDistributionOverBox2(double scale, RealInterval[] bounds, int discretization, RandomLongGenerator rng) {
        this(scale, bounds, R.rep(discretization, bounds.length), rng);
    }

    /**
     * Construct a generator to uniformly sample points over a feasible region.
     *
     * @param scale          the small percentage of disturbance, e.g., 10%
     * @param bounds         the feasible box region
     * @param discretization the number of discretization in all dimensions (bounds)
     */
    public UniformDistributionOverBox2(double scale, RealInterval[] bounds, int discretization) {
        this(scale, bounds, discretization, new UniformRng());
    }

    @Override
    public Vector[] getInitials(Vector... notused) {//initials not used
        Vector[] initials = new Vector[N];

        int i = 0;
        Combination<Double> combination = new Combination<Double>(grid);
        for (List<Double> list : combination) {
            initials[i] = new DenseVector(DoubleUtils.collection2DoubleArray(list));
            initials[i] = perturb(initials[i]);
            i++;
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

    private Vector perturb(Vector v) {
        Vector u = v.deepCopy();
        for (int i = 1; i <= u.size(); ++i) {
            double rand = (1 - scale) + rng.nextDouble() * 2 * scale;
            u.set(i, u.get(i) * rand);
        }

        return u;
    }
}
