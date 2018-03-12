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
package com.numericalmethod.suanshu.matrix.doubles.factorization.triangle;

import com.numericalmethod.suanshu.datastructure.DimensionCheck;
import com.numericalmethod.suanshu.matrix.MatrixSingularityException;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.PermutationMatrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.triangle.LowerTriangularMatrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.triangle.UpperTriangularMatrix;
import com.numericalmethod.suanshu.matrix.doubles.operation.MatrixUtils;
import com.numericalmethod.suanshu.misc.SuanShuUtils;
import static com.numericalmethod.suanshu.number.DoubleUtils.compare;
import com.numericalmethod.suanshu.vector.doubles.Vector;

/**
 * Doolittle algorithm is an LU decomposition of a <em>square</em> matrix.
 * This implementation uses column/partial pivoting.
 * It iteratively fills the first row, then first column, then second row, second column, etc.
 * Before each iteration, it swaps the rows so that the biggest entry in the column is on the main diagonal.
 * If this biggest entry is 0, the Doolittle construction fails and throws a runtime exception.
 * On success, we have <i>P * A = L * U</i>, where
 * <i>A</i> is an <i>n x n</i> matrix;
 * <i>P</i> is an <i>n x n</i> pivoting matrix;
 * <i>L</i> is an <i>n x n</i> lower triangular matrix;
 * <i>U</i> is an <i>n x n</i> upper triangular matrix.
 * That is,
 * <blockquote><code>P.multiply(A) = L.multiply(U)</code></blockquote>
 * Not every non-singular matrix can be LU decomposed but some singular matrix can have valid LU decomposition.
 * For example, the following singular matrix has LU decomposition.
 * \[
 * \begin{bmatrix}
 * 1 & 0 & 0\\
 * 0 & 0 & 2\\
 * 0 & 1 & -1
 * \end{bmatrix}
 * \]
 * On the other hand, the LU decomposition with pivoting always exists, even if the matrix is singular.
 *
 * @author Haksun Li
 */
public class Doolittle implements LUDecomposition {

    private LowerTriangularMatrix L;
    private UpperTriangularMatrix U;
    private PermutationMatrix P;
    private double[][] data;//make a copy of the 2D matrix from the input matrix
    private final int dim;// the dimension of A
    private final boolean usePivoting;
    private final double epsilon;

    /**
     * Run the Doolittle algorithm on a square matrix for LU decomposition.
     *
     * @param A           a square matrix
     * @param usePivoting {@code true} if partial pivoting is wanted, e.g., for numerical stability
     * @param epsilon     a precision parameter: when a number |x| ≤ ε, it is considered 0
     * @throws IllegalArgumentException if <i>A</i> is not square
     */
    public Doolittle(Matrix A, boolean usePivoting, double epsilon) {
        if (!DimensionCheck.isSquare(A)) {
            throw new IllegalArgumentException("the LU decomposition applies to square matrix only");
        }

        dim = A.nRows();
        this.usePivoting = usePivoting;
        this.epsilon = epsilon;

        P = new PermutationMatrix(dim);
        U = new UpperTriangularMatrix(dim);
        U.set(1, 1, 0);//allocate space
        L = new LowerTriangularMatrix(dim);
        for (int i = 1; i <= dim; ++i) {
            L.set(i, i, 1);//doolittle assumption
        }

        data = MatrixUtils.to2DArray(A);

        run();

        data = null;//release the data to save memory
    }

    /**
     * Run the Doolittle algorithm on a square matrix for LU decomposition.
     *
     * @param A a square matrix
     * @throws IllegalArgumentException if <i>A</i> is not square
     */
    public Doolittle(Matrix A) {
        this(A, true, SuanShuUtils.autoEpsilon(A));
    }

    @Override
    public LowerTriangularMatrix L() {
        return new LowerTriangularMatrix(L);
    }

    @Override
    public UpperTriangularMatrix U() {
        return new UpperTriangularMatrix(U);
    }

    @Override
    public PermutationMatrix P() {
        return new PermutationMatrix(P);
    }

    /**
     * an implementation of Doolittle algorithm
     *
     * @throws MatrixSingularityException if the matrix is singular
     */
    private void run() {
        for (int k = 1; k <= dim; ++k) {
            if (usePivoting) {
                pivoting(k);
            }

            Vector Lrow;
            Vector Ucol;

            /*
             * filling U's iter-th row
             * U[k, col] = A[k, col] - Sum{L[k, i] * U[i, k]; 1 <= i < iter}
             */
            Lrow = L.getRow(k);//Lrow is irrelevant when filling the first row
            for (int i = k; i <= dim; ++i) {//filling the row by columns
                Ucol = U.getColumn(i);
                double value = 0;//dot product: Lcol * Urow
                for (int j = 1; j < k; ++j) {
                    value += Lrow.get(j) * Ucol.get(j);
                }
                value = data[k - 1][i - 1] - value;
                U.set(k, i, value);
            }

            /*
             * filling L's iter-th column
             * L[i, k] = A[i, k] - Sum{L[i, i] * U[i, k]; 1 <= i < k}
             */
            Ucol = U.getColumn(k);
            for (int i = k + 1; i <= dim; ++i) {//filling the column by rows
                Lrow = L.getRow(i);
                double value = 0;//dot product: row of L * col of U
                for (int j = 1; j < k; ++j) {
                    value += Lrow.get(j) * Ucol.get(j);
                }
                value = data[i - 1][k - 1] - value;

                double uDiag = U.get(k, k);

                /*
                 * We cannot do uDiag == 0 because
                 * for singular matrix, some entries do go to 0 theoretically after a series of transformation,
                 * but becomes very tiny after numerical operations.
                 *
                 * We need to be able to tell whether an entry is practically 0 somehow.
                 */
                if (compare(value, 0, epsilon) != 0 && compare(uDiag, 0, epsilon) == 0) {
                    throw new MatrixSingularityException("singularity detected during the LU decomposition; if pivoting is used, try a bigger epsilon");
                } else if (compare(uDiag, 0, epsilon) == 0) {//compare(value, 0, epsilon) == 0
                    value = 1;//0/0 = 1
                } else {//normal case
                    value /= uDiag;
                }

                L.set(i, k, value);
            }
        }
    }

    /**
     * partial pivoting:
     * swap rows so that the biggest element in absolute value
     * in the i-th column moves to the diagonal
     */
    private void pivoting(int i) {//row counts from 1
        final int i1 = i - 1;//_row counts from 0

        int pivotRow = i;
        double maxPivot = Double.NEGATIVE_INFINITY;
        for (int j = i; j <= dim; ++j) {//rows
            //compute and compare the potential pivots
            double pivot4row = pivot(j, i);
            if (pivot4row > maxPivot) {
                pivotRow = j;
                maxPivot = pivot4row;
            }
        }

        if (pivotRow > i) {
            /*
             * swap the two rows in data, but no need the full row
             * chop the beginning
             */
            for (int j = i1; j < dim; ++j) {//columns
                double tmp = data[i1][j];
                data[i1][j] = data[pivotRow - 1][j];
                data[pivotRow - 1][j] = tmp;
            }

            /*
             * swap the two rows in L, but no need the full row
             * chop the ending 0s
             */
            for (int j = 1; j < i; ++j) {//columns
                double tmp = L.get(i, j);
                L.set(i, j, L.get(pivotRow, j));
                L.set(pivotRow, j, tmp);
            }

            //record the swaps
            P.swapRow(pivotRow, i);
        }
    }

    /**
     * Compute the pivot for a row.
     * This assumes that L and U are partially filled.
     *
     * @param i
     * @return
     */
    private double pivot(int i, int j) {
        double result = data[i - 1][j - 1];
        for (int k = 1; k < i; ++k) {
            result -= L.get(i, k) * U.get(k, j);
        }

        return Math.abs(result);
    }
}
