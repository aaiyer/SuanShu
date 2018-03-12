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

import com.numericalmethod.suanshu.datastructure.DimensionCheck;
import static com.numericalmethod.suanshu.datastructure.DimensionCheck.throwIfDifferentDimension;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.misc.R;
import static com.numericalmethod.suanshu.number.DoubleUtils.compare;
import com.numericalmethod.suanshu.vector.doubles.Vector;

/**
 * A bi-diagonal matrix is either upper or lower diagonal.
 * It has non-zero entries only on the main, and either the super-diagonal (upper) or sub-diagonal (lower).
 *
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/Bidiagonal_matrix">Wikipedia: Bidiagonal matrix</a>
 */
public class BidiagonalMatrix extends DiagonalDataMatrix {

    /** the types of bi-diagonal matrices available */
    public static enum BidiagonalMatrixType {

        /** an upper bi-diagonal matrix, where there are only non-zero entries on the main and super diagonal */
        UPPER,
        /** a lower bi-diagonal matrix, where there are only non-zero entries on the main and sub diagonal */
        LOWER;
    };

    //<editor-fold defaultstate="collapsed" desc="Ctors">
    private BidiagonalMatrix(DiagonalData data) {
        super(data);
    }

    /**
     * Construct a bi-diagonal matrix from a 2D {@code double[][]} array.
     * There are always two rows.
     * The longer row is the main diagonal and has one more element.
     * If the first row is shorter, it is an upper bi-diagonal matrix.
     * If the second row is shorter, it is a lower bi-diagonal matrix.
     * For example,
     * <blockquote><pre><code>
     *         new double[][]{
     *              {2, 5, 8, 11},
     *              {1, 4, 7, 10, 13}
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
     * We allow {@code null} input when a diagonal is 0s, except for the main diagonal.
     * For example,
     * <blockquote><pre><code>
     *         new double[][]{
     *              {1, 4, 7, 10, 13},
     *              null
     *          }
     * </code></pre></blockquote>
     * gives
     * \[
     * \begin{bmatrix}
     * 1 & 0 & 0 & 0 & 0\\
     * 0 & 4 & 0 & 0 & 0\\
     * 0 & 0 & 7 & 0 & 0\\
     * 0 & 0 & 0 & 10 & 0\\
     * 0 & 0 & 0 & 0 & 13
     * \end{bmatrix}
     * \]
     * The following is not allowed because the dimension cannot be determined.
     * <blockquote><pre><code>
     *         new double[][]{
     *              null,
     *              null
     *          }
     * </code></pre></blockquote>
     * This implementation treats a diagonal matrix as an upper bi-diagonal matrix.
     *
     * @param data the 2D array input
     */
    public BidiagonalMatrix(double[][] data) {
        this(createDataByArray(data));
    }

    private static DiagonalData createDataByArray(double[][] data) {
        if ((data[0] == null) || (data[1] == null)) {
            double[] diag = (data[0] == null)
                            ? data[1]//main diagonal
                            : data[0];//super-diagonal if exists
            //we treat a (main) diagonal matrix as upper diagonal
            return new DiagonalData(new double[][]{R.rep(0.0, diag.length - 1), diag});
        } else {
            return new DiagonalData(data);
        }
    }

    /**
     * Construct a 0 bi-diagonal matrix of dimension <i>dim * dim</i>.
     *
     * @param dim  the dimension of the matrix
     * @param type the type of bi-diagonal matrix to create
     */
    public BidiagonalMatrix(int dim, BidiagonalMatrixType type) {
        this(createDataByType(dim, type));
    }

    private static DiagonalData createDataByType(int dim, BidiagonalMatrixType type) {
        return new DiagonalData(type == BidiagonalMatrixType.UPPER
                                ? DiagonalData.Type.BI_DIAGONAL_UPPER
                                : DiagonalData.Type.BI_DIAGONAL_LOWER, dim);
    }

    /**
     * Copy constructor.
     *
     * @param that a bi-diagonal matrix
     */
    public BidiagonalMatrix(BidiagonalMatrix that) {
        this(new DiagonalData(that.storage));
    }
    //</editor-fold>

    @Override
    public BidiagonalMatrix deepCopy() {
        return new BidiagonalMatrix(this);
    }

    /**
     * Get the bi-diagonal matrix type.
     *
     * @return the bi-diagonal matrix type
     */
    public BidiagonalMatrixType getType() {
        switch (storage.type) {
            case BI_DIAGONAL_UPPER:
                return BidiagonalMatrixType.UPPER;
            case BI_DIAGONAL_LOWER:
                return BidiagonalMatrixType.LOWER;
            default:
                throw new RuntimeException("inconsistent diagonal data");
        }
    }

    /**
     * A bi-diagonal matrix is unreduced if it has no 0 on <em>both</em> the super and main diagonals.
     *
     * @param epsilon a precision parameter: when a number |x| ≤ ε, it is considered 0
     * @return {@code true} if {@code this} is unreduced
     */
    public boolean isUnreduced(double epsilon) {
        double[] diag = storage.getDiagonal();
        for (int i = 0; i < diag.length; ++i) {
            if (compare(diag[i], 0, epsilon) == 0) {
                return false;
            }
        }

        diag = getSuperDiagonal().toArray();
        for (int i = 0; i < diag.length; ++i) {
            if (compare(diag[i], 0, epsilon) == 0) {
                return false;
            }
        }

        return true;
    }

    @Override
    public Matrix add(Matrix that) {
        throwIfDifferentDimension(this, that);

        if (that instanceof BidiagonalMatrix) {
            DiagonalData result = this.storage.add(((BidiagonalMatrix) that).storage);
            BidiagonalMatrix T = new BidiagonalMatrix(result);
            return T;
        }

        return super.add(that);
    }

    @Override
    public Matrix minus(Matrix that) {
        throwIfDifferentDimension(this, that);

        if (that instanceof BidiagonalMatrix) {
            DiagonalData result = this.storage.minus(((BidiagonalMatrix) that).storage);
            BidiagonalMatrix T = new BidiagonalMatrix(result);
            return T;
        }

        return super.minus(that);
    }

    /**
     * {@inheritDoc}
     * <p/>
     * When the two matrices have opposite types, i.e., one is {@link BidiagonalMatrixType#UPPER} and one is {@link BidiagonalMatrixType#LOWER},
     * the product of the two bi-diagonal matrices is a tridiagonal matrix.
     *
     * @param that a matrix
     * @return {@code this} * {@code that}
     */
    @Override
    public Matrix multiply(Matrix that) {
        if (that instanceof BidiagonalMatrix) {
            if (this.getType() != ((BidiagonalMatrix) that).getType()) {
                BidiagonalMatrix B = (BidiagonalMatrix) that;
                DimensionCheck.throwIfDifferentDimension(this, B); // we only multiply square matrices

                int dim = storage.dim;
                double[] superdiagonal = new double[dim - 1];
                double[] maindiagonal = new double[dim];
                double[] subdiagonal = new double[dim - 1];

                Vector row = this.getRow(1);
                Vector col = B.getColumn(1);
                maindiagonal[0] = row.innerProduct(col);

                col = B.getColumn(2);
                superdiagonal[0] = row.innerProduct(col);

                for (int i = 2; i < dim; ++i) {
                    row = this.getRow(i);

                    col = B.getColumn(i - 1);
                    subdiagonal[i - 2] = row.innerProduct(col);

                    col = B.getColumn(i);
                    maindiagonal[i - 1] = row.innerProduct(col);

                    col = B.getColumn(i + 1);
                    superdiagonal[i - 1] = row.innerProduct(col);
                }

                row = this.getRow(dim);
                col = B.getColumn(dim);
                maindiagonal[dim - 1] = row.innerProduct(col);

                col = B.getColumn(dim - 1);
                subdiagonal[dim - 2] = row.innerProduct(col);

                return new TridiagonalMatrix(new double[][]{superdiagonal, maindiagonal, subdiagonal});
            }
        }

        return super.multiply(that);//else, use the default implementation
    }

    @Override
    public BidiagonalMatrix scaled(double c) {
        return new BidiagonalMatrix(storage.scaled(c));
    }

    @Override
    public BidiagonalMatrix opposite() {
        return scaled(-1);
    }

    @Override
    public BidiagonalMatrix t() {//exchanging the super and sub diagonals
        double[][] diags = new double[2][];

        if (getType() == BidiagonalMatrixType.UPPER) {
            diags[0] = storage.getDiagonal();
            diags[1] = storage.getSuperDiagonal();
        } else {
            diags[0] = storage.getSubDiagonal();
            diags[1] = storage.getDiagonal();
        }

        BidiagonalMatrix T = new BidiagonalMatrix(diags);
        return T;
    }

    @Override
    public BidiagonalMatrix ZERO() {
        return new BidiagonalMatrix(storage.dim, BidiagonalMatrixType.UPPER);
    }

    @Override
    public BidiagonalMatrix ONE() {
        double[] one = R.rep(1.0, storage.dim);
        return new BidiagonalMatrix(new double[][]{null, one});
    }
}
