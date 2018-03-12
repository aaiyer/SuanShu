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
package com.numericalmethod.suanshu.matrix.generic.matrixtype;

import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.matrix.generic.Matrix;
import com.numericalmethod.suanshu.number.Real;

/**
 * This is a {@link Real} matrix.
 * Comparing to the {@code double}-based {@link DenseMatrix},
 * this class allows arbitrary precision arithmetic at the cost of (much) slower performance.
 *
 * @author Haksun Li
 */
public class RealMatrix implements Matrix<RealMatrix, Real> {

    /** the storage */
    private final GenericMatrix<Real> A;//composition over inheritance

    private RealMatrix(GenericMatrix<Real> A) {
        this.A = A;
    }

    /**
     * Construct a {@link Real} matrix.
     *
     * @param nRows the number of rows
     * @param nCols the number of columns
     */
    public RealMatrix(int nRows, int nCols) {
        this(new GenericMatrix<Real>(nRows, nCols, Real.ZERO));
    }

    /**
     * Construct a {@link Real} matrix.
     *
     * @param data a matrix data of {@link Real} numbers in a 2D array
     */
    public RealMatrix(Real[][] data) {
        this(new GenericMatrix<Real>(data));
    }

    /**
     * Construct a {@link Real} matrix.
     *
     * @param data a matrix data of {@code double}s in a 2D array
     */
    public RealMatrix(double[][] data) {
        this(toRealArray(data));
    }

    private static Real[][] toRealArray(double[][] data) {
        final int nRows = data.length;
        final int nCols = data[0].length;

        Real[][] result = new Real[nRows][nCols];
        for (int i = 0; i < nRows; ++i) {
            if (data[i].length != nCols) {
                // throw an exception if the input array is a jagged array
                throw new IllegalArgumentException("data is a jagged array");
            }
            for (int j = 0; j < nCols; ++j) {
                result[i][j] = new Real(data[i][j]);
            }
        }

        return result;
    }

    @Override
    public int nRows() {
        return A.nRows();
    }

    @Override
    public int nCols() {
        return A.nCols();
    }

    @Override
    public void set(int row, int col, Real value) {
        A.set(row, col, value);
    }

    @Override
    public Real get(int row, int col) {
        return A.get(row, col);
    }

    @Override
    public RealMatrix add(RealMatrix that) {
        return new RealMatrix(this.A.add(that.A));
    }

    @Override
    public RealMatrix minus(RealMatrix that) {
        return new RealMatrix(this.A.minus(that.A));
    }

    @Override
    public RealMatrix multiply(RealMatrix that) {
        return new RealMatrix(this.A.multiply(that.A));
    }

    @Override
    public RealMatrix scaled(Real scalar) {
        return new RealMatrix(A.scaled(scalar));
    }

    @Override
    public RealMatrix opposite() {
        return new RealMatrix(this.A.opposite());
    }

    @Override
    public RealMatrix ZERO() {
        return new RealMatrix(new GenericMatrix<Real>(nRows(), nCols(), Real.ZERO));
    }

    @Override
    public RealMatrix ONE() {
        return new RealMatrix(new GenericMatrix<Real>(nRows(), nCols(), Real.ZERO).ONE());//TODO: very in-efficient for having to first create a DenseMatrix
    }

    /**
     * Construct a {@link DenseMatrix} equivalent of this Real matrix (rounded if necessary).
     *
     * @return a {@link DenseMatrix} equivalent
     */
    public DenseMatrix doubleValue() {
        DenseMatrix M = new DenseMatrix(nRows(), nCols());

        for (int i = 1; i <= nRows(); ++i) {
            for (int j = 1; j <= nCols(); ++j) {
                M.set(i, j, A.get(i, j).doubleValue());
            }
        }

        return M;
    }

    @Override
    public String toString() {
        return A.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final RealMatrix other = (RealMatrix) obj;
        if (this.A != other.A && (this.A == null || !this.A.equals(other.A))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + (this.A != null ? this.A.hashCode() : 0);
        return hash;
    }
}
