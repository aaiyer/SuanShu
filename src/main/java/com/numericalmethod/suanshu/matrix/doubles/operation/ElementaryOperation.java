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
package com.numericalmethod.suanshu.matrix.doubles.operation;

import com.numericalmethod.suanshu.matrix.MatrixAccessException;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.MatrixTable;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.vector.doubles.Vector;

/**
 * There are three elementary row operations which are equivalent to left multiplying an elementary matrix.
 * They are row switching, row multiplication, and row addition.
 * By applying these operations to an identity matrix, <i>I</i>,
 * the resultant matrix, <i>T</i>, is a transformation matrix, such that
 * left multiplying <i>T</i> with a matrix <i>A</i>, i.e., <i>T * A</i>,
 * is equivalent to applying the same sequence of operations to <i>A</i>.
 * Similarly, the three elementary column operations are:
 * column switching, column multiplication, and column addition.
 * Column operations correspond to right multiplying a transformation matrix.
 *
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/Elementary_row_operations">Wikipedia: Elementary matrix</a>
 */
public class ElementaryOperation implements MatrixTable {

    /** the resultant transformation */
    private DenseMatrix T;

    /**
     * Construct a transformation matrix of elementary operations.
     * The initial transformation matrix <i>T</i> is an identity matrix, if it is square.
     * Otherwise, the rightmost columns are padded with zeros.
     *
     * @param nRows the number of rows of <i>T</i>
     * @param nCols the number of columns of <i>T</i>
     */
    public ElementaryOperation(int nRows, int nCols) {//TODO: write test cases
        T = new DenseMatrix(nRows, nCols).ONE();
    }

    /**
     * Construct a transformation matrix of elementary operations.
     * The initial transformation matrix <i>T</i> is an identity matrix.
     *
     * @param dim the dimension of <i>T</i>
     */
    public ElementaryOperation(int dim) {
        this(dim, dim);
    }

    /**
     * Transform A by elementary operations.
     *
     * @param A a matrix
     */
    public ElementaryOperation(Matrix A) {
        this.T = new DenseMatrix(A);
    }

    @Override
    public int nRows() {
        return T.nRows();
    }

    @Override
    public int nCols() {
        return T.nCols();
    }

    @Override
    public double get(int i, int j) {
        return T.get(i, j);
    }

    /**
     * Get the transformed matrix <i>T</i>.
     *
     * @return <i>T</i>
     */
    public Matrix T() {
        return T.deepCopy();
    }

    /**
     * Swap rows:
     * <blockquote><pre><i>
     * A[i<sub>1</sub>, ] = A[i<sub>2</sub>, ]
     * A[i<sub>2</sub>, ] = A[i<sub>1</sub>, ]
     * </i></pre><blockquote>
     *
     * @param i1 becoming row <i>i<sub>2</sub></i>
     * @param i2 becoming row <i>i<sub>1</sub></i>
     */
    public void swapRow(int i1, int i2) {
        Vector tmp = T.getRow(i1);

        final int nCols = T.nCols();

        for (int i = 1; i <= nCols; ++i) {
            T.set(i1, i, T.get(i2, i));//A[row1, ] = A[row2, ]
        }

        for (int i = 1; i <= nCols; ++i) {
            T.set(i2, i, tmp.get(i));//A[row2, ] = A[row1, ]
        }
    }

    /**
     * Scale a row:
     * <blockquote><i>
     * A[i, ] = c * A[i, ]
     * </i><blockquote>
     *
     * @param i the row to be scaled
     * @param c the scaling factor
     */
    public void scaleRow(int i, double c) {
        for (int j = 1; j <= T.nCols(); ++j) {
            T.set(i, j, c * T.get(i, j));//A[row, ] = scale * A[row, ]
        }
    }

    /**
     * Row addition:
     * <blockquote><i>
     * A[i<sub>1</sub>, ] = A[i<sub>1</sub>, ] + c * A[i<sub>2</sub>, ]
     * </i></blockquote>
     *
     * @param i1 addend; the row to add to; the row is modified afterward
     * @param i2 the row to add with
     * @param c  the scaling factor for row <i>i<sub>2</sub></i>
     */
    public void addRow(int i1, int i2, double c) {
        for (int i = 1; i <= T.nCols(); ++i) {
            T.set(i1, i, T.get(i1, i) + c * T.get(i2, i));//A[row1, ] = A[row1, ] + scale * A[row2, ]
        }
    }

    /**
     * Swap columns:
     * <blockquote><pre><i>
     * A[, j<sub>1</sub>] = A[, j<sub>2</sub>]
     * A[, j<sub>2</sub>] = A[, j<sub>1</sub>]
     * <i></pre><blockquote>
     *
     * @param j1 becoming column <i>j<sub>2</sub></i>
     * @param j2 becoming column <i>j<sub>1</sub></i>
     */
    public void swapColumn(int j1, int j2) {
        Vector tmp = T.getColumn(j1);

        for (int i = 1; i <= T.nRows(); ++i) {
            T.set(i, j1, T.get(i, j2));//A[col1, ] = A[col2, ]
        }

        for (int i = 1; i <= T.nRows(); ++i) {
            T.set(i, j2, tmp.get(i));//A[col2, ] = A[col1, ]
        }
    }

    /**
     * Scale a column:
     * <blockquote><i>
     * A[, j] = c * A[, j]
     * </i></blockquote>
     *
     * @param j the column to be scaled
     * @param c the scaling factor
     */
    public void scaleColumn(int j, double c) {
        for (int i = 1; i <= T.nRows(); ++i) {
            T.set(i, j, c * T.get(i, j));//A[col, ] = scale * A[col, ]
        }
    }

    /**
     * Column addition:
     * <blockquote><i>
     * A[, j<sub>1</sub>] = A[, j<sub>1</sub>] + c * A[, j<sub>2</sub>]
     * </i></blockquote>
     *
     * @param j1 addend; the column to add to; the column is modified afterward
     * @param j2 the column to add with
     * @param c  the scaling factor for
     * <code>col2</code>
     */
    public void addColumn(int j1, int j2, double c) {
        for (int i = 1; i <= T.nRows(); ++i) {
            T.set(i, j1, T.get(i, j1) + c * T.get(i, j2));
        }
    }

    @Override
    public String toString() {
        return T.toString();
    }

    @Deprecated
    @Override
    public void set(int i, int j, double value) throws MatrixAccessException {
        throw new UnsupportedOperationException("please use the elementary operations");
    }
}
