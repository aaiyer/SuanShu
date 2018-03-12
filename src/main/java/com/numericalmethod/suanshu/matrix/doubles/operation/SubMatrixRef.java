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
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.mathoperation.MatrixMathOperation;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.mathoperation.SimpleMatrixMathOperation;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.operation.CreateVector;

/**
 * This is a 'reference' to a sub-matrix of a larger matrix without copying it.
 * For performance reason, we often want to work with a sub-matrix without copying it.
 * <p/>
 * The reference sub-matrix is <em>immutable</em>.
 *
 * @author Haksun Li
 */
public class SubMatrixRef implements Matrix {

    /** the matrix to be referenced */
    private final Matrix ref;
    /** the starting row, counting from 1 */
    private final int rowFrom;
    /** the ending row, counting from 1 */
    private final int rowTo;
    /** the starting column, counting from 1 */
    private final int colFrom;
    /** the ending column, counting from 1 */
    private final int colTo;
    private final MatrixMathOperation matrixComputation = new SimpleMatrixMathOperation();

    /**
     * Construct a sub-matrix reference.
     *
     * @param A       a matrix
     * @param rowFrom the beginning row index
     * @param rowTo   the ending row index
     * @param colFrom the beginning column index
     * @param colTo   the ending column index
     * @throws IndexOutOfBoundsException if <i>rowFrom</i>, <i>rowTo</i>, <i>colFrom</i>, or <i>colTo</i> is invalid
     */
    public SubMatrixRef(Matrix A, int rowFrom, int rowTo, int colFrom, int colTo) {
        if (rowFrom < 1) {
            throw new IndexOutOfBoundsException("rowFrom is not valid; must count from 1");
        } else if (rowTo > A.nRows()) {
            throw new IndexOutOfBoundsException("rowTo is not valid; cannot exceed A's nrows");
        } else if (colFrom < 1) {
            throw new IndexOutOfBoundsException("colFrom is not valid; must count from 1");
        } else if (colTo > A.nCols()) {
            throw new IndexOutOfBoundsException("colTo is not valid; cannot exceed A's ncols");
        }

        this.ref = A;
        this.rowFrom = rowFrom;
        this.rowTo = rowTo;
        this.colFrom = colFrom;
        this.colTo = colTo;
    }

    /**
     * Construct a reference to the whole matrix.
     *
     * @param A a matrix
     */
    public SubMatrixRef(Matrix A) {
        this.ref = A;
        this.rowFrom = 1;
        this.rowTo = A.nRows();
        this.colFrom = 1;
        this.colTo = A.nCols();
    }

    @Override
    public int nRows() {
        return (rowTo - rowFrom + 1);
    }

    @Override
    public int nCols() {
        return (colTo - colFrom + 1);
    }

    @Override
    public double get(int i, int j) {
        return ref.get(i + rowFrom - 1, j + colFrom - 1);
    }

    @Override
    public Vector getRow(int i) {
        Vector v = ref.getRow(i + rowFrom - 1);
        Vector u = CreateVector.subVector(v, colFrom, colTo);
        return u;
    }

    @Override
    public Vector getColumn(int j) {
        Vector v = ref.getColumn(j + colFrom - 1);
        Vector u = CreateVector.subVector(v, rowFrom, rowTo);
        return u;
    }

    @Override
    public Matrix add(Matrix that) {
        return matrixComputation.add(this, that);
    }

    @Override
    public Matrix minus(Matrix that) {
        return matrixComputation.minus(this, that);
    }

    @Override
    public Matrix multiply(Matrix that) {
        return matrixComputation.multiply(this, that);
    }

    @Override
    public Vector multiply(Vector v) {
        return matrixComputation.multiply(this, v);
    }

    @Override
    public Matrix scaled(double scalar) {
        return new DenseMatrix(this).scaled(scalar);
    }

    @Override
    public Matrix opposite() {
        return this.scaled(-1);
    }

    @Override
    public Matrix ZERO() {
        return new DenseMatrix(nRows(), nCols()).ZERO();
    }

    @Override
    public Matrix ONE() {
        return new DenseMatrix(nRows(), nCols()).ONE();
    }

    @Override
    public String toString() {
        return new DenseMatrix(this).toString();
    }

    @Override
    public Matrix t() {
        return new DenseMatrix(this).t();
    }

    /**
     * Return {@code this} as the reference is immutable.
     *
     * @return {@code this}
     */
    @Override
    public SubMatrixRef deepCopy() {
        return this; // immutable matrix
    }

    /**
     * @deprecated SubMatrixRef is immutable
     */
    @Deprecated
    @Override
    public void set(int i, int j, double value) throws MatrixAccessException {
        throw new MatrixAccessException("SubMatrixRef is immutable.");
    }
}
