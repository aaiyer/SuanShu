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

import static com.numericalmethod.suanshu.datastructure.DimensionCheck.*;
import com.numericalmethod.suanshu.mathstructure.Field;
import com.numericalmethod.suanshu.matrix.MatrixAccessException;
import com.numericalmethod.suanshu.matrix.generic.Matrix;
import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * This is a generic matrix over a {@link Field}.
 *
 * @param <F> the number {@link Field}
 * @author Haksun Li
 */
public class GenericMatrix<F extends Field<F>> implements Matrix<GenericMatrix<F>, F> {

    /** the number of rows */
    private final int nRows;
    /** the number of columns */
    private final int nCols;
    /** the matrix entries arranged in a 2D array */
    private F[][] data; // TODO: support sparse matrix for generics as well
    /**
     * This is an element from the field {@code F}.
     * This member is only for programming purpose to get information about the field.
     */
    private final F field;

    /**
     * Construct a matrix over a field.
     *
     * @param nRows the number of rows
     * @param nCols the number of columns
     * @param init  an initial value for the entries, e.g., 0
     */
    @SuppressWarnings("unchecked")
    public GenericMatrix(int nRows, int nCols, F init) {
        this.nRows = nRows;
        this.nCols = nCols;
        this.field = init;
        this.data = (F[][]) Array.newInstance(init.getClass(), nRows, nCols);

        for (int i = 0; i < nRows; ++i) {
            for (int j = 0; j < nCols; ++j) {
                data[i][j] = init;
            }
        }
    }

    /**
     * Construct a matrix over a field.
     *
     * @param data the matrix entries
     */
    @SuppressWarnings("unchecked")
    public GenericMatrix(F[][] data) {
        this.nRows = data.length;
        this.nCols = data[0].length;
        this.field = data[0][0].ZERO();
        this.data = (F[][]) Array.newInstance(field.getClass(), nRows, nCols);

        for (int i = 0; i < nRows; ++i) {
            //Make sure values is not a jagged array. Throw an exception if otherwise.
            if (data[i].length != nCols) {
                this.data = null;
                throw new IllegalArgumentException("data is a jagged array");
            }
            for (int j = 0; j < nCols; ++j) {
                this.data[i][j] = data[i][j];
            }
        }
    }

    @Override
    public int nRows() {
        return nRows;
    }

    @Override
    public int nCols() {
        return nCols;
    }

    @Override
    public void set(int row, int col, F value) throws MatrixAccessException {
        throwIfInvalidRow(this, row);
        throwIfInvalidColumn(this, col);

        data[row - 1][col - 1] = value;
    }

    @Override
    public F get(int row, int col) throws MatrixAccessException {
        throwIfInvalidRow(this, row);
        throwIfInvalidColumn(this, col);

        return data[row - 1][col - 1];
    }

    @Override
    public GenericMatrix<F> add(GenericMatrix<F> that) {
        throwIfDifferentDimension(this, that);

        GenericMatrix<F> result = new GenericMatrix<F>(nRows, nCols, field.ZERO());
        for (int i = 0; i < nRows; ++i) {
            for (int j = 0; j < nCols; ++j) {
                result.data[i][j] = this.data[i][j].add(that.data[i][j]);
            }
        }

        return result;
    }

    @Override
    public GenericMatrix<F> minus(GenericMatrix<F> that) {
        throwIfDifferentDimension(this, that);

        GenericMatrix<F> result = new GenericMatrix<F>(nRows, nCols, field.ZERO());
        for (int i = 0; i < nRows; ++i) {
            for (int j = 0; j < nCols; ++j) {
                result.data[i][j] = this.data[i][j].minus(that.data[i][j]);
            }
        }

        return result;
    }

    @Override
    public GenericMatrix<F> multiply(GenericMatrix<F> that) {
        throwIfIncompatible4Multiplication(this, that);

        GenericMatrix<F> result = new GenericMatrix<F>(this.nRows, that.nCols, field.ZERO());

        for (int i = 0; i < this.nRows; ++i) {
            for (int j = 0; j < that.nCols; ++j) {

                F sum = field.ZERO();
                for (int k = 0; k < nCols; ++k) {//dot product of this i-th row and that j-th column
                    F prod = this.data[i][k].multiply(that.data[k][j]);
                    sum = sum.add(prod);
                }

                result.data[i][j] = sum;
            }
        }

        return result;
    }

    @Override
    public GenericMatrix<F> scaled(F scalar) {
        GenericMatrix<F> result = new GenericMatrix<F>(nRows, nCols, field.ZERO());

        for (int i = 0; i < nRows; ++i) {
            for (int j = 0; j < nCols; ++j) {
                result.data[i][j] = this.data[i][j].multiply(scalar);
            }
        }

        return result;
    }

    @Override
    public GenericMatrix<F> opposite() {
        GenericMatrix<F> result = new GenericMatrix<F>(nRows, nCols, field.ZERO());

        for (int i = 0; i < nRows; ++i) {
            for (int j = 0; j < nCols; ++j) {
                result.data[i][j] = this.data[i][j].opposite();
            }
        }

        return result;
    }

    @Override
    public GenericMatrix<F> ZERO() {
        GenericMatrix<F> result = new GenericMatrix<F>(nRows, nCols, field.ZERO());
        return result;
    }

    @Override
    public GenericMatrix<F> ONE() {
        GenericMatrix<F> result = new GenericMatrix<F>(nRows, nCols, field.ZERO());

        for (int i = 0; i < nRows; ++i) {
            result.data[i][i] = field.ONE();
        }

        return result;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        //dimensions
        result.append(String.format("%dx%d\n", nRows, nCols));

        //col headers
        result.append("\t");
        for (int j = 1; j <= nCols; ++j) {
            result.append(String.format("[,%d] ", j));
        }
        result.append("\n");

        for (int i = 1; i <= nRows; ++i) {
            result.append(String.format("[%d,] ", i));
            for (int j = 1; j <= nCols; ++j) {
                result.append(String.format("%s, ", get(i, j).toString()));
            }
            result.append("\n");
        }

        return result.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        @SuppressWarnings("unchecked")
        final GenericMatrix<F> other = (GenericMatrix<F>) obj;
        if (!isSameDimension(this, other)) {
            return false;
        }
        if (this.data != other.data && (this.data == null || other.data == null || !equals(other.data))) {
            return false;
        }

        return true;
    }

    /**
     * Check whether this matrix data equals to another matrix data.
     *
     * @param that a matrix data arranged in 2D array
     * @return {@code true} if the two matrices are equal, element-by-element
     */
    private boolean equals(F[][] that) { // TODO: this implementation also assume dense matrix
        for (int i = 0; i < nRows; ++i) {
            for (int j = 0; j < nCols; ++j) {
                if (!this.data[i][j].equals(that[i][j])) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 61 * hash + this.nRows;
        hash = 61 * hash + this.nCols;
        hash = 61 * hash + Arrays.deepHashCode(this.data);
        hash = 61 * hash + (this.field != null ? this.field.hashCode() : 0);
        return hash;
    }
}
