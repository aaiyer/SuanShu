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
package com.numericalmethod.suanshu.stats.stochasticprocess.univariate.brownian;

import com.numericalmethod.suanshu.stats.stochasticprocess.timepoints.TimeGrid;
import com.numericalmethod.suanshu.stats.stochasticprocess.timepoints.UnitGrid;
import com.numericalmethod.suanshu.stats.stochasticprocess.univariate.integration.sde.Construction;
import com.numericalmethod.suanshu.stats.stochasticprocess.univariate.integration.sde.RandomWalk.Realization;

/**
 * This is the Random Walk construction of a univariate Brownian motion.
 *
 * <p>
 * For constant μ and σ, this method is exact in the sense that
 * the joint distribution of the simulated values coincides with the joint distribution of the corresponding Brownian motion a the time grid points.
 * Please note that this says nothing about what happens between two successive grid points.
 *
 * <p>
 * For time-dependent μ and σ, this method in general introduce discretization error even at the time grid points,
 * because the increments will no longer have exactly the correct mean and variance.
 *
 * @author Haksun Li
 *
 * @see "P. Glasserman. Monte Carlo Methods in Financial Engineering. Section 3.1. pp. 81. Springer. 2004."
 */
public class RandomWalk extends Brownian implements Construction {

    //composition/layering; we implment this <tt>RandomWalk</tt> using the generic Random Walk construction
    com.numericalmethod.suanshu.stats.stochasticprocess.univariate.integration.sde.RandomWalk rw;

    /**
     * Construct a univariate Brownian motion at time points specified.
     *
     * @param timePoints specifying the time points in a grid
     */
    public RandomWalk(TimeGrid timePoints) {
        this.rw = new com.numericalmethod.suanshu.stats.stochasticprocess.univariate.integration.sde.RandomWalk(this, timePoints);
    }

    /**
     * Construct a univariate Brownian motion at even time points, <i>[0, 1, ......, T]</i>.
     *
     * @param T the length of the grid
     */
    public RandomWalk(int T) {
        this(new UnitGrid(T));
    }

    @Override
    public Realization nextRealization(double x0) {
        return rw.nextRealization(x0);
    }

    @Override
    public void seed(long seed) {
        rw.seed(seed);
    }
}
