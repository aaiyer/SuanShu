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
package com.numericalmethod.suanshu.optimization.constrained.integer;

import com.numericalmethod.suanshu.optimization.constrained.problem.ConstrainedOptimProblem;

/**
 * An Integer Programming problem is a mathematical optimization or feasibility program in which some or all of the variables are restricted to be integers.
 * \[
 * \min_x f(x) \textrm{ s.t.,} \\
 * h_i(x) = 0 \\
 * g_i(x) \leq 0
 * \]
 * Some {x<sub>i</sub>} are integers.
 *
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/Integer_programming">Wikipedia: Integer programming</a>
 */
public interface IPProblem extends ConstrainedOptimProblem {

    /**
     * Get the indices of the integral variables.
     *
     * @return the integral variable indices
     */
    public int[] getIntegerIndices();

    /**
     * Check which elements in <i>x</i> do not satisfy the integral constraints.
     * The indices count from 1.
     *
     * @param x an argument to the objective function
     * @return the set of indices of values in <i>x</i> that do not satisfy the integral constraints.
     * An {@code int[]} of length 0 indicates that all integral variables in <i>x</i> are integers.
     */
    public int[] getNonIntegralIndices(double[] x);

    /**
     * Get the threshold to check whether a variable is an integer.
     *
     * @return the precision parameter: when a number |x| ≤ ε, it is considered 0
     */
    public double epsilon();
}
