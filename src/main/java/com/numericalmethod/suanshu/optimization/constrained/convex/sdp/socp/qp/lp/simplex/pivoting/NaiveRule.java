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
package com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.lp.simplex.pivoting;

import com.numericalmethod.suanshu.misc.SuanShuUtils;
import static com.numericalmethod.suanshu.number.DoubleUtils.compare;
import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.lp.exception.LPUnbounded;
import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.lp.simplex.SimplexTable;

/**
 * This pivoting rule chooses the column with the most negative reduced cost.
 * This gives the biggest decrease in the cost per unit increase in the entering variable.
 * However, since we cannot tell how much we can increase the entering variable until we perform the ratio test,
 * it is not generally true that this choice leads to the best decrease in the cost in this step, among all possible pivot columns.
 * Moreover, this naive rule does not prevent cycling, and should be used only for testing purpose.
 *
 * @author Haksun Li
 * @see "Michael C. Ferris, Olvi L. Mangasarian, Stephen J. Wright, "p.49," Linear Programming with MATLAB."
 */
public class NaiveRule implements SimplexPivoting {

    /**
     * {@inheritDoc}
     * We choose the column with most negative reduced cost (last entry in the column).
     *
     * @param table a simplex table
     * @return the pivot column
     */
    @Override
    public int pricing(SimplexTable table) {
        int s = 0;//pivot column

        for (int i = 1; i < table.nCols(); i++) {//ignore the last B column
            if (table.getColLabel(i).type == SimplexTable.LabelType.NON_BASIC
                || table.getColLabel(i).type == SimplexTable.LabelType.BASIC
                || table.getColLabel(i).type == SimplexTable.LabelType.FREE) {
                if (table.getCostRow(i) < 0) {//a candidate column has a negative value in the bottom row
                    s = s == 0 ? i : (table.getCostRow(i) < table.getCostRow(s) ? i : s);
                }
            }
        }//at this point, all bottom row entries are +ve.

        return s;
    }

    /**
     * {@inheritDoc}
     * The pivot row is the smallest basic variable index, <i>r</i>, such that row <i>r</i> satisfies
     * <i>-h(r) / H(r,s) = min {-h(i) / H(i,s) | H(i,s) &lt; 0}</i>
     *
     * @param table a simplex table
     * @param s     a column
     * @return the pivot row
     */
    @Override
    public int ratioTest(SimplexTable table, int s) {
        int r = 0;//pivot row
        double minRatio = Double.MAX_VALUE;

        for (int i = 1; i < table.nRows(); i++) {//ignore the last COST row
            if (table.getRowLabel(i).type != SimplexTable.LabelType.BASIC
                && table.getRowLabel(i).type != SimplexTable.LabelType.NON_BASIC
                && table.getRowLabel(i).type != SimplexTable.LabelType.ARTIFICIAL) {// possibly swapping back variable to columns
                continue;
            }

            double Hrs = table.get(i, s);//H(r,s)
            if (Hrs >= 0) {
                continue;
            }

            double hi = table.getBCol(i);//h(i)
            double ratio = -hi / Hrs;
            double epsilon = SuanShuUtils.autoEpsilon(ratio);
            if (ratio < minRatio - epsilon
                || (compare(ratio, minRatio, epsilon) == 0 && table.getRowLabel(i).index < table.getRowLabel(r).index)) {
                minRatio = ratio;
                r = i;
            }
        }

        return r;
    }

    @Override
    public Pivot getPivot(SimplexTable table) throws LPUnbounded {
        int s = pricing(table);
        if (s == 0) {
            return null;//already optimal solution
        }

        int r = ratioTest(table, s);
        if (r == 0) {
            throw new LPUnbounded(s);//TODO: is there a better way to represent, pivot, optimality, unboundedness?
        }

        return new Pivot(r, s);
    }
}
