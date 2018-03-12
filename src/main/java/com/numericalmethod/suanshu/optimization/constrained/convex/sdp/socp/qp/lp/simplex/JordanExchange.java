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

import com.numericalmethod.suanshu.datastructure.FlexibleTable;
import com.numericalmethod.suanshu.matrix.doubles.MatrixTable;

/**
 * Jordan Exchange swaps the r-th entering variable (row) with the s-th leaving variable (column) in a matrix <i>A</i>.
 *
 * @author Chen Chen
 * @see "Michael C. Ferris, Olvi L. Mangasarian, Stephen J. Wright, "Section 2.1," Linear Programming with MATLAB."
 */
public class JordanExchange extends FlexibleTable {

    /**
     * Construct a new table by exchanging the r-th row with the s-th column in <i>A</i> using Jordan Exchange.
     *
     * @param A a table
     * @param r the r-th entering variable (row)
     * @param s the s-th leaving variable (column)
     */
    public JordanExchange(MatrixTable A, int r, int s) {
        super(A.nRows(), A.nCols());

        double pivot = A.get(r, s);
        if (pivot == 0) {
            throw new RuntimeException(String.format("zero pivot found at [%d, %d] (linear dependence detected)", r, s));
        }

        //update pivot row
        for (int j = 1; j <= A.nCols(); j++) {
            set(r, j, -A.get(r, j) / pivot);
        }

        //update pivot column
        for (int i = 1; i <= A.nRows(); i++) {
            set(i, s, A.get(i, s) / pivot);
        }

        //update pivot position
        set(r, s, 1.0 / pivot);

        //update remainder of table
        for (int i = 1; i <= A.nRows(); i++) {
            for (int j = 1; j <= A.nCols(); j++) {
                if (i != r && j != s) {
                    set(i, j, A.get(i, j) - get(i, s) * A.get(r, j));
                }
            }
        }
    }

    /**
     * Construct a new table by exchanging the r-th row with the s-th column in <i>A</i> using Jordan Exchange.
     * For a {@link FlexibleTable}, we maintain the row and columns labels.
     *
     * @param A a table
     * @param r the r-th entering variable (row)
     * @param s the s-th leaving variable (column)
     */
    public JordanExchange(FlexibleTable A, int r, int s) {
        this((MatrixTable) A, r, s);

        //fix the labels
        for (int i = 1; i <= nRows(); ++i) {
            renameRow(i, A.getRowLabel(i));
        }

        for (int i = 1; i <= nCols(); ++i) {
            renameCol(i, A.getColLabel(i));
        }

        //swap labels
        Object tmp = getRowLabel(r);
        renameRow(r, getColLabel(s));
        renameCol(s, tmp);
    }
}
