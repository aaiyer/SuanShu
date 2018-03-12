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
package com.numericalmethod.suanshu.optimization.constrained.constraint;

/**
 * The domain of an optimization problem may be restricted by less-than or equal-to constraints.
 * For example,
 * \[
 * \min_x f(x)
 * \]
 * s.t.,
 * \[
 * g_i(x) \leq 0, i = 1, 2, ...
 * \]
 *
 * @author Haksun Li
 * @see "Edwin K. P. Chong, Stanislaw H. Zak. "Chapter 20. Problems with Inequality GeneralConstraints," An Introduction to Optimization. Wiley-Interscience. 2001."
 */
public interface LessThanConstraints extends Constraints {

    /**
     * Convert the less-than or equal-to constraints to greater-than or equal-to constraints.
     *
     * @return the equivalent greater-than or equal-to constraints
     */
    public GreaterThanConstraints toGreaterThanConstraints();
}
