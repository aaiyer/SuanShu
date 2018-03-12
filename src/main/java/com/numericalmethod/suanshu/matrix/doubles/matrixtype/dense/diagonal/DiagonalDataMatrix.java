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
package com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.diagonal;

import com.numericalmethod.suanshu.matrix.MatrixAccessException;
import com.numericalmethod.suanshu.matrix.doubles.AreMatrices;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.Densifiable;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.mathoperation.MatrixMathOperation;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.mathoperation.SimpleMatrixMathOperation;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;

/**
 * This abstract class provides an implementation of different diagonal matrices.
 *
 * <p>
 * It exists mainly for code reuse by subclasses.
 *
 * @author Haksun Li
 */
abstract class DiagonalDataMatrix implements Matrix, Densifiable {

    /** the storage */
    DiagonalData storage;
    private final MatrixMathOperation math = new SimpleMatrixMathOperation();

    /**
     * Construct a matrix implemented using {@code DiagonalData}.
     *
     * @param storage the matrix entries
     */
    DiagonalDataMatrix(DiagonalData data) {
        this.storage = data;
    }

    @Override
    public DenseMatrix toDense() {
        return storage.toDense();
    }

    /**
     * Get the main diagonal of the matrix.
     *
     * @return the main diagonal
     */
    public DenseVector getDiagonal() {
        return new DenseVector(storage.getDiagonal());
    }

    /**
     * Get the super-diagonal of the matrix.
     *
     * @return the super-diagonal
     */
    public DenseVector getSuperDiagonal() {
        return new DenseVector(storage.getSuperDiagonal());
    }

    /**
     * Get the sub-diagonal of the matrix.
     *
     * @return the sub-diagonal
     */
    public DenseVector getSubDiagonal() {
        return new DenseVector(storage.getSubDiagonal());
    }

    @Override
    public int nRows() {
        return storage.nRows();
    }

    @Override
    public int nCols() {
        return storage.nCols();
    }

    @Override
    public void set(int i, int j, double value) throws MatrixAccessException {
        storage.set(i, j, value);
    }

    @Override
    public double get(int i, int j) throws MatrixAccessException {
        return storage.get(i, j);
    }

    @Override
    public Vector getRow(int i) {
        DenseVector result = new DenseVector(storage.nRows());

        if (i > 1) {
            result.set(i - 1, get(i, i - 1));
        }

        result.set(i, get(i, i));

        if (i < storage.nRows()) {
            result.set(i + 1, get(i, i + 1));
        }

        return result;
    }

    @Override
    public Vector getColumn(int j) {
        DenseVector result = new DenseVector(storage.nCols());

        if (j > 1) {
            result.set(j - 1, get(j - 1, j));
        }

        result.set(j, get(j, j));

        if (j < storage.nCols()) {
            result.set(j + 1, get(j + 1, j));
        }

        return result;
    }

    @Override
    public Matrix add(Matrix that) {
        return math.add(this, that);
    }

    @Override
    public Matrix minus(Matrix that) {
        return math.minus(this, that);
    }

    @Override
    public Matrix multiply(Matrix that) {
        return math.multiply(this, that);
    }

    @Override
    public Vector multiply(Vector v) {
        return math.multiply(this, v);
    }

    @Override
    public String toString() {
        return storage.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof DiagonalDataMatrix) && (obj instanceof Matrix)) {
            return AreMatrices.equal(this, (Matrix) obj, 0);
        }
        final DiagonalDataMatrix that = (DiagonalDataMatrix) obj;
        if (this.storage.nRows() != that.storage.nRows()) {
            return false;
        }
        if (this.storage != that.storage && (this.storage == null || !this.storage.equals(that.storage))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + this.storage.nRows();
        hash = 79 * hash + (this.storage != null ? this.storage.hashCode() : 0);
        return hash;
    }
}
