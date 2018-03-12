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
package com.numericalmethod.suanshu.matrix.doubles.factorization.qr;

import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.PermutationMatrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.triangle.UpperTriangularMatrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.sparse.SparseVector;
import com.numericalmethod.suanshu.matrix.doubles.operation.CreateMatrix;
import com.numericalmethod.suanshu.matrix.doubles.operation.Householder;
import com.numericalmethod.suanshu.matrix.doubles.operation.Householder.Context;
import com.numericalmethod.suanshu.misc.SuanShuUtils;
import static com.numericalmethod.suanshu.number.DoubleUtils.compare;
import static com.numericalmethod.suanshu.number.DoubleUtils.isZero;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import static com.numericalmethod.suanshu.vector.doubles.dense.operation.CreateVector.concat;
import static com.numericalmethod.suanshu.vector.doubles.dense.operation.CreateVector.subVector;

/**
 * Successive Householder reflections gradually transform a matrix <i>A</i> to the upper triangular form.
 * For example, the first step is to multiply <i>A</i> with a Householder matrix <i>Q<sub>1</sub></i> so that
 * matrix <i>Q<sub>1</sub> * A</i> has zeros in the left column (except for the first row).
 * That is,
 * \[
 * Q_1 \times A = \begin{bmatrix}
 * a_1 & * & ... & * \\
 * 0 & & & \\
 * ... & & & \\
 * ... & & A' & \\
 * ... & & & \\
 * 0 & & &
 * \end{bmatrix}
 * \]
 * At the end,\(Q_n \times ... \times Q_1 \times A = R\) where <i>R</i> is upper triangular.
 * <p/>
 * Householder reflection has a better numerical stability than the Gram-Schmidt process.
 *
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/QR_decomposition#Using_Householder_reflections">Wikipedia: Using Householder reflections</a>
 */
public class HouseholderReflection implements QRDecomposition {

    private UpperTriangularMatrix R;//nCols x nCols
    private Vector[] cols;
    private Householder[] Hs;
    private final int nRows;//the number of rows
    private final int nCols;//the number of columns
    private final double epsilon;

    /**
     * Run the Householder reflection process to orthogonalize a matrix.
     *
     * @param A       a matrix, where the number of rows &ge; the number of columns
     * @param epsilon a precision parameter: when a number |x| ≤ ε, it is considered 0
     * @throws IllegalArgumentException if the number of rows &lt; the number of columns
     */
    public HouseholderReflection(Matrix A, double epsilon) {
        SuanShuUtils.assertArgument((A.nRows() >= A.nCols()),
                                    "QR decomposition by Householder Reflection applies to matrix where the number of rows >= the number of columns");

        this.nRows = A.nRows();
        this.nCols = A.nCols();
        this.epsilon = epsilon;

        R = new UpperTriangularMatrix(nCols);
        R.set(1, 1, 0);//allocate space
        cols = new Vector[nCols + 1];//the 0-th element is not used
        Hs = new Householder[nCols + 1];//the 0-th element is not used

        //make a copy of A by columns
        for (int i = 1; i <= nCols; ++i) {
            cols[i] = A.getColumn(i);
        }

        //We apply the Householder operator repeatedly on each columns, hence looping over columns.
        for (int i = 1; i <= Math.min(nCols, nRows - 1); i++) {//if square, i <= nRows-1; else i <= nCols (b/c nRows > nCols by assumption)
            /*
             * We will zero out in this column all entries below the i-th entry.
             * E.g., for the first column, we want to have this form (a,0,0,..)'.
             *
             * We work on a sub-matrix., namely,
             * A[i:nRows, i:nCols]
             */
            Context defn = Householder.getContext(subVector(cols[i], i, nRows));
            Vector u = defn.generator;
            /*
             * Treat the (sub-)column as 0, if the norm is too small.
             * This is to prevent spurious Householder operation on a "zero" column,
             * which may produce incorrect result.
             */
            if (isZero(Math.abs(defn.lambda), epsilon)) {//linear dependence on previous columns
                u = new SparseVector(nRows);//ZERO vector
                Hs[i] = new Householder(u);
                R.set(i, i, 0);//record R's diagonal
            } else {//to reduce computation, we perform Householder reflection only for linearly independent columns
                u = concat(
                        new SparseVector(i - 1),//ZERO vector
                        u);
                Hs[i] = new Householder(u);

                /*
                 * Apply the householder transformation to the rest of the columns, but only partially (sub-matrix).
                 * That is, we set only a sub-matrix.
                 *
                 * TODO: how to improve performance by doing inner-product (reflect) only on non-zero entries?
                 */
                for (int j = i + 1; j <= nCols; ++j) {
                    cols[j] = Hs[i].reflect(cols[j]);
                }

                R.set(i, i, defn.lambda);//record R's diagonal
            }
        }

        computeR();
    }

    /**
     * Run the Householder reflection process to orthogonalize a matrix.
     *
     * @param A a matrix, where the number of rows &ge; the number of columns
     */
    public HouseholderReflection(Matrix A) {
        this(A, SuanShuUtils.autoEpsilon(A));
    }

    /**
     * Compute <i>R</i>.
     */
    private void computeR() {
        /*
         * R's diagonal is already correct by the constructor.
         * Just copy the upper triangle entries from the Cols,
         * which are A modified by a sequence of Householder transformations.
         */
        for (int i = 2; i <= nCols; ++i) {//loop over columns
            for (int j = 1; j < i; ++j) {//loop over rows
                R.set(j, i, cols[i].get(j));
            }
        }

        //for square matrix, last column was not worked on to generate diagonal element
        if (nRows == nCols) {
            R.set(nCols, nCols, cols[nCols].get(nCols));
        }
    }

    /**
     * Get the <i>Q</i> matrix in the QR decomposition. The dimension of <i>Q</i> is <i>nRows x nCols</i>.
     * \[
     * \begin{array}{lcl}
     * Q_n \times ... \times Q_3 \times Q_2 \times Q_1 \times A = R \\
     * A = [Q_n \times ... \times Q_3 \times Q_2 \times Q_1]^{-1} \times R \\
     * = [Q_1^{-1} \times Q_2^{-1} \times ... \times Q_n^{-1}] \times R \\
     * = [Q_1 \times Q_2 \times ... \times Q_n] \times R \\
     * Q = Q_1 \times Q_2 \times ... \times Q_n
     * \end{array}
     * \]
     * \(Q_1^{-1} = Q_1\) , by the unitary property of Householder matrix
     * <p/>
     * This implementation does not compute <i>Q</i> by explicitly doing this multiplication.
     * Instead, we improve the performance by applying <i>Q<sub>i</sub></i>'s repeatedly on an identity matrix.
     *
     * @return <i>Q</i>
     */
    @Override
    public Matrix Q() {
        Matrix Q = Householder.product(Hs, 1, nCols, nRows, nCols);//TODO: cache Q
        return Q;
    }

    @Override
    public UpperTriangularMatrix R() {
        return new UpperTriangularMatrix(R);
    }

    /**
     * Get <i>P</i>, the pivoting matrix in the QR decomposition.
     * Householder process does not need pivoting.
     * Hence, <i>P</i> is always an identity matrix.
     *
     * @return an identity matrix
     */
    @Override
    public PermutationMatrix P() {
        return new PermutationMatrix(nCols);
    }

    /**
     * This implementation computes the rank by counting the number of non-zero rows in <i>R</i>.
     * <p/>
     * <em>Note that <i>Q</i> may have more columns than the rank.
     * The user should interpret the result with caution.</em>
     *
     * @return the rank
     */
    @Override
    public int rank() {
        //count the number of non-zero rows in R
        //i.e., count the number of non-zero diagonal elements in R
        int result = 0;
        for (int i = 1; i <= R.nCols(); ++i) {
            if (compare(R.get(i, i), 0, epsilon) != 0) {
                ++result;
            }
        }
        return result;
    }

    @Override
    public Matrix squareQ() {
        Matrix squareQ = Householder.product(Hs, 1, nCols, nRows, nRows);//the dimension is different from that in Q
        return squareQ;
    }

    @Override
    public Matrix tallR() {
        Matrix tallR = new DenseMatrix(nRows, nCols).ZERO();
        CreateMatrix.replace(tallR, 1, nCols, 1, nCols, R);
        return tallR;
    }
}
