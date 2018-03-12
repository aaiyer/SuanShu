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

import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.lp.exception.LPUnbounded;
import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.lp.simplex.SimplexTable;

/**
 * A simplex pivoting finds a row and column to exchange to reduce the cost function.
 *
 * @author Haksun Li
 */
public interface SimplexPivoting {

    /**
     * the pivot
     */
    public class Pivot {//a read-only structure

        /** the pivot row */
        public final int r;
        /** the pivot column */
        public final int s;

        /**
         * Construct a pivot.
         *
         * @param r the pivot row
         * @param s the pivot column
         */
        public Pivot(int r, int s) {
            this.r = r;
            this.s = s;
        }
    }

    /**
     * This is pivot column selection (pricing) rule.
     *
     * @param table a simplex table
     * @return the pivot column
     */
    public int pricing(SimplexTable table);

    /**
     * This is pivot row selection (Ratio test) rule.
     *
     * @param table a simplex table
     * @param s     a column index
     * @return the pivot row
     */
    public int ratioTest(SimplexTable table, int s);

    /**
     * Get the next pivot.
     *
     * @param table a simplex table
     * @return a pivot to reduce the cost; {@code null} if the {@code table} already optimal
     * @throws LPUnbounded if the table is unbounded
     */
    public Pivot getPivot(SimplexTable table) throws LPUnbounded;
}
