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
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.misc.SuanShuUtils;
import com.numericalmethod.suanshu.optimization.constrained.constraint.linear.LinearLessThanConstraints;
import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.lp.exception.LPDimensionNotMatched;
import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.lp.exception.LPEmptyCostVector;
import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.lp.exception.LPNoConstraint;
import com.numericalmethod.suanshu.vector.doubles.ImmutableVector;
import com.numericalmethod.suanshu.vector.doubles.Vector;

/**
 * This is a linear programming problem in the 2nd canonical form (following the convention in the wiki):
 * <blockquote><i>
 * min c'x
 * </i></blockquote>
 * s.t.
 * <blockquote><pre><i>
 * A * x &le; b,
 * x &ge; 0
 * </i></pre></blockquote>
 * <i>b &ge; 0</i> if the problem is feasible
 *
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/Linear_programming#Standard_form">Wikipedia: Standard form</a>
 */
public class CanonicalLPProblem2 extends LPProblemImpl1 {

    /**
     * Construct a linear programming problem in the canonical form.
     *
     * @param c {@code c'x} is the linear objective function to be minimized
     * @param A the less-than inequality constraints {@code A * x ≤ b}
     * @param b the less-than inequality values {@code A * x ≤ b}
     */
    public CanonicalLPProblem2(Vector c, Matrix A, Vector b) {
        super(c, null, new LinearLessThanConstraints(A, b), null, null);
        validate();
    }

    /**
     * Construct a linear programming problem in the canonical form.
     *
     * @param cost the objective function
     * @param less a collection of less-than-or-equal-to constraints
     */
    public CanonicalLPProblem2(Vector cost, LinearLessThanConstraints less) {
        this(cost, less.A(), less.b());
    }

    /**
     * Convert a linear programming problem from the 1st canonical form to the 2nd canonical form.
     *
     * @param problem a linear programming problem in the 1st canonical form
     */
    public CanonicalLPProblem2(CanonicalLPProblem1 problem) {
        this(problem.c(), problem.A().scaled(-1), problem.b().scaled(-1));
    }

    private void validate() throws LPEmptyCostVector, LPNoConstraint, LPDimensionNotMatched {//TODO: make the runtime exception compile time exception?
        ImmutableVector b = getLessThanConstraints().b();
        for (int i = 1; i <= b.size(); ++i) {
            SuanShuUtils.assertArgument(b.get(i) >= 0, "b >= 0");
        }

        if (super.c() == null) {
            throw new LPEmptyCostVector();
        }

        ImmutableMatrix A = getLessThanConstraints().A();
        if (A == null || b == null) {
            throw new LPNoConstraint();
        }

        if (A.nRows() != b.size()) {
            throw new LPDimensionNotMatched("dimension not matched for the inequality constraints");
        }
    }
}
