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
package com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.triangle;

import static com.numericalmethod.suanshu.datastructure.DimensionCheck.*;
import com.numericalmethod.suanshu.matrix.MatrixAccessException;
import com.numericalmethod.suanshu.matrix.doubles.AreMatrices;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.MatrixAccess;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.Densifiable;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.mathoperation.MatrixMathOperation;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.mathoperation.SimpleMatrixMathOperation;
import com.numericalmethod.suanshu.matrix.doubles.operation.MatrixUtils;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;

/**
 * A symmetric matrix is a square matrix such that its transpose equals to itself, i.e.,
 * <blockquote><code>A[i][j] = A[j][i]</code></blockquote>
 *
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/Symmetric_matrix"> Wikipedia: Symmetric matrix</a>
 */
public class SymmetricMatrix implements Matrix, Densifiable {

    private final int dim;
    /** storage */
    private LowerTriangularMatrix L;//TODO: made final
    private final MatrixMathOperation math = new SimpleMatrixMathOperation();

    //<editor-fold defaultstate="collapsed" desc="Ctors">
    private SymmetricMatrix(LowerTriangularMatrix L) {
        this.dim = L.nRows();
        this.L = L;
    }

    /**
     * Construct a symmetric matrix of dimension <i>dim * dim</i>.
     *
     * @param dim the matrix dimension
     */
    public SymmetricMatrix(int dim) {
        this(new LowerTriangularMatrix(dim));
    }

    /**
     * Construct a symmetric matrix from a 2D {@code double[][]} array.
     * The array specifies only the lower triangular part (main diagonal inclusive) of the whole matrix.
     * For example,
     * <blockquote><code><pre>
     *         new double[][]{
     *                  {1},
     *                  {2, 3},
     *                  {4, 5, 6},
     *                  {7, 8, 9, 10},
     *                  {11, 12, 13, 14, 15}});
     * </pre></code></blockquote>
     * gives
     * \[
     * \begin{bmatrix}
     * 1 & 2 & 4 & 7 & 11\\
     * 2 & 3 & 5 & 8 & 12\\
     * 4 & 5 & 6 & 9 & 13\\
     * 7 & 8 & 9 & 10 & 14\\
     * 11 & 12 & 13 & 14 & 15
     * \end{bmatrix}
     * \]
     * This constructor uses lower instead of upper triangular representation for visual reason.
     *
     * @param data the lower triangular specification
     */
    public SymmetricMatrix(double[][] data) {
        this(new LowerTriangularMatrix(data));
    }

    /**
     * Copy constructor.
     *
     * @param S a symmetric matrix
     */
    public SymmetricMatrix(SymmetricMatrix S) {
        this(S.L);
    }
    //</editor-fold>

    @Override
    public int nRows() {
        return dim;
    }

    @Override
    public int nCols() {
        return dim;
    }

    @Override
    public SymmetricMatrix deepCopy() {
        return new SymmetricMatrix(this);
    }

    @Override
    public DenseMatrix toDense() {
        DenseMatrix result = L.toDense();

        for (int i = 1; i <= dim; ++i) {
            for (int j = i + 1; j <= dim; ++j) {
                result.set(i, j, L.get(j, i));
            }
        }

        return result;
    }

    //<editor-fold defaultstate="collapsed" desc="setters and getters">
    @Override
    public void set(int row, int col, double value) throws MatrixAccessException {
        throwIfInvalidRow(this, row);
        throwIfInvalidColumn(this, col);

        if (row < col) {
            L.set(col, row, value);
        } else {
            L.set(row, col, value);
        }
    }

    @Override
    public double get(int i, int j) throws MatrixAccessException {
        throwIfInvalidRow(this, i);
        throwIfInvalidColumn(this, j);

        if (i < j) {
            return L.get(j, i);
        } else {
            return L.get(i, j);
        }
    }

    @Override
    public Vector getRow(int i) throws MatrixAccessException {
        throwIfInvalidRow(this, i);

        final double[] vRow = L.getRow(i).toArray();
        final double[] vCol = L.getColumn(i).toArray();

        double[] data = new double[dim];
        int j = 0;
        for (int k = 0; k < i - 1; ++k) {
            data[j++] = vRow[k];
        }
        for (int k = i - 1; k < dim; ++k) {
            data[j++] = vCol[k];
        }

        return new DenseVector(data);
    }

    @Override
    public Vector getColumn(int j) throws MatrixAccessException {
        throwIfInvalidColumn(this, j);
        return getRow(j);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="the math operations">
    @Override
    public Matrix add(Matrix that) {
        throwIfDifferentDimension(this, that);

        if (that instanceof SymmetricMatrix) {
            SymmetricMatrix sum = new SymmetricMatrix(dim);
            sum.L = (LowerTriangularMatrix) this.L.add(((SymmetricMatrix) that).L);
            return sum;
        }

        return math.add(this, that);
    }

    @Override
    public Matrix minus(Matrix that) {
        throwIfDifferentDimension(this, that);

        if (that instanceof SymmetricMatrix) {
            SymmetricMatrix sum = new SymmetricMatrix(dim);
            sum.L = (LowerTriangularMatrix) this.L.minus(((SymmetricMatrix) that).L);
            return sum;
        }

        return math.minus(this, that);
    }

    @Override
    public Matrix multiply(Matrix that) {
        return math.multiply(this, that);
    }

    /**
     * The transpose of a symmetric matrix is the same as itself.
     *
     * @return a copy of itself
     */
    @Override
    public SymmetricMatrix t() {
        return new SymmetricMatrix(this);
    }

    @Override
    public SymmetricMatrix scaled(double scalar) {
        SymmetricMatrix result = new SymmetricMatrix(dim);
        result.L = this.L.scaled(scalar);
        return result;
    }

    @Override
    public SymmetricMatrix opposite() {
        return scaled(-1);
    }

    @Override
    public SymmetricMatrix ZERO() {
        SymmetricMatrix result = new SymmetricMatrix(dim);
        result.L = L.ZERO();
        return result;
    }

    @Override
    public SymmetricMatrix ONE() {
        SymmetricMatrix result = new SymmetricMatrix(dim);
        result.L = L.ONE();
        return result;
    }

    @Override
    public Vector multiply(Vector v) {
        return math.multiply(this, v);
    }
    //</editor-fold>

    @Override
    public String toString() {
        return MatrixUtils.toString(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof SymmetricMatrix) && (obj instanceof MatrixAccess)) {
            return AreMatrices.equal(this, (Matrix) obj, 0);
        }
        final SymmetricMatrix that = (SymmetricMatrix) obj;
        if (this.dim != that.dim) {
            return false;
        }
        if (this.L != that.L && (this.L == null || !this.L.equals(that.L))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 17 * hash + this.dim;
        hash = 17 * hash + (this.L != null ? this.L.hashCode() : 0);
        return hash;
    }
}
