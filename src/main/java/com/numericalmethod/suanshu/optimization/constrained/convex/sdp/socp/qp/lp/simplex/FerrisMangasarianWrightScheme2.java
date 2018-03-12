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

import com.numericalmethod.suanshu.misc.SuanShuUtils;
import static com.numericalmethod.suanshu.number.DoubleUtils.compare;
import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.lp.exception.LPInfeasible;
import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.lp.exception.LPUnbounded;
import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.lp.simplex.pivoting.SmallestSubscriptRule;

/**
 * The scheme 2 procedure removes equalities and free variables.
 *
 * @author Haksun Li
 * @see "Michael C. Ferris, Olvi L. Mangasarian, Stephen J. Wright. "Section 3.6.3, Scheme II," Linear Programming with MATLAB"
 */
public class FerrisMangasarianWrightScheme2 {

    private SimplexTable table0;
    private final double epsilon;

    /**
     * Construct the scheme 2 algorithm for a table with equalities and free variables.
     *
     * @param table a table with equalities and free variables
     */
    public FerrisMangasarianWrightScheme2(SimplexTable table) {
        this.table0 = new SimplexTable(table);//make a copy
        this.epsilon = SuanShuUtils.autoEpsilon(table);
    }

    /**
     * Remove equalities and free variables, if possible.
     * The resultant table may be infeasible.
     *
     * @return a simplex table without equalities and free variables
     * @throws LPInfeasible if the linear programming problem is infeasible
     * @throws LPUnbounded  if the linear programming problem is unbounded
     */
    public SimplexTable process() throws LPInfeasible, LPUnbounded {
        SimplexTable table1 = swapEqualities(table0);
        SimplexTable table2 = swapFrees(table1);

        return table2;
    }

    /** Move the equalities to columns then delete. */
    private SimplexTable swapEqualities(SimplexTable table) throws LPInfeasible {
        for (int r = 1; r < table.nRows(); ++r) {//rows
            if (table.getRowLabel(r).type != SimplexTable.LabelType.EQUALITY) {
                continue;
            }

            //swap with the first FREE column, otherwise NON_BASIC column
            int s = 1;
            for (; s < table.nCols(); ++s) {//columns
                if (table.getColLabel(s).type == SimplexTable.LabelType.FREE) {//TODO: can we avoid looking for FREE column if there isn't any? keep track of the number of FREE columns left?
                    double pivot = table.get(r, s);
                    if (compare(pivot, 0, epsilon) == 0) {//zero pivot
                        continue;
                    }

                    table = table.swap(r, s);
                    //TODO: delete column s; labeling?
                    break;
                }
            }

            if (s >= table.nCols()) {//no swap with FREE column, or all possible pivot = 0
                for (s = 1; s < table.nCols(); ++s) {//columns
                    if (table.getColLabel(s).type == SimplexTable.LabelType.NON_BASIC) {
                        double pivot = table.get(r, s);
                        if (compare(pivot, 0, epsilon) == 0) {//zero pivot
                            continue;
                        }

                        table = table.swap(r, s);
                        //TODO: delete column s; labeling?
                        break;
                    }
                }
            }

            if (s >= table.nCols()) {//all possible pivot = 0
                if (compare(table.getBCol(r), 0, epsilon) != 0) {//inconsistent
                    throw new LPInfeasible();
                }// else, ignore
            }
        }

        return table;
    }

    /** Move the free variables to rows. */
    private SimplexTable swapFrees(SimplexTable table) throws LPUnbounded {
        SmallestSubscriptRule pivoting = new SmallestSubscriptRule();

        for (int s = 1; s < table.nCols(); ++s) {//columns
            if (table.getColLabel(s).type != SimplexTable.LabelType.FREE) {
                continue;
            }

            //swap with the first BASIC or NON_BASIC column
            int r = pivoting.ratioTest(table, s);
            if (r == 0) {
                if (table.isFeasible()) {
                    throw new LPUnbounded(s);// we can determine the problem is unbounded only if it is feasible
                }
            } else {
                table = table.swap(r, s);
            }
        }

        return table;
    }
}
