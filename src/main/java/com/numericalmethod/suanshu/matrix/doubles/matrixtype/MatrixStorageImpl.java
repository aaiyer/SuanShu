/*
 * Copyright (c) Numerical Method Inc.
 * http://www.numericalmethod.com/ * Copyright (c)
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
package com.numericalmethod.suanshu.matrix.doubles.matrixtype;

import static com.numericalmethod.suanshu.datastructure.DimensionCheck.throwIfInvalidColumn;
import static com.numericalmethod.suanshu.datastructure.DimensionCheck.throwIfInvalidRow;
import com.numericalmethod.suanshu.datastructure.Table;
import com.numericalmethod.suanshu.matrix.MatrixAccessException;
import com.numericalmethod.suanshu.matrix.doubles.MatrixAccess;
import com.numericalmethod.suanshu.number.DoubleUtils;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;

/**
 * There are multiple ways to implement the storage data structure depending on the matrix type for optimization purpose.
 * This class provides a default way to access the storage structure.
 * A particular matrix implementation may override these methods for performance, if implementation details of {@code storage} are known.
 *
 * @author Haksun Li
 */
public class MatrixStorageImpl implements Table, MatrixAccess {

    private final MatrixAccess storage;//TODO: make final?
    private final int nRows;
    private final int nCols;

    /**
     * Construct a {@code MatrixStorageImpl} to wrap a storage for access.
     *
     * @param nRows   the number of rows
     * @param nCols   the number of columns
     * @param storage the matrix data storage/representation
     */
    public MatrixStorageImpl(int nRows, int nCols, MatrixAccess storage) {
        this.storage = storage;
        this.nRows = nRows;
        this.nCols = nCols;
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
    public void set(int row, int col, double value) throws MatrixAccessException {
//        throwIfInvalidRow(this, row);//too expensive to do it for every set
//        throwIfInvalidColumn(this, col);//too expensive to do it for every set
        storage.set(row, col, value);
    }

    @Override
    public double get(int row, int col) throws MatrixAccessException {
//        throwIfInvalidRow(this, row);//too expensive to do it for every get
//        throwIfInvalidColumn(this, col);//too expensive to do it for every get
        return storage.get(row, col);
    }

    /**
     * Set the values for a row in the matrix, i.e., <i>[i, *]</i>.
     *
     * @param i      the row index, counting from 1
     * @param values the values to change the row entries to
     * @throws MatrixAccessException if the number of {@code values} does not match the column size
     */
    public void setRow(int i, double... values) {
        throwIfInvalidRow(this, i);

        if (values.length != nCols) {
            throw new MatrixAccessException(
                    String.format("the number of values (%d) does not match the column size (%d)",
                                  values.length, nCols));
        }

        for (int j = 1; j <= values.length; ++j) {
            set(i, j, values[j - 1]);
        }
    }

    /**
     * Set the values for a row in the matrix, i.e., <i>[i, *]</i>.
     *
     * @param i the row index, counting from 1
     * @param v the vector to change the row entries to
     * @throws MatrixAccessException if the number of {@code values} does not match the column size
     */
    public void setRow(int i, Vector v) {
        throwIfInvalidRow(this, i);

        final int size = v.size();
        if (size != nCols) {
            throw new MatrixAccessException(
                    String.format("the number of values (%d) does not match the column size (%d)",
                                  size, nCols));
        }

        for (int j = 1; j <= size; ++j) {
            set(i, j, v.get(j));
        }
    }

    /**
     * Get a row.
     *
     * @param i the row index, counting from 1
     * @return row <i>i</i>
     * @throws MatrixAccessException if the row index is invalid
     */
    public Vector getRow(int i) throws MatrixAccessException {
        throwIfInvalidRow(this, i);

        DenseVector result = new DenseVector(nCols);
        for (int j = 1; j <= result.size(); ++j) {
            result.set(j, get(i, j));
        }
        return result;
    }

    /**
     * Set the values for a column in the matrix, i.e., <i>[*, j]</i>.
     *
     * @param j      the column index, counting from 1
     * @param values the values to change the column entries to
     * @throws MatrixAccessException if the number of {@code values} does not match the row size
     */
    public void setColumn(int j, double... values) {
        throwIfInvalidColumn(this, j);

        if (values.length != nRows) {
            throw new MatrixAccessException(
                    String.format("the number of values (%d) does not match the row size (%d)",
                                  values.length, nRows));
        }

        for (int i = 1; i <= values.length; ++i) {
            set(i, j, values[i - 1]);
        }
    }

    /**
     * Set the values for a column in the matrix, i.e., <i>[*, j]</i>.
     *
     * @param j the column index, counting from 1
     * @param v the vector to change the column entries to
     * @throws MatrixAccessException if the number of {@code values} does not match the row size
     */
    public void setColumn(int j, Vector v) {
        throwIfInvalidColumn(this, j);

        final int size = v.size();
        if (size != nRows) {
            throw new MatrixAccessException(
                    String.format("the number of values (%d) does not match the row size (%d)",
                                  size, nRows));
        }

        for (int i = 1; i <= size; ++i) {
            set(i, j, v.get(i));
        }
    }

    /**
     * Get a column.
     *
     * @param j the column index, counting from 1
     * @return row <i>j</i>
     * @throws MatrixAccessException if the column index is invalid
     */
    public Vector getColumn(int j) throws MatrixAccessException {
        throwIfInvalidColumn(this, j);

        DenseVector result = new DenseVector(nRows);
        for (int i = 1; i <= result.size(); ++i) {
            result.set(i, get(i, j));
        }
        return result;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        // dimensions
        result.append(String.format("%dx%d\n", nRows, nCols));

        // col headers
        result.append("\t");
        for (int j = 1; j <= nCols; ++j) {
            result.append(String.format("[,%d] ", j));
        }
        result.append("\n");

        for (int i = 1; i <= nRows; ++i) {
            result.append(String.format("[%d,] ", i));
            for (int j = 1; j <= nCols; ++j) {
                result.append(String.format("%f, ", get(i, j)));
            }

            if (i != nRows) {
                result.append("\n");
            }
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
        final MatrixStorageImpl other = (MatrixStorageImpl) obj;
        if (this.storage != other.storage && (this.storage == null || !equals(this.storage, other.storage))) {
            return false;
        }
        if (this.nRows != other.nRows) {
            return false;
        }
        if (this.nCols != other.nCols) {
            return false;
        }
        return true;
    }

    private static boolean equals(MatrixAccess table1, MatrixAccess table2) {
        if (table1.nRows() != table2.nRows()) {
            return false;
        }
        if (table1.nCols() != table2.nCols()) {
            return false;
        }

        final int nRows = table1.nRows();
        final int nCols = table1.nCols();
        for (int i = 1; i <= nRows; ++i) {
            for (int j = 1; j <= nCols; ++j) {
                if (DoubleUtils.compare(table1.get(i, j), table2.get(i, j), 0) != 0) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + (this.storage != null ? this.storage.hashCode() : 0);
        hash = 67 * hash + this.nRows;
        hash = 67 * hash + this.nCols;
        return hash;
    }
}
