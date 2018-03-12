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

import com.numericalmethod.suanshu.stats.stochasticprocess.multivariate.MultiVariateRealization;
import com.numericalmethod.suanshu.vector.doubles.Vector;

/**
 * This interface defines how to construct a realization for a stochastic process.
 *
 * @author Haksun Li
 */
public interface Construction {

    /**
     * Construct a realization of a stochastic process. The realization a time series.
     *
     * @param x0 the starting point of the trajectory
     * @return a realization
     */
    public MultiVariateRealization nextRealization(Vector x0);

    /**
     * Seed the construction process so that we may generate the same realizations.
     * 
     * @param seed seeding the same {@code seed} gives arise to the same realization
     */
    public void seed(long seed);
}
