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
 * The list of lists (LIL) format for sparse matrix stores one list per row,
 * where each entry stores a column index and value. Typically, these entries
 * are kept sorted by column index for faster lookup. This is another format
 * which is good for incremental matrix construction.
 *
 * @author Ken Yiu
 * @see <a
 * href="http://en.wikipedia.org/wiki/Sparse_matrix#List_of_lists_.28LIL.29">
 * Wikipedia: List of lists (LIL)</a>
 */
public class LILSparseMatrix implements SparseMatrix {

    private final SparseVector[] rows;
    private final int nRows;
    private final int nCols;
    private final MatrixMathOperation math = new SimpleMatrixMathOperation();

    /**
     * Construct a sparse matrix in LIL format.
     *
     * @param nRows the number of rows
     * @param nCols the number of columns
     */
    public LILSparseMatrix(int nRows, int nCols) {
        this.nRows = nRows;
        this.nCols = nCols;
        this.rows = new SparseVector[nRows];
        for (int i = 0; i < nRows; ++i) {
            this.rows[i] = new SparseVector(nCols);
        }
    }

    /**
     * Construct a sparse matrix in LIL format.
     *
     * @param nRows         the number of rows
     * @param nCols         the number of columns
     * @param rowIndices    the row indices of the non-zeros values
     * @param columnIndices the column indices of the non-zeros values
     * @param value         the non-zero values
     */
    public LILSparseMatrix(int nRows, int nCols, int[] rowIndices, int[] columnIndices, double[] value) {
        this(nRows, nCols);
        SuanShuUtils.assertArgument(rowIndices.length == columnIndices.length && rowIndices.length == value.length,
                                    "input arrays size mismatch");

        for (int k = 0; k < value.length; ++k) {
            rows[rowIndices[k] - 1].set(columnIndices[k], value[k]);
        }
    }

    /**
     * Construct a sparse matrix in LIL format by a list of non-zero entries.
     *
     * @param nRows   the number of rows
     * @param nCols   the number of columns
     * @param entries the list of entries
     */
    public LILSparseMatrix(int nRows, int nCols, List<SparseEntry> entries) {
        this(nRows, nCols);

        Collections.sort(entries, SparseEntry.TopLeftFirstComparator.INSTANCE);
        for (SparseEntry entry : entries) {
            rows[entry.coordinates.i - 1].set(entry.coordinates.j, entry.value);
        }
    }

    /**
     * Copy constructor.
     *
     * @param that the matrix to be copied
     */
    public LILSparseMatrix(LILSparseMatrix that) {
        this(that.nRows, that.nCols);

        for (int i = 0; i < nRows; ++i) {
            this.rows[i] = new SparseVector(that.rows[i]);
        }
    }

    private LILSparseMatrix(SparseVector[] rows) {
        this.nRows = rows.length;
        this.nCols = rows[0].size();
        this.rows = rows;
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
        int k = 0;
        for (int i = 1; i <= nRows; ++i) { // from top to bottom
            for (SparseVector.Entry entry : rows[i - 1]) { // from left to right
                entries.add(k++, new SparseEntry(new Coordinates(i, entry.index()), entry.value()));
            }
        }
        return entries;
    }

    @Override
    public double get(int row, int col) {
        throwIfInvalidRow(this, row);
        throwIfInvalidColumn(this, col);

        return rows[row - 1].get(col);
    }

    @Override
    public void set(int row, int col, double value) {
        throwIfInvalidRow(this, row);
        throwIfInvalidColumn(this, col);

        rows[row - 1].set(col, value);
    }

    @Override
    public SparseVector getRow(int i) {
        throwIfInvalidRow(this, i);
        return new SparseVector(rows[i - 1]);
    }

    @Override
    public SparseVector getColumn(int j) {
        throwIfInvalidColumn(this, j);

        SparseVector column = new SparseVector(this.nRows);
        for (int i = 1; i <= nRows; ++i) {
            double value = this.get(i, j);
            if (Double.compare(0., value) != 0) {
                column.set(i, value);
            }
        }
        return column;
    }

    @Override
    public Matrix add(Matrix that) {
        if (that instanceof LILSparseMatrix) {
            return add((LILSparseMatrix) that);
        }

        return math.add(this, that);
    }

    private LILSparseMatrix add(LILSparseMatrix that) {
        throwIfDifferentDimension(this, that);

        SparseVector[] sum = new SparseVector[nRows];
        for (int i = 0; i < nRows; ++i) {
            sum[i] = (SparseVector) this.rows[i].add(that.rows[i]);
        }
        return new LILSparseMatrix(sum);
    }

    @Override
    public Matrix minus(Matrix that) {
        if (that instanceof LILSparseMatrix) {
            return minus((LILSparseMatrix) that);
        }

        return math.minus(this, that);
    }

    private LILSparseMatrix minus(LILSparseMatrix that) {
        throwIfDifferentDimension(this, that);

        SparseVector[] diff = new SparseVector[nRows];
        for (int i = 0; i < nRows; ++i) {
            diff[i] = (SparseVector) this.rows[i].minus(that.rows[i]);
        }
        return new LILSparseMatrix(diff);
    }

    @Override
    public Matrix multiply(Matrix that) {
        if (that instanceof LILSparseMatrix) {
            return multiply((LILSparseMatrix) that);
        }

        return math.multiply(this, that);
    }

    private LILSparseMatrix multiply(LILSparseMatrix that) {
        throwIfIncompatible4Multiplication(this, that);

        LILSparseMatrix thatTransposed = that.t();
        SparseVector[] product = new SparseVector[that.nCols];
        for (int j = 0; j < that.nCols; ++j) {
            product[j] = this.multiply(thatTransposed.rows[j]);
        }
        return new LILSparseMatrix(product).t();
    }

    @Override
    public Vector multiply(Vector v) {
        if (v instanceof SparseVector) {
            return this.multiply((SparseVector) v);
        }

        throwIfIncompatible4Multiplication(this, v);

        double[] Av = new double[nRows];
        for (int i = 0; i < nRows; ++i) {
            Av[i] = rows[i].innerProduct(v);
        }
        return new DenseVector(Av);
    }

    private SparseVector multiply(SparseVector v) {
        throwIfIncompatible4Multiplication(this, v);

        SparseVector Av = new SparseVector(nRows);
        double val = 0.;
        for (int i = 0; i < nRows; ++i) {
            val = rows[i].innerProduct(v);
            if (Double.compare(0., val) != 0) {
                Av.set(i + 1, val);
            }
        }

        return Av;
    }

    @Override
    public LILSparseMatrix scaled(double c) {
        if (c == 0) {
            return new LILSparseMatrix(nRows, nCols);
        }

        SparseVector[] cA = new SparseVector[nRows];
        for (int i = 0; i < nRows; ++i) {
            cA[i] = rows[i].scaled(c);
        }

        return new LILSparseMatrix(cA);
    }

    @Override
    public LILSparseMatrix opposite() {
        return scaled(-1);
    }

    @Override
    public LILSparseMatrix t() {
        LILSparseMatrix result = new LILSparseMatrix(nCols, nRows);
        for (int i = 0; i < nRows; ++i) {
            for (SparseVector.Entry entry : rows[i]) {
                result.set(entry.index(), i + 1, entry.value());
            }
        }
        return result;
    }

    @Override
    public LILSparseMatrix ZERO() {
        return new LILSparseMatrix(nRows, nCols);
    }

    @Override
    public LILSparseMatrix ONE() {
        final int dim = Math.min(nRows, nCols);
        double[] values = R.rep(1.0, dim);
        int[] i = R.seq(1, dim);
        int[] j = R.seq(1, dim);

        return new LILSparseMatrix(nRows, nCols, i, j, values);
    }

    @Override
    public LILSparseMatrix deepCopy() {
        return new LILSparseMatrix(this);
    }

    @Override
    public DenseMatrix toDense() {
        DenseMatrix result = new DenseMatrix(nRows, nCols).ZERO();

        int rowIndex = 1;
        for (SparseVector row : rows) {
            for (SparseVector.Entry entry : row) {
                result.set(rowIndex, entry.index(), entry.value());
            }
            rowIndex++;
        }

        return result;
    }

    @Override
    public int nNonZeros() {
        int nnz = 0;
        for (SparseVector row : rows) {
            nnz += row.nNonZeros();
        }
        return nnz;
    }
//    public int dropTolerance(double tol) {
//        int nDrops = 0;
//        for (SparseVector row : rows) {
//            nDrops += row.dropTolerance(tol);
//        }
//        return nDrops;
//    }

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
        final LILSparseMatrix other = (LILSparseMatrix) obj;
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
        int hash = 3;
        hash = 59 * hash + Arrays.deepHashCode(this.rows);
        hash = 59 * hash + this.nRows;
        hash = 59 * hash + this.nCols;
        return hash;
    }
}
