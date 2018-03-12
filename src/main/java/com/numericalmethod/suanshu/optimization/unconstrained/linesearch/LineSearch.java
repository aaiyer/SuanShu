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
package com.numericalmethod.suanshu.optimization.unconstrained.linesearch;

import com.numericalmethod.suanshu.optimization.Optimizer;
import com.numericalmethod.suanshu.optimization.problem.C2OptimProblem;
import com.numericalmethod.suanshu.vector.doubles.Vector;

/**
 * A line search is often used in another minimization algorithm to improve the current solution in one iteration step.
 * It begins the search from the current solution along a minimization direction.
 * A line search does not need to be very accurate but it needs to be quick.
 *
 * @author Haksun Li
 */
public interface LineSearch extends Optimizer<C2OptimProblem, LineSearch.Solution> {

    /**
     * This is the solution to a line search minimization.
     */
    public static interface Solution {

        /**
         * Get the increment <i>α</i> so that <i>f(x + α * d)</i> is (approximately) minimized.
         *
         * @param x the initial position
         * @param d the line search direction
         * @return the increment α
         * @see "Andreas Antoniou, Wu-Sheng Lu, "Section 2.6, Algorithm 4.6," Practical Optimization: Algorithms and Engineering Applications."
         */
        public double linesearch(final Vector x, final Vector d);
    }

}
