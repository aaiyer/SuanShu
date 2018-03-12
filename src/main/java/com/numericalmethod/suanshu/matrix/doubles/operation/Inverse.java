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
package com.numericalmethod.suanshu.matrix.doubles.operation;

import com.numericalmethod.suanshu.datastructure.DimensionCheck;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.factorization.triangle.LU;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.PermutationMatrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.triangle.LowerTriangularMatrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.triangle.UpperTriangularMatrix;
import static com.numericalmethod.suanshu.misc.SuanShuUtils.assertArgument;
import static com.numericalmethod.suanshu.misc.SuanShuUtils.autoEpsilon;
import static com.numericalmethod.suanshu.number.DoubleUtils.equal;

/**
 * For a <em>square</em> matrix <i>A</i>, the inverse, A<sup>-1</sup>, if exists, satisfies
 * <blockquote><code>A.multiply(A.inverse()) == A.ONE()</code></blockquote>
 * There are multiple ways to compute the inverse of a matrix. They are,
 * <ul>
 * <li>an analytic solution for small matrices, e.g., 2x2,
 * <li>LU Decomposition,
 * <li>the Moore-Penrose pseudoinverse.
 * </ul>
 *
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/Matrix_inverse">Wikipedia: Invertible matrix</a>
 */
public class Inverse extends DenseMatrix {

    /** the dimension of the square matrix */
    private final int dim;

    /**
     * Construct a the inverse of a matrix.
     *
     * @param A       a matrix
     * @param epsilon a precision parameter: when a number |x| ≤ ε, it is considered 0
     */
    public Inverse(Matrix A, double epsilon) {
        super(inv(A, epsilon));
        this.dim = A.nRows();
    }

    private static Matrix inv(Matrix A, double epsilon) {
        if (!DimensionCheck.isSquare(A)) {
            throw new IllegalArgumentException("Inverse applies to square matrix only");
        }

        LU lu = new LU(A, epsilon);
        DenseMatrix Linv = new Inverse(lu.L(), epsilon);//TODO: taking up too much space
        DenseMatrix Uinv = new Inverse(lu.U(), epsilon);//TODO: taking up too much space
        PermutationMatrix P = lu.P();

        Matrix inv = P.rightMultiply(Uinv.multiply(Linv));//TODO: improve the efficiency; taking up too much space
        return inv;
    }

    /**
     * Construct a the inverse of a matrix.
     *
     * @param A a matrix
     */
    public Inverse(Matrix A) {
        this(A, autoEpsilon(A));
    }

    /**
     * Construct the inverse of an upper triangular matrix.
     *
     * @param U       an upper triangular matrix
     * @param epsilon a precision parameter: when a number |x| ≤ ε, it is considered 0
     * @see "G. W. Stewart, "Algorithm 1.10, Basic Decompositions," Matrix Algorithms, Vol 1."
     */
    public Inverse(UpperTriangularMatrix U, double epsilon) {
        super(U.nRows(), U.nRows());
        this.dim = U.nRows();

        for (int i = 1; i <= dim; ++i) {//col
            assertArgument(!equal(0, U.get(i, i), epsilon), "U is not invertible");

            set(i, i, 1d / U.get(i, i));
            for (int j = i - 1; j >= 1; --j) {//row

                double value = 0;
                for (int k = j + 1; k <= dim; ++k) {
                    value -= U.get(j, k) * this.get(k, i);//TODO: improve efficiency by using sub-rows/columns
                }

                set(j, i, value / U.get(j, j));
            }
        }
    }

    /**
     * Construct the inverse of a lower triangular matrix.
     *
     * @param L       a lower triangular matrix
     * @param epsilon a precision parameter: when a number |x| ≤ ε, it is considered 0
     * @see "G. W. Stewart, "Algorithm 2.3, Basic Decompositions," Matrix Algorithms, Vol 1."
     */
    public Inverse(LowerTriangularMatrix L, double epsilon) {
        super(L.nRows(), L.nRows());
        this.dim = L.nRows();

        for (int i = 1; i <= dim; ++i) {//col
            assertArgument(!equal(0, L.get(i, i), epsilon), "L is not invertible");

            set(i, i, 1d / L.get(i, i));
            for (int j = i + 1; j <= dim; ++j) {//row

                double value = 0;
                for (int k = i; k <= j - 1; ++k) {
                    value -= L.get(j, k) * this.get(k, i);//TODO: improve efficiency by using sub-rows/columns
                }

                set(j, i, value / L.get(j, j));
            }
        }
    }
}
