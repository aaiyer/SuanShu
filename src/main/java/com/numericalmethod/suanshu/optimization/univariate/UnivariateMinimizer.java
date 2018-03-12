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
package com.numericalmethod.suanshu.optimization.univariate;

import com.numericalmethod.suanshu.optimization.Minimizer;
import com.numericalmethod.suanshu.optimization.problem.C2OptimProblem;
import com.numericalmethod.suanshu.optimization.problem.MinimizationSolution;

/**
 * A univariate minimizer minimizes a univariate function.
 *
 * @author Haksun Li
 */
public interface UnivariateMinimizer extends Minimizer<C2OptimProblem, UnivariateMinimizer.Solution> {

    /**
     * This is the solution to a univariate minimization problem.
     */
    public static interface Solution extends MinimizationSolution<Double> {

        /**
         * Search for a minimum within the interval <i>[lower, upper]</i>.
         *
         * @param lower   the lower bound for the bracketing interval which contains a minimum
         * @param initial an initial guess
         * @param upper   the upper bound for the bracketing interval which contains a minimum
         * @return an approximate minimizer
         */
        public double search(double lower, double initial, double upper);

        /**
         * Search for a minimum within the interval <i>[lower, upper]</i>.
         *
         * @param lower the lower bound for the bracketing interval which contains a minimum
         * @param upper the upper bound for the bracketing interval which contains a minimum
         * @return an approximate minimizer
         */
        public double search(double lower, double upper);
    }

}
