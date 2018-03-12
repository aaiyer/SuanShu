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
package com.numericalmethod.suanshu.optimization.constrained.constraint.linear;

import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.matrix.doubles.operation.CreateMatrix;
import com.numericalmethod.suanshu.misc.R;
import com.numericalmethod.suanshu.misc.SuanShuUtils;
import com.numericalmethod.suanshu.optimization.constrained.constraint.GreaterThanConstraints;
import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.lp.problem.LPProblem;
import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.lp.problem.LPProblemImpl1;
import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.lp.simplex.solution.LPSimplexSolution;
import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.lp.simplex.solver.LPSimplexSolver;
import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.lp.simplex.solver.LPTwoPhaseSolver;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;

/**
 * This is a collection of linear greater-than-or-equal-to constraints.
 * <blockquote><pre><i>
 * A * x &ge; b
 * </i></pre></blockquote>
 *
 * @author Haksun Li
 */
public class LinearGreaterThanConstraints extends LinearConstraints implements GreaterThanConstraints {

    /**
     * Construct a collection of linear greater-than or equal-to constraints.
     *
     * @param A the inequality coefficients
     * @param b the inequality values
     */
    public LinearGreaterThanConstraints(Matrix A, Vector b) {
        super(A, b);
    }

    @Override
    public LinearLessThanConstraints toLessThanConstraints() {
        return new LinearLessThanConstraints(A().scaled(-1), b().scaled(-1));
    }

    /**
     * Given a collection of linear greater-than-or-equal-to constraints as well as a collection of equality constraints,
     * find a feasible initial point that satisfy the constraints.
     * This implementation solves eq. 11.25 in the reference.
     * The first (n-1) entries consist of a feasible initial point.
     * The last entry is the single point perturbation.
     *
     * @param equal a collection of linear equality constraints
     * @return a feasible initial point, and the single point perturbation (in one vector)
     * @see
     * <ul>
     * <li>"Jorge Nocedal, Stephen Wright, "p. 473," Numerical Optimization."
     * <li>"Andreas Antoniou, Wu-Sheng Lu, "Eq 11.25, Quadratic and Convex Programming," Practical Optimization: Algorithms and Engineering Applications."
     * <li>http://www.mathworks.com/help/toolbox/optim/ug/brnox7l.html (initialization)
     * </ul>
     */
    public Vector getFeasibleInitialPoint(LinearEqualityConstraints equal) {
        final int n = dimension();
        SuanShuUtils.assertArgument(equal == null || n == equal.dimension(),
                                    "the domains (variables) of both inequality and equality constraints must match");

        Vector cost = new DenseVector(n + 1);
        cost.set(n + 1, 1);// phi

        Matrix e = new DenseMatrix(R.rep(1.0, size()), size(), 1);
        Matrix A = CreateMatrix.cbind(A(), e);
        LinearGreaterThanConstraints greater = new LinearGreaterThanConstraints(A, b());

        LinearEqualityConstraints equalAppended = null;
        if (equal != null) {
            Matrix Aeq = CreateMatrix.cbind(equal.A(), new DenseMatrix(equal.A().nRows(), 1));
            equalAppended = new LinearEqualityConstraints(Aeq, equal.b());
        }

        BoxConstraints.Bound[] bound = new BoxConstraints.Bound[n];
        for (int i = 0; i < n; ++i) {
            bound[i] = new BoxConstraints.Bound(i + 1, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        }
        BoxConstraints box = new BoxConstraints(n, bound);

        LPProblem problem = new LPProblemImpl1(cost, greater, null, equalAppended, box);
        LPSimplexSolver<LPProblem> solver = new LPTwoPhaseSolver();

        try {
            LPSimplexSolution soln = solver.solve(problem);
            return soln.minimizer().minimizer();
//        } catch (LPInfeasible ex) {
//            throw new RuntimeException("LP problem not setup properly; there is no feasible initial");
        } catch (Exception ex) {//e.g., null pointer exception
            throw new RuntimeException("LP problem not setup properly; there is no feasible initial");
        }
    }

    /**
     * Given a collection of linear greater-than-or-equal-to constraints,
     * find a feasible initial point that satisfy the constraints.
     * This implementation solves eq. 11.25 in the reference.
     * The first (n-1) entries consist of a feasible initial point.
     * The last entry is the single point perturbation.
     *
     * @return a feasible initial point, and the single point perturbation (in one vector)
     * @see "Andreas Antoniou, Wu-Sheng Lu, "Eq 11.25, Quadratic and Convex Programming," Practical Optimization: Algorithms and Engineering Applications."
     */
    public Vector getFeasibleInitialPoint() {
        return getFeasibleInitialPoint(null);
    }
}
