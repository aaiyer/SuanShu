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

import static com.numericalmethod.suanshu.datastructure.DimensionCheck.throwIfDifferentDimension;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.misc.R;

/**
 * A tri-diagonal matrix has non-zero entries only on the super, main and sub diagonals.
 *
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/Tridiagonal_matrix">Wikipedia: Tridiagonal matrix</a>
 */
public class TridiagonalMatrix extends DiagonalDataMatrix {

    //<editor-fold defaultstate="collapsed" desc="Ctors">
    /**
     * Construct a 0 tri-diagonal matrix of dimension <i>dim * dim</i>.
     *
     * @param dim the dimension of the matrix
     */
    public TridiagonalMatrix(int dim) {
        this(new DiagonalData(DiagonalData.Type.TRI_DIAGONAL, dim));
    }

    /**
     * Construct a tri-diagonal matrix from a 3-row 2D {@code double[][]} array such that:
     * <ol>
     * <li>the first row is the super diagonal with <i>(dim - 1)</i> entries;
     * <li>the second row is the main diagonal with <i>dim</i> entries;
     * <li>the third row is the sub diagonal with <i>(dim - 1)</i> entries.
     * </ol>
     * For example,
     * <blockquote><pre><code>
     *         new double[][]{
     *              {2, 5, 8, 11},
     *              {1, 4, 7, 10, 13},
     *              {3, 6, 9, 12}
     *          }
     * </code></pre></blockquote>
     * gives
     * \[
     * \begin{bmatrix}
     * 1 & 2 & 0 & 0 & 0\\
     * 3 & 4 & 5 & 0 & 0\\
     * 0 & 6 & 7 & 8 & 0\\
     * 0 & 0 & 9 & 10 & 11\\
     * 0 & 0 & 0 & 12 & 13
     * \end{bmatrix}
     * \]
     * We allow {@code null} input when a diagonal is 0s.
     * For example,
     * <blockquote><pre><code>
     *         new double[][]{
     *              {2, 5, 8, 11},
     *              {1, 4, 7, 10, 13},
     *              null
     *          }
     * </code></pre></blockquote>
     * gives
     * \[
     * \begin{bmatrix}
     * 1 & 2 & 0 & 0 & 0\\
     * 0 & 4 & 5 & 0 & 0\\
     * 0 & 0 & 7 & 8 & 0\\
     * 0 & 0 & 0 & 10 & 11\\
     * 0 & 0 & 0 & 0 & 13
     * \end{bmatrix}
     * \]
     * The following is not allowed because the dimension cannot be determined.
     * <blockquote><pre><code>
     *         new double[][]{
     *              null,
     *              null,
     *              null
     *          }
     * </code></pre></blockquote>
     *
     * @param data the 2D array input
     */
    public TridiagonalMatrix(double[][] data) {
        this(createDataByArray(data));
    }

    private static DiagonalData createDataByArray(double[][] data) {
        if (data.length != 3) {
            throw new IllegalArgumentException("tri-diagonal matrix data must have 3 rows");
        }

        int dim = 0;
        if (data[0] != null) {
            dim = data[0].length + 1;
        } else if (data[1] != null) {
            dim = data[1].length;
        } else if (data[2] != null) {
            dim = data[2].length + 1;
        } else {
            throw new IllegalArgumentException("there must be at least one non-null row");
        }

        double[][] diags = new double[3][];
        diags[0] = (data[0] != null) ? data[0] : R.rep(0.0, dim - 1);
        diags[1] = (data[1] != null) ? data[1] : R.rep(0.0, dim);
        diags[2] = (data[2] != null) ? data[2] : R.rep(0.0, dim - 1);

        return new DiagonalData(diags);
    }

    /**
     * Copy constructor performing a deep copy.
     *
     * @param that a tri-diagonal matrix
     */
    public TridiagonalMatrix(TridiagonalMatrix that) {
        this(new DiagonalData(that.storage));
    }

    private TridiagonalMatrix(DiagonalData data) {
        super(data);
    }
    //</editor-fold>

    @Override
    public TridiagonalMatrix deepCopy() {
        return new TridiagonalMatrix(this);
    }

    @Override
    public Matrix add(Matrix that) {
        throwIfDifferentDimension(this, that);

        if (that instanceof TridiagonalMatrix) {
            DiagonalData result = this.storage.add(((TridiagonalMatrix) that).storage);
            TridiagonalMatrix T = new TridiagonalMatrix(result);
            return T;
        }

        return super.add(that);
    }

    @Override
    public Matrix minus(Matrix that) {
        throwIfDifferentDimension(this, that);

        if (that instanceof TridiagonalMatrix) {
            DiagonalData result = this.storage.minus(((TridiagonalMatrix) that).storage);
            TridiagonalMatrix T = new TridiagonalMatrix(result);
            return T;
        }

        return super.minus(that);
    }

    @Override
    public TridiagonalMatrix scaled(double scalar) {
        DiagonalData result = this.storage.scaled(scalar);
        TridiagonalMatrix T = new TridiagonalMatrix(result);
        return T;
    }

    @Override
    public TridiagonalMatrix opposite() {
        return scaled(-1);
    }

    @Override
    public TridiagonalMatrix t() { // exchanging the super and sub diagonals
        double[][] diags = new double[3][];
        diags[0] = storage.getSubDiagonal();
        diags[1] = storage.getDiagonal();
        diags[2] = storage.getSuperDiagonal();

        TridiagonalMatrix T = new TridiagonalMatrix(diags);
        return T;
    }

    @Override
    public TridiagonalMatrix ZERO() {
        return new TridiagonalMatrix(storage.dim);
    }

    @Override
    public TridiagonalMatrix ONE() {
        double[][] one = new double[3][];
        int dim = storage.dim;
        one[0] = R.rep(0.0, dim - 1);
        one[1] = R.rep(1.0, dim);
        one[2] = R.rep(0.0, dim - 1);
        TridiagonalMatrix T = new TridiagonalMatrix(one);
        return T;
    }

    /**
     * Don't use this.
     *
     * @param that
     * @return a matrix
     * @deprecated TridiagonalMatrices do not constitute a Monoid
     */
    @Deprecated
    private TridiagonalMatrix multiply(TridiagonalMatrix that) { // TODO: if so, why still implement TridiagonalMatrixSpace?
        throw new UnsupportedOperationException("TridiagonalMatrices do not constitute a Monoid.");
    }
}
