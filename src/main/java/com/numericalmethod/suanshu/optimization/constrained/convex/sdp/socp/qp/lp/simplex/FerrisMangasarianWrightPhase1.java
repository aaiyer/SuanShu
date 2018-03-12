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
package com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.lp.simplex;

import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.lp.exception.LPInfeasible;
import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.lp.problem.CanonicalLPProblem1;
import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.lp.simplex.SimplexTable.LabelType;
import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.lp.simplex.solution.LPSimplexMinimizer;
import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.lp.simplex.solution.LPSimplexSolution;
import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.lp.simplex.solution.LPUnboundedMinimizer;
import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.lp.simplex.solver.FerrisMangasarianWrightPhase2;
import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.lp.simplex.solver.LPSimplexSolver;

/**
 * The phase 1 procedure finds a feasible table from an infeasible one by pivoting the simplex table of a related problem.
 *
 * @author Haksun Li
 * @see "Michael C. Ferris, Olvi L. Mangasarian, Stephen J. Wright, "Algorithm 3.2, Section 3.4, The Phase I Procedure," Linear Programming with MATLAB."
 */
public class FerrisMangasarianWrightPhase1 {

    private SimplexTable table0;

    /**
     * Construct the phase 1 algorithm for an infeasible table corresponding to a non-standard linear programming problem, e.g., b &ge; 0.
     *
     * @param table an infeasible table
     */
    public FerrisMangasarianWrightPhase1(SimplexTable table) {
        this.table0 = new SimplexTable(table);//make a copy
    }

    /**
     * Find a feasible table, if any.
     *
     * @return a feasible table. If the original table is already feasible, return itself.
     * @throws LPInfeasible if the linear programming problem is infeasible
     * @throws Exception    if any error
     */
    public SimplexTable process() throws LPInfeasible, Exception {
        if (table0.isFeasible()) {
            return table0;
        }

        addArtificials();
        specialPivoting();

        //solve the related problem
        LPSimplexSolver<CanonicalLPProblem1> solver = new FerrisMangasarianWrightPhase2();
        LPSimplexSolution soln = solver.solve(table0);
        LPSimplexMinimizer minimizer = soln.minimizer();
        SimplexTable table1 = minimizer.getResultantTableau();

        //when z0 is positive, stop; the problem is either unbounded or infeasible
        double z0 = table1.minimum();
        if (z0 > 0) {
            if (minimizer instanceof LPUnboundedMinimizer) {
                return table1;
            }

            throw new LPInfeasible();
        }

        //move x0 to the top (if needed)
        for (int r = 1; r <= table1.nRows(); ++r) {
            if (table1.getRowLabel(r).type == LabelType.ARTIFICIAL) {
                table1 = table1.swap(r, 1);
                break;
            }
        }

        //remove the artificial variables
        table1.deleteRow(table1.getIndexFromRowLabel(SimplexTable.ARTIFICIAL_COST));
        table1.deleteCol(table1.getIndexFromColLabel(SimplexTable.ARTIFICIAL));

        return table1;
    }

    private void addArtificials() {//TODO: made static?
        table0.addRowAt(table0.nRows() + 1, SimplexTable.ARTIFICIAL_COST);

        table0.addColAt(1, SimplexTable.ARTIFICIAL);
        for (int i = 1; i < table0.nRows() - 1; ++i) {//ignore rows COST and ARTIFICIAL_COST
            if (table0.get(i, table0.getIndexFromColLabel(SimplexTable.B)) < 0
                && (table0.getRowLabel(i).type == LabelType.BASIC || table0.getRowLabel(i).type == LabelType.NON_BASIC)) {//TODO: sure about the NON_BASIC? do we ever switch variable (NON_BASIC) back to column?          
                table0.set(i, 1, 1);
            }
        }

        table0.set(table0.nRows(), 1, 1);
    }

    private void specialPivoting() {//TODO: made static?
        //find the most negative B row
        int r = 0;
        double minB = Double.POSITIVE_INFINITY;
        for (int i = 1; i <= table0.nRows() - 2; ++i) {
            if (table0.getRowLabel(i).type == SimplexTable.LabelType.BASIC
                || table0.getRowLabel(i).type == SimplexTable.LabelType.NON_BASIC) {
                double b = table0.getBCol(i);
                if (b < minB) {
                    minB = b;
                    r = i;
                }
            }
        }

        if (r == 0) {
            throw new RuntimeException("cannot find a negative B row to swap");
        }

        table0 = table0.swap(r, 1);//table1.toFlexibleTable().getIndexFromColLabel(SimplexTable.ARTIFICIAL)
    }
}
