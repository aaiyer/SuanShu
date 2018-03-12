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
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.PermutationMatrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.matrix.doubles.operation.ElementaryOperation;
import com.numericalmethod.suanshu.misc.SuanShuUtils;
import static com.numericalmethod.suanshu.number.DoubleUtils.compare;
import com.numericalmethod.suanshu.vector.doubles.Vector;

/**
 * The Gaussian elimination performs elementary row operations to reduce a matrix to the row echelon form.
 * The two elementary row operations are:
 * swapping rows, and adding multiples of a row to another row.
 * This is equivalent to multiplying the original matrix with invertible matrices from the left.
 * For a square matrix, this algorithm essentially computes an LU decomposition.
 * We have,
 * <pre><i>
 * T * A == U
 * </i></pre>
 * where <i>T</i> is the transformation matrix, <i>U</i> is in the row echelon form.
 * And,
 * <pre><i>
 * P * A == L * U
 * </i></pre>
 * where <i>P</i> is the permutation matrix, <i>U</i> is lower triangular, <i>U</i> is in the row echelon form.
 *
 * @author Haksun Li
 * @see
 * <ul>
 * <li><a href="http://en.wikipedia.org/wiki/Gaussian_elimination">Wikipedia: Gaussian elimination</a>
 * <li><a href="http://en.wikipedia.org/wiki/Reduced_row_echelon_form#Reduced_row_echelon_form">Wikipedia: Row echelon form</a>
 * </ul>
 */
public class GaussianElimination {

    private ElementaryOperation T;
    private ElementaryOperation U;
    private Matrix L;
    private PermutationMatrix P;
    private final boolean usePivoting;
    private final int nRows;
    private final int nCols;
    private final double epsilon;

    /**
     * Run the Gaussian elimination algorithm.
     *
     * @param A           a matrix
     * @param usePivoting {@code true} if to use partial pivoting, e.g., for numerical stability.
     * In general, no pivoting means no row interchanges.
     * It can be done only if Gaussian elimination never runs into zeros on the diagonal.
     * Since division by zero is a fatal error we usually avoid no pivoting.
     * @param epsilon     a precision parameter: when a number |x| ≤ ε, it is considered 0
     */
    public GaussianElimination(Matrix A, boolean usePivoting, double epsilon) {
        this.nRows = A.nRows();
        this.nCols = A.nCols();
        this.usePivoting = usePivoting;
        this.epsilon = epsilon;

        this.T = new ElementaryOperation(nRows, nRows);
        this.U = new ElementaryOperation(A);
        this.L = new DenseMatrix(nRows, nRows).ONE();
        this.P = new PermutationMatrix(nRows);//TODO: test for rectangle matrices

        run();
    }

    /**
     * Run the Gaussian elimination algorithm with partial pivoting.
     *
     * @param A a matrix
     */
    public GaussianElimination(Matrix A) {
        this(A, true, SuanShuUtils.autoEpsilon(A));
    }

    /**
     * Get the transformation matrix, <i>T</i>, such that <i>T * A = U</i>.
     *
     * @return <i>T</i>
     */
    public Matrix T() {
        return T.T();
    }

    /**
     * Get the upper triangular matrix, <i>U</i>, such that
     * <i>T * A = U</i> and <i>P * A = L * U</i>.
     *
     * @return <i>U</i>
     */
    public Matrix U() {
        return U.T();
    }

    /**
     * Get the lower triangular matrix <i>L</i>, such that <i>P * A = L * U</i>.
     *
     * @return <i>L</i>
     */
    public Matrix L() {
        return L.deepCopy();
    }

    /**
     * Get the permutation matrix, <i>P</i>, such that <i>P * A = L * U</i>.
     *
     * @return <i>P</i>
     */
    public PermutationMatrix P() {
        return new PermutationMatrix(P);
    }

    /**
     * This implementation is based on the algorithm described in
     * Fundamentals of Matrix Computations
     * by Watkins D.
     * Wiley, 2002
     * p. 95 - 96
     */
    private void run() {
        for (int i = 1, j = 1; i <= nRows && j <= nCols;) {
            int maxRow = i;
            if (usePivoting) {
                for (int k = i + 1; k <= nRows; ++k) {
                    if (Math.abs(U.T().get(k, j)) > Math.abs(U.T().get(maxRow, j))) {
                        maxRow = k;
                    }
                }
            }

            /*
             * We cannot do
             * U.T().get(maxRow, j) == 0
             * because, for singular matrix, some entries do go to 0 theoretically after a series of transformation,
             * but becomes very tiny after numerical operations.
             *
             * We need to be able to tell whether an entry is practically 0 somehow.
             */
            if (compare(U.T().get(maxRow, j), 0, epsilon) != 0) {//the abs-max element in the column != 0
                //swap rows so that the biggest element in absolute value in the row-th column moves to the diagonal
                if (i != maxRow) {
                    U.swapRow(i, maxRow);
                    T.swapRow(i, maxRow);
                    P.swapRow(i, maxRow);
                    swapL(i, maxRow);
                }

                //subtract A[i,j] * i-th row from the k-th row
                for (int k = i + 1; k <= nRows; ++k) {
                    double scale = U.T().get(k, j) / U.T().get(i, j);
                    U.addRow(k, i, -scale);
                    T.addRow(k, i, -scale);
                    L.set(k, j, scale);
                }

                ++i;
            }

            ++j;
        }
    }

    /**
     * Swap row <i>i</i> with <i>maxRow</i> but up to only <i>(i-1)th</i> column.
     *
     * @param i      a row index
     * @param maxRow another row index
     */
    private void swapL(int i, int maxRow) {
        Vector row = L.getRow(i);
        for (int j = 1; j < i; ++j) {
            L.set(i, j, L.get(maxRow, j));
            L.set(maxRow, j, row.get(j));
        }
    }
}
