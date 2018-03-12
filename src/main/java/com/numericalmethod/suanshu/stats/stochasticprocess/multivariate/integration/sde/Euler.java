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
package com.numericalmethod.suanshu.stats.stochasticprocess.multivariate.integration.sde;

import com.numericalmethod.suanshu.stats.stochasticprocess.multivariate.sde.SDE;
import com.numericalmethod.suanshu.stats.stochasticprocess.timepoints.TimeGrid;
import com.numericalmethod.suanshu.stats.stochasticprocess.timepoints.UnitGrid;

/**
 * The Euler method is a first-order numerical procedure for integrating stochastic differential equations (SDEs) with a given initial value.
 *
 * @author Haksun Li
 *
 * @see <a href="http://en.wikipedia.org/wiki/Euler%E2%80%93Maruyama_method">Wikipedia: Euler–Maruyama method</a>
 */
public class Euler extends RandomWalk {

    /**
     * Simulate an SDE using the Euler scheme at time points specified.
     *
     * @param sde the stochastic differential equation specification
     * @param timePoints specifying the time points in a grid
     */
    public Euler(SDE sde, TimeGrid timePoints) {
        super(new com.numericalmethod.suanshu.stats.stochasticprocess.multivariate.sde.Euler(sde), timePoints);
    }

    /**
     * Simulate an SDE using the Euler scheme at even time points, <i>[0, 1, ......, T]</i>.
     *
     * @param sde the stochastic differential equation specification
     * @param T the duration of the simulation
     */
    public Euler(SDE sde, int T) {
        this(sde, new UnitGrid(T));
    }
}
