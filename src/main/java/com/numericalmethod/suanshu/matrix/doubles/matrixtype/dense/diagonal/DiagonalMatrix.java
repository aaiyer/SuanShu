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
import static com.numericalmethod.suanshu.datastructure.DimensionCheck.throwIfIncompatible4Multiplication;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.misc.R;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;

/**
 * A diagonal matrix has non-zero entries only on the main diagonal.
 *
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/Diagonal_matrix">Wikipedia: Diagonal matrix</a>
 */
public class DiagonalMatrix extends DiagonalDataMatrix {

    //<editor-fold defaultstate="collapsed" desc="Ctors">
    /**
     * Construct a diagonal matrix from a {@code double[]}.
     * For example,
     * <blockquote><pre><code>
     *         new double[][]{
     *              {1, 2, 3, 4, 5},
     *          }
     * </code></pre></blockquote>
     * gives
     * \[
     * \begin{bmatrix}
     * 1 & 0 & 0 & 0 & 0\\
     * 0 & 2 & 0 & 0 & 0\\
     * 0 & 0 & 3 & 0 & 0\\
     * 0 & 0 & 0 & 4 & 0\\
     * 0 & 0 & 0 & 0 & 5
     * \end{bmatrix}
     * \]
     *
     * @param data the 1D array input
     */
    public DiagonalMatrix(double[] data) {
        this(new DiagonalData(new double[][]{data}));
    }

    /**
     * Construct a 0 diagonal matrix of dimension <i>dim * dim</i>.
     *
     * @param dim the matrix dimension
     */
    public DiagonalMatrix(int dim) {
        this(new DiagonalData(DiagonalData.Type.DIAGONAL, dim));
    }

    /**
     * Copy constructor.
     *
     * @param that a diagonal matrix
     */
    public DiagonalMatrix(DiagonalMatrix that) {
        this(new DiagonalData(that.storage));
    }

    private DiagonalMatrix(DiagonalData data) {
        super(data);
    }
    //</editor-fold>

    @Override
    public DiagonalMatrix deepCopy() {
        return new DiagonalMatrix(this);
    }

    /**
     * Compute the sum of two diagonal matrices.
     *
     * @param that a diagonal matrix
     * @return {@code this} + {@code that}
     */
    @Override
    public Matrix add(Matrix that) {
        throwIfDifferentDimension(this, that);

        if (that instanceof DiagonalMatrix) {
            DiagonalData result = this.storage.add(((DiagonalMatrix) that).storage);
            DiagonalMatrix T = new DiagonalMatrix(result);
            return T;
        }

        return super.add(that);
    }

    /**
     * Compute the difference between two diagonal matrices.
     *
     * @param that a diagonal matrix
     * @return {@code this} - {@code that}
     */
    @Override
    public Matrix minus(Matrix that) {
        throwIfDifferentDimension(this, that);

        if (that instanceof DiagonalMatrix) {
            DiagonalData result = this.storage.minus(((DiagonalMatrix) that).storage);
            DiagonalMatrix T = new DiagonalMatrix(result);
            return T;
        }

        return super.minus(that);
    }

    /**
     * Compute the product of two diagonal matrices.
     *
     * @param that a diagonal matrix
     * @return {@code this} * {@code that}
     */
    @Override
    public Matrix multiply(Matrix that) {
        throwIfIncompatible4Multiplication(this, that);//we only multiply sqaure matrices

        if (that instanceof DiagonalMatrix) {
            double[] d1 = this.storage.getDiagonal();
            double[] d2 = ((DiagonalMatrix) that).storage.getDiagonal();

            double[] d3 = new double[storage.dim];
            for (int i = 0; i < storage.dim; ++i) {
                d3[i] = d1[i] * d2[i];
            }
            return new DiagonalMatrix(d3);
        }

        return super.multiply(that);
    }

    @Override
    public Vector multiply(Vector v) {
        throwIfIncompatible4Multiplication(this, v);
        Vector diagonal = new DenseVector(this.storage.getDiagonal());
        return v.multiply(diagonal);
    }

    @Override
    public DiagonalMatrix scaled(double scalar) {
        DiagonalData result = this.storage.scaled(scalar);
        DiagonalMatrix T = new DiagonalMatrix(result);
        return T;
    }

    @Override
    public DiagonalMatrix opposite() {
        return scaled(-1);
    }

    /**
     * The transpose of a diagonal matrix is the same as itself.
     *
     * @return a copy
     */
    @Override
    public DiagonalMatrix t() {
        return new DiagonalMatrix(this);
    }

    @Override
    public DiagonalMatrix ZERO() {
        return new DiagonalMatrix(storage.dim);
    }

    @Override
    public DiagonalMatrix ONE() {
        double[] one = R.rep(1.0, storage.dim);
        DiagonalMatrix T = new DiagonalMatrix(one);
        return T;
    }
}
