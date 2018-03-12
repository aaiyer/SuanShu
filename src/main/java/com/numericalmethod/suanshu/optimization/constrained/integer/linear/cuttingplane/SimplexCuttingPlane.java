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
package com.numericalmethod.suanshu.optimization.constrained.integer.linear.cuttingplane;

import com.numericalmethod.suanshu.optimization.constrained.ConstrainedMinimizer;
import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.lp.simplex.SimplexTable;
import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.lp.simplex.solution.LPSimplexMinimizer;
import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.lp.simplex.solution.LPSimplexSolution;
import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.lp.simplex.solver.LPSimplexSolver;
import com.numericalmethod.suanshu.optimization.constrained.integer.linear.cuttingplane.SimplexCuttingPlane.CutterFactory.Cutter;
import com.numericalmethod.suanshu.optimization.constrained.integer.linear.problem.ILPProblem;
import com.numericalmethod.suanshu.optimization.problem.MinimizationSolution;
import com.numericalmethod.suanshu.vector.doubles.ImmutableVector;
import com.numericalmethod.suanshu.vector.doubles.Vector;

/**
 * The use of cutting planes to solve Mixed Integer Linear Programming (MILP) problems was introduced by Ralph E Gomory.
 * Cutting plane methods for MILP work by solving a non-integer linear program,
 * the linear relaxation of the given integer program.
 * The theory of Linear Programming dictates that under mild assumptions
 * (if the linear program has an optimal solution, and if the feasible region does not contain a line),
 * one can always find an extreme point or a corner point that is optimal.
 * The obtained optimum is tested for being an integer solution.
 * If it is not, there is guaranteed to exist a linear inequality that separates the optimum from the convex hull of the true feasible set.
 * Finding such an inequality is the separation problem, and such an inequality is a cut.
 * A cut can be added to the relaxed linear program. Then, the current non-integer solution is no longer feasible to the relaxation.
 * This process is repeated until an optimal integer solution is found.
 *
 * @author Haksun Li
 * @see
 * <ul>
 * <li><a href="http://en.wikipedia.org/wiki/Cutting-plane_method">Wikipedia: Cutting-plane method</a>
 * <li>"Der-San Chen, Robert G. Batson, Yu Dang, "11.2.1 Dual Cutting Place Approach," Applied Integer Programming: Modeling and Solution."
 * </ul>
 */
public class SimplexCuttingPlane implements ConstrainedMinimizer<ILPProblem, MinimizationSolution<Vector>> {

    /**
     * This factory constructs a new {@code Cutter} for each MILP problem.
     */
    public static interface CutterFactory {

        /**
         * A {@code Cutter} defines how to cut a simplex table, i.e., how to relax a linear program so that
         * the current non-integer solution is no longer feasible to the relaxation.
         */
        public static interface Cutter {

            /**
             * Cut a simplex table.
             * For efficiency reason, the input {@code table} is changed after this operation.
             *
             * @param table a simple table
             * @return a cut simplex table
             */
            public SimplexTable cut(SimplexTable table);
        }

        /**
         * Construct a new {@code Cutter} for a MILP problem.
         *
         * @param problem a MILP problem
         * @return a {@code Cutter}
         */
        public Cutter getCutter(ILPProblem problem);
    }

    private class Solution implements MinimizationSolution<Vector> {

        private LPSimplexMinimizer minimizer = null;

        private Solution(ILPProblem problem) throws Exception {
            CutterFactory.Cutter cutter = cutterFactory.getCutter(problem);
            SimplexTable table = new SimplexTable(problem);

            do {
                if (minimizer != null) {
                    table = cutter.cut(table);
                }

                LPSimplexSolution soln = solver.solve(table);
                minimizer = soln.minimizer();
                table = minimizer.getResultantTableau();
            } while (problem.getNonIntegralIndices(table.minimizer().toArray()).length != 0);//TODO: max iteration?
        }

        @Override
        public double minimum() {
            return minimizer.minimum();
        }

        @Override
        public Vector minimizer() {
            return new ImmutableVector(minimizer.minimizer());
        }
    }

    private final LPSimplexSolver solver;
    private final CutterFactory cutterFactory;

    /**
     * Construct a cutting-plane minimizer to solve an MILP problem.
     *
     * @param solver        a simplex solver to solve an LP problem
     * @param cutterFactory a factory that constructs a new {@code Cutter} for each problem
     */
    public SimplexCuttingPlane(LPSimplexSolver solver, CutterFactory cutterFactory) {
        this.solver = solver;
        this.cutterFactory = cutterFactory;
    }

    @Override
    public MinimizationSolution<Vector> solve(ILPProblem problem) throws Exception {
        return new Solution(problem);
    }
}
