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
import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.lp.simplex.SimplexTable;
import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.lp.simplex.pivoting.SimplexPivoting;
import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.lp.simplex.pivoting.SimplexPivoting.Pivot;
import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.lp.simplex.pivoting.SmallestSubscriptRule;
import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.lp.simplex.solution.LPBoundedMinimizer;
import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.lp.simplex.solution.LPSimplexMinimizer;
import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.lp.simplex.solution.LPSimplexSolution;
import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.lp.simplex.solution.LPUnboundedMinimizer;

/**
 * This implementation solves a canonical linear programming problem that does not need preprocessing its simplex table.
 *
 * @author Haksun Li
 * @see "Michael C. Ferris, Olvi L. Mangasarian, Stephen J. Wright, "Algorithm 3.1, Section 3, The Simplex Method," Linear Programming with MATLAB."
 */
public class FerrisMangasarianWrightPhase2 implements LPSimplexSolver<CanonicalLPProblem1> {

    private class Solution implements LPSimplexSolution {

        private SimplexTable table;
        private LPSimplexMinimizer minimizer;
        private LPUnbounded unboundedEx = null;//unbound exception

        private Solution(SimplexTable table0) throws LPInfeasible {
//            table = new SimplexTable(table0);//TODO: make a copy?
            table = table0;

            //phase 2 always assumes that the table is feasible
            if (!table.isFeasible()) {
                throw new LPInfeasible();
            }

            try {
                for (Pivot pivot = pivoting.getPivot(table); pivot != null; pivot = pivoting.getPivot(table)) {
                    if (!table.isFeasible()) {//TODO: is this step necessary?
                        throw new LPInfeasible();
                    }

                    table = table.swap(pivot.r, pivot.s);
                }
            } catch (LPUnbounded ex) {
                unboundedEx = ex;
                minimizer = new LPUnboundedMinimizer(table, unboundedEx == null ? 0 : unboundedEx.s);
                return;
            }

            minimizer = new LPBoundedMinimizer(table);
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

    /** the Jordan exchange between a basic and a non-basic variables */
    private final SimplexPivoting pivoting;

    /**
     * Construct an LP solver to solve canonical LP problems using the Phase 2 algorithm in Ferris, Mangasarian & Wright.
     *
     * @param pivoting a pivoting rule
     */
    public FerrisMangasarianWrightPhase2(SimplexPivoting pivoting) {
        this.pivoting = pivoting;
    }

    /**
     * Construct an LP solver to solve canonical LP problems using the Phase 2 algorithm in Ferris, Mangasarian & Wright.
     */
    public FerrisMangasarianWrightPhase2() {
        this(new SmallestSubscriptRule());
    }

    @Override
    public LPSimplexSolution solve(SimplexTable table) {
        try {
            return new Solution(table);
        } catch (LPInfeasible ex) {
            return null;//TODO: allow solve to throw an Exception?
        }
    }

    @Override
    public LPSimplexSolution solve(CanonicalLPProblem1 problem) {
        return solve(new SimplexTable(problem));
    }
}
