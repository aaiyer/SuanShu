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
import com.numericalmethod.suanshu.number.complex.Complex;

/**
 * This is a {@link Complex} matrix.
 *
 * @author Haksun Li
 */
public class ComplexMatrix implements Matrix<ComplexMatrix, Complex> {

    /** the storage */
    private final GenericMatrix<Complex> A;////composition over inheritance

    private ComplexMatrix(GenericMatrix<Complex> A) {
        this.A = A;
    }

    /**
     * Construct a {@link Complex} matrix.
     *
     * @param nRows the number of rows
     * @param nCols the number of columns
     */
    public ComplexMatrix(int nRows, int nCols) {
        A = new GenericMatrix<Complex>(nRows, nCols, Complex.ZERO);
    }

    /**
     * Construct a {@link Complex} matrix.
     *
     * @param data a matrix data of {@link Complex} numbers in a 2D array
     */
    public ComplexMatrix(Complex[][] data) {
        A = new GenericMatrix<Complex>(data);
    }

    /**
     * Construct a {@link Complex} matrix.
     *
     * @param data a matrix data of {@code double}s in a 2D array
     */
    public ComplexMatrix(double[][] data) {
        this(toComplexArray(data));
    }

    private static Complex[][] toComplexArray(double[][] data) {
        int nrows = data.length;
        int ncols = data[0].length;
        Complex[][] result = new Complex[nrows][ncols];

        for (int i = 0; i < nrows; ++i) {
            if (data[i].length != ncols) {
                // throw an exception if the input array is a jagged array
                throw new IllegalArgumentException("data is a jagged array");
            }
            for (int j = 0; j < ncols; ++j) {
                result[i][j] = new Complex(data[i][j]);
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
    public void set(int row, int col, Complex value) {
        A.set(row, col, value);
    }

    @Override
    public Complex get(int row, int col) {
        return A.get(row, col);
    }

    @Override
    public ComplexMatrix add(ComplexMatrix that) {
        return new ComplexMatrix(this.A.add(that.A));
    }

    @Override
    public ComplexMatrix minus(ComplexMatrix that) {
        return new ComplexMatrix(this.A.minus(that.A));
    }

    @Override
    public ComplexMatrix multiply(ComplexMatrix that) {
        return new ComplexMatrix(this.A.multiply(that.A));
    }

    @Override
    public ComplexMatrix scaled(Complex scalar) {
        return new ComplexMatrix(A.scaled(scalar));
    }

    @Override
    public ComplexMatrix opposite() {
        return new ComplexMatrix(this.A.opposite());
    }

    @Override
    public ComplexMatrix ZERO() {
        return new ComplexMatrix(new GenericMatrix<Complex>(nRows(), nCols(), Complex.ZERO));
    }

    @Override
    public ComplexMatrix ONE() {
        //TODO: very in-efficient for having to first create a GenericMatrix
        return new ComplexMatrix(new GenericMatrix<Complex>(nRows(), nCols(), Complex.ZERO).ONE());
    }

    /**
     * Construct a {@link DenseMatrix} equivalent of this Complex matrix (rounded if necessary).
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
        final ComplexMatrix other = (ComplexMatrix) obj;
        if (this.A != other.A && (this.A == null || !this.A.equals(other.A))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 61 * hash + (this.A != null ? this.A.hashCode() : 0);
        return hash;
    }
}
