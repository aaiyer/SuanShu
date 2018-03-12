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
package com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.lp.simplex.solver;

import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.lp.exception.LPInfeasible;
import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.lp.exception.LPUnbounded;
import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.lp.problem.CanonicalLPProblem1;
import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.lp.problem.LPProblem;
import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.lp.simplex.FerrisMangasarianWrightPhase1;
import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.lp.simplex.FerrisMangasarianWrightScheme2;
import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.lp.simplex.SimplexTable;
import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.lp.simplex.solution.LPSimplexMinimizer;
import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.lp.simplex.solution.LPSimplexSolution;
import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.lp.simplex.solution.LPUnboundedMinimizerScheme2;

/**
 * This implementation solves a linear programming problem, {@link LPProblem}, using a two-step approach.
 * It first converts the LP problem into a canonical LP problem and then solve it using a canonical LP solver.
 *
 * @author Haksun Li
 * @see "Michael C. Ferris, Olvi L. Mangasarian, Stephen J. Wright, "Section 3.6.4," Linear Programming with MATLAB."
 */
public class LPTwoPhaseSolver implements LPSimplexSolver<LPProblem> {

    private class Solution implements LPSimplexSolution {

        private SimplexTable table;
        private final LPSimplexMinimizer minimizer;

        private Solution(SimplexTable table0) throws LPInfeasible, Exception {
//            table = new SimplexTable(table0);//TODO: make a copy?
            table = table0;

            try {
                FerrisMangasarianWrightScheme2 scheme2 = new FerrisMangasarianWrightScheme2(table);
                table = scheme2.process();
            } catch (LPUnbounded ex) {
                minimizer = new LPUnboundedMinimizerScheme2(table, ex.s);
                return;
            }

            if (!table.isFeasible()) {
                FerrisMangasarianWrightPhase1 phase1 = new FerrisMangasarianWrightPhase1(table);
                table = phase1.process();
            }

            //now a canonical feasible table
            LPSimplexSolution soln = solver.solve(table);
            minimizer = soln.minimizer();
        }

        @Override
        public double minimum() {
            return minimizer.minimum();

        }

        @Override
        public LPSimplexMinimizer minimizer() {
            return minimizer;
        }
    }

    private final LPSimplexSolver<CanonicalLPProblem1> solver;//a canonical LP solver

    /**
     * Construct an LP solver to solve LP problems.
     *
     * @param solver a canonical LP solver
     */
    public LPTwoPhaseSolver(LPSimplexSolver<CanonicalLPProblem1> solver) {
        this.solver = solver;
    }

    /**
     * Construct an LP solver to solve LP problems.
     */
    public LPTwoPhaseSolver() {
        this(new LPCanonicalSolver());
    }

    @Override
    public LPSimplexSolution solve(SimplexTable table) throws LPInfeasible, Exception {
        return new Solution(table);
    }

    @Override
    public LPSimplexSolution solve(LPProblem problem) throws LPInfeasible, Exception {
        return solve(new SimplexTable(problem));
    }
}
