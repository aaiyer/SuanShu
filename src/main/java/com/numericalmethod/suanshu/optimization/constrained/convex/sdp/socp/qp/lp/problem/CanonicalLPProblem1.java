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

import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.optimization.constrained.constraint.linear.LinearGreaterThanConstraints;
import com.numericalmethod.suanshu.vector.doubles.Vector;

/**
 * This is a linear programming problem in the 1st canonical form (following the convention in the reference):
 * <blockquote><i>
 * min c'x
 * </i></blockquote>
 * s.t.
 * <blockquote><pre><i>
 * A * x &ge; b,
 * x &ge; 0
 * </i></pre></blockquote>
 * <i>b &le; 0</i> if the problem is feasible
 *
 * @author Haksun Li
 * @see "Michael C. Ferris, Olvi L. Mangasarian, Stephen J. Wright, "Eq. 3.1, Chapter 3, The Simplex Method," Linear Programming with MATLAB."
 */
public class CanonicalLPProblem1 extends LPProblemImpl1 {

    /**
     * Construct a linear programming problem in the canonical form.
     *
     * @param c the objective function
     * @param A the coefficients, <i>A</i>, of the greater-than-or-equal-to constraints <i>A * x &ge; b</i>
     * @param b the values, <i>b</i>, of the greater-than-or-equal-to constraints <i>A * x &ge; b</i>
     */
    public CanonicalLPProblem1(Vector c, Matrix A, Vector b) {
        super(c, new LinearGreaterThanConstraints(A, b), null);
    }

    /**
     * Construct a linear programming problem in the canonical form.
     *
     * @param cost    the objective function
     * @param greater a collection of greater-than-or-equal-to constraints
     */
    public CanonicalLPProblem1(Vector cost, LinearGreaterThanConstraints greater) {
        this(cost, greater.A(), greater.b());
    }

    /**
     * Convert a linear programming problem from the 2nd canonical form to the 1st canonical form.
     *
     * @param problem a linear programming problem in the 2nd canonical form
     */
    public CanonicalLPProblem1(CanonicalLPProblem2 problem) {
        this(problem.c(), problem.A().scaled(-1), problem.b().scaled(-1));
    }

    /**
     * Get the greater-than-or-equal-to constraints of the linear programming problem.
     *
     * @return the greater-than-or-equal-to constraints
     */
    public LinearGreaterThanConstraints getGreaterThanConstraints() {
        return super.getLessThanConstraints().toGreaterThanConstraints();
    }
}
