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
package com.numericalmethod.suanshu.matrix.doubles.matrixtype.sparse;

import static com.numericalmethod.suanshu.datastructure.DimensionCheck.*;
import com.numericalmethod.suanshu.matrix.MatrixAccessException;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.mathoperation.MatrixMathOperation;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.mathoperation.SimpleMatrixMathOperation;
import com.numericalmethod.suanshu.misc.R;
import com.numericalmethod.suanshu.misc.SuanShuUtils;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * The Compressed Sparse Row (CSR) format for sparse matrix has this
 * representation: {@code (value, col_ind, row_ptr)}. Three arrays are used to
 * represent a sparse matrix:
 * {@code value} stores all the non-zero entries of the matrix from left to
 * right, top to bottom.
 * {@code col_ind} is the column indices corresponding to the {@code values}.
 * {@code row_ptr} is the list of {@code values} indices where each row starts.
 * For example:
 * \[
 * \begin{bmatrix}
 * 1 & 2 & 0 & 0\\
 * 0 & 3 & 9 & 0\\
 * 0 & 1 & 4 & 0
 * \end{bmatrix}
 * \]
 * <blockquote><pre><code>
 * value   = [ 1 2 3 9 1 4 ]
 * col_ind = [ 0 1 1 2 1 2 ]
 * row_ptr = [ 0 2 4 6 ]
 * </code></pre></blockquote>
 * Note: {@code (row_ptr[i] - row_ptr[i - 1])} is the number of non-zero entries
 * in row <i>i</i>.
 * <p/>
 * This format is very inefficient for incremental construction or changes using {@link #set(int, int, double)},
 * but efficient for matrix computation.
 *
 * @author Ken Yiu
 * @see <a
 * href="http://en.wikipedia.org/wiki/Sparse_matrix#Compressed_sparse_row_.28CSR_or_CRS.29">
 * Wikipedia: Compressed sparse row (CSR or CRS)</a>
 */
public class CSRSparseMatrix implements SparseMatrix {

    /** number of non-zeros */
    private int nnz = 0;
    private int[] row_ptr = null;
    private int[] col_ind = null;
    /** non-zero values */
    private double[] value = null;
    private final int nRows;
    private final int nCols;
    private final MatrixMathOperation math = new SimpleMatrixMathOperation();

    /**
     * Construct a sparse matrix in CSR format.
     *
     * @param nRows the number of rows
     * @param nCols the number of columns
     */
    public CSRSparseMatrix(int nRows, int nCols) {
        this.nRows = nRows;
        this.nCols = nCols;
        this.nnz = 0;
        this.value = new double[0];
        this.col_ind = new int[0];
        this.row_ptr = new int[nRows + 1];
    }

    // TODO: add 1 more ctor to allocate max nNonZeros
    /**
     * Construct a sparse matrix in CSR format.
     *
     * @param nRows         the number of rows
     * @param nCols         the number of columns
     * @param rowIndices    the row indices of the non-zeros values
     * @param columnIndices the column indices of the non-zeros values
     * @param value         the non-zero values
     */
    public CSRSparseMatrix(int nRows, int nCols, int[] rowIndices, int[] columnIndices, double[] value) {
        this(nRows, nCols);

        SuanShuUtils.assertArgument(columnIndices.length == value.length && rowIndices.length == value.length,
                                    "input arrays size mismatch");

        this.nnz = value.length;
        this.value = new double[value.length];
        this.col_ind = new int[value.length];

        //TODO: why is this so complicated? why not just copy the three arrays as in the copy ctor?
        LILSparseMatrix lil = new LILSparseMatrix(nRows, nCols, rowIndices, columnIndices, value);

        int valueIndex = 0;
        this.row_ptr[0] = valueIndex;// 0
        for (int i = 1; i <= nRows; ++i) {
            SparseVector row = lil.getRow(i);
            for (SparseVector.Entry entry : row) {
                this.value[valueIndex] = entry.value();
                this.col_ind[valueIndex] = entry.index();
                valueIndex++;
            }
            this.row_ptr[i] = valueIndex;
        }
    }

    /**
     * Construct a sparse matrix in CSR format by a list of non-zero entries.
     *
     * @param nRows   the number of rows
     * @param nCols   the number of columns
     * @param entries the list of entries
     */
    public CSRSparseMatrix(int nRows, int nCols, List<SparseEntry> entries) {
        this(nRows, nCols);
        Collections.sort(entries, SparseEntry.TopLeftFirstComparator.INSTANCE);

        this.nnz = entries.size();
        this.value = new double[nnz];
        this.col_ind = new int[nnz];

        int[] rowCounts = new int[nRows + 1];
        int valueIndex = 0;
        for (SparseEntry entry : entries) {
            Coordinates coord = entry.coordinates;
            if (coord.i < 1 || coord.i > nRows || coord.j < 1 || coord.j > nCols) {
                throw new IllegalArgumentException("out-of-range element coordinates");
            }
            this.value[valueIndex] = entry.value;
            this.col_ind[valueIndex] = entry.coordinates.j;
            valueIndex++;
            rowCounts[entry.coordinates.i]++; // count elements at row i
        }

        this.row_ptr = R.cumsum(rowCounts);
    }

    /**
     * Copy constructor.
     *
     * @param that the matrix to be copied
     */
    public CSRSparseMatrix(CSRSparseMatrix that) {
        this.nRows = that.nRows();
        this.nCols = that.nCols();
        this.nnz = that.nnz;
        // copy the arrays using clone (the quickest way)
        this.value = that.value.clone();
        this.col_ind = that.col_ind.clone();
        this.row_ptr = that.row_ptr.clone();
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
    public List<SparseEntry> getEntrytList() {
        ArrayList<SparseEntry> entries = new ArrayList<SparseEntry>(nNonZeros());
        for (int i = 1; i <= nRows; ++i) {
            for (int k = row_ptr[i - 1]; k < row_ptr[i]; ++k) {
                entries.add(new SparseEntry(new Coordinates(i, col_ind[k]), value[k]));
            }
        }
        return entries;
    }

    @Override
    public void set(int row, int col, double value) {
        throwIfInvalidRow(this, row);
        throwIfInvalidColumn(this, col);

        int rowPointerBegin = row_ptr[row - 1]; // row index starts from 1
        int rowPointerEnd = row_ptr[row];
        int j = 0;
        for (j = rowPointerBegin; j < rowPointerEnd; ++j) {
            if (col_ind[j] >= col) {
                break;
            }
        }
        if (j != rowPointerEnd && col_ind[j] == col) { // the non-zero entry exists at index j
            if (value != 0) {
                this.value[j] = value;
            } else {
                // remove the zero entry
                double[] oldValues = this.value;
                this.value = new double[oldValues.length - 1];
                System.arraycopy(oldValues, 0, this.value, 0, j);
                System.arraycopy(oldValues, j + 1, this.value, j, this.value.length - j);

                int[] oldColumns = col_ind;
                col_ind = new int[oldColumns.length - 1];
                System.arraycopy(oldColumns, 0, col_ind, 0, j);
                System.arraycopy(oldColumns, j + 1, col_ind, j, col_ind.length - j);

                for (int k = row; k < row_ptr.length; ++k) {
                    row_ptr[k]--; // row index of subsequent rows shift left
                }

                nnz--;
            }
        } else { // no such non-zero entry
            // insert the new value at index j
            double[] oldValues = this.value;
            this.value = new double[oldValues.length + 1];
            System.arraycopy(oldValues, 0, this.value, 0, j);
            System.arraycopy(oldValues, j, this.value, j + 1, oldValues.length - j);
            this.value[j] = value;

            int[] oldColumns = col_ind;
            col_ind = new int[oldColumns.length + 1];
            System.arraycopy(oldColumns, 0, col_ind, 0, j);
            System.arraycopy(oldColumns, j, col_ind, j + 1, oldColumns.length - j);
            col_ind[j] = col;

            for (int k = row; k < row_ptr.length; ++k) {
                row_ptr[k]++; // row index of subsequent rows shift right
            }

            nnz++;
        }
    }

    @Override
    public double get(int i, int j) {
        throwIfInvalidRow(this, i);
        throwIfInvalidColumn(this, j);

        int rowPointerBegin = row_ptr[i - 1];
        int rowPointerEnd = row_ptr[i];
        int k = 0;
        for (k = rowPointerBegin; k < rowPointerEnd; ++k) {
            if (col_ind[k] == j) {
                break;
            }
        }
        return k == rowPointerEnd ? 0 : value[k];
    }

    @Override
    public SparseVector getRow(int i) throws MatrixAccessException {
        throwIfInvalidRow(this, i);

        SparseVector row = new SparseVector(nCols);
        for (int k = row_ptr[i - 1]; k < row_ptr[i]; ++k) {
            row.set(col_ind[k], value[k]);
        }

        return row;
    }

    @Override
    public SparseVector getColumn(int j) throws MatrixAccessException {
        throwIfInvalidColumn(this, j);

        SparseVector col = new SparseVector(nRows);
        for (int p = 1; p <= nRows; ++p) {
            for (int q = row_ptr[p - 1]; q < row_ptr[p]; ++q) {
                if (col_ind[q] == j) {
                    col.set(p, value[q]);
                }
            }
        }

        return col;
    }

    @Override
    public Matrix add(Matrix that) {
        if (that instanceof CSRSparseMatrix) {
            return this.add((CSRSparseMatrix) that, +1);
        }

        return math.add(this, that);
    }

    @Override
    public Matrix minus(Matrix that) {
        if (that instanceof CSRSparseMatrix) {
            return this.add((CSRSparseMatrix) that, -1);
        }

        return math.minus(this, that);
    }

    private CSRSparseMatrix add(CSRSparseMatrix that, double beta) {
        throwIfDifferentDimension(this, that);

        CSRSparseMatrix sum = new CSRSparseMatrix(nRows, nCols);
        double[] sumValue = new double[this.nNonZeros() + that.nNonZeros()]; // initialize array
        int[] sumColumn = new int[this.nNonZeros() + that.nNonZeros()]; // initialize array
        int sumNnz = 0;

        Scatter scatter = new Scatter(nCols); // create a scatter for accumulate 2 rows from 'this' and 'that'

        for (int i = 1; i <= nRows; ++i) {
            /* assign row pointer for row i */
            sum.row_ptr[i - 1] = sumNnz; // row i starts here

            /* sum the 2 row vectors in 'this' and 'that' matrices */
            scatter.startRow(i);
            scatter.addRow(this, i, 1.);
            scatter.addRow(that, i, beta);

            /* assign the sum to the resulting row */
            sumNnz += scatter.copyResults(sumValue, sumColumn, sumNnz);
        }

        sum.row_ptr[nRows] = sumNnz; // the last entry of rowPointers

        sum.nnz = sumNnz;
        sum.value = Arrays.copyOf(sumValue, sumNnz);
        sum.col_ind = Arrays.copyOf(sumColumn, sumNnz);

        sum.dropZeros();
        sum.sortColumnIndices();
        return sum;
    }

    private void sortColumnIndices() {
        this.t().t();
    }

    @Override
    public Matrix multiply(Matrix that) {
        if (that instanceof CSRSparseMatrix) {
            return this.multiply((CSRSparseMatrix) that);
        }

        return math.multiply(this, that);
    }

    /**
     * This implementation is the same as CSparse's (or Matlab's) for CSR
     * matrices.
     * We compute the resultant matrix by rows.
     * In particular, for C = A * B, each row C_i is the linear combination of
     * rows in B,
     * where the weights of the rows are given by the non-zeros in row A_i.
     *
     * @param that the multiplicand matrix
     * @return the product
     * @see "Timothy A. Davis, "Matrix Multiplication," in <i>Direct Methods for
     * Sparse Linear Systems</i>, ch. 2, sec. 8, p. 17-19."
     */
    private CSRSparseMatrix multiply(CSRSparseMatrix that) {
        throwIfIncompatible4Multiplication(this, that);

        CSRSparseMatrix product = new CSRSparseMatrix(nRows, that.nCols);
        double[] productValues = new double[this.nNonZeros() + that.nNonZeros()]; // initialize array
        int[] productColumns = new int[this.nNonZeros() + that.nNonZeros()]; // initialize array
        int productNnz = 0;

        Scatter scatter = new Scatter(that.nCols); // create a scatter for accumulate rows
        for (int i = 1; i <= nRows; ++i) {
            /* resize arrays if not enough */
            if (productNnz + that.nCols > productValues.length) {
                productValues = Arrays.copyOf(productValues, 2 * productValues.length + that.nCols);
                productColumns = Arrays.copyOf(productColumns, 2 * productColumns.length + that.nCols);
            }

            /* assign row pointer for row i */
            product.row_ptr[i - 1] = productNnz; // row i starts here

            /* compute the linear combination of row vectors in 'that' matrix */
            scatter.startRow(i);
            for (int k = row_ptr[i - 1]; k < row_ptr[i]; ++k) { // for each non-zero in row i
                scatter.addRow(that, col_ind[k], value[k]);
            }

            /* assign the sum to the result */
            productNnz += scatter.copyResults(productValues, productColumns, productNnz);
        }

        product.row_ptr[nRows] = productNnz; // the last entry of rowPointers
        product.nnz = productNnz;
        product.value = Arrays.copyOf(productValues, productNnz);
        product.col_ind = Arrays.copyOf(productColumns, productNnz);

        product.dropZeros();
        product.sortColumnIndices();
        return product;
    }

    @Override
    public Vector multiply(Vector v) {
        throwIfIncompatible4Multiplication(this, v);

        Vector Av = (v instanceof SparseVector)
                    ? new SparseVector(nRows)
                    : new DenseVector(nRows);

        for (int i = 1; i <= nRows; ++i) {
            Av.set(i, this.getRow(i).innerProduct(v));
        }

        return Av;
    }

    @Override
    public CSRSparseMatrix scaled(double c) {
        if (Double.compare(0., c) == 0) {
            return new CSRSparseMatrix(nRows, nCols);
        }

        CSRSparseMatrix cA = new CSRSparseMatrix(this);
        for (int k = 0; k < cA.value.length; ++k) {
            cA.value[k] *= c;
        }
        return cA;
    }

    @Override
    public CSRSparseMatrix opposite() {
        return scaled(-1);
    }

    @Override
    public CSRSparseMatrix t() {
        CSRSparseMatrix result = new CSRSparseMatrix(nCols, nRows);
        double[] resultValues = new double[value.length];
        int[] resultColumns = new int[col_ind.length];

        /* count column indices for computing row pointers of the transpose */
        int[] columnCounts = new int[nCols + 1];
        for (int k = 0; k < col_ind.length; ++k) {
            columnCounts[col_ind[k]]++;
        }

        /* compute row pointers as culmulative sum of column counts */
        columnCounts = R.cumsum(columnCounts); // columnCounts as insertion point for each row
        result.row_ptr = columnCounts.clone();

        /* assign column indices and values to the transposed array */
        for (int i = 1; i <= nRows; ++i) { // for each row
            for (int p = row_ptr[i - 1]; p < row_ptr[i]; ++p) {
                int q = columnCounts[col_ind[p] - 1]++; // shift the insertion point by 1
                resultColumns[q] = i; // assign row index to column index (i.e., transpose)
                resultValues[q] = value[p];
            }
        }

        result.value = resultValues;
        result.col_ind = resultColumns;
        return result;
    }

    @Override
    public DenseMatrix toDense() {
        DenseMatrix result = new DenseMatrix(nRows, nCols);

        for (int i = 1; i <= nRows; ++i) {
            for (int k = row_ptr[i - 1]; k < row_ptr[i]; ++k) {
                result.set(i, col_ind[k], value[k]);
            }
        }
        return result;
    }

    @Override
    public CSRSparseMatrix ZERO() {
        return new CSRSparseMatrix(nRows, nCols);
    }

    @Override
    public CSRSparseMatrix ONE() {
        final int dim = Math.min(nRows, nCols);
        double[] ones = R.rep(1.0, dim);
        int[] i = R.seq(1, dim);
        int[] j = R.seq(1, dim);

        return new CSRSparseMatrix(nRows, nCols, i, j, ones);
    }

    @Override
    public CSRSparseMatrix deepCopy() {
        return new CSRSparseMatrix(this);
    }

    @Override
    public int nNonZeros() {
        return nnz;
    }

    @Override
    public String toString() {
        return SparseMatrixUtils.toString(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CSRSparseMatrix other = (CSRSparseMatrix) obj;
        if (this.nRows != other.nRows) {
            return false;
        }
        if (this.nCols != other.nCols) {
            return false;
        }
        return SparseMatrixUtils.equals(this, other);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 47 * hash + Arrays.hashCode(this.row_ptr);
        hash = 47 * hash + Arrays.hashCode(this.col_ind);
        hash = 47 * hash + Arrays.hashCode(this.value);
        hash = 47 * hash + this.nRows;
        hash = 47 * hash + this.nCols;
        return hash;
    }

//    public int dropTolerance(final double tol) {
//        int oldNnz = nnz;
//        keepEntries(new CheckForKeepValue() {
//
//            public boolean toKeep(double value) {
//                return Double.compare(Math.abs(value), tol) > 0;
//            }
//        });
//
//        return oldNnz - nnz;
//    }
    /**
     * This is a utility class for accumulating sum of rows in CSR format.
     * It is useful for computing matrix addition and multiplication,
     * where the rows in the resultant matrix is a linear combination of rows in
     * the operand matrices.
     * <p/>
     * The original algorithm in C is written as a stateless procedure with
     * pointers passing states in and out of the procedure,
     * and assigning incremental results on each call, which makes the algorithm
     * difficult to read and understand.
     * This class is a neat OO design of the original procedure, which hides all
     * the necessary states within an instance.
     * <p/>
     * Note: It is too difficult for the public to use properly. Do not open it
     * to public.
     * <p/>
     * Usage:
     * <blockquote><code>
     * Scatter scatter = new Scatter(sizeOfRow);
     * // ...
     * for (int i = 1; i <= nRows; ++i) { // for each row i
     *     scatter.startRow(i);
     *     scatter.add(matrix, i, weight); // add row i of matrix times weight to the sum
     *     // ... add all the rows needed
     *     nNonZeros += scatter.copyResults(values, columns, nNonZeros); // copy the results to values and columns array
     * }
     * </code></blockquote>
     */
    private static class Scatter {

        private final int[] w;
        private int currentRowIndex;
        private int nnz;
        private int[] columns;
        private double[] sum;

        private Scatter(int size) {
            w = new int[size];
            columns = new int[size];
            sum = new double[size];
        }

        /**
         * This clears the result for the last computed row.
         *
         * @param throwIfInvalidRow
         */
        private void startRow(int rowIndex) {
            currentRowIndex = rowIndex;
            Arrays.fill(columns, 0, nnz, 0); // just reset the dirty cells for performance
            nnz = 0; // reset nNonZeros
        }

        /**
         * Add to the sum row {@code i} of matrix {@code m} scaled by {@code beta},
         * i.e.,
         * <code>sum += m_ii * beta</code>
         */
        private void addRow(CSRSparseMatrix m, int i, double beta) {
            for (int k = m.row_ptr[i - 1]; k < m.row_ptr[i]; ++k) {
                int j = m.col_ind[k]; // column j
                if (w[j - 1] < currentRowIndex) {
                    w[j - 1] = currentRowIndex;
                    columns[nnz++] = j;
                    sum[j - 1] = 0.; // clear this element for storing new sum in this row
                }
                sum[j - 1] += beta * m.value[k];
            }
        }

        /**
         * Copy the results to the input arrays.
         *
         * @param destValues  destination for computed values
         * @param destColumns destination for computed columns
         * @param fromIndex   starting array index for storing the results
         * @return the number of non-zeros in the sum
         */
        private int copyResults(double[] destValues, int[] destColumns, int fromIndex) {
            System.arraycopy(columns, 0, destColumns, fromIndex, nnz);
            for (int p = 0; p < nnz; ++p) {
                destValues[fromIndex + p] = sum[columns[p] - 1];
            }
            return nnz;
        }
    }

    private static interface CheckForKeptValue {

        boolean toKeep(double value);
    }

    private void dropZeros() {
        keepEntries(
                new CheckForKeptValue() {

                    @Override
                    public boolean toKeep(double value) {
                        return Double.compare(0., value) != 0;
                    }
                });
    }

    private void keepEntries(CheckForKeptValue check) {
        int nz = 0;
        for (int i = 0; i < nRows; ++i) {
            int p = row_ptr[i];
            row_ptr[i] = nz;
            for (; p < row_ptr[i + 1]; ++p) {
                if (check.toKeep(value[p])) {
                    value[nz] = value[p];
                    col_ind[nz] = col_ind[p];
                    nz++;
                }
            }
        }
        row_ptr[nRows] = nz;
        value = Arrays.copyOf(value, nz);
        col_ind = Arrays.copyOf(col_ind, nz);
        nnz = nz;
    }
}
