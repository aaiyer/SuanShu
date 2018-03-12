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
package com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.lp.problem;

import com.numericalmethod.suanshu.matrix.doubles.ImmutableMatrix;
import com.numericalmethod.suanshu.optimization.constrained.problem.ConstrainedOptimProblem;
import com.numericalmethod.suanshu.vector.doubles.ImmutableVector;

/**
 * A linear programming (LP) problem minimizes a linear objective function subject to a collection of linear constraints.
 * <blockquote><pre><i>
 * min c'x
 * </i></pre></blockquote>
 * s.t.
 * <blockquote><pre><i>
 * A * x &ge; b
 * Aeq * x = beq
 * </i></pre></blockquote>
 * some <i>x &ge; 0</i>, some <i>x</i> are free.
 *
 * @author Haksun Li
 */
public interface LPProblem extends ConstrainedOptimProblem {

    /**
     * Get the objective function.
     *
     * @return the objective function
     */
    public ImmutableVector c();

    /**
     * Get the coefficients, <i>A</i>, of the greater-than-or-equal-to constraints <i>A * x &ge; b</i>.
     *
     * @return the coefficients of the greater-than-or-equal-to constraints
     */
    public ImmutableMatrix A();

    /**
     * Get the values, <i>b</i>, of the greater-than-or-equal-to constraints <i>A * x &ge; b</i>.
     *
     * @return the values of the greater-than-or-equal-to constraints
     */
    public ImmutableVector b();

    /**
     * Get the coefficients, <i>Aeq</i>, of the equality constraints <i>Aeq * x &ge; beq</i>.
     *
     * @return the coefficients of the equality constraints
     */
    public ImmutableMatrix Aeq();

    /**
     * Get the values, <i>beq</i>, of the equality constraints <i>Aeq * x &ge; beq</i>.
     *
     * @return the values of the equality constraints
     */
    public ImmutableVector beq();

    /**
     * Check whether x<sub>i</sub> is a free variable <em>after</em> handling the box constraints.
     *
     * @param i the index of a variable, counting from 1
     * @return {@code true} if x<sub>i</sub> is free
     */
    public boolean isFree(int i);
}
