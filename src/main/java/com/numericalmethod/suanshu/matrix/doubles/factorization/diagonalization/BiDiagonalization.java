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
package com.numericalmethod.suanshu.matrix.doubles.factorization.diagonalization;

import com.numericalmethod.suanshu.datastructure.DimensionCheck;
import com.numericalmethod.suanshu.matrix.doubles.ImmutableMatrix;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.diagonal.BidiagonalMatrix;
import com.numericalmethod.suanshu.matrix.doubles.operation.CreateMatrix;
import com.numericalmethod.suanshu.matrix.doubles.operation.Householder;
import com.numericalmethod.suanshu.matrix.doubles.operation.Householder.Context;
import com.numericalmethod.suanshu.misc.SuanShuUtils;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import static com.numericalmethod.suanshu.vector.doubles.dense.operation.CreateVector.*;

/**
 * Given a tall (<i>m x n</i>) matrix <i>A</i>, where <i>m &ge; n</i>,
 * we find orthogonal matrices <i>U</i> and <i>V</i> such that <i>U' * A * V = B</i>.
 * <i>B</i> is an upper bi-diagonal matrix.
 * That is,
 * \[
 * U'AV = \begin{bmatrix}
 * d_1 & f_1 & ... & & & \\
 * 0 & d_2 & f_2 & ... & & \\
 * 0 & ... & & & & \\
 * ... & & & & d_{n-1} & f_{n-1} \\
 * ... & & & & & d_n \\
 * 0 & ... & & & & 0 \\
 * & ... & & & & ... \\
 * 0 & ... & & & & 0
 * \end{bmatrix}
 * \]
 * This implementation uses the Householder reflection process to repeatedly zero out the columns and the rows (partially).
 * For example, the first step is to left multiply <i>A</i> with the Householder matrix <i>U<sub>1</sub>'</i> so that
 * matrix <i>U1 * A</i> has zeros in the left column (except for the first row). That is,
 * \[
 * \begin{bmatrix}
 * a_{1,1} & * & ... & * \\
 * 0 & & & \\
 * 0 & & & \\
 * ... & & A_1 & \\
 * ... & & & \\
 * 0 & & &
 * \end{bmatrix}
 * \]
 * Then, we right multiply <i>A</i> with <i>V<sub>1</sub></i>. (Note that the <i>V's</i> are Hermitian.) We have
 * \[
 * U_1'AV_1 = \begin{bmatrix}
 * a_{1,1} & a_{1,2} & 0 & ... & 0\\
 * 0 & & & & \\
 * 0 & & & & \\
 * ... & & A_2 & & \\
 * ... & & & & \\
 * 0 & & & &
 * \end{bmatrix}
 * \]
 * In the end,
 * <i>(U<sub>1</sub> * ... * U<sub>n</sub>)' * A * (V<sub>1</sub> * ... * V<sub>n</sub>) = B</i> where <i>B</i> is upper bi-diagonal.
 * The upper part of <i>B</i>, an <i>n x n</i> matrix, is a square, bi-diagonal matrix.
 * <p/>
 * This transformation always succeeds.
 *
 * @author Haksun Li
 */
public class BiDiagonalization {

    private Householder[] Us;
    private Householder[] Vs;
    private BidiagonalMatrix B;
    private final int nRows;
    private final int nCols;

    /**
     * Run the Householder bi-diagonalization for a tall matrix.
     *
     * @param A a tall matrix
     * @throws IllegalArgumentException if <i>A</i> is not tall
     * @see "G. H. Golub, C. F. van Loan, "Algorithm 5.4.2," Matrix Computations, 3rd edition."
     */
    public BiDiagonalization(Matrix A) {
        SuanShuUtils.assertArgument(DimensionCheck.isTall(A), "Bi-diagonalization applies only to tall matrices");

        nRows = A.nRows();
        nCols = A.nCols();

        Matrix M = A.deepCopy();
        Us = new Householder[nCols + 1];//0-th element is not used
        Vs = new Householder[nCols - 1];//0-th element is not used; up to (nCols-2)

        //We apply the Householder operator repeatedly on each columns, hence looping over columns.
        for (int i = 1; i <= nCols; i++) {
            /*
             * We will zero out in this column all entries below the (k+1)-th entry.
             * E.g., for the first column, we want to have this form (a11,a21,0,..)'.
             */
            Context defn = Householder.getContext(subVector(M.getColumn(i), i, nRows));
            Us[i] = new Householder(defn.generator);

            //TODO: how to avoid copying the sub-matrices back and forth?

            //reflect columns
            Matrix sub1 = CreateMatrix.subMatrix(M, i, nRows, i, nCols);
            Matrix Uxsub1 = Us[i].reflect(sub1);//TODO: reflect takes DenseMatrix, but not SubMatrixRef
            CreateMatrix.replace(M, i, nRows, i, nCols, Uxsub1);

            //reflect rows
            if (i <= nCols - 2) {
                defn = Householder.getContext(subVector(M.getRow(i), i + 1, nCols));
                Vs[i] = new Householder(defn.generator);

                Matrix sub2 = CreateMatrix.subMatrix(M, i, nRows, i + 1, nCols);
                Matrix Hxsub2 = Vs[i].reflectRows(sub2);
                CreateMatrix.replace(M, i, nRows, i + 1, nCols, Hxsub2);
            }
        }

        double[] superdiagonal = M.nCols() > 1 ? superDiagonal(M).toArray() : null;//column matrix has no super-diagonal
        double[] maindiagonal = diagonal(M).toArray();
        B = new BidiagonalMatrix(new double[][]{superdiagonal, maindiagonal});
    }

    /**
     * Get <i>U</i>, where
     * <i>U<sup>'</sup> = U<sub>k</sub> * ... * U<sub>1</sub></i>, {@code k = A.nCols()}.
     * The dimension of <i>U</i> is <i>m x m</i>.
     * <p/>
     * To compute <i>U</i>,
     * instead of explicitly doing this multiplication, this implementation improves the performance
     * by applying <i>U<sub>i</sub></i>'s repeatedly on an identity matrix.
     * We take the transpose afterward.
     *
     * @return the <i>U</i> matrix
     */
    public Matrix U() {
        //pad the Hs so that they all have the same dimension
        for (int i = 1; i < Us.length; ++i) {
            Vector u = Us[i].generator();
            u = concat(
                    new DenseVector(i - 1),//essentially ZERO()
                    u);
            Us[i] = new Householder(u);
        }

        Matrix U = Householder.product(Us, Us.length - 1, 1);

        return U.t();
    }

    /**
     * Get <i>V</i>, where
     * <i>V<sup>'</sup> = V<sub>k</sub> * ... * V<sub>1</sub></i>, {@code k = A.nCols() - 2}.
     * The dimension of <i>V</i> is <i>n x n</i>.
     * <p/>
     * To compute <i>V</i>,
     * instead of explicitly doing this multiplication, this implementation improves the performance
     * by applying <i>V<sub>i</sub></i>'s repeatedly on an identity matrix.
     * We take the transpose afterward.
     *
     * @return the <i>V</i> matrix
     */
    public Matrix V() {
        //pad the Hs so that they all have the same dimension
        for (int i = 1; i < Vs.length; ++i) {
            Vector v = Vs[i].generator();
            v = concat(
                    new DenseVector(i),//essentially ZERO()
                    v);
            Vs[i] = new Householder(v);
        }

        Matrix V = null;
        if (Vs.length > 1) {
            V = Householder.product(Vs, Vs.length - 1, 1);
        } else {
            V = new DenseMatrix(nCols, nCols).ONE();
        }

        return V.t();
    }

    /**
     * Get <i>B</i>, which is the square upper part of {@code U.t().multiply(A).multiply(V)}.
     * The dimension of <i>B</i> is <i>n x n</i>.
     *
     * @return <i>B</i>
     */
    public BidiagonalMatrix B() {
        return new BidiagonalMatrix(B);
    }
}
