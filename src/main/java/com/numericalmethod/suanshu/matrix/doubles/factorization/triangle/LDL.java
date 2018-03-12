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
package com.numericalmethod.suanshu.matrix.doubles.factorization.triangle;

import com.numericalmethod.suanshu.matrix.doubles.IsMatrix;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.diagonal.DiagonalMatrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.triangle.LowerTriangularMatrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.triangle.UpperTriangularMatrix;
import static com.numericalmethod.suanshu.number.DoubleUtils.isZero;

/**
 * The LDL decomposition decomposes a real and symmetric (hence square) matrix <i>A</i> into <i>A = L * D * L<sup>t</sup></i>.
 * <i>L</i> is a lower triangular matrix.
 * <i>D</i> is a diagonal matrix.
 * Unlike Cholesky decomposition, this decomposition applies to all real and symmetric matrices,
 * whether positive definite or not.
 * Moreover, when <i>A</i> is positive definite the elements of the diagonal matrix <i>D</i> are all positive.
 * In other words, if <i>A</i> is semi/positive/negative definite, so is <i>D</i>.
 * This algorithm eliminates the need to take square roots.
 *
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/LDL_decomposition#Avoiding_taking_square_roots">Wikipedia: Avoiding taking square roots</a>
 */
public class LDL {

    private LowerTriangularMatrix L;
    private DiagonalMatrix D;
    private final int dim;
    private final Matrix A;

    /**
     * Run the LDL decomposition on a real and symmetric (hence square) matrix.
     *
     * @param A       a real and symmetric (hence square) matrix
     * @param epsilon a precision parameter: when a number |x| ≤ ε, it is considered 0
     * @throws IllegalArgumentException if <i>A</i> is not symmetric
     */
    public LDL(Matrix A, double epsilon) {
        if (!IsMatrix.symmetric(A, epsilon)) {
            throw new IllegalArgumentException("LDL decomposition applies to only symmetric matrix");
        }

        dim = A.nRows();
        this.A = A;

        run();
    }

    /**
     * Run the LDL decomposition on a real and symmetric (hence square) matrix.
     *
     * @param A a real and symmetric (hence square) matrix
     * @throws IllegalArgumentException if <i>A</i> is not symmetric
     */
    public LDL(Matrix A) {
        this(A, 0);
    }

    /**
     * Get <i>L</i> as in the LDL decomposition.
     *
     * @return <i>L</i>
     */
    public LowerTriangularMatrix L() {
        return new LowerTriangularMatrix(L);
    }

    /**
     * Get the transpose of <i>L</i> as in the LDL decomposition.
     * The transpose is upper triangular.
     *
     * @return <i>L'</i>
     */
    public UpperTriangularMatrix Lt() {
        return L.t();
    }

    /**
     * Get <i>D</i> the the diagonal matrix in the LDL decomposition.
     *
     * @return <i>D</i>
     */
    public DiagonalMatrix D() {
        return new DiagonalMatrix(D);
    }

    /**
     * an implementation of the LDL decomposition
     *
     * @see <a href="http://en.wikipedia.org/wiki/LDL_decomposition#Avoiding_taking_square_roots">Wikipedia: Avoiding taking square roots</a>
     */
    private void run() {
        L = new LowerTriangularMatrix(dim);
        D = new DiagonalMatrix(dim);

        for (int i = 1; i <= dim; ++i) {
            //for L[i,j], where i > j
            for (int j = 1; j < i; ++j) {
                double Djj = D.get(j, j);
                if (isZero(Djj, 0)) {//a diagonal entry is 0
                    L.set(i, j, 0);
                } else {
                    double value = A.get(i, j);
                    for (int k = 1; k <= j - 1; ++k) {
                        value -= L.get(i, k) * L.get(j, k) * D.get(k, k);
                    }
                    value /= D.get(j, j);
                    L.set(i, j, value);
                }
            }

            //L[i,i] = 1, always
            L.set(i, i, 1);

            //for D
            double value = A.get(i, i);
            for (int k = 1; k <= i - 1; ++k) {
                value -= L.get(i, k) * L.get(i, k) * D.get(k, k);
            }
            D.set(i, i, value);
        }
    }
}
