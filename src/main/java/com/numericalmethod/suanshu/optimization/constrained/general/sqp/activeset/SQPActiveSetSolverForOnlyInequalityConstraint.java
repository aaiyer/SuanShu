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
 * FITNESS FOR Jacobian PARTICULAR PURPOSE, MERCHANTABILITY, NON-INFRINGEMENT, 
 * TITLE AND USEFULNESS.
 * 
 * IN NO EVENT AND UNDER NO LEGAL THEORY,
 * WHETHER IN ACTION, CONTRACT, NEGLIGENCE, TORT, OR OTHERWISE,
 * SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR
 * ANY CLAIMS, DAMAGES OR OTHER LIABILITIES,
 * ARISING AS Jacobian RESULT OF USING OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.numericalmethod.suanshu.optimization.constrained.general.sqp.activeset;

import com.numericalmethod.suanshu.analysis.function.rn2r1.RealScalarFunction;
import static com.numericalmethod.suanshu.misc.SuanShuUtils.assertArgument;
import com.numericalmethod.suanshu.optimization.constrained.constraint.EqualityConstraints;
import com.numericalmethod.suanshu.optimization.constrained.constraint.GreaterThanConstraints;
import com.numericalmethod.suanshu.optimization.constrained.problem.ConstrainedOptimProblem;
import com.numericalmethod.suanshu.optimization.constrained.problem.ConstrainedOptimProblemImpl1;
import com.numericalmethod.suanshu.optimization.problem.IterativeMinimizer;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;

/**
 * This implementation is a modified version of Algorithm 15.2 in the reference to solve a general constrained optimization problem with only inequality constraints.
 * \[
 * \min_x f(x) \textrm{ s.t.,} \\
 * c_j(x) = 0, j = 1, 2, ..., q\\
 * \]
 *
 * @author Haksun Li
 * @see "Andreas Antoniou, Wu-Sheng Lu, "Algorithm 15.2, SQP algorithm for nonlinear problems with inequality constraint," Practical Optimization: Algorithms and Engineering Applications."
 */
public class SQPActiveSetSolverForOnlyInequalityConstraint extends SQPActiveSetSolver {

    /**
     * This is the solution to a general minimization problem with only inequality constraints using the SQP Active Set algorithm.
     */
    public class Solution extends SQPActiveSetSolver.Solution {

        private Solution(RealScalarFunction f, EqualityConstraints equal, GreaterThanConstraints greater) {
            super(f, equal, greater);
        }

        @Override
        public Vector search(Vector... initials) throws Exception {
            switch (initials.length) {
                case 2:
                    return search(initials[0], new DenseVector(0), initials[1]);//no inequality constraint
                default:
                    return super.search(initials);
            }
        }
    }

    /**
     * Construct an SQP Active Set minimizer to solve general minimization problems with only inequality constraints.
     *
     * @param variant       a factory that constructs a new instance of {@code SQPASVariation} for each problem
     * @param epsilon       a precision parameter: when a number |x| ≤ ε, it is considered 0
     * @param maxIterations the maximum number of iterations
     */
    public SQPActiveSetSolverForOnlyInequalityConstraint(VariationFactory variant, double epsilon, int maxIterations) {
        super(variant, epsilon, maxIterations);
    }

    /**
     * Construct an SQP Active Set minimizer to solve general minimization problems with only inequality constraints.
     *
     * @param epsilon       a precision parameter: when a number |x| ≤ ε, it is considered 0
     * @param maxIterations the maximum number of iterations
     */
    public SQPActiveSetSolverForOnlyInequalityConstraint(double epsilon, int maxIterations) {
        super(epsilon, maxIterations);
    }

    /**
     * Minimize a function subject to only inequality constraints.
     *
     * @param f       the objective function to be minimized
     * @param greater the greater-than-or-equal-to constraints
     * @return a solution to the minimization problem
     * @throws Exception when there is an error solving the problem
     */
    public IterativeMinimizer<Vector> solve(RealScalarFunction f, GreaterThanConstraints greater) throws Exception {
        ConstrainedOptimProblemImpl1 problem = new ConstrainedOptimProblemImpl1(f, null, greater.toLessThanConstraints());
        return solve(problem);
    }

    @Override
    public SQPActiveSetSolverForOnlyInequalityConstraint.Solution solve(ConstrainedOptimProblem problem) throws Exception {
        assertArgument(problem.getEqualityConstraints() == null, "there must be only inequality constraints");
        assertArgument(problem.getLessThanConstraints() != null, "there must be inequality constraints");

        return new Solution(problem.f(), problem.getEqualityConstraints(), problem.getLessThanConstraints().toGreaterThanConstraints());
    }
}
