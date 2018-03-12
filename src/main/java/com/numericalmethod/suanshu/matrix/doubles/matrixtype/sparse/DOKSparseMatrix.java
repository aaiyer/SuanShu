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
import java.util.*;

/**
 * The Dictionary Of Key (DOK) format for sparse matrix uses the coordinates of
 * non-zero entries in the matrix as keys. The non-zero values are hashed such
 * that adding, removing, retrieving values can be done in constant time. This
 * data structure is efficient for incremental construction but poor for
 * iterating non-zero elements in a matrix.
 *
 * @author Ken Yiu
 * @see <a
 * href="http://en.wikipedia.org/wiki/Sparse_matrix#Dictionary_of_keys_.28DOK.29">
 * Wikipedia: Dictionary of keys (DOK)</a>
 */
public class DOKSparseMatrix implements SparseMatrix {

    private HashMap<Coordinates, Double> dictionary;
    private final int nRows;
    private final int nCols;
    private final MatrixMathOperation math = new SimpleMatrixMathOperation();

    /**
     * Construct a sparse matrix in DOK format.
     *
     * @param nRows the number of rows
     * @param nCols the number of columns
     */
    public DOKSparseMatrix(int nRows, int nCols) {
        this.nRows = nRows;
        this.nCols = nCols;
        this.dictionary = new HashMap<Coordinates, Double>(0);
    }

    /**
     * Construct a sparse matrix in DOK format.
     *
     * @param nRows         the number of rows
     * @param nCols         the number of columns
     * @param rowIndices    the row indices of the non-zeros values
     * @param columnIndices the column indices of the non-zeros values
     * @param value         the non-zero values
     */
    public DOKSparseMatrix(int nRows, int nCols, int[] rowIndices, int[] columnIndices, double[] value) {
        this(nRows, nCols);
        this.dictionary = new HashMap<Coordinates, Double>(value.length);

        SuanShuUtils.assertArgument(rowIndices.length == columnIndices.length && rowIndices.length == value.length,
                                    "input arrays size mismatch");

        for (int k = 0; k < rowIndices.length; ++k) {
            this.set(rowIndices[k], columnIndices[k], value[k]);
        }
    }

    /**
     * Construct a sparse matrix in DOK format by a list of non-zero entries.
     *
     * @param nRows   the number of rows
     * @param nCols   the number of columns
     * @param entries the entry list
     */
    public DOKSparseMatrix(int nRows, int nCols, List<SparseEntry> entries) {
        this(nRows, nCols);
        this.dictionary = new HashMap<Coordinates, Double>(entries.size());

        Collections.sort(entries, SparseEntry.TopLeftFirstComparator.INSTANCE);
        for (SparseEntry entry : entries) {
            this.set(entry.coordinates.i, entry.coordinates.j, entry.value);
        }
    }

    /**
     * Copy constructor.
     *
     * @param that the matrix to be copied
     */
    public DOKSparseMatrix(DOKSparseMatrix that) {
        this(that.nRows, that.nCols);
        this.dictionary = new HashMap<Coordinates, Double>(that.dictionary); // it's okay since both key and value are immutable
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
        for (Map.Entry<Coordinates, Double> entry : dictionary.entrySet()) {
            entries.add(new SparseEntry(entry.getKey(), entry.getValue()));
        }

        return entries;
    }

    @Override
    public void set(int row, int col, double value) {
        throwIfInvalidRow(this, row);
        throwIfInvalidColumn(this, col);

        if (Double.compare(0., value) != 0) {
            dictionary.put(new Coordinates(row, col), value);
        } else {
            dictionary.remove(new Coordinates(row, col));
        }
    }

    @Override
    public double get(int i, int j) {
        throwIfInvalidRow(this, i);
        throwIfInvalidColumn(this, j);

        Double value = dictionary.get(new Coordinates(i, j));
        return value == null ? 0. : value.doubleValue();
    }

    @Override
    public SparseVector getRow(int i) throws MatrixAccessException {
        throwIfInvalidRow(this, i);

        SparseVector row = new SparseVector(nCols);
        for (Map.Entry<Coordinates, Double> entry : dictionary.entrySet()) {
            if (entry.getKey().i == i) {
                row.set(entry.getKey().j, entry.getValue());
            }
        }
        return row;
    }

    @Override
    public SparseVector getColumn(int j) throws MatrixAccessException {
        throwIfInvalidColumn(this, j);

        SparseVector col = new SparseVector(nRows);
        for (Map.Entry<Coordinates, Double> entry : dictionary.entrySet()) {
            if (entry.getKey().j == j) {
                col.set(entry.getKey().i, entry.getValue());
            }
        }

        return col;
    }

    @Override
    public Matrix add(Matrix that) {
        if (that instanceof DOKSparseMatrix) {
            return this.add((DOKSparseMatrix) that, +1);
        }

        return math.add(this, that);
    }

    private DOKSparseMatrix add(DOKSparseMatrix that, int sign) {
        throwIfDifferentDimension(this, that);
        DOKSparseMatrix result = new DOKSparseMatrix(this);

        for (Map.Entry<Coordinates, Double> entry : that.dictionary.entrySet()) {
            Coordinates coord = entry.getKey();
            Double value = result.dictionary.get(coord);
            value = value == null ? sign * entry.getValue() : value + sign * entry.getValue();
            if (Double.compare(0., value) != 0) {
                result.dictionary.put(coord, value);
            } else {
                result.dictionary.remove(coord);
            }
        }

        return result;
    }

    @Override
    public Matrix minus(Matrix that) {
        if (that instanceof DOKSparseMatrix) {
            return this.add((DOKSparseMatrix) that, -1);
        }

        return math.minus(this, that);
    }

    @Override
    public Matrix multiply(Matrix that) {
        throwIfIncompatible4Multiplication(this, that);

        final int ncols = that.nCols();
        Matrix product = (that instanceof SparseMatrix)
                         ? new DOKSparseMatrix(nRows, ncols)
                         : new DenseMatrix(nRows, ncols);

        for (Map.Entry<Coordinates, Double> entry : dictionary.entrySet()) {
            int i = entry.getKey().i;
            int k = entry.getKey().j;
            double value = entry.getValue();
            for (int j = 1; j <= ncols; ++j) {
                product.set(i, j, product.get(i, j) + value * that.get(k, j));
            }
        }

        return product;
    }

    @Override
    public Vector multiply(Vector v) {
        throwIfIncompatible4Multiplication(this, v);

        Vector Av = (v instanceof SparseVector)
                    ? new SparseVector(nRows)
                    : new DenseVector(nRows);

        for (Map.Entry<Coordinates, Double> entry : dictionary.entrySet()) {
            double value = entry.getValue() * v.get(entry.getKey().j);
            int index = entry.getKey().i;
            Av.set(index, Av.get(index) + value);
        }

        return Av;
    }

    @Override
    public DOKSparseMatrix scaled(double c) {
        if (Double.compare(0., c) == 0) {
            return new DOKSparseMatrix(this.nRows, this.nCols);
        }

        DOKSparseMatrix cA = new DOKSparseMatrix(this.nRows, this.nCols);
        for (Map.Entry<Coordinates, Double> entry : this.dictionary.entrySet()) {
            cA.dictionary.put(entry.getKey(), c * entry.getValue());
        }

        return cA;
    }

    @Override
    public DOKSparseMatrix opposite() {
        return scaled(-1);
    }

    @Override
    public DOKSparseMatrix t() {
        DOKSparseMatrix result = new DOKSparseMatrix(this.nCols, this.nRows);

        for (Map.Entry<Coordinates, Double> entry : this.dictionary.entrySet()) {
            Coordinates coord = entry.getKey();
            result.set(coord.j, coord.i, entry.getValue());
        }

        return result;
    }

    @Override
    public DOKSparseMatrix ZERO() {
        return new DOKSparseMatrix(nRows, nCols);
    }

    @Override
    public DOKSparseMatrix ONE() {
        final int dim = Math.min(nRows, nCols);
        double[] values = R.rep(1.0, dim);
        int[] i = R.seq(1, dim);
        int[] j = R.seq(1, dim);

        return new DOKSparseMatrix(nRows, nCols, i, j, values);
    }

    @Override
    public DOKSparseMatrix deepCopy() {
        return new DOKSparseMatrix(this);
    }

    @Override
    public DenseMatrix toDense() {
        DenseMatrix result = new DenseMatrix(nRows, nCols);
        for (Map.Entry<Coordinates, Double> entry : dictionary.entrySet()) {
            Coordinates coord = entry.getKey();
            result.set(coord.i, coord.j, entry.getValue());
        }
        return result;
    }

    @Override
    public int nNonZeros() {
        return dictionary.size();
    }

//    public int dropTolerance(double tol) {
//        int nDrops = 0;
//        for (Iterator<Map.Entry<Coordinates, Double>> iterator = dictionary.entrySet().iterator(); iterator.hasNext();) {
//            Map.Entry<Coordinates, Double> entry = iterator.next();
//            if (Double.compare(Math.abs(entry.getValue()), tol) <= 0) {
//                iterator.remove();
//                nDrops++;
//            }
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
        final DOKSparseMatrix other = (DOKSparseMatrix) obj;
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
        int hash = 7;
        hash = 71 * hash + (this.dictionary != null ? this.dictionary.hashCode() : 0);
        hash = 71 * hash + this.nRows;
        hash = 71 * hash + this.nCols;
        return hash;
    }
}
