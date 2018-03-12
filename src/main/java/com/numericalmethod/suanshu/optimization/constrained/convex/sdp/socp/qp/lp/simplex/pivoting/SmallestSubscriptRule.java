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

import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.lp.simplex.SimplexTable;

/**
 * Bland's smallest-subscript rule is for anti-cycling in choosing a pivot.
 *
 * @author Haksun Li
 * @see "Michael C. Ferris, Olvi L. Mangasarian, Stephen J. Wright. "Section 3.5.3," Linear Programming with MATLAB."
 */
public class SmallestSubscriptRule extends NaiveRule {

    /**
     * {@inheritDoc}
     * The pivot column is the smallest non-basic variable index, <i>s</i>, such that
     * column <i>s</i> has a negative element in the bottom row (reduced cost).
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
                    s = s == 0 ? i : table.getColLabel(i).index < table.getColLabel(s).index ? i : s;
                }
            }
        }//at this point, all bottom row entries are +ve.

        return s;
    }
}
