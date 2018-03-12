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
package com.numericalmethod.suanshu.optimization.constrained.problem;

import com.numericalmethod.suanshu.optimization.constrained.constraint.EqualityConstraints;
import com.numericalmethod.suanshu.optimization.constrained.constraint.LessThanConstraints;
import com.numericalmethod.suanshu.optimization.problem.OptimProblem;

/**
 * A constrained optimization problem takes this form.
 * \[
 * \min_x f(x)
 * \]
 * s.t.,
 * <blockquote><pre><i>
 * h<sub>i</sub>(x) = 0
 * g<sub>i</sub>(x) &le; 0
 * </i></pre></blockquote>
 *
 * @author Haksun Li
 * @see "Edwin K. P. Chong, Stanislaw H. Zak, "Chapter 20, Problems with Inequality Constraints," An Introduction to Optimization. Wiley-Interscience, 2001."
 */
public interface ConstrainedOptimProblem extends OptimProblem {

    /**
     * Get the less-than-or-equal-to constraints, <i>g<sub>i</sub>(x) &le; 0</i>
     *
     * @return the less-than-or-equal-to constraints
     */
    public LessThanConstraints getLessThanConstraints();

    /**
     * Get the equality constraints, <i>h<sub>i</sub>(x) = 0</i>
     *
     * @return the equality constraints
     */
    public EqualityConstraints getEqualityConstraints();
}
