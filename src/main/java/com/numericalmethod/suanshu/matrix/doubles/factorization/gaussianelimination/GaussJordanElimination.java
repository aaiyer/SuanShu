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
package com.numericalmethod.suanshu.matrix.doubles.factorization.gaussianelimination;

import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.operation.ElementaryOperation;
import com.numericalmethod.suanshu.misc.SuanShuUtils;
import static com.numericalmethod.suanshu.number.DoubleUtils.compare;

/**
 * Gauss-Jordan elimination performs elementary row operations to reduce a matrix to the reduced row echelon form.
 * The three elementary row operations are:
 * scaling rows, swapping rows, and adding multiples of a row to another row.
 * That is,
 * <i>T * A == U</i>
 * where <i>T</i> is the transformation matrix, <i>U</i> is in the reduced row echelon form.
 * <p/>
 * This implementation makes sure that the leading 1s are numerically 1 for comparison purpose.
 * Suppose there is a leading 1 at [i,j], {@code U.get(i, j) == 1} always returns {@code true}.
 *
 * @author Haksun Li
 * @see
 * <ul>
 * <li><a href="http://en.wikipedia.org/wiki/Gauss%E2%80%93Jordan_elimination">Wikipedia: Gauss–Jordan elimination</a>
 * <li><a href="http://en.wikipedia.org/wiki/Reduced_row_echelon_form#Reduced_row_echelon_form">Wikipedia: Row echelon form</a>
 * </ul>
 */
public class GaussJordanElimination {

    private ElementaryOperation T;
    private ElementaryOperation U;
    private final boolean usePivoting;//{@code true} if partial pivoting is wanted, e.g., for numerical stability
    private final int nRows;//the number of rows
    private final int nCols;//the number of columns
    private final double epsilon;

    /**
     * Run the Gauss-Jordan elimination algorithm.
     *
     * @param A           a matrix
     * @param usePivoting {@code true} if partial pivoting is wanted, e.g., for numerical stability
     * @param epsilon     a precision parameter: when a number |x| ≤ ε, it is considered 0
     */
    public GaussJordanElimination(Matrix A, boolean usePivoting, double epsilon) {
        this.usePivoting = usePivoting;
        this.nRows = A.nRows();
        this.nCols = A.nCols();
        this.epsilon = epsilon;

        this.T = new ElementaryOperation(nRows, nRows);
        this.U = new ElementaryOperation(A);

        run();
    }

    /**
     * Run the Gauss-Jordan elimination algorithm.
     *
     * @param A a matrix
     */
    public GaussJordanElimination(Matrix A) {
        this(A, true, SuanShuUtils.autoEpsilon(A));
    }

    /**
     * Get the transformation matrix, <i>T</i>, such that <i>T * A = U</i>.
     *
     * @return the transformation matrix <i>T</i>
     */
    public Matrix T() {
        return T.T();
    }

    /**
     * Get the reduced row echelon form matrix, <i>U</i>, such that <i>T * A = U</i>.
     *
     * @return the reduced row echelon form matrix
     */
    public Matrix U() {
        return U.T();
    }

    /**
     * an implementation of the Gauss-Jordan elimination algorithm
     */
    private void run() {
        /*
         * Remember, for each row, the column position of the leading 1.
         * If 0, it means that the row does not have a leading 1.
         */
        int[] leading1s = new int[nRows + 1];//initialized to 0s

        for (int i = 1, j = 1; i <= nRows && j <= nCols;) {
            int maxRow = i;
            if (usePivoting) {
                for (int k = i + 1; k <= nRows; ++k) {
                    if (Math.abs(U.get(k, j)) > Math.abs(U.get(maxRow, j))) {
                        maxRow = k;
                    }
                }
            }

            /*
             * We cannot do
             * U.get(maxRow, j) == 0
             * because, for singular matrix, some entries do go to 0 theoretically after a series of transformation,
             * but becomes very tiny after numerical operations.
             *
             * We need to be able to tell whether an entry is practically 0 somehow.
             */
            if (compare(U.get(maxRow, j), 0, epsilon) != 0) {//the abs-max element in the column != 0
                //swap rows so that the biggest element in absolute value in the row-th column moves to the diagonal
                if (i != maxRow) {
                    U.swapRow(i, maxRow);
                    T.swapRow(i, maxRow);
                }

                //scaling the row to get a leading 1
                double scale = U.get(i, j);
                U.scaleRow(i, 1d / scale);//TODO: could this have division by zero problem?
                leading1s[i] = j;//row i has the leading 1 in column j
                T.scaleRow(i, 1d / scale);

                //subtract A[i,j] * i-th row from the k-th row
                for (int k = 1; k <= nRows; ++k) {
                    if (k == i) {
                        continue;
                    }

                    scale = U.get(k, j) / U.get(i, j);
                    U.addRow(k, i, -scale);
                    T.addRow(k, i, -scale);
                }

                ++i;//try to make the rows below 0s
            }

            ++j;//try to make leading 1s by columns
        }

        //Round the leading 1s to {@code double} 1.
        Matrix UT = U.T();
        for (int i = 1; i <= nRows; ++i) {
            if (leading1s[i] > 0) {//there is no leading 1 in this row, e.g., all 0s
                UT.set(i, leading1s[i], 1);
            }
        }

        U = new ElementaryOperation(UT);
    }
}
