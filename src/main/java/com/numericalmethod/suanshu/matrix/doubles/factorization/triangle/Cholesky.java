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

import com.numericalmethod.suanshu.matrix.MatrixSingularityException;
import com.numericalmethod.suanshu.matrix.doubles.IsMatrix;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.triangle.LowerTriangularMatrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.triangle.UpperTriangularMatrix;
import com.numericalmethod.suanshu.number.DoubleUtils;

/**
 * Cholesky decomposition decomposes a real, symmetric (hence square), and positive definite matrix <i>A</i> into
 * <i>A = L * L<sup>t</sup></i>, where <i>L</i> is a lower triangular matrix.
 * For any real, symmetric, positive definite matrix, there is a unique Cholesky decomposition, such that <i>L</i>'s diagonal entries are all positive.
 * This implementation uses the Cholesky-Crout algorithm,
 * which starts from the upper left corner of the matrix <i>L</i> and proceeds to calculate the matrix row by row.
 *
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/Cholesky_decomposition">Wikipedia: Cholesky decomposition</a>
 */
public class Cholesky {

    /** the resultant lower triangular matrix */
    private LowerTriangularMatrix L;

    /**
     * Run the Cholesky decomposition on a real, symmetric (hence square), and positive definite matrix.
     *
     * @param A       a real, symmetric (hence square), and positive definite matrix
     * @param epsilon a precision parameter: when a number |x| ≤ ε, it is considered 0
     * @throws IllegalArgumentException if <i>A</i> is not symmetric
     * @throws RuntimeException         if <i>A</i> is not positive definite matrix
     */
    public Cholesky(Matrix A, double epsilon) {
        if (!IsMatrix.symmetric(A, epsilon)) {
            throw new IllegalArgumentException("Cholesky decomposition applies to only symmetric matrix");
        }

        run(A);
    }

    /**
     * Run the Cholesky decomposition on a real, symmetric (hence square), and positive definite matrix.
     *
     * @param A a real, symmetric (hence square), and positive definite matrix
     * @throws IllegalArgumentException if <i>A</i> is not symmetric
     * @throws RuntimeException         if <i>A</i> is not positive definite matrix
     */
    public Cholesky(Matrix A) {
        this(A, 0);
    }

    /**
     * Get the lower triangular matrix <i>L</i>.
     *
     * @return <i>L</i>
     */
    public LowerTriangularMatrix L() {
        return new LowerTriangularMatrix(L);
    }

    /**
     * Get the transpose of the lower triangular matrix, <i>L'</i>.
     * The transpose is upper triangular.
     *
     * @return <i>L'</i>
     */
    public UpperTriangularMatrix Lt() {
        return L.t();
    }

    /**
     * An implementation of Cholesky-Banachiewicz algorithm.
     *
     * @param A a <em>square</em> matrix
     * @throws MatrixSingularityException if the matrix is singular
     * @see <a href="http://en.wikipedia.org/wiki/Cholesky_decomposition#The_Cholesky-Banachiewicz_and_Cholesky-Crout_algorithms">Wikipedia: The Cholesky-Banachiewicz and Cholesky-Crout algorithms</a>
     */
    private void run(Matrix A) {
        final int dim = A.nRows();
        L = new LowerTriangularMatrix(dim);

        for (int i = 1; i <= dim; ++i) {
            //for L[i,j], where j < i
            for (int j = 1; j < i; ++j) {
                double value = A.get(i, j);
                for (int k = 1; k <= j - 1; ++k) {
                    value -= L.get(i, k) * L.get(j, k);//TODO: improve efficiency by using sub-rows/columns
                }
                value /= L.get(j, j);
                L.set(i, j, value);
            }

            //for L[i,i]
            double value = A.get(i, i);
            for (int k = 1; k <= i - 1; ++k) {
                value -= L.get(i, k) * L.get(i, k);
            }

            if (DoubleUtils.compare(value, 0, 0) <= 0) {
                throw new RuntimeException("A is not positive definite");
            }
            value = Math.sqrt(value);
            L.set(i, i, value);
        }
    }
}
