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
package com.numericalmethod.suanshu.stats.stochasticprocess.multivariate.sde;

import com.numericalmethod.suanshu.vector.doubles.Vector;

/**
 * This interface represents the discretized version of a multivariate SDE.
 *
 * <p>
 * We specify an SDE in the differential form, i.e., by its increments.
 *
 * @author Haksun Li
 */
public interface DiscretizedSDE {

    /**
     * Get the number of independent driving Brownian motions.
     *
     * @return number of independent driving Brownian motions
     */
    public int nB();

    /**
     * Get an empty filtration for the process.
     * 
     * @return an empty filtration
     */
    public Ft getNewFt();

    /**
     * This is the SDE specification of a stochastic process.
     *
     * @param ft filtration
     * @return the increment of the process in {@code dt}
     */
    public Vector dXt(Ft ft);
}
