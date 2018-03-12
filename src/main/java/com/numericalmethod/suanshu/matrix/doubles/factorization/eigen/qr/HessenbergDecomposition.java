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
package com.numericalmethod.suanshu.matrix.doubles.factorization.eigen.qr;

import com.numericalmethod.suanshu.datastructure.DimensionCheck;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.operation.CreateMatrix;
import com.numericalmethod.suanshu.matrix.doubles.operation.Householder;
import com.numericalmethod.suanshu.matrix.doubles.operation.Householder.Context;
import com.numericalmethod.suanshu.misc.SuanShuUtils;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import static com.numericalmethod.suanshu.vector.doubles.dense.operation.CreateVector.concat;
import static com.numericalmethod.suanshu.vector.doubles.dense.operation.CreateVector.subVector;

/**
 * Given a <em>square</em> matrix <i>A</i>, we find <i>Q</i> such that <i>Q' * A * Q = H</i> where <i>H</i> is a Hessenberg matrix.
 * This implementation uses Householder reflection to repeatedly zero out the elements below the sub-diagonal.
 * For example, the first step is to left multiply <i>A</i> by a Householder matrix <i>Q<sub>1</sub></i> so that
 * matrix <i>Q<sub>1</sub> * A</i> has zeros in the left column (except for the first two rows).
 * That is (<i>Q's</i> are Hermitian.),
 * \[
 * Q_1 \times A = Q_1' \times A = \begin{bmatrix}
 * a_{11} & * & ... & * \\
 * a_{21} & & & \\
 * 0 & & & \\
 * ... & &A ' & \\
 * ... & & & \\
 * 0 & & &
 * \end{bmatrix}
 * \]
 * Then, we right multiply <i>A</i> by <i>Q<sub>1</sub></i>.
 * We have
 * \[
 * Q_1' \times A \times Q_1 = \begin{bmatrix}
 * a_{11} & ? & ... & ? \\
 * a_{21} & & & \\
 * 0 & & & \\
 * ... & & A'' & \\
 * ... & & & \\
 * 0 & & &
 * \end{bmatrix}
 * \]
 * At the end, we have a Hessenberg <i>H</i> such that
 * \[
 * (Q_n' \times ... \times Q_1') \times A \times (Q_1 \times ... \times Q_n) = H
 * \]
 * We have
 * \[
 * Q = (Q_1 \times ... \times Q_n)
 * \]
 * This transformation always succeeds.
 *
 * @author Haksun Li
 */
public class HessenbergDecomposition {

    private Matrix H;
    private volatile Matrix Q = null;//TODO: is this sufficient to prevent race condition?
    private Householder[] Hs;
    private final int dim;

    /**
     * Run the Hessenberg decomposition for a <em>square</em> matrix.
     * This decomposition does not require a precision parameter, though checking the result will need an epsilon.
     *
     * @param A a square matrix
     * @throws IllegalArgumentException if <i>A</i> is not square
     * @see Hessenberg#isHessenberg(com.numericalmethod.suanshu.matrix.doubles.Matrix, double)
     * @see "G. H. Golub, C. F. van Loan, "Algorithm 7.4.2," Matrix Computations, 3rd edition."
     */
    public HessenbergDecomposition(Matrix A) {
        SuanShuUtils.assertArgument(DimensionCheck.isSquare(A), "Hessenberg Reduction applies only to square matrices");

        dim = A.nRows();

        //already in Hessenberg form
        if (Hessenberg.isHessenberg(A, 0)) {
            H = A.deepCopy();
            Q = A.ONE();
            return;
        }

        H = A.deepCopy();
        Hs = new Householder[dim - 1];//0-th element is not used

        //We apply the Householder operator repeatedly on each columns, hence looping over columns.
        for (int i = 1; i <= dim - 2; i++) {
            /*
             * We will zero out in this column all entries below the (i+1)-th entry.
             * E.g., for the first column, we want to have this form (a11,a21,0,..)'.
             */
            Context defn = Householder.getContext(subVector(H.getColumn(i), i + 1, dim));
            Hs[i] = new Householder(defn.generator);

            //TODO: how to avoid copying the sub-matrices back and forth?

            //reflect columns
            Matrix sub1 = CreateMatrix.subMatrix(H, i + 1, dim, i, dim);
            Matrix Hxsub1 = Hs[i].reflect(sub1);//TODO: reflect takes DenseMatrix, but not SubMatrixRef
            CreateMatrix.replace(H, i + 1, dim, i, dim, Hxsub1);

            //reflect rows
            Matrix sub2 = CreateMatrix.subMatrix(H, 1, dim, i + 1, dim);
            Matrix Hxsub2 = Hs[i].reflectRows(sub2);
            CreateMatrix.replace(H, 1, dim, i + 1, dim, Hxsub2);
        }
    }

    /**
     * Get the <i>Q</i> matrix, where
     * \[
     * Q = (Q_1 \times ... \times Q_n)
     * \]
     * <i>n = dim - 2</i>.
     * <p/>
     * To compute <i>Q</i>, instead of explicitly doing this multiplication,
     * we can improve the performance by applying the <i>Q<sub>i</sub></i>'s repeatedly on an identity matrix.
     *
     * @return the <i>Q</i> matrix in the QR decomposition
     */
    public Matrix Q() {
        if (Q == null) {
            //pad the Hs so that they all have the same dimension
            for (int i = 1; i <= dim - 2; ++i) {
                Vector u = Hs[i].generator();
                u = concat(
                        new DenseVector(i),//essentially ZERO()
                        u);
                Hs[i] = new Householder(u);
            }

            Q = Householder.product(Hs, 1, dim - 2, dim, dim);
        }

        return Q.deepCopy();
    }

    /**
     * Get the <i>H</i> matrix.
     *
     * @return <i>H</i>
     */
    public Matrix H() {
        return H.deepCopy();
    }
}
